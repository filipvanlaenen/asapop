package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Class representing a multivariate hypergeometric distribution, but sampled.
 */
class SampledMultivariateHypergeometricDistribution {
    /**
     * Class functioning as a register to track the largest and the second largest range.
     */
    static class WinnersRegister {
        /**
         * The largest range.
         */
        private Range largestRange;
        /**
         * The second largest range.
         */
        private Range secondLargestRange;
        /**
         * The index of the largest range.
         */
        private Integer indexOfLargestRange;
        /**
         * The index of the second largest range.
         */
        private Integer indexOfSecondLargestRange;

        /**
         * Returns the index of the largest range.
         *
         * @return The index of the largest range.
         */
        int getIndexOfLargestRange() {
            return indexOfLargestRange;
        }

        /**
         * Returns the index of the second largest range.
         *
         * @return The index of the second largest range.
         */
        int getIndexOfSecondLargestRange() {
            return indexOfSecondLargestRange;
        }

        /**
         * Returns the largest range.
         *
         * @return The largest range.
         */
        Range getLargestRange() {
            return largestRange;
        }

        /**
         * Initializes the register to start a new round.
         */
        void initialize() {
            indexOfLargestRange = null;
            indexOfSecondLargestRange = null;
        }

        /**
         * Updates the register with a new range and index.
         *
         * @param range A new range.
         * @param index The index of the new range.
         */
        void update(final Range range, final int index) {
            if (indexOfLargestRange == null) {
                largestRange = range;
                indexOfLargestRange = index;
            } else if (indexOfSecondLargestRange == null) {
                if (range.compareTo(largestRange) <= 0) {
                    secondLargestRange = range;
                    indexOfSecondLargestRange = index;
                } else {
                    secondLargestRange = largestRange;
                    indexOfSecondLargestRange = indexOfLargestRange;
                    largestRange = range;
                    indexOfLargestRange = index;
                }
            } else if (largestRange.compareTo(range) < 0) {
                secondLargestRange = largestRange;
                indexOfSecondLargestRange = indexOfLargestRange;
                largestRange = range;
                indexOfLargestRange = index;
            } else if (secondLargestRange.compareTo(range) < 0) {
                secondLargestRange = range;
                indexOfSecondLargestRange = index;
            }
        }
    }

    /**
     * The magic number 0.999999 (six nines).
     */
    private static final double SIX_NINES = 0.999999;
    /**
     * The probability mass functions.
     */
    private final List<SampledHypergeometricDistribution> probabilityMassFunctions;
    /**
     * A map with the cardinalities for each of the unique probability mass functions.
     */
    private final Map<SampledHypergeometricDistribution, Integer> probabilityMassFunctionCardinalities;
    /**
     * A map with the accumulated probability masses for the probability mass functions as single winners of the first
     * round, with equal probability mass functions combined together.
     */
    private final Map<Integer, BigDecimal> accumulatedSingleWinnerProbabilityMasses;
    /**
     * A map with the probability masses for the probability mass functions as single winners of the first round.
     */
    private final Map<SampledHypergeometricDistribution, Double> singleWinnerProbabilityMasses;
    /**
     * A map with the accumulated probability masses for pairs of probability mass functions as winners of the first
     * round, with equal probability mass functions combined together.
     */
    private final Map<Set<Integer>, BigDecimal> accumulatedPairProbabilityMasses;
    /**
     * A map with the probability masses for the probability mass function pairs as winners of the first round.
     */
    private final Map<Set<SampledHypergeometricDistribution>, Double> pairProbabilityMasses;
    /**
     * A list with the probability mass functions with a non-zero probability of winning the first round.
     */
    private final List<SampledHypergeometricDistribution> relevantProbabilityMassFunctions;
    /**
     * The number of iterations performed.
     */
    private long numberOfIterations;

