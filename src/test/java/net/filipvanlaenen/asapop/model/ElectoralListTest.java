package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;

/**
 * Unit tests on the class <code>ElectoralList</code>.
 */
public class ElectoralListTest {
    /**
     * Verifies that an electoral list with a given ID can be retrieved, or is created upon requesting it.
     */
    @Test
    public void getShouldReturnAnElectoralListWithTheCorrectId() {
        String id = "ElectoralListTestGetNew" + new Random().nextInt();
        assertEquals(id, ElectoralList.get(id).getId());
    }

    /**
     * Verifies that the same electoral list is returned when the same ID is being used. An ID is used with high
     * probability of never being used before.
     */
    @Test
    public void getShouldReturnTheSameElectoralListForTheSameId() {
        ElectoralList expected = ElectoralList.get("ElectoralListTestGetSame");
        assertSame(expected, ElectoralList.get("ElectoralListTestGetSame"));
    }

    /**
     * Verifies that a set with the electoral lists can be retrieved by providing a set of IDs.
     */
    @Test
    public void getShouldReturnSetOfElectoralLists() {
        Set<ElectoralList> expected = Set.of(ElectoralList.get("A"), ElectoralList.get("B"));
        assertEquals(expected, ElectoralList.get(Set.of("A", "B")));
    }

    /**
     * Verifies the conversion from a set of electoral lists to a set of their IDs.
     */
    @Test
    public void shouldConvertSetOfElectoraListToASetOfIds() {
        assertEquals(Set.of("A", "B"), ElectoralList.getIds(Set.of(ElectoralList.get("A"), ElectoralList.get("B"))));
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
     * Verifies that the getter method <code>getRomanizedAbbreviation</code> is wired correctly to the setter method
     * <code>setRomanizedAbbreviation</code>.
     */
    @Test
    public void getRomanizedAbbreviationShouldReturnValueFromSetRomanizedAbbreviation() {
        ElectoralList electoralList = ElectoralList.get("ElectoralListTestGetRomanizedAbbreviation");
        electoralList.setRomanizedAbbreviation("ABBR");
        assertEquals("ABBR", electoralList.getRomanizedAbbreviation());
    }

    /**
     * Verifies that the getter method <code>getName</code> is wired correctly to the setter method
     * <code>setNames</code>.
     */
    @Test
    public void getNameShouldReturnValueFromSetNames() {
        ElectoralList electoralList = ElectoralList.get("ElectoralListTestGetName");
        Map<String, String> names = Map.of("EN", "English Name");
        electoralList.setNames(names);
        assertEquals("English Name", electoralList.getName("EN"));
    }

    /**
     * Verifies that the set of language codes is returned correctly.
     */
    @Test
    public void shouldReturnTheLanguageCodes() {
        ElectoralList electoralList = ElectoralList.get("ElectoralListTestGetLanguageCodes");
        Map<String, String> names = Map.of("EN", "English Name");
        electoralList.setNames(names);
        assertTrue(Collection.of("EN").containsSame(electoralList.getLanguageCodes()));
    }
}
