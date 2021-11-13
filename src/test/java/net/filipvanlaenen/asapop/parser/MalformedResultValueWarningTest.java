package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>MalformedResultValueWarning</code> class.
 */
public final class MalformedResultValueWarningTest {
    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        Warning warning = new MalformedResultValueWarning(1, "a");
        assertEquals("Malformed result value (“a”) detected in line 1.", warning.toString());
    }

    /**
     * Verifies that a malformed result value warning is not equal to null.
     */
    @Test
    public void aMalformedResultValueWarningShouldNotBeEqualToNull() {
        assertFalse(new MalformedResultValueWarning(1, "a").equals(null));
    }

    /**
     * Verifies that a malformed result value warning is not equal to an object of another class, like a string.
     */
    @Test
    public void aMalformedResultValueWarningShouldNotBeEqualToAString() {
        assertFalse(new MalformedResultValueWarning(1, "a").equals(""));
    }

    /**
     * Verifies that a malformed result value warning is equal to itself.
     */
    @Test
    public void aMalformedResultValueWarningShouldBeEqualToItself() {
        MalformedResultValueWarning warning = new MalformedResultValueWarning(1, "a");
        assertTrue(warning.equals(warning));
    }

    /**
     * Verifies that calling hashCode twice on a malformed result value warning returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnMalformedResultValueWarningReturnsTheSameResult() {
        MalformedResultValueWarning warning = new MalformedResultValueWarning(1, "a");
        assertEquals(warning.hashCode(), warning.hashCode());
    }

    /**
     * Verifies that two malformed result value warnings constructed with the same parameters are equal.
     */
    @Test
    public void twoMalformedResultValueWarningsConstructedWithTheSameParametersShouldBeEqual() {
        assertEquals(new MalformedResultValueWarning(1, "a"),
                     new MalformedResultValueWarning(1, "a"));
    }

    /**
     * Verifies that two malformed result value warnings constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoMalformedResultValueWarningsConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(new MalformedResultValueWarning(1, "a").hashCode(),
                     new MalformedResultValueWarning(1, "a").hashCode());
    }
}
