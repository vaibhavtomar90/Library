package flipkart.pricing.apps.kaizen.testrules;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import java.util.Map;

/**
 * @understands : Wrapper around Dw's ResourceTestRule, Used for adding spring autowired resources
 */
@Component
public class SpringAwareResourceTestRule implements TestRule {
    private ResourceTestRule resourceTestRule;

    @Autowired
    public SpringAwareResourceTestRule(ApplicationContext applicationContext) {
        Map<String, Object> resources = applicationContext.getBeansWithAnnotation(Path.class);
        final ResourceTestRule.Builder builder = new ResourceTestRule.Builder();
        resources.values().forEach(builder::addResource);
        this.resourceTestRule = builder.build();
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return resourceTestRule.apply(base,description);
    }

    public Client client() {
        return this.resourceTestRule.client();
    }
}
