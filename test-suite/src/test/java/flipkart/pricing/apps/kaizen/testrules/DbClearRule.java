package flipkart.pricing.apps.kaizen.testrules;

import org.junit.rules.ExternalResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

@Named
public class DbClearRule extends ExternalResource {
    @Inject
    private DataSource dataSource;

    @Override
    protected void before() throws Throwable {
        super.before();
        JdbcTestUtils.deleteFromTables(new JdbcTemplate(dataSource),"signal_types","listing_infos"); // TODO Implement this better after merging master
    }
}
