package flipkart.pricing.apps.kaizen.boot.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

/**
 * @understands : Spring context configuration
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({"flipkart.pricing.apps.kaizen"})
public class KaizenContextConfiguration {
    @Deprecated // for CGLIB only
    public KaizenContextConfiguration() {
    }

    @Bean
    @Qualifier("yamlPropertiesLoader")
    public YamlPropertiesFactoryBean yamlPropertiesLoader() {
        final YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new CustomYamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(new ClassPathResource(KaizenConfiguration.YML_CONFIG_FILE));
        return yamlPropertiesFactoryBean;
    }

    //TODO ideally this session factory should be the same as used by dropwizard
    @Bean
    public LocalSessionFactoryBean sessionFactory(@Qualifier("yamlPropertiesLoader") YamlPropertiesFactoryBean yamlPropertiesFactoryBean) throws PropertyVetoException {
       final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
       sessionFactory.setDataSource(dataSource(yamlPropertiesFactoryBean));
       sessionFactory.setPackagesToScan("flipkart.pricing.apps.kaizen.db.model");
       sessionFactory.setHibernateProperties(hibernateProperties(yamlPropertiesFactoryBean));
       return sessionFactory;
    }

    @Bean
    public DataSource dataSource(YamlPropertiesFactoryBean yamlPropertiesFactoryBean) throws PropertyVetoException {
        Properties properties = yamlPropertiesFactoryBean.getObject();
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(properties.getProperty("database.driverClass"));
        dataSource.setJdbcUrl(properties.getProperty("database.url"));
        dataSource.setUser(properties.getProperty("database.user"));
        dataSource.setPassword(properties.getProperty("database.pass"));
        return dataSource;
    }


    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }


    private Properties hibernateProperties(YamlPropertiesFactoryBean yamlPropertiesFactoryBean) {
        Properties properties = yamlPropertiesFactoryBean.getObject();
        return new Properties() {
            {
                setProperty("hibernate.hbm2ddl.auto", properties.getProperty("database.properties.hibernate.hbm2ddl.auto"));
                setProperty("hibernate.dialect", properties.getProperty("database.properties.hibernate.dialect"));
                setProperty("hibernate.connection.isolation", properties.getProperty("database.properties.hibernate.connection.isolation"));
            }
        };
    }
}
