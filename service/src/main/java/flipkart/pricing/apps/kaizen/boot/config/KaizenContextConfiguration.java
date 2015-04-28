package flipkart.pricing.apps.kaizen.boot.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @understands : Spring context configuration
 */
@Configuration
@ComponentScan({"flipkart.pricing.apps.kaizen"})
public class KaizenContextConfiguration {
    @Deprecated // for CGLIB only
    public KaizenContextConfiguration() {
    }

    @Bean
    public YamlPropertiesFactoryBean yamlPropertiesLoader() {
        final YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new CustomYamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(new ClassPathResource(KaizenConfiguration.YML_CONFIG_FILE));
        return yamlPropertiesFactoryBean;
    }

}
