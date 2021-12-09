package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>ResultValue</code> class.
 */
public final class ResultValueTest {
    /**
     * Verifies that the getter for text is wired correctly to the constructor.
     */
    @Test
    public void getTextShouldReturnTextFromConstructor() {
        assertEquals("1", new ResultValue("1").getText());
    }

    /**
     * Verifies that the primitive text for an integer is an integer.
     */
    @Test
    public void primitiveTextShouldBeIntegerForInteger() {
        assertEquals("1", new ResultValue("1").getPrimitiveText());
    }

    /**
     * Verifies that the primitive text for a decimal is a decimal.
     */
    @Test
    public void primitiveTextShouldBeDecimalForDecimal() {
        assertEquals("0.1", new ResultValue("0.1").getPrimitiveText());
    }

    /**
     * Verifies that the primitive text for a less than value is zero.
     */
    @Test
    public void primitiveTextShouldBeZeroForLessThanValue() {
        assertEquals("0", new ResultValue("<1").getPrimitiveText());
    }

    /**
     * Verifies that a result value is not equal to null.
     */
    @Test
    public void aResultValueShouldNotBeEqualToNull() {
        assertFalse(new ResultValue("1").equals(null));
    }

    /**
     * Verifies that a result value is not equal to an object of another class, like a string.
     */
    @Test
    public void aResultValueShouldNotBeEqualToAString() {
        assertFalse(new ResultValue("1").equals(""));
    }

    /**
     * Verifies that a result value is equal to itself.
     */
    @Test
    public void aResultValueShouldBeEqualToItself() {
        ResultValue resultValue = new ResultValue("1");
        assertTrue(resultValue.equals(resultValue));
    }

    /**
     * Verifies that calling hashCode twice on a result value returns the same result.
     */
    @Test
    public void callingHashCodeTwiceOnResultValueReturnsTheSameResult() {
        ResultValue resultValue = new ResultValue("1");
        assertEquals(resultValue.hashCode(), resultValue.hashCode());
    }

    /**
     * Verifies that two result values constructed with the same parameter are equal.
     */
    @Test
    public void twoResultValuesConstructedWithTheSameParameterShouldBeEqual() {
        assertEquals(new ResultValue("1"), new ResultValue("1"));
    }

    /**
     * Verifies that two result values constructed with the same parameters return the same hashCode.
     */
    @Test
    public void twoResultValuesConstructedWithTheSameParametersShouldHaveTheSameHashCode() {
        assertEquals(new ResultValue("1").hashCode(), new ResultValue("1").hashCode());
    }

    /**
     * Verifies that two different result values with different texts are not equal.
     */
    @Test
    public void twoDifferentResultValuesWithDifferentTextsShouldNotBeEqual() {
        assertFalse(new ResultValue("1").equals(new ResultValue("2")));
    }

    /**
     * Verifies that two different result values with different texts have different hash codes.
     */
    @Test
    public void twoDifferentResultValuesWithDifferentTextsShouldHaveDifferentHashCodes() {
        assertFalse(new ResultValue("1").hashCode() == new ResultValue("2").hashCode());
    }
}
