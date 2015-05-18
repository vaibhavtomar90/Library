package flipkart.pricing.apps.kaizen.pipeline;

import flipkart.pricing.apps.kaizen.api.SignalFetchDto;
import flipkart.pricing.apps.kaizen.api.SignalSaveDetail;
import flipkart.pricing.apps.kaizen.boot.config.KaizenContextConfiguration;
import flipkart.pricing.apps.kaizen.db.service.SignalService;
import flipkart.pricing.apps.kaizen.exceptions.ListingNotFoundException;
import flipkart.pricing.apps.kaizen.exceptions.SignalNameNotFoundException;
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

import static org.hamcrest.Matchers.containsString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {KaizenContextConfiguration.class})
@ActiveProfiles("test")
@DirtiesContext
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


    @Test
    public void ingestionRouteShould_ThrowExceptionIfWriteToKafkaFails() throws Exception {
        final RouteDefinition route = camelContext.getRouteDefinitions().get(0); // Works since we currently have only one route
        route.adviceWith(camelContext, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint(KafkaEndpointUriBuilderUtil.getUri(brokerHost,brokerPort,postIngestionTopic))
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
        final RouteDefinition route = camelContext.getRouteDefinitions().get(0); // Works since we currently have only one route
        // Sending a signal that does not exist
        boolean signalNotFoundExceptionThrown = false;
        try {
            incomingSignals.sendBody(SignalDtoTestUtils.getSampleSignalSaveDto(
                "foo", 1l, Collections.singletonList(new SignalSaveDetail("foo", "bar", 1l))));
        } catch (CamelExecutionException e) {
            // TODO : See if Camel can return the actual exception
            if (e.getCause().getCause() instanceof SignalNameNotFoundException)
                signalNotFoundExceptionThrown = true;
        }
        assertTrue(signalNotFoundExceptionThrown);
    }

    @Test(expected = ListingNotFoundException.class)
    public void ingestionRouteShould_NotWriteToDbOnFailure() throws Exception {
        final RouteDefinition route = camelContext.getRouteDefinitions().get(1); // Works since we currently have only one route
        route.adviceWith(camelContext, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint(KafkaEndpointUriBuilderUtil.getUri(brokerHost, brokerPort, postIngestionTopic))
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
}