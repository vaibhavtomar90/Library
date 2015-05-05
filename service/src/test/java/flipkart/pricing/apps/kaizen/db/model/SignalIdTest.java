package flipkart.pricing.apps.kaizen.db.model;

import org.junit.Test;
import static org.junit.Assert.assertTrue;


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
