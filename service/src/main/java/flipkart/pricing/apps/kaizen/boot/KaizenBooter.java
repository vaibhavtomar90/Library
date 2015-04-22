package flipkart.pricing.apps.kaizen.boot;

import flipkart.pricing.apps.kaizen.boot.config.KaizenConfiguration;
import flipkart.pricing.apps.kaizen.boot.healthcheck.RotationStatusHealthCheck;
import io.dropwizard.Application;
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
    public void run(KaizenConfiguration kaizenConfiguration, Environment environment) throws Exception {
        environment.healthChecks().register("Rotation Status",new RotationStatusHealthCheck());
    }
}
