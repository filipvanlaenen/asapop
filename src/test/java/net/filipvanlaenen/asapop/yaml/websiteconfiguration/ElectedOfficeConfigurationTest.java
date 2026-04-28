package net.filipvanlaenen.asapop.yaml.websiteconfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.OrderedCollection;

/**
 * Unit tests on the <code>ElectedOffice</code> class.
 */
public class ElectedOfficeConfigurationTest {
    /**
     * Verifies that the getter method <code>getId</code> is wired correctly to the setter method <code>setId</code>.
     */
    @Test
    public void getIdShouldBeWiredCorrectlyToSetId() {
        ElectedOfficeConfiguration electedOffice = new ElectedOfficeConfiguration();
        electedOffice.setId("ab");
        assertEquals("ab", electedOffice.getId());
    }

    /**
     * Verifies that the getter method <code>getElectionDates</code> is wired correctly to the setter method
     * <code>setElectionDates</code>.
     */
    @Test
    public void getElectionDatesShouldBeWiredCorrectlyToSetElectionDates() {
        ElectedOfficeConfiguration electedOffice = new ElectedOfficeConfiguration();
        OrderedCollection<String> electionDates = OrderedCollection.of("2016-04-17");
        electedOffice.setElectionDates(electionDates);
        assertEquals(electionDates, electedOffice.getElectionDates());
    }

    /**
     * Verifies that the getter method <code>getProperNames</code> is wired correctly to the setter method
     * <code>setProperNames</code>.
     */
    @Test
    public void getProperNamesShouldBeWiredCorrectlyToSetProperNames() {
        ElectedOfficeConfiguration electedOffice = new ElectedOfficeConfiguration();
        Map<String, String> properNames = Map.of("ab", "Foo");
        electedOffice.setProperNames(properNames);
        assertEquals(properNames, electedOffice.getProperNames());
    }

    /**
     * Verifies that the getter method <code>getTranslatedNames</code> is wired correctly to the setter method
     * <code>setTranslatedNames</code>.
     */
    @Test
    public void getTranslatedNamesShouldBeWiredCorrectlyToSetTranslatedNames() {
        ElectedOfficeConfiguration electedOffice = new ElectedOfficeConfiguration();
        Map<String, String> translatedNames = Map.of("ab", "Foo");
        electedOffice.setTranslatedNames(translatedNames);
        assertEquals(translatedNames, electedOffice.getTranslatedNames());
    }
}
