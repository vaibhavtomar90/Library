package flipkart.pricing.apps.kaizen.db.model;


import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.assertTrue;

public class SignalInfoTest {

    @Test
    public void testCompareToForSameListingAndSignalTypeIds() {
        SignalInfo signalInfo1 = new SignalInfo(1l, 1l, "foo", 1l);
        SignalInfo signalInfo2 = new SignalInfo(1l, 1l, "bar", 2l);
        assertTrue(signalInfo1.compareTo(signalInfo2) == 0);
    }

    @Test
    public void testCompareToForSameListingIdButDifferentSignalTypeIds() {
        SignalInfo signalInfo1 = new SignalInfo(1l, 2l, "foo", 1l);
        SignalInfo signalInfo2 = new SignalInfo(1l, 3l, "foo", 1l);
        assertTrue(signalInfo1.compareTo(signalInfo2) < 0);
        assertTrue(signalInfo2.compareTo(signalInfo1) > 0);
    }

    @Test
    public void testCompareToForSameSignalTypeIdButDifferentListingIds() {
        SignalInfo signalInfo1 = new SignalInfo(2l, 1l, "foo", 1l);
        SignalInfo signalInfo2 = new SignalInfo(3l, 1l, "foo", 1l);
        assertTrue(signalInfo1.compareTo(signalInfo2) < 0);
        assertTrue(signalInfo2.compareTo(signalInfo1) > 0);
    }

    @Test
    public void testCompareToForDifferentSignalTypeAndListingIds() {
        SignalInfo signalInfo1 = new SignalInfo(1l, 2l, "foo", 1l);
        SignalInfo signalInfo2 = new SignalInfo(3l, 4l, "foo", 1l);
        assertTrue(signalInfo1.compareTo(signalInfo2) < 0);
        assertTrue(signalInfo2.compareTo(signalInfo1) > 0);
    }

}
