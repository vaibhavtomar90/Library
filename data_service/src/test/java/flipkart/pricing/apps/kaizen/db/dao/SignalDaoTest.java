package flipkart.pricing.apps.kaizen.db.dao;

import flipkart.pricing.apps.kaizen.db.model.Signal;
import flipkart.pricing.apps.kaizen.testrules.HibernateSessionTestRule;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class SignalDaoTest {

    @Rule
    public HibernateSessionTestRule hibernateSessionTestRule = new HibernateSessionTestRule();

    @Test
    public void shouldInsertSignalIfNotPresent() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MILLISECOND, 0);
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        Timestamp timestamp = new Timestamp(c.getTimeInMillis());
        Signal persistedSignal = new Signal(1l, 2l, "1.0", 0l, timestamp);
        int insertions = signalDao.insertOrUpdateSignal(persistedSignal);
        assertTrue(insertions == 1);
        Signal fetchedSignal = signalDao.fetchSignals(1l, 2l);
        assertTrue(fetchedSignal.equals(persistedSignal));
    }

    @Test
    public void shouldUpdateSignalIfNewVersionIsGreater() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MILLISECOND, 0);
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        Timestamp timestamp = new Timestamp(c.getTimeInMillis());
        Signal persistedSignal = new Signal(1l, 2l, "1.0", 0l, timestamp);
        signalDao.insertOrUpdateSignal(persistedSignal);
        Signal updatedSignal = new Signal(1l, 2l, "2.0", 1l, timestamp);
        int updates = signalDao.insertOrUpdateSignal(updatedSignal);
        assertTrue(updates == 1);
    }

    @Test
    public void shouldNotUpdateSignalIfNewVersionIsLesser() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MILLISECOND, 0);
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        Timestamp timestamp = new Timestamp(c.getTimeInMillis());
        Signal persistedSignal = new Signal(1l, 2l, "1.0", 1l, timestamp);
        signalDao.insertOrUpdateSignal(persistedSignal);
        Signal updatedSignal = new Signal(1l, 2l, "2.0", 0l, timestamp);
        int updates = signalDao.insertOrUpdateSignal(updatedSignal);
        assertTrue(updates == 0);
    }

    @Test
    public void shouldFetchSignals() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MILLISECOND, 0);
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        Timestamp timestamp = new Timestamp(c.getTimeInMillis());
        Signal signal = new Signal(1l, 1l, "1.0", 1l, timestamp);
        signalDao.insertOrUpdateSignal(signal);
        Signal signal1 = new Signal(1l, 2l, "2.0", 2l, timestamp);
        signalDao.insertOrUpdateSignal(signal1);
        Signal signal2 = new Signal(1l, 3l, "3.0", 3l, timestamp);
        signalDao.insertOrUpdateSignal(signal2);
        List<Signal> fetchedSignals = signalDao.fetchSignals(1l);
        assertTrue(fetchedSignals.equals(Arrays.asList(signal, signal1, signal2)));
        assertTrue(signalDao.fetchSignals(1l, 1l).equals(signal));
        assertTrue(signalDao.fetchSignals(1l, 2l).equals(signal1));
        assertTrue(signalDao.fetchSignals(1l, 3l).equals(signal2));
        List<Signal> fetchedSignals1 = signalDao.fetchSignals(1l, Arrays.asList(1l, 2l));
        assertTrue(fetchedSignals1.equals(Arrays.asList(signal, signal1)));
    }
}
