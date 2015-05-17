package flipkart.pricing.apps.kaizen.testrules;

import org.junit.rules.ExternalResource;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Entity;
import javax.sql.DataSource;
import java.util.Map;

@Named
public class DbClearRule extends ExternalResource {
    @Inject
    private DataSource dataSource;

    @Inject
    private ApplicationContext applicationContext;

    @Override
    protected void before() throws Throwable {
        super.before();
        JdbcTestUtils.deleteFromTables(new JdbcTemplate(dataSource),"SignalType","ListingInfo"); // TODO Implement this better
    }
}
