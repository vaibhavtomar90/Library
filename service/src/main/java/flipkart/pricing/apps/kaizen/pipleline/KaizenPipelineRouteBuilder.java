package flipkart.pricing.apps.kaizen.pipleline;

import flipkart.pricing.apps.kaizen.api.ListingUpdateMessage;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class KaizenPipelineRouteBuilder extends RouteBuilder {

    public static final String DIRECT_PROCESS_INCOMING_SIGNALS = "direct:processIncomingSignals";
    private static final String DIRECT_POST_INGESTION = "direct:toKafkaPostIngestion";
    public static final String PROCESS_INCOMING_SIGNALS_ROUTE = "processIncomingSignals";
    public static final String POST_INGESTION_ROUTE = "postIngestionRoute";
    public static final String PROPOGATE_ROUTE = "propogateRoute";
    public static final String FATAK_ROUTE = "fatakRoute";
    private final String kafkaBrokerHost;
    private final String postIngestionTopic;
    private final String kafkaBrokerPort;

    @Inject
    public KaizenPipelineRouteBuilder(@Value("${kaizen.kafka.brokerHost}") String kafkaBrokerHost,
                                      @Value("${kaizen.kafka.dataservice.postIngestionTopic}") String postIngestionTopic,
                                      @Value("${kaizen.kafka.brokerPort}") String kafkaBrokerPort) {
        this.kafkaBrokerHost = kafkaBrokerHost;
        this.postIngestionTopic = postIngestionTopic;
        this.kafkaBrokerPort = kafkaBrokerPort;
    }

    @Override
    public void configure() throws Exception {
        from(DIRECT_PROCESS_INCOMING_SIGNALS)
                .transacted()
                .multicast()
                    .beanRef("signalService", "updateSignals")
                    .to(DIRECT_POST_INGESTION)
                    .stopOnException()
                .end()
                .routeId(PROCESS_INCOMING_SIGNALS_ROUTE)
        ;
        from(DIRECT_POST_INGESTION)
                .transacted()
                .convertBodyTo(ListingUpdateMessage.class)
                .to(KafkaEndpointUriBuilderUtil.getToUri(kafkaBrokerHost, kafkaBrokerPort, postIngestionTopic))
                .routeId(POST_INGESTION_ROUTE)
        ;
    }

}
