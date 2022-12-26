package net.filipvanlaenen.asapop.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.ElectoralList;

/**
 * Unit tests on the <code>MissingSaporMappingWarning</code> class.
 */
public class MissingSaporMappingWarningTest {
    /**
     * A missing SAPOR mapping warning.
     */
    private static final MissingSaporMappingWarning WARNING_1 =
            new MissingSaporMappingWarning(Set.of(ElectoralList.get("FOO")));

    /**
     * Verifies that the warning is converted to a String correctly.
     */
    @Test
    public void toStringShouldProduceCorrectMessage() {
        assertEquals("SAPOR mapping missing for “FOO”.", WARNING_1.toString());
    }

    /**
     * Verifies that the warning is converted to a String correctly when the combination of electoral lists is large.
     */
    @Test
    public void toStringShouldProduceCorrectMessageForLargeElectoralListCombination() {
        assertEquals("SAPOR mapping missing for “BAR+BAZ+FOO+QUX”.",
                new MissingSaporMappingWarning(ElectoralList.get(Set.of("BAR", "BAZ", "FOO", "QUX"))).toString());
    }

    /**
     * Verifies that a missing SAPOR mapping warning is not equal to null.
     */
    @Test
    public void aMissingSaporMappingWarningShouldNotBeEqualToNull() {
        assertFalse(WARNING_1.equals(null));
    }

    /**
     * Verifies that a missing SAPOR mapping warning is not equal to an object of another class, like a string.
     */
    @Test
    public void aMissingSaporMappingWarningShouldNotBeEqualToAString() {
        assertFalse(WARNING_1.equals(""));
    }

    /**
     * Verifies that a missing SAPOR mapping warning is equal to itself.
     */
    @Test
    public void aMissingSaporMappingWarningShouldBeEqualToItself() {
        assertTrue(WARNING_1.equals(WARNING_1));
    }

    /**
     * Verifies that calling hashCode twice on a missing SAPOR mapping warning returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnMissingSaporMappingWarningReturnsTheSameResult() {
        assertEquals(WARNING_1.hashCode(), WARNING_1.hashCode());
    }

    /**
     * Verifies that two missing SAPOR mapping warnings constructed with the same parameters are equal.
     */
    @Test
    public void twoMissingSaporMappingWarningsConstructedWithTheSameParametersShouldBeEqual() {
        assertEquals(WARNING_1, new MissingSaporMappingWarning(Set.of(ElectoralList.get("FOO"))));
    }

    /**
     * Verifies that two missing SAPOR mapping warnings constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoMissingSaporMappingWarningsConstructedWithSameParametersShouldHaveTheSameHashCode() {
        assertEquals(WARNING_1.hashCode(), new MissingSaporMappingWarning(Set.of(ElectoralList.get("FOO"))).hashCode());
    }

    /**
     * Verifies that two different missing SAPOR mapping warnings with different line numbers are not equal.
     */
    @Test
    public void twoDifferentMissingSaporMappingWarningsWithDifferentLineNumbersShouldNotBeEqual() {
        assertFalse(WARNING_1.equals(new MissingSaporMappingWarning(Set.of(ElectoralList.get("BAR")))));
    }

    /**
     * Verifies that two different missing SAPOR mapping warnings with different line numbers have different hash codes.
     */
    @Test
    public void twoMissingSaporMappingWarningsWithDifferentLineNumbersShouldHaveDifferentHashCodes() {
        assertFalse(
                WARNING_1.hashCode() == new MissingSaporMappingWarning(Set.of(ElectoralList.get("BAR"))).hashCode());
    }

}
