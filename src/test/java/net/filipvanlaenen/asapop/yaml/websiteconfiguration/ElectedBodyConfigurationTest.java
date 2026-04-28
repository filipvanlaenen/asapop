package net.filipvanlaenen.asapop.yaml.websiteconfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.OrderedCollection;

/**
 * Unit tests on the <code>ElectedBody</code> class.
 */
public class ElectedBodyConfigurationTest {
    /**
     * Verifies that the getter method <code>getId</code> is wired correctly to the setter method <code>setId</code>.
     */
    @Test
    public void getIdShouldBeWiredCorrectlyToSetId() {
        ElectedBodyConfiguration electedBody = new ElectedBodyConfiguration();
        electedBody.setId("ab");
        assertEquals("ab", electedBody.getId());
    }

    /**
     * Verifies that the getter method <code>getElectionDates</code> is wired correctly to the setter method
     * <code>setElectionDates</code>.
     */
    @Test
    public void getElectionDatesShouldBeWiredCorrectlyToSetElectionDates() {
        ElectedBodyConfiguration electedBody = new ElectedBodyConfiguration();
        OrderedCollection<String> electionDates = OrderedCollection.of("2016-04-17");
        electedBody.setElectionDates(electionDates);
        assertEquals(electionDates, electedBody.getElectionDates());
    }

    /**
     * Verifies that the getter method <code>getProperNames</code> is wired correctly to the setter method
     * <code>setProperNames</code>.
     */
    @Test
    public void getProperNamesShouldBeWiredCorrectlyToSetProperNames() {
        ElectedBodyConfiguration electedBody = new ElectedBodyConfiguration();
        Map<String, String> properNames = Map.of("ab", "Foo");
        electedBody.setProperNames(properNames);
        assertEquals(properNames, electedBody.getProperNames());
    }

    /**
     * Verifies that the getter method <code>getTranslatedNames</code> is wired correctly to the setter method
     * <code>setTranslatedNames</code>.
     */
    @Test
    public void getTranslatedNamesShouldBeWiredCorrectlyToSetTranslatedNames() {
        ElectedBodyConfiguration electedBody = new ElectedBodyConfiguration();
        Map<String, String> translatedNames = Map.of("ab", "Foo");
        electedBody.setTranslatedNames(translatedNames);
        assertEquals(translatedNames, electedBody.getTranslatedNames());
    }
}
