package flipkart.pricing.apps.kaizen.boot.config;


import flipkart.pricing.apps.kaizen.pipleline.KaizenPipelineRouteBuilder;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @understands : Spring context configuration
 */
@Configuration
@ComponentScan(value = {"flipkart.pricing.apps.kaizen"})
@Import({DataSourceConfiguration.class,HibernateConfiguration.class})
@EnableTransactionManagement
public class KaizenContextConfiguration {
    @Deprecated // for CGLIB only
    public KaizenContextConfiguration() {
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer yamlPropertiesLoader() {
        final PropertySourcesPlaceholderConfigurer yamlPropertiesFactoryBean = new CustomPropertySourcesPlaceHolderConfigurer();
        yamlPropertiesFactoryBean.setIgnoreUnresolvablePlaceholders(false);
        return yamlPropertiesFactoryBean;
    }

    @Bean(name = "incomingSignal")
    public ProducerTemplate getIncomingSignalProducerTemplate(CamelContext camelContext) {
        final ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        producerTemplate.setDefaultEndpointUri("direct:processIncomingSignals");
        return producerTemplate;
    }

    @Bean
    public CamelContext getCamelContext(KaizenPipelineRouteBuilder kaizenPipelineRouteBuilder) throws Exception {
        CamelContext camelContext = new SpringCamelContext();
        camelContext.addRoutes(kaizenPipelineRouteBuilder); // TODO : Use spring context to read all routes (not an issue now since we have only one route)
        return camelContext;
    }
}
