package flipkart.pricing.apps.kaizen.pipleline;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;

public class KaizenPipelineRouteBuilder extends RouteBuilder {
    
    private String kafkaBrokerHost;
    private String postIngestionTopic;
    private String postComputeTopic;
    private String propagationTopic;
    private String kafkaBrokerPort;

    public KaizenPipelineRouteBuilder(String kafkaBrokerHost, 
                                      String postIngestionTopic, 
                                      String postComputeTopic, 
                                      String propagationTopic, 
                                      String kafkaBrokerPort) {
        this.kafkaBrokerHost = kafkaBrokerHost;
        this.postIngestionTopic = postIngestionTopic;
        this.postComputeTopic = postComputeTopic;
        this.propagationTopic = propagationTopic;
        this.kafkaBrokerPort = kafkaBrokerPort;
    }

    @Override
    public void configure() throws Exception {
        from("direct:processIncomingSignals")
            .beanRef("signalService","updateSignals")
            .to("kafka:"+kafkaBrokerHost+":"+kafkaBrokerPort+
                "?topic="+postIngestionTopic+
                "&serializerClass=kafka.serializer.StringEncoder&requestRequiredAcks=-1");
    }
}
