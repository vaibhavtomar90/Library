package flipkart.pricing.apps.kaizen.db.dao;


import flipkart.pricing.apps.kaizen.db.model.SignalDataTypes;
import flipkart.pricing.apps.kaizen.db.model.SignalTypes;
import flipkart.pricing.apps.kaizen.testrules.HibernateSessionTestRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


public class SignalTypeDaoTest {

    @Rule
    public HibernateSessionTestRule hibernateSessionTestRule = new HibernateSessionTestRule();

    @Test
    public void shouldPersistAndFetchAllSignalTypes() {
        List<SignalTypes> signalTypes = new ArrayList<>();
        signalTypes.add(new SignalTypes("foo", SignalDataTypes.DOUBLE, "1.0"));
        signalTypes.add(new SignalTypes("bar", SignalDataTypes.INT, "1"));
        SignalTypeDao signalTypeDao = new SignalTypeDao(hibernateSessionTestRule.getSessionFactory());
        for (SignalTypes signalType : signalTypes) {
            signalTypeDao.insertSignalType(signalType);
        }
        List<SignalTypes> fetchedSignalTypes = signalTypeDao.fetchAll();
        assertTrue(fetchedSignalTypes.size() == 2);
        int idx = 0;
        for (SignalTypes fetchedSignalType : fetchedSignalTypes) {
            SignalTypes persistedSignalType = signalTypes.get(idx);
            assertNotNull(fetchedSignalType.getId());
            assertTrue(fetchedSignalType.equals(persistedSignalType));
            idx ++;
        }
    }

    @Test
    public void shouldCreateNameSignalTypeMap() {
        List<SignalTypes> signalTypes = new ArrayList<>();
        signalTypes.add(new SignalTypes("foo", SignalDataTypes.DOUBLE, "1.0"));
        signalTypes.add(new SignalTypes("bar", SignalDataTypes.INT, "1"));
        SignalTypeDao signalTypeDao = new SignalTypeDao(hibernateSessionTestRule.getSessionFactory());
        for (SignalTypes signalType : signalTypes) {
            signalTypeDao.insertSignalType(signalType);
        }
        Map<String, SignalTypes> fetchedNameSignalTypesMap = signalTypeDao.fetchNameSignalTypesMap();
        assertTrue(fetchedNameSignalTypesMap.size() == 2);
        for (SignalTypes persistedSignalType : signalTypes) {
            SignalTypes fetchedSignalType = fetchedNameSignalTypesMap.get(persistedSignalType.getName());
            assertNotNull(fetchedSignalType.getId());
            assertTrue(fetchedSignalType.equals(persistedSignalType));
        }
    }


}
