package net.filipvanlaenen.asapop.yaml;

/**
 * Class representing a list with European, national and presidential elections for the YAML file containing the website
 * configuration.
 */
public class ElectionLists {
    /**
     * The list with the European elections.
     */
    private ElectionList european;
    /**
     * The list with the national elections.
     */
    private ElectionList national;
    /**
     * The list with the presidential elections.
     */
    private ElectionList presidential;

    /**
     * Returns the list with the European elections.
     *
     * @return The list with the European elections.
     */
    public ElectionList getEuropean() {
        return european;
    }

    /**
     * Returns the list with the national elections.
     *
     * @return The list with the national elections.
     */
    public ElectionList getNational() {
        return national;
    }

    /**
     * Returns the list with the presidential elections.
     *
     * @return The list with the presidential elections.
     */
    public ElectionList getPresidential() {
        return presidential;
    }

    /**
     * Sets the list with the European elections.
     *
     * @param european The list with the European elections.
     */
    public void setEuropean(final ElectionList european) {
        this.european = european;
    }

    /**
     * Sets the list with the national elections.
     *
     * @param national The list with the national elections.
     */
    public void setNational(final ElectionList national) {
        this.national = national;
    }

    /**
     * Sets the list with the presidential elections.
     *
     * @param presidential The list with the presidential elections.
     */
    public void setPresidential(final ElectionList presidential) {
        this.presidential = presidential;
    }
}
