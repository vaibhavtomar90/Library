package flipkart.pricing.apps.kaizen.pipleline;

/**
 * @understands Util for building kafka endpoints to be used in camel route definitions
 */
public class KafkaEndpointUriBuilderUtil {

    public static String getUri(String brokerHost, String brokerPort, String topic) {
        return "kafka:" + brokerHost + ":" + brokerPort +
            "?topic=" + topic +
            "&serializerClass=kafka.serializer.StringEncoder&requestRequiredAcks=-1";
    }
}
