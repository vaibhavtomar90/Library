package flipkart.pricing.apps.kaizen.db.dao;


import flipkart.pricing.apps.kaizen.db.model.SignalDataType;
import flipkart.pricing.apps.kaizen.db.model.SignalType;
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
        List<SignalType> signalTypes = new ArrayList<>();
        signalTypes.add(new SignalType("foo", SignalDataType.DOUBLE, "1.0"));
        signalTypes.add(new SignalType("bar", SignalDataType.INT, "1"));
        SignalTypeDao signalTypeDao = new SignalTypeDao(hibernateSessionTestRule.getSessionFactory());
        for (SignalType signalType : signalTypes) {
            signalTypeDao.insertSignalType(signalType);
        }
        List<SignalType> fetchedSignalTypes = signalTypeDao.fetchAll();
        assertTrue(fetchedSignalTypes.size() == 2);
        int idx = 0;
        for (SignalType fetchedSignalType : fetchedSignalTypes) {
            SignalType persistedSignalType = signalTypes.get(idx);
            assertNotNull(fetchedSignalType.getId());
            assertTrue(fetchedSignalType.equals(persistedSignalType));
            idx ++;
        }
    }

    @Test
    public void shouldCreateNameSignalTypeMap() {
        List<SignalType> signalTypes = new ArrayList<>();
        signalTypes.add(new SignalType("foo", SignalDataType.DOUBLE, "1.0"));
        signalTypes.add(new SignalType("bar", SignalDataType.INT, "1"));
        SignalTypeDao signalTypeDao = new SignalTypeDao(hibernateSessionTestRule.getSessionFactory());
        for (SignalType signalType : signalTypes) {
            signalTypeDao.insertSignalType(signalType);
        }
        Map<String, SignalType> fetchedNameSignalTypesMap = signalTypeDao.fetchNameSignalTypesMap();
        assertTrue(fetchedNameSignalTypesMap.size() == 2);
        for (SignalType persistedSignalType : signalTypes) {
            SignalType fetchedSignalType = fetchedNameSignalTypesMap.get(persistedSignalType.getName());
            assertNotNull(fetchedSignalType.getId());
            assertTrue(fetchedSignalType.equals(persistedSignalType));
        }
    }


}
