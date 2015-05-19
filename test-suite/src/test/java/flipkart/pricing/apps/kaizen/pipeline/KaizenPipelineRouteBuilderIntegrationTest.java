package flipkart.pricing.apps.kaizen.pipeline;

import com.fasterxml.jackson.databind.ObjectMapper;
import flipkart.pricing.apps.kaizen.api.PricingUpdate;
import flipkart.pricing.apps.kaizen.api.SignalFetchDto;
import flipkart.pricing.apps.kaizen.api.SignalSaveDetail;
import flipkart.pricing.apps.kaizen.boot.config.KaizenContextConfiguration;
import flipkart.pricing.apps.kaizen.db.service.SignalService;
import flipkart.pricing.apps.kaizen.exceptions.ListingNotFoundException;
import flipkart.pricing.apps.kaizen.exceptions.SignalNameNotFoundException;
import flipkart.pricing.apps.kaizen.pipleline.HttpEndpointUriBuilderUtil;
import flipkart.pricing.apps.kaizen.pipleline.KafkaEndpointUriBuilderUtil;
import flipkart.pricing.apps.kaizen.pipleline.KaizenPipelineRouteBuilder;
import flipkart.pricing.apps.kaizen.testrules.DbSetupRule;
import flipkart.pricing.apps.kaizen.utils.HibernateSessionUtil;
import flipkart.pricing.apps.kaizen.utils.SignalDtoTestUtils;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static flipkart.pricing.apps.kaizen.pipleline.KaizenPipelineRouteBuilder.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {KaizenContextConfiguration.class})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class KaizenPipelineRouteBuilderIntegrationTest extends CamelTestSupport {

    @Rule
    @Inject
    public DbSetupRule dbSetupRule;

    @Inject
    private KaizenPipelineRouteBuilder kaizenPipelineRouteBuilder;

    @EndpointInject(uri = "direct:processIncomingSignals")
    private ProducerTemplate incomingSignals;

    @Inject
    private SpringCamelContext camelContext;

    @Value("${kaizen.kafka.brokerHost}")
    private String brokerHost;

    @Value("${kaizen.kafka.brokerPort}")
    private String brokerPort;

    @Value("${kaizen.kafka.dataservice.postIngestionTopic}")
    private String postIngestionTopic;

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

    @Inject
    private HibernateSessionUtil<SignalFetchDto> hibernateSessionUtil;

    @Inject
    private SignalService signalService;

    @Override
    public CamelContext createCamelContext() {
        return camelContext;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return kaizenPipelineRouteBuilder;
    }

    ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void ingestionRouteShould_ThrowExceptionIfWriteToKafkaFails() throws Exception {
        final RouteDefinition route = camelContext.getRouteDefinition(POST_INGESTION_ROUTE);
        route.adviceWith(camelContext, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint(KafkaEndpointUriBuilderUtil.getToUri(brokerHost, brokerPort, postIngestionTopic))
                        .skipSendToOriginalEndpoint()
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                throw new RuntimeException("You shall not Pass!");
                            }
                        });
            }
        });

        boolean dummyRuntimeExcpetionThrown = false;
        try {
            incomingSignals.sendBody(SignalDtoTestUtils.getSampleSignalSaveDto());
        } catch (RuntimeCamelException e) {
            assertThat(e.getCause().getMessage(),containsString("You shall not Pass!"));
            dummyRuntimeExcpetionThrown = true;
        }
        assertTrue(dummyRuntimeExcpetionThrown);
    }

    @Test
    public void ingestionRouteShould_ThrowExceptionIfWriteToDbFails() throws Exception {
        try {
            incomingSignals.sendBody(SignalDtoTestUtils.getSampleSignalSaveDto(
                    "foo", 1l, Collections.singletonList(new SignalSaveDetail("foo", "bar", 1l))));
            fail("Should have thrown error");
        } catch (CamelExecutionException e) {
            // TODO : See if Camel can return the actual exception
            if (e.getCause().getCause().getCause().getCause() instanceof SignalNameNotFoundException) {
            } else {
                fail("Should have thrown error SignalNameNotFoundExceprion");
            }
        }
    }

    @Test(expected = ListingNotFoundException.class)
    public void ingestionRouteShould_NotWriteToDbOnFailure() throws Exception {
        final RouteDefinition route = camelContext.getRouteDefinition(POST_INGESTION_ROUTE);
        route.adviceWith(camelContext, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint(KafkaEndpointUriBuilderUtil.getToUri(brokerHost, brokerPort, postIngestionTopic))
                        .skipSendToOriginalEndpoint()
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                throw new RuntimeException("You shall not Pass!");
                            }
                        });
            }
        });
        boolean dummyRuntimeExcpetionThrown = false;
        try {
            incomingSignals.sendBody(SignalDtoTestUtils.getSampleSignalSaveDto());
        } catch (RuntimeCamelException e) {
            assertThat(e.getCause().getMessage(),containsString("You shall not Pass!"));
            dummyRuntimeExcpetionThrown = true;
        }
        assertTrue(dummyRuntimeExcpetionThrown);
        // Ensure nothing was written to DB. Would result in Exception
        final SignalFetchDto fetchedSignalsForLst1 = hibernateSessionUtil.withinSession(() -> signalService.fetchSignals("lst1"));
    }

    @Test
    public void ingestionRouteShould_NotSendDataToKafkaOnRouteFailure() throws Exception {
        final RouteDefinition route = camelContext.getRouteDefinition(POST_INGESTION_ROUTE); // Works since we currently have only one route
        route.adviceWith(camelContext, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint(KafkaEndpointUriBuilderUtil.getToUri(brokerHost, brokerPort, postIngestionTopic))
                        .skipSendToOriginalEndpoint()
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                fail("Should not have been called");
                            }
                        });
            }
        });
        // Sending a signal that does not exist
        try {
            incomingSignals.sendBody(SignalDtoTestUtils.getSampleSignalSaveDto(
                    "foo", 1l, Collections.singletonList(new SignalSaveDetail("foo", "bar", 1l))));
            fail("Should have thrown error");
        } catch (CamelExecutionException e) {
            // TODO : See if Camel can return the actual exception
            if (e.getCause().getMessage().equals("Should not have been called")) {
                fail("Failed because kafka endpoint was called");
            }
            if (e.getCause() == null
                    || e.getCause().getCause() == null
                    || e.getCause().getCause().getCause() == null
                    || !(e.getCause().getCause().getCause().getCause() instanceof SignalNameNotFoundException)) {
                fail("Should have thrown error SignalNameNotFoundExceprion");
            }
        }
    }

    // TODO : Add Tests for redelivery in case of exceptions

    @Test
    public void propogateRouteShould_NotSendDataToPropogationTopic_OnFatakFailure() throws Exception {
        // TODO : We are throwing error in fatak. When we have redelivery will have to care that redeliveries are exhausted in this test itself otherwise kafka might have pending message which might disrupt other tests
        final RouteDefinition route = camelContext.getRouteDefinition(PROPOGATE_ROUTE);
        AtomicBoolean fatakCalled = new AtomicBoolean(false);
        AtomicBoolean propogationCalled = new AtomicBoolean(false);
        AtomicBoolean done = new AtomicBoolean(false);
        RouteDefinition fatakRoute = camelContext.getRouteDefinition(FATAK_ROUTE);
        fatakRoute.adviceWith(camelContext, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint(HttpEndpointUriBuilderUtil.getUri(fatakPriceUpdateUrl))
                        .skipSendToOriginalEndpoint()
                        .process(exchange -> {
                            fatakCalled.set(true);
                            throw new RuntimeException("You shall not pass");
                        });
            }
        });
        route.adviceWith(camelContext, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                onCompletion().process(exchange -> done.set(true));
                interceptSendToEndpoint(KafkaEndpointUriBuilderUtil.getToUri(brokerHost, brokerPort, propagationTopic))
                        .skipSendToOriginalEndpoint()
                        .process(exchange -> propogationCalled.set(true));
            }
        });
        String pricingUpdate = objectMapper.writeValueAsString(new PricingUpdate("foo", 10.0d, 20.0d, 1.0d, 100l));
        incomingSignals.sendBody(KafkaEndpointUriBuilderUtil.getFromUri(brokerHost, brokerPort, zookeeper, postComputeTopic, postComputeGroupId), pricingUpdate);
        int wait_time_counter = 0;
        while (!done.get() && (++wait_time_counter) < 10 ) {
            Thread.sleep(1000);
        }
        assertTrue("Fatak should have been called", fatakCalled.get());
        assertFalse("Propogation should not have been called", propogationCalled.get());
    }

    @Test
    public void propogateRouteShould_SendDataFirstToFatak_ThenToPropogate() throws Exception {
        RouteDefinition route = camelContext.getRouteDefinition(PROPOGATE_ROUTE);
        AtomicLong fatakCallingTime = new AtomicLong(0);
        AtomicLong propogateCallingTime = new AtomicLong(0);
        AtomicBoolean done = new AtomicBoolean(false);

        RouteDefinition fatakRoute = camelContext.getRouteDefinition(FATAK_ROUTE);
        fatakRoute.adviceWith(camelContext, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint(HttpEndpointUriBuilderUtil.getUri(fatakPriceUpdateUrl))
                        .skipSendToOriginalEndpoint()
                        .process(exchange -> fatakCallingTime.set(System.nanoTime()));
            }
        });
        route.adviceWith(camelContext, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                onCompletion().process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        done.set(true);
                    }
                });
                interceptSendToEndpoint(KafkaEndpointUriBuilderUtil.getToUri(brokerHost, brokerPort, propagationTopic))
                        .skipSendToOriginalEndpoint()
                        .process(exchange -> propogateCallingTime.set(System.nanoTime()));
            }
        });
        String pricingUpdate = objectMapper.writeValueAsString(new PricingUpdate("foo", 10.0d, 20.0d, 1.0d, 100l));
        incomingSignals.sendBody(KafkaEndpointUriBuilderUtil.getFromUri(brokerHost, brokerPort, zookeeper, postComputeTopic, postComputeGroupId), pricingUpdate);
        int wait_time_counter = 0;
        while (!done.get() && (++wait_time_counter) < 10 ) {
            Thread.sleep(1000);
        }
        assertThat("Fatak should be called before propogate", fatakCallingTime.get(), is(lessThan(propogateCallingTime.get())));
    }

    @Test
    public void propogateRouteShould_SendCorrectDataToFatak() throws Exception {
        final RouteDefinition route = camelContext.getRouteDefinition(PROPOGATE_ROUTE);
        AtomicBoolean fatakReceivedMessage = new AtomicBoolean();
        AtomicBoolean propogateReceivedMessage = new AtomicBoolean();
        AtomicBoolean done = new AtomicBoolean(false);

        RouteDefinition fatakRoute = camelContext.getRouteDefinition(FATAK_ROUTE);
        fatakRoute.adviceWith(camelContext, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint(HttpEndpointUriBuilderUtil.getUri(fatakPriceUpdateUrl))
                        .skipSendToOriginalEndpoint()
                        .process(exchange -> {
                            String body = (String) exchange.getIn().getBody();
                            fatakReceivedMessage.set(body.equals("[{\"listing\":\"foo\",\"mrp\":10.0,\"fsp\":20.0,\"fk_discount\":1.0,\"version\":100}]"));
                        });
            }
        });
        route.adviceWith(camelContext, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                onCompletion().process(exchange -> done.set(true));
                interceptSendToEndpoint(KafkaEndpointUriBuilderUtil.getToUri(brokerHost, brokerPort, propagationTopic))
                        .skipSendToOriginalEndpoint()
                        .process(exchange -> {
                            String body = (String) exchange.getIn().getBody();
                            propogateReceivedMessage.set(body.equals("{\"listing\":\"foo\",\"mrp\":10.0,\"fsp\":20.0,\"fk_discount\":1.0,\"version\":100}"));
                        });
            }
        });
        incomingSignals.sendBody(KafkaEndpointUriBuilderUtil.getFromUri(brokerHost, brokerPort, zookeeper, postComputeTopic, postComputeGroupId), objectMapper.writeValueAsString(new PricingUpdate("foo", 10.0d, 20.0d, 1.0d, 100l)));
        int wait_time_counter = 0;
        while (!done.get() && (++wait_time_counter) < 10 ) {
            Thread.sleep(1000);
        }
        assertTrue(fatakReceivedMessage.get());
        assertTrue(propogateReceivedMessage.get());

    }
}
