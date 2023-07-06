package net.filipvanlaenen.asapop.yaml;

/**
 * Class representing the electoral system in a YAML file contaning the election data.
 */
public class ElectoralSystem {
    /**
     * The number of seats.
     */
    private int numberOfSeats;

    /**
     * Returns the number of seats.
     *
     * @return The number of seats.
     */
    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    /**
     * Sets the number of seats.
     *
     * @param numberOfSeats The number of seats.
     */
    public void setNumberOfSeats(final int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }
}
