package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the class <code>ElectoralList</code>.
 */
public class ElectoralListTest {
    /**
     * Verifies that an electoral list with a given key can be retrieved, or is
     * created upon requesting it. A key is used with high probability of never
     * being used before.
     */
    @Test
    public void getShouldReturnAnElectoralListWithTheCorrectKey() {
        assertEquals("ElectoralListTestGetNew", ElectoralList.get("ElectoralListTestGetNew").getKey());
    }

    /**
     * Verifies that the same electoral list is returned when the same key is
     * being used. A key is used with high probability of never being used
     * before.
     */
    @Test
    public void getShouldReturnTheSameElectoralListForTheSameKey() {
        ElectoralList expected = ElectoralList.get("ElectoralListTestGetSame");
        assertEquals(expected, ElectoralList.get("ElectoralListTestGetSame"));
    }
}
