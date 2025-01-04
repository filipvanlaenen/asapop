package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>Terms</code> class.
 */
public class TermsTest {
    /**
     * Verifies that the getter method <code>getTerms</code> is wired correctly to the setter method
     * <code>setTerms</code>.
     */
    @Test
    public void getTermsShouldBeWiredCorrectlyToSetTerms() {
        Terms terms = new Terms();
        Term[] termSet = new Term[] {new Term()};
        terms.setTerms(termSet);
        assertEquals(termSet, terms.getTerms());
    }
}
