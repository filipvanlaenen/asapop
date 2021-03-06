package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the class <code>ElectoralList</code>.
 */
public class ElectoralListTest {
    /**
     * Verifies that an electoral list with a given key can be retrieved, or is created upon requesting it.
     */
    @Test
    public void getShouldReturnAnElectoralListWithTheCorrectKey() {
        String key = "ElectoralListTestGetNew" + new Random().nextInt();
        assertEquals(key, ElectoralList.get(key).getKey());
    }

    /**
     * Verifies that the same electoral list is returned when the same key is being used. A key is used with high
     * probability of never being used before.
     */
    @Test
    public void getShouldReturnTheSameElectoralListForTheSameKey() {
        ElectoralList expected = ElectoralList.get("ElectoralListTestGetSame");
        assertSame(expected, ElectoralList.get("ElectoralListTestGetSame"));
    }

    /**
     * Verifies that the getter method <code>getAbbreviation</code> is wired correctly to the setter method
     * <code>setAbbreviation</code>.
     */
    @Test
    public void getAbbreviationShouldReturnValueFromSetAbbreviation() {
        ElectoralList electoralList = ElectoralList.get("ElectoralListTestGetAbbreviation");
        electoralList.setAbbreviation("ABBR");
        assertEquals("ABBR", electoralList.getAbbreviation());
    }

    /**
     * Verifies that the getter method <code>getName</code> is wired correctly to the setter method
     * <code>setNames</code>.
     */
    @Test
    public void getNameShouldReturnValueFromSetNames() {
        ElectoralList electoralList = ElectoralList.get("ElectoralListTestGetName");
        Map<String, String> names = new HashMap<String, String>();
        names.put("EN", "English Name");
        electoralList.setNames(names);
        assertEquals("English Name", electoralList.getName("EN"));
    }
}