    /**
     * Creates a sampled multivariate hypergeometric distribution based on a set of probability mass functions for an
     * sample size in a population size, for a requested number of iterations.
     *
     * @param probabilityMassFunctions    The probability mass functions to base the sampled multivariate hypergeometric
     *                                    distribution on.
     * @param populationSize              The population size.
     * @param sampleSize                  The sample size.
     * @param requestedNumberOfIterations The requested number of iterations.
     */
    SampledMultivariateHypergeometricDistribution(
            final List<SampledHypergeometricDistribution> probabilityMassFunctions, final long populationSize,
            final long sampleSize, final long requestedNumberOfIterations) {
        this.probabilityMassFunctions = Collections.unmodifiableList(probabilityMassFunctions);
        probabilityMassFunctionCardinalities = new HashMap<SampledHypergeometricDistribution, Integer>();
        accumulatedSingleWinnerProbabilityMasses = new HashMap<Integer, BigDecimal>();
        singleWinnerProbabilityMasses = new HashMap<SampledHypergeometricDistribution, Double>();
        accumulatedPairProbabilityMasses = new HashMap<Set<Integer>, BigDecimal>();
        pairProbabilityMasses = new HashMap<Set<SampledHypergeometricDistribution>, Double>();
        relevantProbabilityMassFunctions = new ArrayList<SampledHypergeometricDistribution>();
        calculateCardinalities();
        filterRelevantProbabilityMassFunctions();
        long halfPopulationSize = populationSize / 2L;
        ConfidenceInterval<Range> confidenceIntervalOfLargestList = relevantProbabilityMassFunctions.get(0)
                .getConfidenceInterval(SIX_NINES);
        if (isConfidenceIntervalAbove(confidenceIntervalOfLargestList, halfPopulationSize)) {
            accumulatedSingleWinnerProbabilityMasses.put(0, BigDecimal.ONE);
            numberOfIterations = requestedNumberOfIterations;
        } else
        // EQMU: Changing the conditional boundary below produces a mutant that is practically equivalent.
        if (relevantProbabilityMassFunctions.size() <= 2
                && isConfidenceIntervalBelow(confidenceIntervalOfLargestList, halfPopulationSize)) {
            accumulatedPairProbabilityMasses.put(Set.of(0, relevantProbabilityMassFunctions.size() == 1 ? -1 : 1),
                    BigDecimal.ONE);
            numberOfIterations = requestedNumberOfIterations;
        } else if (relevantProbabilityMassFunctions.size() <= 2
                && !isConfidenceIntervalBelow(confidenceIntervalOfLargestList, halfPopulationSize)
                && (relevantProbabilityMassFunctions.size() == 1 || isConfidenceIntervalBelow(
                        relevantProbabilityMassFunctions.get(1).getConfidenceInterval(SIX_NINES),
                        halfPopulationSize))) {
            double probabilityForDirectWin = relevantProbabilityMassFunctions.get(0)
                    .getProbabilityMassFractionAbove(halfPopulationSize);
            accumulatedSingleWinnerProbabilityMasses.put(0, new BigDecimal(probabilityForDirectWin));
            accumulatedPairProbabilityMasses.put(Set.of(0, relevantProbabilityMassFunctions.size() == 1 ? -1 : 1),
                    new BigDecimal(1D - probabilityForDirectWin));
            numberOfIterations = requestedNumberOfIterations;
        } else {
            SampledHypergeometricDistribution otherPmf = calculateProbabilityMassFunctionForOthers(populationSize,
                    sampleSize);
            Map<SampledHypergeometricDistribution, List<Range>> ranges = calculateRanges(otherPmf);
            runSimulations(ranges, otherPmf, populationSize, requestedNumberOfIterations);
        }
        convertAccumulatedProbabilityMassesToProbabilityMasses();
    }

