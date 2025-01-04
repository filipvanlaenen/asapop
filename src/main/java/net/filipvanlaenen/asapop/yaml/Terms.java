package net.filipvanlaenen.asapop.yaml;

/**
 * Class representing the top element for the YAML file containing the terms with their translations.
 */
public class Terms {
    /**
     * An array with all the terms.
     */
    private Term[] terms;

    /**
     * Returns an aray with all the terms.
     *
     * @return An array with all the terms.
     */
    public Term[] getTerms() {
        return terms;
    }

    /**
     * Sets the array with all the terms.
     *
     * @param terms An array with all the terms.
     */
    public void setTerms(final Term[] terms) {
        this.terms = terms;
    }
}
