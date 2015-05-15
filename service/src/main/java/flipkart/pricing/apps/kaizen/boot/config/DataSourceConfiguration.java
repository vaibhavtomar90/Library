package flipkart.pricing.apps.kaizen.boot.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
public class DataSourceConfiguration {

    @Bean
    public DataSource dataSource(@Value("${database.driverClass}") String dbDriverClass,
                                 @Value("${database.url}") String dbUrl,
                                 @Value("${database.user}") String dbUser,
                                 @Value("${database.password}") String dbPass
                                 ) throws PropertyVetoException {
        ComboPooledDataSource comboDataSource = new ComboPooledDataSource();
        comboDataSource.setDriverClass(dbDriverClass);
        comboDataSource.setJdbcUrl(dbUrl);
        comboDataSource.setUser(dbUser);
        comboDataSource.setPassword(dbPass);
        return comboDataSource;
    }

}