    /**
     * Calculates how many times each probability mass function occurs in the input.
     */
    private void calculateCardinalities() {
        for (SampledHypergeometricDistribution probabilityMassFunction : probabilityMassFunctions) {
            if (probabilityMassFunctionCardinalities.containsKey(probabilityMassFunction)) {
                probabilityMassFunctionCardinalities.put(probabilityMassFunction,
                        probabilityMassFunctionCardinalities.get(probabilityMassFunction) + 1);
            } else {
                probabilityMassFunctionCardinalities.put(probabilityMassFunction, 1);
            }
        }
    }

    /**
     * Calculates the sampled hypergeometric distribution for others relative to the relevant probability mass
     * functions.
     *
     * @param populationSize      The size of the population.
     * @param effectiveSampleSize The effective sample size.
     * @return A sampled hypergeometric distribution for others relative to the relevant probability mass functions.
     */
    private SampledHypergeometricDistribution calculateProbabilityMassFunctionForOthers(final long populationSize,
            final long effectiveSampleSize) {
        long others = populationSize;
        for (SampledHypergeometricDistribution probabilityMassFunction : relevantProbabilityMassFunctions) {
            others -= probabilityMassFunction.getMedian().getMidpoint();
        }
        Long sampled = Math.round(((double) others * effectiveSampleSize) / populationSize);
        long numberOfSamples = relevantProbabilityMassFunctions.iterator().next().getNumberOfSamples();
        return SampledHypergeometricDistributions.get(sampled, (long) effectiveSampleSize, numberOfSamples,
                populationSize);
    }

    /**
     * Calculates the list of ranges of the 99.9999% confidence interval for each of the relevant probability mass
     * functions.
     *
     * @param probabilityMassFunctionForOthers The probability mass function for others relative to the relevant
     *                                         probability mass functions.
     * @return A map containing the list of ranges of the 99.9999% confidence interval for each relevant probability
     *         mass function.
     */
    private Map<SampledHypergeometricDistribution, List<Range>> calculateRanges(
            final SampledHypergeometricDistribution probabilityMassFunctionForOthers) {
        Map<SampledHypergeometricDistribution, List<Range>> ranges;
        ranges = new HashMap<SampledHypergeometricDistribution, List<Range>>();
        ranges.put(probabilityMassFunctionForOthers,
                probabilityMassFunctionForOthers.getConfidenceIntervalKeyList(SIX_NINES));
        for (SampledHypergeometricDistribution probabilityMassFunction : relevantProbabilityMassFunctions) {
            if (!ranges.containsKey(probabilityMassFunction)) {
                ranges.put(probabilityMassFunction, probabilityMassFunction.getConfidenceIntervalKeyList(SIX_NINES));
            }
        }
        return ranges;
    }

    /**
     * Calculates the sum of accumulated probability masses.
     *
     * @return The sum of accumulated probability masses.
     */
    private BigDecimal calculateSumOfAccumulatedProbabilityMasses() {
        BigDecimal sumOfProbabilityMasses = BigDecimal.ZERO;
        for (BigDecimal mass : accumulatedSingleWinnerProbabilityMasses.values()) {
            sumOfProbabilityMasses = sumOfProbabilityMasses.add(mass, MathContext.DECIMAL128);
        }
        for (BigDecimal mass : accumulatedPairProbabilityMasses.values()) {
            sumOfProbabilityMasses = sumOfProbabilityMasses.add(mass, MathContext.DECIMAL128);
        }
        return sumOfProbabilityMasses;
    }

    /**
     * Converts all the accumulated probability masses (from the simulations) to probability masses (as probabilities).
     */
    private void convertAccumulatedProbabilityMassesToProbabilityMasses() {
        BigDecimal sumOfAccumulatedProbabilityMasses = calculateSumOfAccumulatedProbabilityMasses();
        convertSingleWinnerAccumulatedProbabilityMassesToProbabilityMasses(sumOfAccumulatedProbabilityMasses);
        convertWinnerPairAccumulatedProbabilityMassesToProbabilityMasses(sumOfAccumulatedProbabilityMasses);
    }

