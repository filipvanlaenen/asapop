package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the class <code>Candidate</code>.
 */
public class CandidateTest {
    /**
     * Verifies that a candidate with a given ID can be retrieved, or is created upon requesting it.
     */
    @Test
    public void getShouldReturnACandidateWithTheCorrectId() {
        String id = "CandidateTestGetNew" + new Random().nextInt();
        assertEquals(id, Candidate.get(id).getId());
    }

    /**
     * Verifies that the same candidate is returned when the same ID is being used. An ID is used with high probability
     * of never being used before.
     */
    @Test
    public void getShouldReturnTheSameElectoralListForTheSameId() {
        Candidate expected = Candidate.get("CandidateTestGetSame");
        assertSame(expected, Candidate.get("CandidateTestGetSame"));
    }

    /**
     * Verifies that the getter method <code>getAbbreviation</code> is wired correctly to the setter method
     * <code>setAbbreviation</code>.
     */
    @Test
    public void getAbbreviationShouldReturnValueFromSetAbbreviation() {
        Candidate candidate = Candidate.get("ElectoralListTestGetAbbreviation");
        candidate.setAbbreviation("ABBR");
        assertEquals("ABBR", candidate.getAbbreviation());
    }

    /**
     * Verifies that the getter method <code>getName</code> is wired correctly to the setter method
     * <code>setName</code>.
     */
    @Test
    public void getNameShouldReturnValueFromSetName() {
        Candidate candidate = Candidate.get("ElectoralListTestGetName");
        candidate.setName("John Doe");
        assertEquals("John Doe", candidate.getName());
    }

    /**
     * Verifies that the getter method <code>getRomanizedName</code> is wired correctly to the setter method
     * <code>setRomanizedName</code>.
     */
    @Test
    public void getRomanizedNameShouldReturnValueFromSetRomanizedName() {
        Candidate candidate = Candidate.get("ElectoralListTestGetRomanizedName");
        candidate.setRomanizedName("Vasya Pupkin");
        assertEquals("Vasya Pupkin", candidate.getRomanizedName());
    }

    /**
     * Verifies that <code>idRegistered</code> returns false when an ID hasn't been registered yet.
     */
    @Test
    public void idRegisteredShouldReturnFalseForAnIdNotYetRegistered() {
        assertFalse(Candidate.idRegistered("NOT_YET_REGISTERED"));
    }

    /**
     * Verifies that <code>idRegistered</code> returns true when an ID has been registered.
     */
    @Test
    public void idRegisteredShouldReturnTrueForAnIdAlreadyRegistered() {
        Candidate.get("ALREADY_REGISTERED");
        assertTrue(Candidate.idRegistered("ALREADY_REGISTERED"));
    }
}
