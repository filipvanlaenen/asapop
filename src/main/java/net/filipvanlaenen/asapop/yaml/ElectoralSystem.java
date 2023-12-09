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
     * The threshold.
     */
    private double threshold;

    /**
     * Returns the number of seats.
     *
     * @return The number of seats.
     */
    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    /**
     * Returns the threshold.
     *
     * @return The threshold.
     */
    public double getThreshold() {
        return threshold;
    }

    /**
     * Sets the number of seats.
     *
     * @param numberOfSeats The number of seats.
     */
    public void setNumberOfSeats(final int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    /**
     * Sets the threshold.
     *
     * @param threshold The threshold.
     */
    public void setThreshold(final double threshold) {
        this.threshold = threshold;
    }
}
