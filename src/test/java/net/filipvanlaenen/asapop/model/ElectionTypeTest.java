package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>ElectionType</code> class.
 */
public class ElectionTypeTest {
    /**
     * Verifies that getter for the key of the term is wired correctly to the constructor.
     */
    @Test
    public void getTermKeyShouldBeWiredCorrectlyToConstructor() {
        assertEquals("parliament", ElectionType.NATIONAL.getTermKey());
    }
}
