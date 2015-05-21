package flipkart.pricing.apps.kaizen.pipleline;

/**
 * @understands Util for building kafka endpoints to be used in camel route definitions
 */
public class HttpEndpointUriBuilderUtil {

    public static String getUri(String url) {
        return "http4:" + url;
    }
}
