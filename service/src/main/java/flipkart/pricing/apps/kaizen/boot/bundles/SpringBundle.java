package flipkart.pricing.apps.kaizen.boot.bundles;

import com.codahale.metrics.health.HealthCheck;
import flipkart.pricing.apps.kaizen.boot.config.KaizenConfiguration;
import flipkart.pricing.apps.kaizen.boot.config.KaizenContextConfiguration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.ws.rs.Path;
import java.util.Map;

/**
 * @understands : Bootstraps the spring application context
 */
public class SpringBundle implements ConfiguredBundle<KaizenConfiguration> {
    @Override
    public void run(KaizenConfiguration configuration, Environment environment) throws Exception {
        // TODO : Use the DB config in spring.
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(KaizenContextConfiguration.class);
        final Map<String, Object> resources = context.getBeansWithAnnotation(Path.class);
        resources.forEach((name, res) -> environment.jersey().register(res));
        final Map<String, HealthCheck> healthChecks = context.getBeansOfType(HealthCheck.class);
        healthChecks.forEach((name,hc) -> environment.healthChecks().register(name,hc));
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        // Do Nothing
    }
}
