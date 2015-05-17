package flipkart.pricing.apps.kaizen.utils;

import flipkart.pricing.apps.kaizen.db.dao.SignalTypeDao;
import flipkart.pricing.apps.kaizen.db.model.SignalDataType;
import flipkart.pricing.apps.kaizen.db.model.SignalType;

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
        signalTypeDao.insertSignalType(new SignalType(MRP_PRICE_TYPE_SIGNALTYPE, SignalDataType.PRICE, null));
        signalTypeDao.insertSignalType(new SignalType(ATP_INT_TYPE_SIGNALTYPE, SignalDataType.INT, "0"));
        signalTypeDao.insertSignalType(new SignalType(BAND_DOUBLE_TYPE_SIGNALTYPE, SignalDataType.DOUBLE, "0.0"));
        signalTypeDao.insertSignalType(new SignalType(BRAND_STRING_TYPE_SIGNALTYPE, SignalDataType.STRING,null));
        signalTypeDao.insertSignalType(new SignalType(PAGEHITS_LONG_TYPE_SIGNALTYPE, SignalDataType.LONG, "0"));
    }
}
