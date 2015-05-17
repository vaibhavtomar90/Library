package flipkart.pricing.apps.kaizen.pipleline;

import flipkart.pricing.apps.kaizen.api.ListingUpdateMessage;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Named;

import static flipkart.pricing.apps.kaizen.pipleline.KafkaEndpointUriBuilderUtil.getUri;

@Named
public class KaizenPipelineRouteBuilder extends RouteBuilder {

    private String kafkaBrokerHost;
    private String postIngestionTopic;
    private String postComputeTopic;
    private String propagationTopic;
    private String kafkaBrokerPort;

    @Inject
    public KaizenPipelineRouteBuilder(@Value("${kaizen.kafka.brokerHost}")String kafkaBrokerHost,
                                      @Value("${kaizen.kafka.dataservice.postIngestionTopic}")String postIngestionTopic,
                                      @Value("${kaizen.kafka.compute.postComputeTopic}")String postComputeTopic,
                                      @Value("${kaizen.kafka.propagate.propagationTopic}")String propagationTopic,
                                      @Value("${kaizen.kafka.brokerPort}")String kafkaBrokerPort)  {
        this.kafkaBrokerHost = kafkaBrokerHost;
        this.postIngestionTopic = postIngestionTopic;
        this.postComputeTopic = postComputeTopic;
        this.propagationTopic = propagationTopic;
        this.kafkaBrokerPort = kafkaBrokerPort;
    }

    @Override
    public void configure() throws Exception {
        from("direct:processIncomingSignals")
            .transacted()
            .multicast()
            .beanRef("signalService", "updateSignals")
            .to("direct:toKafka");
        from("direct:toKafka")
            .transacted()
            .convertBodyTo(ListingUpdateMessage.class).to(getUri(kafkaBrokerHost, kafkaBrokerPort, postIngestionTopic));
    }

}
