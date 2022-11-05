package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>PollingFirmAndCommissionerMissingWarning</code> class.
 */
public final class PollingFirmAndCommissionerMissingWarningTest {
    /**
     * A polling firm and commissioner missing warning for a on line 1.
     */
    private static final PollingFirmAndCommissionerMissingWarning WARNING_1 =
            new PollingFirmAndCommissionerMissingWarning(1);

    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        assertEquals("Polling firm and commissioner missing in line 1.", WARNING_1.toString());
    }

    /**
     * Verifies that a polling firm and commissioner missing warning is not equal to null.
     */
    @Test
    public void aPollingFirmAndCommissionerMissingWarningShouldNotBeEqualToNull() {
        assertFalse(WARNING_1.equals(null));
    }

    /**
     * Verifies that a polling firm and commissioner missing warning is not equal to an object of another class, like a
     * string.
     */
    @Test
    public void aPollingFirmAndCommissionerMissingWarningShouldNotBeEqualToAString() {
        assertFalse(WARNING_1.equals(""));
    }

    /**
     * Verifies that a polling firm and commissioner missing warning is equal to itself.
     */
    @Test
    public void aPollingFirmAndCommissionerMissingWarningShouldBeEqualToItself() {
        assertTrue(WARNING_1.equals(WARNING_1));
    }

    /**
     * Verifies that calling hashCode twice on a polling firm and commissioner missing warning returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnPollingFirmAndCommissionerMissingWarningReturnsTheSameResult() {
        assertEquals(WARNING_1.hashCode(), WARNING_1.hashCode());
    }

    /**
     * Verifies that two polling firm and commissioner missing warnings constructed with the same parameters are equal.
     */
    @Test
    public void twoPollingFirmAndCommissionerMissingWarningsConstructedWithTheSameParametersShouldBeEqual() {
        assertEquals(WARNING_1, new PollingFirmAndCommissionerMissingWarning(1));
    }

    /**
     * Verifies that two polling firm and commissioner missing warnings constructed with the same parameters return the
     * same hashCode.
     */
    @Test
    public void twoPollingFirmAndCommissionerMissingWarningsConstructedWithSameParametersShouldHaveTheSameHashCode() {
        assertEquals(WARNING_1.hashCode(), new PollingFirmAndCommissionerMissingWarning(1).hashCode());
    }

    /**
     * Verifies that two different polling firm and commissioner missing warnings with different line numbers are not
     * equal.
     */
    @Test
    public void twoDifferentPollingFirmAndCommissionerMissingWarningsWithDifferentLineNumbersShouldNotBeEqual() {
        assertFalse(WARNING_1.equals(new PollingFirmAndCommissionerMissingWarning(2)));
    }

    /**
     * Verifies that two different polling firm and commissioner missing warnings with different line numbers have
     * different hash codes.
     */
    @Test
    public void twoPollingFirmAndCommissionerMissingWarningsWithDifferentLineNumbersShouldHaveDifferentHashCodes() {
        assertFalse(WARNING_1.hashCode() == new PollingFirmAndCommissionerMissingWarning(2).hashCode());
    }
}
