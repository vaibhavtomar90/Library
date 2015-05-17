package flipkart.pricing.apps.kaizen.utils;

import flipkart.pricing.apps.kaizen.db.dao.SignalTypeDao;
import flipkart.pricing.apps.kaizen.db.model.SignalDataTypes;
import flipkart.pricing.apps.kaizen.db.model.SignalTypes;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import static flipkart.pricing.apps.kaizen.testrules.SignalTypesInjectionRule.*;


@Named
@Transactional
public class SignalInsertHelper {
    private final SignalTypeDao signalTypeDao;

    @Inject
    public SignalInsertHelper(SignalTypeDao signalTypeDao) {
        this.signalTypeDao = signalTypeDao;
    }

    public void createSignals() {
        signalTypeDao.insertSignalType(new SignalTypes(MRP_PRICE_TYPE_SIGNALTYPE, SignalDataTypes.PRICE, null));
        signalTypeDao.insertSignalType(new SignalTypes(ATP_INT_TYPE_SIGNALTYPE, SignalDataTypes.INT, "0"));
        signalTypeDao.insertSignalType(new SignalTypes(BAND_DOUBLE_TYPE_SIGNALTYPE, SignalDataTypes.DOUBLE, "0.0"));
        signalTypeDao.insertSignalType(new SignalTypes(BRAND_STRING_TYPE_SIGNALTYPE, SignalDataTypes.STRING,null));
        signalTypeDao.insertSignalType(new SignalTypes(PAGEHITS_LONG_TYPE_SIGNALTYPE, SignalDataTypes.LONG, "0"));
    }
}
