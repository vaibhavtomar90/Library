package flipkart.pricing.apps.kaizen.boot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Properties;

/**
 *
 */
public class CustomPropertySourcesPlaceHolderConfigurer extends PropertySourcesPlaceholderConfigurer implements InitializingBean{

    private static final Logger logger = LoggerFactory.getLogger(CustomPropertySourcesPlaceHolderConfigurer.class);

    @Override
    protected void loadProperties(Properties props) throws IOException {
        super.loadProperties(props);
        final YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(new ClassPathResource(KaizenConfiguration.YML_CONFIG_FILE));
        final Properties yamlProperties = yamlPropertiesFactoryBean.getObject();
        CollectionUtils.mergePropertiesIntoMap(yamlProperties,props);
    }

    @Override
    public void afterPropertiesSet() throws Exception{
        final Properties properties = this.mergeProperties();
        logger.info("Read the following properties : ");
        properties.entrySet().forEach((entry) -> logger.info(entry.getKey() + ":" + entry.getValue()));
    }
}
