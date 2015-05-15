package flipkart.pricing.apps.kaizen.boot.config;

import flipkart.pricing.apps.kaizen.pipleline.KaizenPipelineRouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelPipelineConfig {
    @Bean
    public KaizenPipelineRouteBuilder getRouteBuilder(@Value("${kaizen.kafka.brokerHost}")String kafkaBrokerHost,
                                                      @Value("${kaizen.kafka.dataservice.postIngestionTopic}")String postIngestionTopic,
                                                      @Value("${kaizen.kafka.compute.postComputeTopic}")String postComputeTopic,
                                                      @Value("${kaizen.kafka.propagate.propagationTopic}")String propagationTopic,
                                                      @Value("${kaizen.kafka.brokerPort}")String kafkaBrokerPort) {
        return new KaizenPipelineRouteBuilder(kafkaBrokerHost, postIngestionTopic, postComputeTopic, propagationTopic, kafkaBrokerPort);
    }
}