    /**
     * Converts the single winners' accumulated probability masses (from the simulations) to probability masses (as
     * probabilities).
     *
     * @param sumOfAccumulatedProbabilityMasses The sum of accumulated probability masses.
     */
    private void convertSingleWinnerAccumulatedProbabilityMassesToProbabilityMasses(
            final BigDecimal sumOfAccumulatedProbabilityMasses) {
        for (Integer indexKey : accumulatedSingleWinnerProbabilityMasses.keySet()) {
            SampledHypergeometricDistribution key = relevantProbabilityMassFunctions.get(indexKey);
            if (singleWinnerProbabilityMasses.containsKey(key)) {
                singleWinnerProbabilityMasses.put(key,
                        singleWinnerProbabilityMasses.get(key) + accumulatedSingleWinnerProbabilityMasses.get(indexKey)
                                .divide(sumOfAccumulatedProbabilityMasses, MathContext.DECIMAL128).doubleValue());

            } else {
                singleWinnerProbabilityMasses.put(key, accumulatedSingleWinnerProbabilityMasses.get(indexKey)
                        .divide(sumOfAccumulatedProbabilityMasses, MathContext.DECIMAL128).doubleValue());
            }
        }
        Set<SampledHypergeometricDistribution> keys = singleWinnerProbabilityMasses.keySet();
        for (SampledHypergeometricDistribution key : keys) {
            singleWinnerProbabilityMasses.put(key,
                    singleWinnerProbabilityMasses.get(key) / probabilityMassFunctionCardinalities.get(key));
        }
    }

    /**
     * Converts the winner pairs' accumulated probability masses (from the simulations) to probability masses (as
     * probabilities).
     *
     * @param sumOfAccumulatedProbabilityMasses The sum of accumulated probability masses.
     */
    private void convertWinnerPairAccumulatedProbabilityMassesToProbabilityMasses(
            final BigDecimal sumOfAccumulatedProbabilityMasses) {
        for (Set<Integer> key : accumulatedPairProbabilityMasses.keySet()) {
            Set<SampledHypergeometricDistribution> actualKey = new HashSet<SampledHypergeometricDistribution>();
            for (Integer i : key) {
                actualKey.add(i == -1 ? null : relevantProbabilityMassFunctions.get(i));
            }
            if (pairProbabilityMasses.containsKey(actualKey)) {
                pairProbabilityMasses.put(actualKey,
                        pairProbabilityMasses.get(actualKey) + accumulatedPairProbabilityMasses.get(key)
                                .divide(sumOfAccumulatedProbabilityMasses, MathContext.DECIMAL128).doubleValue());
            } else {
                pairProbabilityMasses.put(actualKey, accumulatedPairProbabilityMasses.get(key)
                        .divide(sumOfAccumulatedProbabilityMasses, MathContext.DECIMAL128).doubleValue());
            }
        }
        Set<Set<SampledHypergeometricDistribution>> keySet = pairProbabilityMasses.keySet();
        for (Set<SampledHypergeometricDistribution> key : keySet) {
            if (key.size() == 1) {
                int cardinality = probabilityMassFunctionCardinalities.get(key.iterator().next());
                int factor = cardinality * (cardinality - 1) / 2;
                pairProbabilityMasses.put(key, pairProbabilityMasses.get(key) / factor);
            } else {
                int factor = 1;
                for (SampledHypergeometricDistribution pmf : key) {
                    if (pmf != null) {
                        factor *= probabilityMassFunctionCardinalities.get(pmf);
                    }
                }
                pairProbabilityMasses.put(key, pairProbabilityMasses.get(key) / factor);
            }
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SampledMultivariateHypergeometricDistribution) {
            SampledMultivariateHypergeometricDistribution other = (SampledMultivariateHypergeometricDistribution) obj;
            return other.probabilityMassFunctions.equals(probabilityMassFunctions)
                    && other.numberOfIterations == numberOfIterations;
        } else {
            return false;
        }
    }

