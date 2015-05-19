package flipkart.pricing.apps.kaizen.pipleline;

import flipkart.pricing.apps.kaizen.api.ListingUpdateMessage;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Named;

import static flipkart.pricing.apps.kaizen.pipleline.KafkaEndpointUriBuilderUtil.getFromUri;

@Named
public class KaizenPipelineRouteBuilder extends RouteBuilder {

    public static final String DIRECT_PROCESS_INCOMING_SIGNALS = "direct:processIncomingSignals";
    private static final String DIRECT_POST_INGESTION = "direct:toKafkaPostIngestion";
    public static final String DIRECT_FATAK_INGESTION = "direct:toFatakIngestion";
    public static final String PROCESS_INCOMING_SIGNALS_ROUTE = "processIncomingSignals";
    public static final String POST_INGESTION_ROUTE = "postIngestionRoute";
    public static final String PROPOGATE_ROUTE = "propogateRoute";
    public static final String FATAK_ROUTE = "fatakRoute";
    private final String kafkaBrokerHost;
    private final String postIngestionTopic;
    private final String postComputeTopic;
    private final String propagationTopic;
    private final String kafkaBrokerPort;
    private final String fatakPriceUpdateUrl;
    private final String postComputeGroupId;
    private final String zookeeper;
    private final ByteArrayToStringProcessor byteArrayToStringProcessor;

    @Inject
    public KaizenPipelineRouteBuilder(@Value("${kaizen.kafka.brokerHost}") String kafkaBrokerHost,
                                      @Value("${kaizen.kafka.dataservice.postIngestionTopic}") String postIngestionTopic,
                                      @Value("${kaizen.kafka.compute.postComputeTopic}") String postComputeTopic,
                                      @Value("${kaizen.kafka.propagate.propagationTopic}") String propagationTopic,
                                      @Value("${kaizen.kafka.brokerPort}") String kafkaBrokerPort,
                                      @Value("${kaizen.http.fatak.priceUpdateUrl}") String fatakPriceUpdateUrl,
                                      @Value("${kaizen.kafka.compute.groupId}") String postComputeGroupId,
                                      @Value("${kaizen.kafka.zookeeper}") String zookeeper, ByteArrayToStringProcessor byteArrayToStringProcessor) {
        this.kafkaBrokerHost = kafkaBrokerHost;
        this.postIngestionTopic = postIngestionTopic;
        this.postComputeTopic = postComputeTopic;
        this.propagationTopic = propagationTopic;
        this.kafkaBrokerPort = kafkaBrokerPort;
        this.fatakPriceUpdateUrl = fatakPriceUpdateUrl;
        this.postComputeGroupId = postComputeGroupId;
        this.zookeeper = zookeeper;
        this.byteArrayToStringProcessor = byteArrayToStringProcessor;
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
        from(getFromUri(kafkaBrokerHost, kafkaBrokerPort, zookeeper, postComputeTopic, postComputeGroupId))
            .process(byteArrayToStringProcessor)
                .removeHeader(KafkaConstants.TOPIC)
                .multicast()
                    .to(DIRECT_FATAK_INGESTION)
                    .to(KafkaEndpointUriBuilderUtil.getToUri(kafkaBrokerHost, kafkaBrokerPort, propagationTopic))
                    .stopOnException()
                .end()
                .routeId(PROPOGATE_ROUTE)
        ;
        from(DIRECT_FATAK_INGESTION)
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.POST))
                .setHeader("Z-CLIENT-ID", constant(1))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .process(exchange -> {
                    String body = "[" + exchange.getIn().getBody() + "]";
                    exchange.getIn().setBody(body);
                })
                .to(HttpEndpointUriBuilderUtil.getUri(fatakPriceUpdateUrl))
                .routeId(FATAK_ROUTE);

    }

}
