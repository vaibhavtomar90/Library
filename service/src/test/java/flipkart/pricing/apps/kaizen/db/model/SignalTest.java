package flipkart.pricing.apps.kaizen.db.model;


import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.assertTrue;

public class SignalTest {

    @Test
    public void testCompareToForSameListingAndSignalTypeIds() {
        long currTime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(currTime);
        Signal signal1 = new Signal(1l, 1l, "foo", 1l, timestamp, timestamp);
        Signal signal2 = new Signal(1l, 1l, "bar", 2l, new Timestamp(currTime + 100), new Timestamp(currTime + 100));
        assertTrue(signal1.compareTo(signal2) == 0);
    }

    @Test
    public void testCompareToForSameListingIdButDifferentSignalTypeIds() {
        long currTime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(currTime);
        Signal signal1 = new Signal(1l, 2l, "foo", 1l, timestamp, timestamp);
        Signal signal2 = new Signal(1l, 3l, "foo", 1l, timestamp, timestamp);
        assertTrue(signal1.compareTo(signal2) < 0);
        assertTrue(signal2.compareTo(signal1) > 0);
    }

    @Test
    public void testCompareToForSameSignalTypeIdButDifferentListingIds() {
        long currTime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(currTime);
        Signal signal1 = new Signal(2l, 1l, "foo", 1l, timestamp, timestamp);
        Signal signal2 = new Signal(3l, 1l, "foo", 1l, timestamp, timestamp);
        assertTrue(signal1.compareTo(signal2) < 0);
        assertTrue(signal2.compareTo(signal1) > 0);
    }

    @Test
    public void testCompareToForDifferentSignalTypeAndListingIds() {
        long currTime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(currTime);
        Signal signal1 = new Signal(1l, 2l, "foo", 1l, timestamp, timestamp);
        Signal signal2 = new Signal(3l, 4l, "foo", 1l, timestamp, timestamp);
        assertTrue(signal1.compareTo(signal2) < 0);
        assertTrue(signal2.compareTo(signal1) > 0);
    }

}
