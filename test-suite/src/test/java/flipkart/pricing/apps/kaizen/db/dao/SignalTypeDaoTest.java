package flipkart.pricing.apps.kaizen.db.dao;


import flipkart.pricing.apps.kaizen.boot.config.KaizenContextConfiguration;
import flipkart.pricing.apps.kaizen.db.model.SignalDataType;
import flipkart.pricing.apps.kaizen.db.model.SignalType;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = KaizenContextConfiguration.class)
@ActiveProfiles("test")
public class SignalTypeDaoTest {

    @Inject
    private SignalTypeDao signalTypeDao;

    @Test
    @Transactional
    public void shouldPersistAndFetchAllSignalTypes() {
        List<SignalType> signalTypes = new ArrayList<>();
        signalTypes.add(new SignalType("foo", SignalDataType.DOUBLE, "1.0"));
        signalTypes.add(new SignalType("bar", SignalDataType.INT, "1"));
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
    @Transactional
    public void shouldCreateNameSignalTypeMap() {
        List<SignalType> signalTypes = new ArrayList<>();
        signalTypes.add(new SignalType("foo", SignalDataType.DOUBLE, "1.0"));
        signalTypes.add(new SignalType("bar", SignalDataType.INT, "1"));
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

    @Test(expected = ConstraintViolationException.class)
    @Transactional
    public void shouldThrowExceptionWhilePersistingSameSignalNameTwice() {
       signalTypeDao.insertSignalType(new SignalType("foo", SignalDataType.DOUBLE, "1.0"));
       signalTypeDao.insertSignalType(new SignalType("foo", SignalDataType.DOUBLE, "1.0"));
    }


}
