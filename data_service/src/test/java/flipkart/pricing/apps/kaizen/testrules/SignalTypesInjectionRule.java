package flipkart.pricing.apps.kaizen.testrules;

import flipkart.pricing.apps.kaizen.utils.SignalInsertHelper;
import org.junit.rules.ExternalResource;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

@Named
public class SignalTypesInjectionRule extends ExternalResource {

    public static final String ATP_INT_TYPE_SIGNALTYPE = "atp";
    public static final String BAND_DOUBLE_TYPE_SIGNALTYPE = "band";
    public static final String BRAND_STRING_TYPE_SIGNALTYPE = "brand";
    public static final String PAGEHITS_LONG_TYPE_SIGNALTYPE = "length";
    public static final String MRP_PRICE_TYPE_SIGNALTYPE = "mrp";

    @Inject
    private DataSource dataSource;

    @Inject
    private SignalInsertHelper signalInsertHelper;

    @Override
    protected void before() throws Throwable {
        super.before();
        signalInsertHelper.createSignals();
    }

}
