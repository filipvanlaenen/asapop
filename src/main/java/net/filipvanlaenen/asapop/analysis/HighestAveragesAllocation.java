package net.filipvanlaenen.asapop.analysis;

import java.util.List;
import java.util.stream.Collectors;

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
     * Allocates a number of seats for a list of number of votes.
     *
     * @param numberOfSeats  The number of seats to allocate.
     * @param numbersOfVotes A list with the number of votes.
     * @return A list with the number of seats for each of the number of votes.
     */
    static List<Integer> allocate(final int numberOfSeats, final List<Long> numbersOfVotes) {
        int numberOfNumbersOfVotes = numbersOfVotes.size();
        List<Integer> result = numbersOfVotes.stream().map(x -> 0).collect(Collectors.toList());
        List<Double> quotients = numbersOfVotes.stream().map(x -> (double) x).collect(Collectors.toList());
        for (int s = 0; s < numberOfSeats; s++) {
            int indexOfLargestQuotient = 0;
            double largestQuotient = 0D;
            for (int currentIndex = 0; currentIndex < numberOfNumbersOfVotes; currentIndex++) {
                double currentQuotient = quotients.get(currentIndex);
                // EQMU: Changing the conditional boundary below produces an equivalent mutant.
                if (currentQuotient > largestQuotient) {
                    indexOfLargestQuotient = currentIndex;
                    largestQuotient = currentQuotient;
                }
            }
            result.set(indexOfLargestQuotient, result.get(indexOfLargestQuotient) + 1);
            quotients.set(indexOfLargestQuotient, (double) numbersOfVotes.get(indexOfLargestQuotient)
                    / (double) (result.get(indexOfLargestQuotient) + 1));
        }
        return result;
    }
}
