package net.filipvanlaenen.asapop.yaml;

import java.util.Set;

/**
 * Class representing the top element for the YAML file containing the terms with their translations.
 */
public class Terms {
    /**
     * The set containing all the erms.
     */
    private Set<Term> terms;

    /**
     * Returns a set with all the terms.
     *
     * @return A set with all the terms.
     */
    public Set<Term> getTerms() {
        return terms;
    }

    /**
     * Sets the set with all the terms.
     *
     * @param terms A set with all the terms.
     */
    public void setTerms(final Set<Term> terms) {
        this.terms = terms;
    }
}
