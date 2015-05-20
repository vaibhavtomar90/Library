package flipkart.pricing.apps.kaizen.boot.config;

import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
public class HibernateConfiguration {
    //TODO ideally this session factory should be the same as used by dropwizard
    @Bean
    public LocalSessionFactoryBean sessionFactory(@Value("${database.properties.hibernate.hbm2ddl.auto}") String hibernateDDlAutoSetting,
                                                  @Value("${database.properties.hibernate.dialect}") String hibernateDialect,
                                                  @Value("${database.properties.hibernate.connection.isolation}") String hibernateIsolation,
                                                  DataSource dataSource
    ) throws PropertyVetoException {
        final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("flipkart.pricing.apps.kaizen.db.model");
        sessionFactory.setHibernateProperties(new Properties() {
            {
                setProperty("hibernate.hbm2ddl.auto", hibernateDDlAutoSetting);
                setProperty("hibernate.dialect", hibernateDialect);
                setProperty("hibernate.connection.isolation", hibernateIsolation);
            }
        });
        return sessionFactory;
    }


    @Bean
    public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }
}