    /**
     * Filters out the relevant probability mass functions. A probability mass function is relevant if it is one of the
     * two largest probability mass functions, or its 99.9999% confidence interval overlaps with the second largest
     * probability mass function.
     */
    private void filterRelevantProbabilityMassFunctions() {
        if (probabilityMassFunctions.size() <= 1) {
            relevantProbabilityMassFunctions.addAll(probabilityMassFunctions);
        } else {
            List<Long> lowerBounds = new ArrayList<Long>();
            for (SampledHypergeometricDistribution probabilityMassFunction : probabilityMassFunctions) {
                lowerBounds
                        .add(probabilityMassFunction.getConfidenceInterval(SIX_NINES).getLowerBound().getLowerBound());
            }
            Collections.sort(lowerBounds);
            Collections.reverse(lowerBounds);
            long lowerBound = lowerBounds.get(1);
            for (SampledHypergeometricDistribution probabilityMassFunction : probabilityMassFunctions) {
                if (!isConfidenceIntervalBelow(probabilityMassFunction.getConfidenceInterval(SIX_NINES), lowerBound)) {
                    relevantProbabilityMassFunctions.add(probabilityMassFunction);
                }
            }
        }
    }

    /**
     * Returns the number of iterations performed.
     *
     * @return The number of iterations performed.
     */
    long getNumberOfIterations() {
        return numberOfIterations;
    }

    /**
     * Returns the probability mass for a probability mass function as a single winner of the first round.
     *
     * @param pmf The probability mass function.
     * @return The probability mass for a probability mass function as a single winner of the first round.
     */
    double getProbabilityMass(final SampledHypergeometricDistribution pmf) {
        if (singleWinnerProbabilityMasses.containsKey(pmf)) {
            return singleWinnerProbabilityMasses.get(pmf);
        } else {
            return 0D;
        }
    }

