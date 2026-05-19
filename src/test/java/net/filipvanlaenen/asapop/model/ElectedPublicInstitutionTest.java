package net.filipvanlaenen.asapop.model;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.website.Language;
import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedValueCollection;

/**
 * Unit tests on the <code>ElectedPublicInstitution</code> class.
 */
public class ElectedPublicInstitutionTest {
    /**
     * An election date for the test instances.
     */
    private static final ElectionDate ELECTION_DATE = ElectionDate.parse("2027-01-01");
    /**
     * A map with proper names for the test instances.
     */
    private static final Map<String, String> PROPER_NAMES = Map.of("aa", "Foo", "bb", "Bar", "de", "Baz");
    /**
     * A map with translated names for the test instances.
     */
    private static final Map<Language, String> TRANSLATED_NAMES =
            Map.of(Language.ENGLISH, "Foo", Language.FRENCH, "Bar");
    /**
     * A collection of election dates for the test instances.
     */
    private static final OrderedCollection<OrderedValueCollection<ElectionDate>> ELECTIONS =
            OrderedCollection.<OrderedValueCollection<ElectionDate>>of(OrderedValueCollection.of(ELECTION_DATE));
    /**
     * An elected body acting as a test instance for the unit tests.
     */
    private static final ElectedPublicInstitution TEST_INSTANCE =
            new ElectedBody("A", PROPER_NAMES, TRANSLATED_NAMES, ELECTIONS, false);
    /**
     * A defunct elected body acting as a test instance for the unit tests.
     */
    private static final ElectedPublicInstitution DEFUNCT_TEST_INSTANCE =
            new ElectedBody("A", PROPER_NAMES, TRANSLATED_NAMES, ELECTIONS, true);

    /**
     * Verifies that the <code>getAllProperNames</code> returns all proper names. This unit test uses the subclass
     * <code>ElectedBody</code>.
     */
    @Test
    public void getAllProperNamesShouldReturnAllProperNames() {
        assertTrue(TEST_INSTANCE.getAllProperNames().containsSame(Collection.of("Foo", "Bar", "Baz")));
    }

    /**
     * Verifies that the <code>getAllProperNamesConcatenated</code> returns all proper names concatenated as a string.
     * This unit test uses the subclass <code>ElectedBody</code>.
     */
    @Test
    public void getAllProperNamesConcatenatedShouldReturnAllProperNamesConcatenatedAsAString() {
        assertEquals("Bar · Baz · Foo", TEST_INSTANCE.getAllProperNamesConcatenated());
    }

    /**
     * Verifies that the <code>getDefunct</code> returns false for the test instance. This unit test uses the subclass
     * <code>ElectedBody</code>.
     */
    @Test
    public void getDefunctShouldReturnFalseForTestInstance() {
        assertFalse(TEST_INSTANCE.getDefunct());
    }

    /**
     * Verifies that the <code>getDefunct</code> method returns true for the defunct test instance. This unit test uses
     * the subclass <code>ElectedBody</code>.
     */
    @Test
    public void getDefunctShouldReturnTrueForDefunctTestInstance() {
        assertTrue(DEFUNCT_TEST_INSTANCE.getDefunct());
    }

    /**
     * Verifies that the <code>getId</code> method returns the ID. This unit test uses the subclass
     * <code>ElectedBody</code>.
     */
    @Test
    public void getIdShouldReturnTheId() {
        assertEquals("A", TEST_INSTANCE.getId());
    }

    /**
     * Verifies that the <code>getLanguagesOfProperNames</code> method returns the language codes for all proper names.
     * This unit test uses the subclass <code>ElectedBody</code>.
     */
    @Test
    public void getLanguagesOfProperNamesShouldReturnAllLanguageCodesForTheProperNames() {
        assertTrue(TEST_INSTANCE.getLanguagesOfProperNames().containsSame(Collection.of("aa", "bb", "de")));
    }

    /**
     * Verifies that <code>getName</code> returns the proper name for the provided language code. This unit test uses
     * the subclass <code>ElectedBody</code>.
     */
    @Test
    public void getNameShouldReturnProperNameForLanguageCode() {
        assertEquals("Baz", TEST_INSTANCE.getName(Language.GERMAN));
    }

    /**
     * Verifies that <code>getName</code> returns the translated name for the provided language code. This unit test
     * uses the subclass <code>ElectedBody</code>.
     */
    @Test
    public void getNameShouldReturnTranslatedNameForLanguageCode() {
        assertEquals("Foo", TEST_INSTANCE.getName(Language.ENGLISH));
    }

    /**
     * Verifies that <code>getName</code> returns null for absent language code. This unit test uses the subclass
     * <code>ElectedBody</code>.
     */
    @Test
    public void getNameShouldReturnNullForAbsentLanguageCode() {
        assertNull(TEST_INSTANCE.getName(Language.ESPERANTO));
    }

    /**
     * Verifies that <code>getNextElectionDate</code> returns the first future election date relative to the provided
     * date. This unit test uses the subclass <code>ElectedBody</code>.
     */
    @Test
    public void getNextElectionDateShouldReturnNextElectionDate() {
        assertEquals(ELECTION_DATE, TEST_INSTANCE.getNextElectionDate(ELECTION_DATE.getEndDate()));
    }

    /**
     * Verifies that <code>getNextElectionDate</code> returns <code>null</code> when there's no future election date
     * relative to the provided date. This unit test uses the subclass <code>ElectedBody</code>.
     */
    @Test
    public void getNextElectionDateShouldReturnNullWhenThereIsNoRelativeFutureElectionDate() {
        assertNull(TEST_INSTANCE.getNextElectionDate(ELECTION_DATE.getEndDate().plusDays(1)));
    }
}
