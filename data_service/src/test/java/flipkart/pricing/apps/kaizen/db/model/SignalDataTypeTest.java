package flipkart.pricing.apps.kaizen.db.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SignalDataTypeTest {

    @Test
    public void isValidForIntShouldWork() {
        assertTrue(SignalDataType.INT.isValid(null));
        assertTrue(SignalDataType.INT.isValid("1234"));
        assertFalse(SignalDataType.INT.isValid("123foo"));
        assertFalse(SignalDataType.INT.isValid("123.34"));
        assertFalse(SignalDataType.INT.isValid("23456788854123"));
    }

    @Test
    public void isValidForStringShouldWork() {
        assertTrue(SignalDataType.STRING.isValid(null));
        assertTrue(SignalDataType.STRING.isValid(""));
        assertTrue(SignalDataType.STRING.isValid("foo"));
    }

    @Test
    public void isValidForDoubleShouldWork() {
        assertTrue(SignalDataType.DOUBLE.isValid(null));
        assertTrue(SignalDataType.DOUBLE.isValid("1"));
        assertTrue(SignalDataType.DOUBLE.isValid("12.34345844900330099"));
        assertFalse(SignalDataType.DOUBLE.isValid("12.34foo"));
    }

    @Test
    public void isValidForFloatShouldWork() {
        assertTrue(SignalDataType.FLOAT.isValid(null));
        assertTrue(SignalDataType.FLOAT.isValid("1"));
        assertTrue(SignalDataType.FLOAT.isValid("12.34345844900330099"));
        assertFalse(SignalDataType.FLOAT.isValid("12.34foo"));
    }

    @Test
    public void isValidForLongShouldWork() {
        assertTrue(SignalDataType.LONG.isValid(null));
        assertTrue(SignalDataType.LONG.isValid("23456788854123"));
        assertFalse(SignalDataType.INT.isValid("123foo"));
        assertFalse(SignalDataType.INT.isValid("123.34"));
    }

    @Test
    public void isValidForPriceShouldWork() {
        assertTrue(SignalDataType.DOUBLE.isValid(null));
        assertTrue(SignalDataType.DOUBLE.isValid("1"));
        assertTrue(SignalDataType.DOUBLE.isValid("12.34345844900330099"));
        assertFalse(SignalDataType.DOUBLE.isValid("12.34foo"));
    }

    @Test
    public void testNeedQualifier() {
        assertFalse(SignalDataType.DOUBLE.needsQualifier());
        assertFalse(SignalDataType.FLOAT.needsQualifier());
        assertFalse(SignalDataType.INT.needsQualifier());
        assertFalse(SignalDataType.LONG.needsQualifier());
        assertFalse(SignalDataType.STRING.needsQualifier());
        assertTrue(SignalDataType.PRICE.needsQualifier());
    }
}