    /**
     * Returns the probability mass for a pair of probability mass functions as winners of the first round.
     *
     * @param pmf1 A probability mass function.
     * @param pmf2 A probability mass function.
     * @return The probability mass for a pair of probability mass functions as winners of the first round.
     */
    double getProbabilityMass(final SampledHypergeometricDistribution pmf1,
            final SampledHypergeometricDistribution pmf2) {
        Set<SampledHypergeometricDistribution> key = new HashSet<SampledHypergeometricDistribution>();
        key.add(pmf1);
        if (!pmf1.equals(pmf2)) {
            key.add(pmf2);
        }
        if (pairProbabilityMasses.containsKey(key)) {
            return pairProbabilityMasses.get(key);
        } else {
            return 0D;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(probabilityMassFunctions, numberOfIterations);
    }

    /**
     * Checks whether a confidence interval is above a threshold.
     *
     * @param confidenceInterval A confidence interval.
     * @param threshold          A threshold.
     * @return True if the lower bound of the confidence interval is above the threshold, false otherwise.
     */
    static boolean isConfidenceIntervalAbove(final ConfidenceInterval<Range> confidenceInterval, final long threshold) {
        return confidenceInterval.getLowerBound().getLowerBound() > threshold;
    }

    /**
     * Checks whether a confidence interval is below a threshold.
     *
     * @param confidenceInterval A confidence interval.
     * @param threshold          A threshold.
     * @return True if the upper bound of the confidence interval is below the threshold, false otherwise.
     */
    static boolean isConfidenceIntervalBelow(final ConfidenceInterval<Range> confidenceInterval, final long threshold) {
        return confidenceInterval.getUpperBound().getUpperBound() < threshold;
    }

    /**
     * Runs the simulations.
     *
     * @param rangesMap                        The map with the ranges for the probability mass function.
     * @param probabilityMassFunctionForOthers The probability mass function for the others.
     * @param populationSize                   The population size.
     * @param requestedNumberOfIterations      The requested number of iterations.
     */
    private void runSimulations(final Map<SampledHypergeometricDistribution, List<Range>> rangesMap,
            final SampledHypergeometricDistribution probabilityMassFunctionForOthers, final long populationSize,
            final long requestedNumberOfIterations) {
        long halfPopulationSize = populationSize / 2L;
        Random random = new Random();
        numberOfIterations = 0;
        List<Range> otherRanges = rangesMap.get(probabilityMassFunctionForOthers);
        long otherRangesUpperbound = otherRanges.get(otherRanges.size() - 1).getUpperBound();
        int numberOfRelevantProbabilityMassFunctions = relevantProbabilityMassFunctions.size();
        // Using SampledHypergeometricDistribution as a key is time consuming, hence we build a parallel list with
        // ranges.
        List<List<Range>> rangesList = new ArrayList<List<Range>>();
        for (SampledHypergeometricDistribution probabilityMassFunction : relevantProbabilityMassFunctions) {
            rangesList.add(rangesMap.get(probabilityMassFunction));
        }
        WinnersRegister winnersRegister = new WinnersRegister();
        while (numberOfIterations < requestedNumberOfIterations) {
            BigDecimal probabilityMass = BigDecimal.ONE;
            long sumOfMidpoints = 0;
            winnersRegister.initialize();
            for (int i = 0; i < numberOfRelevantProbabilityMassFunctions; i++) {
                SampledHypergeometricDistribution probabilityMassFunction = relevantProbabilityMassFunctions.get(i);
                List<Range> ranges = rangesList.get(i);
                Range range = ranges.get(random.nextInt(ranges.size()));
                sumOfMidpoints += range.getMidpoint();
                probabilityMass = probabilityMass.multiply(probabilityMassFunction.getProbabilityMass(range),
                        MathContext.DECIMAL128);
                winnersRegister.update(range, i);
            }
            if (sumOfMidpoints < populationSize) {
                Long remainder = populationSize - sumOfMidpoints;
                if (otherRangesUpperbound > remainder) {
                    Range otherRange = otherRanges.get(0);
                    for (Range r : otherRanges) {
                        // EQMU: Changing the conditional boundary below produces a mutant that is practically
                        // equivalent.
                        if (r.getUpperBound() > remainder) {
                            otherRange = r;
                            break;
                        }
                    }
                    probabilityMass = probabilityMass.multiply(
                            probabilityMassFunctionForOthers.getProbabilityMass(otherRange), MathContext.DECIMAL128);
                    int indexOfLargestRange = winnersRegister.getIndexOfLargestRange();
                    if (winnersRegister.getLargestRange().getMidpoint() > halfPopulationSize) {
                        if (accumulatedSingleWinnerProbabilityMasses.containsKey(indexOfLargestRange)) {
                            accumulatedSingleWinnerProbabilityMasses.put(indexOfLargestRange,
                                    probabilityMass.add(
                                            accumulatedSingleWinnerProbabilityMasses.get(indexOfLargestRange),
                                            MathContext.DECIMAL128));
                        } else {
                            accumulatedSingleWinnerProbabilityMasses.put(indexOfLargestRange, probabilityMass);
                        }
                    } else {
                        Set<Integer> runoffPair = Set.of(indexOfLargestRange,
                                winnersRegister.getIndexOfSecondLargestRange());
                        if (accumulatedPairProbabilityMasses.containsKey(runoffPair)) {
                            accumulatedPairProbabilityMasses.put(runoffPair, probabilityMass
                                    .add(accumulatedPairProbabilityMasses.get(runoffPair), MathContext.DECIMAL128));
                        } else {
                            accumulatedPairProbabilityMasses.put(runoffPair, probabilityMass);
                        }
                    }
                    numberOfIterations += 1;
                }
            }
        }
    }
}
