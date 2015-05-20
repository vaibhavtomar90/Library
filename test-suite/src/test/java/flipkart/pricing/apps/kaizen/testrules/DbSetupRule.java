package flipkart.pricing.apps.kaizen.testrules;

import org.junit.rules.ExternalResource;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class DbSetupRule extends ExternalResource {

    @Inject
    private DbClearRule dbClearRule;

    @Inject
    private SignalTypesInjectionRule signalTypesInjectionRule;

    @Override
    protected void before() throws Throwable {
        super.before();
        dbClearRule.before();
        signalTypesInjectionRule.before();
    }
}
