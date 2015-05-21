package flipkart.pricing.apps.kaizen.pipeline;


import com.fasterxml.jackson.databind.ObjectMapper;
import flipkart.pricing.apps.kaizen.api.PricingUpdate;
import flipkart.pricing.apps.kaizen.boot.config.KaizenContextConfiguration;
import flipkart.pricing.apps.kaizen.pipleline.HttpEndpointUriBuilderUtil;
import flipkart.pricing.apps.kaizen.pipleline.KafkaEndpointUriBuilderUtil;
import flipkart.pricing.apps.kaizen.pipleline.PropogationPipelineRouteBuilder;
import flipkart.pricing.apps.kaizen.testrules.DbSetupRule;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableLong;
import org.apache.commons.lang3.mutable.MutableObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static flipkart.pricing.apps.kaizen.pipleline.KaizenPipelineRouteBuilder.FATAK_ROUTE;
import static flipkart.pricing.apps.kaizen.pipleline.KaizenPipelineRouteBuilder.PROPOGATE_ROUTE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {KaizenContextConfiguration.class})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PropogationPipelineRouteBuilderTest {

    @Rule
    @Inject
    public DbSetupRule dbSetupRule;

    @Inject
    private PropogationPipelineRouteBuilder propogationPipelineRouteBuilder;

    @EndpointInject(uri = "direct:processIncomingSignals")
    private ProducerTemplate incomingSignals;

    @Inject
    private SpringCamelContext camelContext;

    @Value("${kaizen.kafka.brokerHost}")
    private String brokerHost;

    @Value("${kaizen.kafka.brokerPort}")
    private String brokerPort;

    @Value("${kaizen.kafka.propagate.propagationTopic}")
    private String propagationTopic;

    @Value("${kaizen.http.fatak.priceUpdateUrl}")
    String fatakPriceUpdateUrl;

    @Value("${kaizen.kafka.zookeeper}")
    String zookeeper;

    @Value("${kaizen.kafka.compute.postComputeTopic}")
    String postComputeTopic;

    @Value("${kaizen.kafka.compute.groupId}")
    String postComputeGroupId;

    ObjectMapper objectMapper = new ObjectMapper();

    // TODO : Add Tests for redelivery in case of exceptions

    @Test
    public void propogateRouteShould_NotSendDataToPropogationTopic_OnFatakFailure() throws Exception {
        // TODO : We are throwing error in fatak. When we have redelivery will have to care that redeliveries are exhausted in this test itself otherwise kafka might have pending message which might disrupt other tests
        incomingSignals = camelContext.createProducerTemplate();
        final RouteDefinition route = camelContext.getRouteDefinition(PROPOGATE_ROUTE);
        MutableBoolean fatakCalled = new MutableBoolean(false);
        MutableBoolean propogationCalled = new MutableBoolean(false);
        MutableBoolean done = new MutableBoolean(false);
        RouteDefinition fatakRoute = camelContext.getRouteDefinition(FATAK_ROUTE);
        fatakRoute.adviceWith(camelContext, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint(HttpEndpointUriBuilderUtil.getUri(fatakPriceUpdateUrl))
                        .skipSendToOriginalEndpoint()
                        .process(exchange -> {
                            fatakCalled.setTrue();
                            throw new RuntimeException("You shall not pass");
                        });
            }
        });
        route.adviceWith(camelContext, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                onCompletion().process(exchange -> done.setTrue());
                interceptSendToEndpoint(KafkaEndpointUriBuilderUtil.getToUri(brokerHost, brokerPort, propagationTopic))
                        .skipSendToOriginalEndpoint()
                        .process(exchange -> propogationCalled.setTrue());
            }
        });
        String pricingUpdate = objectMapper.writeValueAsString(new PricingUpdate("foo", 10.0d, 20.0d, 1.0d, 1000l));
        incomingSignals.sendBody(KafkaEndpointUriBuilderUtil.getFromUri(brokerHost, brokerPort, zookeeper, postComputeTopic, postComputeGroupId), pricingUpdate);
        int wait_time_counter = 0;
        while (done.isFalse() && (++wait_time_counter) < 10) {
            Thread.sleep(1000);
        }
        assertThat("Fatak should have been called", fatakCalled.getValue(), is(true));
        assertThat("Propogation should not have been called", propogationCalled.getValue(), is(false));
    }

    @Test
    public void propogateRouteShould_SendDataFirstToFatak_ThenToPropogate() throws Exception {
        incomingSignals = camelContext.createProducerTemplate();
        RouteDefinition route = camelContext.getRouteDefinition(PROPOGATE_ROUTE);
        MutableLong fatakCallingTime = new MutableLong(0);
        MutableLong propogateCallingTime = new MutableLong(0);
        MutableBoolean done = new MutableBoolean(false);

        RouteDefinition fatakRoute = camelContext.getRouteDefinition(FATAK_ROUTE);
        fatakRoute.adviceWith(camelContext, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint(HttpEndpointUriBuilderUtil.getUri(fatakPriceUpdateUrl))
                        .skipSendToOriginalEndpoint()
                        .process(exchange -> fatakCallingTime.setValue(System.nanoTime()));
            }
        });
        route.adviceWith(camelContext, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                onCompletion().process(exchange -> done.setTrue());
                interceptSendToEndpoint(KafkaEndpointUriBuilderUtil.getToUri(brokerHost, brokerPort, propagationTopic))
                        .skipSendToOriginalEndpoint()
                        .process(exchange -> propogateCallingTime.setValue(System.nanoTime()));
            }
        });
        String pricingUpdate = objectMapper.writeValueAsString(new PricingUpdate("foo", 10.0d, 20.0d, 1.0d, 1000l));
        incomingSignals.sendBody(KafkaEndpointUriBuilderUtil.getFromUri(brokerHost, brokerPort, zookeeper, postComputeTopic, postComputeGroupId), pricingUpdate);
        int wait_time_counter = 0;
        while (done.isFalse() && (++wait_time_counter) < 10) {
            Thread.sleep(1000);
        }
        assertThat("Fatak should be called before propogate", fatakCallingTime.getValue(), is(lessThan(propogateCallingTime.getValue())));
    }

    @Test
    public void propogateRouteShould_SendCorrectDataToFatak() throws Exception {
        incomingSignals = camelContext.createProducerTemplate();
        final RouteDefinition route = camelContext.getRouteDefinition(PROPOGATE_ROUTE);
        MutableObject<String> fatakReceivedMessage = new MutableObject<>();
        MutableObject<String> propogateReceivedMessage = new MutableObject<>();
        MutableBoolean done = new MutableBoolean(false);

        RouteDefinition fatakRoute = camelContext.getRouteDefinition(FATAK_ROUTE);
        fatakRoute.adviceWith(camelContext, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint(HttpEndpointUriBuilderUtil.getUri(fatakPriceUpdateUrl))
                        .skipSendToOriginalEndpoint()
                        .process(exchange -> fatakReceivedMessage.setValue((String) exchange.getIn().getBody()));
            }
        });
        route.adviceWith(camelContext, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                onCompletion().process(exchange -> done.setTrue());
                interceptSendToEndpoint(KafkaEndpointUriBuilderUtil.getToUri(brokerHost, brokerPort, propagationTopic))
                        .skipSendToOriginalEndpoint()
                        .process(exchange -> propogateReceivedMessage.setValue((String) exchange.getIn().getBody()));
            }
        });
        incomingSignals.sendBody(KafkaEndpointUriBuilderUtil.getFromUri(brokerHost, brokerPort, zookeeper, postComputeTopic, postComputeGroupId), objectMapper.writeValueAsString(new PricingUpdate("foo", 10.0d, 20.0d, 1.0d, 1000l)));
        int wait_time_counter = 0;
        while (done.isFalse() && (++wait_time_counter) < 10) {
            Thread.sleep(1000);
        }
        assertThat("Fatak should have received correct message", fatakReceivedMessage.getValue(), is("[{\"listing\":\"foo\",\"mrp\":10.0,\"fsp\":20.0,\"fk_discount\":1.0,\"version\":1000}]"));
        assertThat("Fatak should have received correct message", propogateReceivedMessage.getValue(), is("{\"listing\":\"foo\",\"mrp\":10.0,\"fsp\":20.0,\"fk_discount\":1.0,\"version\":1000}"));

    }
}
