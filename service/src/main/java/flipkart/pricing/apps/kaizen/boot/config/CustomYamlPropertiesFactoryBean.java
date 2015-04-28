package flipkart.pricing.apps.kaizen.boot.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;

import java.util.Properties;

/**
 *
 */
public class CustomYamlPropertiesFactoryBean extends YamlPropertiesFactoryBean {
    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        final Properties properties = getObject();
        System.out.println("Read the following properties : "); // TODO : change to using a real logger
        properties.entrySet().forEach((entry) -> System.out.println(entry.getKey() + ":" + entry.getValue()));
    }
}
