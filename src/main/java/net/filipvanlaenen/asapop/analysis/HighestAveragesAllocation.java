package net.filipvanlaenen.asapop.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class running allocations based on highest averages.
 */
final class HighestAveragesAllocation {
    /**
     * Private constructor.
     */
    private HighestAveragesAllocation() {
    }

    /**
     * Allocates a number of seats for a list of votes.
     *
     * @param numberOfSeats The number of seats to allocate.
     * @param numberOfVotes The number of votes for each list to allocate seats for.
     * @return A list with the number of seats for each of the number of votes.
     */
    static List<Integer> allocate(final int numberOfSeats, final List<Long> numberOfVotes) {
        int n = numberOfVotes.size();
        List<Integer> result = new ArrayList<Integer>(n);
        List<Double> quotients = new ArrayList<Double>(n);
        for (int i = 0; i < n; i++) {
            result.add(0);
            quotients.add((double) numberOfVotes.get(i));
        }
        for (int s = 0; s < numberOfSeats; s++) {
            int highestAverageAt = 0;
            double highestAverage = 0D;
            for (int i = 0; i < n; i++) {
                // EQMU: Changing the conditional boundary below produces an equivalent mutant.
                highestAverageAt = quotients.get(i) > highestAverage ? i : highestAverageAt;
                highestAverage = quotients.get(i);
            }
            result.set(highestAverageAt, result.get(highestAverageAt) + 1);
            quotients.set(highestAverageAt,
                    (double) numberOfVotes.get(highestAverageAt) / (double) (result.get(highestAverageAt) + 1));
        }
        return result;
    }
}
