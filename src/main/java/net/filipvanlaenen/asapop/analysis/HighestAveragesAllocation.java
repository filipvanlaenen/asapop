package net.filipvanlaenen.asapop.analysis;

import java.util.Comparator;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.Map.Entry;
import net.filipvanlaenen.kolektoj.array.ArrayCollection;
import net.filipvanlaenen.kolektoj.hash.HashMap;

/**
 * Class implementing highest-averages allocation.
 */
public final class HighestAveragesAllocation {
    /**
     * Register containing the number of seats and current quotient for a number of votes.
     */
    private final class Register {
        /**
         * The number of votes.
         */
        private final long numberOfVotes;
        /**
         * The number of seats.
         */
        private int numberOfSeats;
        /**
         * The current quotient.
         */
        private double quotient;

        /**
         * Constructor takig the number of votes as its parameter.
         *
         * @param numberOfVotes The number of votes.
         */
        private Register(final Long numberOfVotes) {
            this.numberOfVotes = numberOfVotes;
            this.numberOfSeats = 0;
            this.quotient = numberOfVotes;
        }

        /**
         * Returns the number of seats.
         *
         * @return The number of seats.
         */
        private int getNumberOfSeats() {
            return numberOfSeats;
        }

        /**
         * Returns the number of votes.
         *
         * @return The number of votes.
         */
        private long getNumberOfVotes() {
            return numberOfVotes;
        }

        /**
         * Returns the current quotient.
         *
         * @return The current quotient.
         */
        private double getQuotient() {
            return quotient;
        }

        /**
         * Increases the number of seats and updates the current quotient.
         */
        private void increaseNumberOfSeats() {
            numberOfSeats++;
            quotient = numberOfVotes / (numberOfSeats + 1D);
        }
    }

    /**
     * The magic number one hundred.
     */
    private static final double ONE_HUNDRED = 100D;
    /**
     * Map of maps containing vulgar fractions.
     */
    private static final Map<Integer, Map<Long, String>> VULGAR_FRACTIONS =
            Map.of(2, Map.of(1L, "½"), 3, Map.of(1L, "⅓", 2L, "⅔"), 4, Map.of(1L, "¼", 2L, "½", 3L, "¾"), 5,
                    Map.of(1L, "⅕", 2L, "⅖", 3L, "⅗", 4L, "⅘"), 6, Map.of(1L, "⅙", 2L, "⅓", 3L, "½", 4L, "⅔", 5L, "⅚"));

    /**
     * A map with the allocation of number of seats per number of votes.
     */
    private Map<Long, Integer> allocation;
    /**
     * The number of seats to be allocated.
     */
    private final int numberOfSeats;
    /**
     * The threshold.
     */
    private final Double threshold;

    /**
     * Private constructor taking the number of seats the a collection with the number of votes as its parameters.
     *
     * @param numberOfSeats The number of seats.
     * @param threshold     The threshold.
     * @param numberOfVotes A collection with the number of votes.
     */
    public HighestAveragesAllocation(final int numberOfSeats, final Double threshold,
            final Collection<Long> numberOfVotes) {
        this.numberOfSeats = numberOfSeats;
        this.threshold = threshold;
        this.allocation = calculateAllocation(numberOfVotes);
    }

    /**
     * Calculates the number of seats for the number of votes.
     *
     * @param numberOfVotes A collection with the number of votes.
     * @return A map with the number of seats per number of votes.
     */
    private Map<Long, Integer> calculateAllocation(final Collection<Long> numberOfVotes) {
        Collection<Register> registers = new ArrayCollection<Register>(
                numberOfVotes.stream().map(n -> new Register(n)).toArray(Register[]::new));
        long totalNumberOfVotes = numberOfVotes.stream().reduce(0L, Long::sum);
        long votesThreshold = threshold == null ? 0 : Math.round(threshold * totalNumberOfVotes / ONE_HUNDRED);
        Collection<Register> qualifiedRegisters = new ArrayCollection<Register>(
                registers.stream().filter(r -> r.getNumberOfVotes() > votesThreshold).toArray(Register[]::new));
        for (int s = 0; s < numberOfSeats; s++) {
            Register nextSeat = null;
            double maxQuotient = 0D;
            for (Register r : qualifiedRegisters) {
                double quotient = r.getQuotient();
                // EQMU: Changing the conditional boundary on the third condition produces an equivalent mutant.
                if (quotient > maxQuotient
                        || quotient == maxQuotient && r.getNumberOfSeats() < nextSeat.getNumberOfSeats()) {
                    nextSeat = r;
                    maxQuotient = quotient;
                }
            }
            nextSeat.increaseNumberOfSeats();
        }
        Entry<Long, Integer>[] entries = registers.stream()
                .map(r -> new Entry<Long, Integer>(r.getNumberOfVotes(), r.getNumberOfSeats())).toArray(Entry[]::new);
        return new HashMap<Long, Integer>(Map.KeyAndValueCardinality.DUPLICATE_KEYS_WITH_DUPLICATE_VALUES, entries);
    }

    /**
     * Returns a collection with the number of seats for a number of votes.
     *
     * @param numberOfVotes The number of votes.
     * @return A collection with the number of seats.
     */
    Collection<Integer> getNumberOfSeats(final long numberOfVotes) {
        return allocation.getAll(numberOfVotes);
    }

    /**
     * Returns the number of seats for a number of votes as a string.
     *
     * @param numberOfVotes The number of votes.
     * @return A string with the number of votes.
     */
    public String getNumberOfSeatsString(final long numberOfVotes) {
        Collection<Integer> allNumberOfSeats = allocation.getAll(numberOfVotes);
        if (allNumberOfSeats.size() == 1) {
            return Integer.toString(allNumberOfSeats.get());
        } else {
            int low = allNumberOfSeats.stream().min(Comparator.naturalOrder()).orElse(0);
            long numberOfHighs = allNumberOfSeats.stream().filter(i -> i > low).count();
            if (numberOfHighs == 0) {
                return Integer.toString(low);
            } else {
                return ((low == 0 ? "" : Integer.toString(low))
                        + getVulgarFractionString(numberOfHighs, allNumberOfSeats.size())).trim();
            }
        }
    }

    /**
     * Returns the vulgar fraction for a numerator and a denominator.
     *
     * @param numerator   The numerator.
     * @param denominator The denominator.
     * @return A vulgar fractrion string.
     */
    private String getVulgarFractionString(final long numerator, final int denominator) {
        if (VULGAR_FRACTIONS.containsKey(denominator) && VULGAR_FRACTIONS.get(denominator).containsKey(numerator)) {
            return VULGAR_FRACTIONS.get(denominator).get(numerator);
        } else {
            return " " + Long.toString(numerator) + "/" + Integer.toString(denominator);
        }
    }
}
