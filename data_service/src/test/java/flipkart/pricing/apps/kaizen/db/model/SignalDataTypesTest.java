package flipkart.pricing.apps.kaizen.db.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SignalDataTypesTest {

    @Test
    public void isValidForIntShouldWork() {
        assertTrue(SignalDataTypes.INT.isValid(null));
        assertTrue(SignalDataTypes.INT.isValid("1234"));
        assertFalse(SignalDataTypes.INT.isValid("123foo"));
        assertFalse(SignalDataTypes.INT.isValid("123.34"));
        assertFalse(SignalDataTypes.INT.isValid("23456788854123"));
    }

    @Test
    public void isValidForStringShouldWork() {
        assertTrue(SignalDataTypes.STRING.isValid(null));
        assertTrue(SignalDataTypes.STRING.isValid(""));
        assertTrue(SignalDataTypes.STRING.isValid("foo"));
    }

    @Test
    public void isValidForDoubleShouldWork() {
        assertTrue(SignalDataTypes.DOUBLE.isValid(null));
        assertTrue(SignalDataTypes.DOUBLE.isValid("1"));
        assertTrue(SignalDataTypes.DOUBLE.isValid("12.34345844900330099"));
        assertFalse(SignalDataTypes.DOUBLE.isValid("12.34foo"));
    }

    @Test
    public void isValidForFloatShouldWork() {
        assertTrue(SignalDataTypes.FLOAT.isValid(null));
        assertTrue(SignalDataTypes.FLOAT.isValid("1"));
        assertTrue(SignalDataTypes.FLOAT.isValid("12.34345844900330099"));
        assertFalse(SignalDataTypes.FLOAT.isValid("12.34foo"));
    }

    @Test
    public void isValidForLongShouldWork() {
        assertTrue(SignalDataTypes.LONG.isValid(null));
        assertTrue(SignalDataTypes.LONG.isValid("23456788854123"));
        assertFalse(SignalDataTypes.INT.isValid("123foo"));
        assertFalse(SignalDataTypes.INT.isValid("123.34"));
    }

    @Test
    public void isValidForPriceShouldWork() {
        assertTrue(SignalDataTypes.DOUBLE.isValid(null));
        assertTrue(SignalDataTypes.DOUBLE.isValid("1"));
        assertTrue(SignalDataTypes.DOUBLE.isValid("12.34345844900330099"));
        assertFalse(SignalDataTypes.DOUBLE.isValid("12.34foo"));
    }

    @Test
    public void testNeedQualifier() {
        assertFalse(SignalDataTypes.DOUBLE.needsQualifier());
        assertFalse(SignalDataTypes.FLOAT.needsQualifier());
        assertFalse(SignalDataTypes.INT.needsQualifier());
        assertFalse(SignalDataTypes.LONG.needsQualifier());
        assertFalse(SignalDataTypes.STRING.needsQualifier());
        assertTrue(SignalDataTypes.PRICE.needsQualifier());
    }
}
