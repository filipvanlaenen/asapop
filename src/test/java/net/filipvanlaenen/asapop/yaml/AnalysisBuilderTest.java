package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>AnalysisBuilder</code> class.
 */
public class AnalysisBuilderTest {
    /**
     * <code>nullOrToString</code> should return when <code>null</code> is passed as an argument.
     */
    @Test
    public void nullOrToStringShouldReturnNullWhenPassingNull() {
        assertNull(AnalysisBuilder.nullOrToString(null));
    }

    /**
     * <code>nullOrToString</code> should return the result of calling <code>toString</code> on the object passed as an
     * argument when it isn't <code>null</code>.
     */
    @Test
    public void nullOrToStringShouldReturnResultOfCallingToStringWhenPassingNonNullObject() {
        assertEquals("1", AnalysisBuilder.nullOrToString(1));
    }
}
