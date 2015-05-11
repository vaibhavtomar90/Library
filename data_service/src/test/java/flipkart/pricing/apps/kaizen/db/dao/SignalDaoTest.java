package flipkart.pricing.apps.kaizen.db.dao;

import flipkart.pricing.apps.kaizen.db.model.Signal;
import flipkart.pricing.apps.kaizen.testrules.HibernateSessionTestRule;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SignalDaoTest {

    @Rule
    public HibernateSessionTestRule hibernateSessionTestRule = new HibernateSessionTestRule();


    @Test
    public void shouldSetServerTimestampOnCreateSignal() {
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        Signal persistedSignal = new Signal(1l, 2l, "1.0", 0l);
        signalDao.insertOrUpdateSignal(persistedSignal);
        Signal fetchedSignal = signalDao.fetchSignals(1l, 2l);
        assertNotNull(fetchedSignal.getServerTimestamp());
    }

    @Test
    public void shouldUpdateServerTimestampOnUpdateSignal() throws InterruptedException{
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        Signal persistedSignal = new Signal(1l, 2l, "1.0", 0l);
        signalDao.insertOrUpdateSignal(persistedSignal);
        Signal fetchedSignal1 = signalDao.fetchSignals(1l, 2l);
        Signal updatedSignal = new Signal(1l, 2l, "2.0", 1l);
        Thread.sleep(1000l); //Adding a small sleep so that the timestamps are different
        signalDao.insertOrUpdateSignal(updatedSignal);
        hibernateSessionTestRule.clearSession(); //Otherwise hibernate is caching results
        Signal fetchedSignal2 = signalDao.fetchSignals(1l, 2l);
        assertTrue(fetchedSignal2.getServerTimestamp().compareTo(fetchedSignal1.getServerTimestamp()) == 1);
    }

    @Test
    public void shouldInsertSignalIfNotPresent() {
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        Signal persistedSignal = new Signal(1l, 2l, "1.0", 0l);
        int insertions = signalDao.insertOrUpdateSignal(persistedSignal);
        assertTrue(insertions == 1);
        Signal fetchedSignal = signalDao.fetchSignals(1l, 2l);
        assertTrue(fetchedSignal.equals(persistedSignal));
    }

    @Test
    public void shouldUpdateSignalIfNewVersionIsGreater() {
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        Signal persistedSignal = new Signal(1l, 2l, "1.0", 0l);
        signalDao.insertOrUpdateSignal(persistedSignal);
        Signal updatedSignal = new Signal(1l, 2l, "2.0", 1l);
        int updates = signalDao.insertOrUpdateSignal(updatedSignal);
        assertTrue(updates == 1);
        hibernateSessionTestRule.clearSession();
        Signal fetchedSignal = signalDao.fetchSignals(1l, 2l);
        assertTrue(fetchedSignal.equals(updatedSignal));
    }

    @Test
    public void shouldNotUpdateSignalIfNewVersionIsLesser() {
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        Signal persistedSignal = new Signal(1l, 2l, "1.0", 1l);
        signalDao.insertOrUpdateSignal(persistedSignal);
        Signal updatedSignal = new Signal(1l, 2l, "2.0", 0l);
        int updates = signalDao.insertOrUpdateSignal(updatedSignal);
        assertTrue(updates == 0);
        hibernateSessionTestRule.clearSession();
        Signal fetchedSignal = signalDao.fetchSignals(1l, 2l);
        assertTrue(fetchedSignal.equals(persistedSignal));
    }

    @Test
    public void shouldFetchSignals() {
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        Signal signal = new Signal(1l, 1l, "1.0", 1l);
        signalDao.insertOrUpdateSignal(signal);
        Signal signal1 = new Signal(1l, 2l, "2.0", 2l);
        signalDao.insertOrUpdateSignal(signal1);
        Signal signal2 = new Signal(1l, 3l, "3.0", 3l);
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
