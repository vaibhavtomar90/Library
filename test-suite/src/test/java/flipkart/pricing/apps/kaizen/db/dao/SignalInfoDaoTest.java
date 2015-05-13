package flipkart.pricing.apps.kaizen.db.dao;

import flipkart.pricing.apps.kaizen.boot.config.KaizenContextConfiguration;
import flipkart.pricing.apps.kaizen.db.model.SignalInfo;
import flipkart.pricing.apps.kaizen.utils.HibernateSessionUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = KaizenContextConfiguration.class)
@ActiveProfiles("test")
public class SignalInfoDaoTest {

    @Inject
    private SignalInfoDao signalInfoDao;

    @Inject
    private HibernateSessionUtil hibernateSessionUtil;

    @Test
    @Transactional
    public void shouldSetServerTimestampOnCreateSignal() {
        SignalInfo persistedSignalInfo = new SignalInfo(1l, 2l, "1.0", 0l);
        signalInfoDao.insertOrUpdateSignal(persistedSignalInfo);
        SignalInfo fetchedSignalInfo = signalInfoDao.fetchSignals(1l, 2l);
        assertNotNull(fetchedSignalInfo.getServerTimestamp());
    }

    @Test
    @Transactional
    public void shouldUpdateServerTimestampOnUpdateSignal() throws InterruptedException{
        SignalInfo persistedSignalInfo = new SignalInfo(1l, 2l, "1.0", 0l);
        signalInfoDao.insertOrUpdateSignal(persistedSignalInfo);
        SignalInfo fetchedSignalInfo1 = signalInfoDao.fetchSignals(1l, 2l);
        SignalInfo updatedSignalInfo = new SignalInfo(1l, 2l, "2.0", 1l);
        Thread.sleep(1000l); //Adding a small sleep so that the timestamps are different
        signalInfoDao.insertOrUpdateSignal(updatedSignalInfo);
        hibernateSessionUtil.clearSession();
        SignalInfo fetchedSignalInfo2 = signalInfoDao.fetchSignals(1l, 2l);
        assertTrue(fetchedSignalInfo2.getServerTimestamp().compareTo(fetchedSignalInfo1.getServerTimestamp()) == 1);

    }

    @Test
    @Transactional
    public void shouldInsertSignalIfNotPresent() {
        SignalInfo persistedSignalInfo = new SignalInfo(1l, 2l, "1.0", 0l);
        int insertions = signalInfoDao.insertOrUpdateSignal(persistedSignalInfo);
        assertTrue(insertions == 1);
        SignalInfo fetchedSignalInfo = signalInfoDao.fetchSignals(1l, 2l);
        assertTrue(fetchedSignalInfo.equals(persistedSignalInfo));
    }

    @Test
    @Transactional
    public void shouldUpdateSignalIfNewVersionIsGreater() {
        SignalInfo persistedSignalInfo = new SignalInfo(1l, 2l, "1.0", 0l);
        signalInfoDao.insertOrUpdateSignal(persistedSignalInfo);
        SignalInfo updatedSignalInfo = new SignalInfo(1l, 2l, "2.0", 1l);
        int updates = signalInfoDao.insertOrUpdateSignal(updatedSignalInfo);
        assertTrue(updates == 1);
        hibernateSessionUtil.clearSession();
        SignalInfo fetchedSignalInfo = signalInfoDao.fetchSignals(1l, 2l);
        assertTrue(fetchedSignalInfo.equals(updatedSignalInfo));
    }

    @Test
    @Transactional
    public void shouldNotUpdateSignalIfNewVersionIsLesser() {
        SignalInfo persistedSignalInfo = new SignalInfo(1l, 2l, "1.0", 1l);
        signalInfoDao.insertOrUpdateSignal(persistedSignalInfo);
        SignalInfo updatedSignalInfo = new SignalInfo(1l, 2l, "2.0", 0l);
        int updates = signalInfoDao.insertOrUpdateSignal(updatedSignalInfo);
        assertTrue(updates == 0);
        hibernateSessionUtil.clearSession();
        SignalInfo fetchedSignalInfo = signalInfoDao.fetchSignals(1l, 2l);
        assertTrue(fetchedSignalInfo.equals(persistedSignalInfo));
    }

    @Test
    @Transactional
    public void shouldFetchSignals() {
        SignalInfo signalInfo = new SignalInfo(1l, 1l, "1.0", 1l);
        signalInfoDao.insertOrUpdateSignal(signalInfo);
        SignalInfo signalInfo1 = new SignalInfo(1l, 2l, "2.0", 2l);
        signalInfoDao.insertOrUpdateSignal(signalInfo1);
        SignalInfo signalInfo2 = new SignalInfo(1l, 3l, "3.0", 3l);
        signalInfoDao.insertOrUpdateSignal(signalInfo2);
        List<SignalInfo> fetchedSignalInfos = signalInfoDao.fetchSignals(1l);
        assertTrue(fetchedSignalInfos.equals(Arrays.asList(signalInfo, signalInfo1, signalInfo2)));
        assertTrue(signalInfoDao.fetchSignals(1l, 1l).equals(signalInfo));
        assertTrue(signalInfoDao.fetchSignals(1l, 2l).equals(signalInfo1));
        assertTrue(signalInfoDao.fetchSignals(1l, 3l).equals(signalInfo2));
        List<SignalInfo> fetchedSignals1 = signalInfoDao.fetchSignals(1l, Arrays.asList(1l, 2l));
        assertTrue(fetchedSignals1.equals(Arrays.asList(signalInfo, signalInfo1)));
    }

}
