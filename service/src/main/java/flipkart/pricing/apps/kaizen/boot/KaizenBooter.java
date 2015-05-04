package flipkart.pricing.apps.kaizen.boot;

import flipkart.pricing.apps.kaizen.boot.bundles.SpringBundle;
import flipkart.pricing.apps.kaizen.boot.config.KaizenConfiguration;
import flipkart.pricing.apps.kaizen.db.domain.ListingInfo;
import flipkart.pricing.apps.kaizen.db.domain.Signal;
import flipkart.pricing.apps.kaizen.db.domain.SignalDataTypes;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * @understands Kaizen boots from here!
 */
public class KaizenBooter extends Application<KaizenConfiguration>
{
    public static void main( String[] args ) throws Exception {
        new KaizenBooter().run(args);
    }

    @Override
    public void initialize(Bootstrap<KaizenConfiguration> bootstrap) {
        super.initialize(bootstrap);
        bootstrap.addBundle(new SpringBundle());
        bootstrap.addBundle(new HibernateBundle<KaizenConfiguration>(ListingInfo.class, Signal.class, SignalDataTypes.class) {
            @Override
            public DataSourceFactory getDataSourceFactory(KaizenConfiguration kaizenConfiguration) {
                return kaizenConfiguration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(new MigrationsBundle<KaizenConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(KaizenConfiguration kaizenConfiguration) {
                return kaizenConfiguration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(KaizenConfiguration kaizenConfiguration, Environment environment) throws Exception {
        // Do nothing - all taken care by the SpringBundle
    }
}
