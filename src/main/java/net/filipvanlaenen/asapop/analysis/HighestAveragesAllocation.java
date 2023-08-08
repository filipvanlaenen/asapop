package net.filipvanlaenen.asapop.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     * @return A map with the number of seats for each of the number of votes.
     */
    static Map<Long, List<Integer>> allocate(final int numberOfSeats, final List<Long> numbersOfVotes) {
        int numberOfNumbersOfVotes = numbersOfVotes.size();
        List<Integer> seats = numbersOfVotes.stream().map(x -> 0).collect(Collectors.toList());
        List<Double> quotients = numbersOfVotes.stream().map(x -> (double) x).collect(Collectors.toList());
        for (int s = 0; s < numberOfSeats; s++) {
            int indexOfLargestQuotient = 0;
            double largestQuotient = 0D;
            for (int currentIndex = 0; currentIndex < numberOfNumbersOfVotes; currentIndex++) {
                double currentQuotient = quotients.get(currentIndex);
                // EQMU: Changing the conditional boundary on the third condition produces an equivalent mutant.
                if (currentQuotient > largestQuotient || currentQuotient == largestQuotient
                        && numbersOfVotes.get(currentIndex) < numbersOfVotes.get(indexOfLargestQuotient)) {
                    indexOfLargestQuotient = currentIndex;
                    largestQuotient = currentQuotient;
                }
            }
            seats.set(indexOfLargestQuotient, seats.get(indexOfLargestQuotient) + 1);
            quotients.set(indexOfLargestQuotient, (double) numbersOfVotes.get(indexOfLargestQuotient)
                    / (double) (seats.get(indexOfLargestQuotient) + 1));
        }
        Map<Long, List<Integer>> result = new HashMap<Long, List<Integer>>();
        for (int i = 0; i < numberOfNumbersOfVotes; i++) {
            long numberOfVotes = numbersOfVotes.get(i);
            if (result.containsKey(numberOfVotes)) {
                result.get(numberOfVotes).add(seats.get(i));
            } else {
                List<Integer> value = new ArrayList<Integer>();
                value.add(seats.get(i));
                result.put(numberOfVotes, value);
            }
        }
        return result;
    }
}
