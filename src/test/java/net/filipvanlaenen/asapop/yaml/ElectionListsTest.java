package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>ElectionLists</code> class.
 */
public class ElectionListsTest {
    /**
     * Verifies that the getter method <code>getEuropean</code> is wired correctly to the setter method
     * <code>setEuropean</code>.
     */
    @Test
    public void getEuropeanShouldBeWiredCorrectlyToSetEuropean() {
        ElectionLists electionLists = new ElectionLists();
        ElectionList electionList = new ElectionList();
        electionLists.setEuropean(electionList);
        assertEquals(electionList, electionLists.getEuropean());
    }

    /**
     * Verifies that the getter method <code>getNational</code> is wired correctly to the setter method
     * <code>setNational</code>.
     */
    @Test
    public void getNationalShouldBeWiredCorrectlyToSetNational() {
        ElectionLists electionLists = new ElectionLists();
        ElectionList electionList = new ElectionList();
        electionLists.setNational(electionList);
        assertEquals(electionList, electionLists.getNational());
    }

    /**
     * Verifies that the getter method <code>getPresidential</code> is wired correctly to the setter method
     * <code>setPresidential</code>.
     */
    @Test
    public void getPresidentialShouldBeWiredCorrectlyToSetPresidential() {
        ElectionLists electionLists = new ElectionLists();
        ElectionList electionList = new ElectionList();
        electionLists.setPresidential(electionList);
        assertEquals(electionList, electionLists.getPresidential());
    }
}
