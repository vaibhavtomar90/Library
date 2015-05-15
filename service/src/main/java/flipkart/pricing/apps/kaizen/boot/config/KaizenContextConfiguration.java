package flipkart.pricing.apps.kaizen.boot.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

/**
 * @understands : Spring context configuration
 */
@Configuration
@ComponentScan(value = {"flipkart.pricing.apps.kaizen"}, excludeFilters = {@ComponentScan.Filter(value = {DataSourceConfiguration.class}, type = FilterType.ASSIGNABLE_TYPE)})
@Import({DataSourceConfiguration.class,HibernateConfiguration.class})
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
}
