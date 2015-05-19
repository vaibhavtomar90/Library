package flipkart.pricing.apps.kaizen.pipleline;

/**
 * @understands Util for building kafka endpoints to be used in camel route definitions
 */
public class KafkaEndpointUriBuilderUtil {

    public static String getToUri(String brokerHost, String brokerPort, String topic) {
        return "kafka:" + brokerHost + ":" + brokerPort +
                "?topic=" + topic +
                "&serializerClass=kafka.serializer.StringEncoder&requestRequiredAcks=-1";
    }

    public static String getFromUri(String brokerHost, String brokerPort, String zookeeper, String topic, String groupId) {
        return "kafka:" + brokerHost + ":" + brokerPort +
                "?" +
                "zookeeperConnect=" + zookeeper +
                "&topic=" + topic +
                "&groupId=" + groupId +
                "&serializerClass=kafka.serializer.StringEncoder";

    }
}
