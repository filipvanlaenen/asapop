package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>ResultValue</code> class.
 */
public final class ResultValueTest {
    /**
     * The magic number 0.5.
     */
    private static final double DOUBLE_0_5 = 0.5D;
    /**
     * The magic number 1.2.
     */
    private static final double DOUBLE_1_2 = 1.2D;

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

    /**
     * Verifies that a precision of a half is higher than a precision of one.
     */
    @Test
    public void halfShouldBeHigherPrecisionThanOne() {
        assertEquals(ResultValue.Precision.HALF,
                ResultValue.Precision.highest(ResultValue.Precision.HALF, ResultValue.Precision.ONE));
    }

    /**
     * Verifies the calculation of the precision on a collection of result values.
     */
    @Test
    public void shouldCalculatePrecisionOnCollectionOfResultValuesCorrectly() {
        assertEquals(ResultValue.Precision.HALF,
                ResultValue.Precision.getHighestPrecision(Set.of(new ResultValue("0.5"), new ResultValue("1"))));
    }

    /**
     * Verifies the string value of a precision.
     */
    @Test
    public void stringValueOfPrecisionHalfShouldBe05() {
        assertEquals("0.5", ResultValue.Precision.HALF.toString());
    }

    /**
     * Verifies the value of a precision.
     */
    @Test
    public void valueOfPrecisionHalfShouldBe05() {
        assertEquals(DOUBLE_0_5, ResultValue.Precision.HALF.getValue());
    }

    /**
     * Verifies that the precision of 1.2 is 0.1.
     */
    @Test
    public void precisionOfADecimalResultValueShouldBeTenth() {
        assertEquals(ResultValue.Precision.TENTH, new ResultValue("1.2").getPrecision());
    }

    /**
     * Verifies that the precision of 1.5 is 0.5.
     */
    @Test
    public void precisionOfAResultValueWithHalfShouldBeHalf() {
        assertEquals(ResultValue.Precision.HALF, new ResultValue("1.5").getPrecision());
    }

    /**
     * Verifies that the precision of 1.0 is 1.
     */
    @Test
    public void precisionOfAResultValueWithDecimalZeroShouldBeOne() {
        assertEquals(ResultValue.Precision.ONE, new ResultValue("1.0").getPrecision());
    }

    /**
     * Verifies that the precision of 1 is 1.
     */
    @Test
    public void precisionOfAnIntegerResultValueShouldBeOne() {
        assertEquals(ResultValue.Precision.ONE, new ResultValue("1").getPrecision());
    }

    /**
     * Verifies the calculation of the nominal value.
     */
    @Test
    public void nominalValueOfADecimalNumberShouldBeCorrect() {
        assertEquals(DOUBLE_1_2, new ResultValue("1.2").getNominalValue());
    }

    /**
     * Verifies the calculation of the nominal value for a less than result value.
     */
    @Test
    public void nominalValueOfALessThanResultValueShouldBeZero() {
        assertEquals(0D, new ResultValue("<1").getNominalValue());
    }

    /**
     * Verifies the calculation of the nominal value for a malformed number.
     */
    @Test
    public void nominalValueOfMalformedNumberShouldBeNull() {
        assertNull(new ResultValue("a").getNominalValue());
    }
}
