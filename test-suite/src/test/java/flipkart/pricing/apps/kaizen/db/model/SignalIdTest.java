package flipkart.pricing.apps.kaizen.db.model;

import flipkart.pricing.apps.kaizen.boot.config.KaizenContextConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = KaizenContextConfiguration.class)
@ActiveProfiles("test")
public class SignalIdTest {

    @Test
    public void testCompareToForSameListingAndSignalTypeIds() {
        SignalId signalId1 = new SignalId(1l, 1l);
        SignalId signalId2 = new SignalId(1l, 1l);
        assertTrue(signalId1.compareTo(signalId2) == 0);
    }

    @Test
    public void testCompareToForSameListingIdButDifferentSignalTypeIds() {
        SignalId signalId1 = new SignalId(1l, 1l);
        SignalId signalId2 = new SignalId(1l, 2l);
        assertTrue(signalId1.compareTo(signalId2) < 0);
        assertTrue(signalId2.compareTo(signalId1) > 0);
    }

    @Test
    public void testCompareToForSameSignalTypeIdButDifferentListingIds() {
        SignalId signalId1 = new SignalId(1l, 1l);
        SignalId signalId2 = new SignalId(2l, 1l);
        assertTrue(signalId1.compareTo(signalId2) < 0);
        assertTrue(signalId2.compareTo(signalId1) > 0);
    }

    @Test
    public void testCompareToForDifferentListingAndSignalTypeIds() {
        SignalId signalId1 = new SignalId(1l, 2l);
        SignalId signalId2 = new SignalId(2l, 1l);
        assertTrue(signalId1.compareTo(signalId2) < 0);
        assertTrue(signalId2.compareTo(signalId1) > 0);
    }
}
