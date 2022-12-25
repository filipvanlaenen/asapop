package net.filipvanlaenen.asapop.yaml;

/**
 * Class representing the SAPOR mapping element for the YAML file containing the SAPOR configuration.
 */
public class SaporMapping {
    /**
     * A direct mapping from a result in an ROPF file to an entry in a SAPOR file.
     */
    private DirectSaporMapping directMapping;

    /**
     * Returns the direct mapping from a result in an ROPF file to an entry in a SAPOR file.
     *
     * @return The direct mapping from a result in an ROPF file to an entry in a SAPOR file.
     */
    public DirectSaporMapping getDirectMapping() {
        return directMapping;
    }

    /**
     * Sets the direct mapping from a result in an ROPF file to an entry in a SAPOR file.
     *
     * @param directMapping The direct mapping from a result in an ROPF file to an entry in a SAPOR file.
     */
    public void setDirectMapping(final DirectSaporMapping directMapping) {
        this.directMapping = directMapping;
    }
}
