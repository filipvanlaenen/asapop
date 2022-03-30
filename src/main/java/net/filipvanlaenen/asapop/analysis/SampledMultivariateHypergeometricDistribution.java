package net.filipvanlaenen.asapop.analysis;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Class representing a multivariate hypergeometric distribution, but sampled.
 */
class SampledMultivariateHypergeometricDistribution {
    /**
     * The magic number 0.999999 (six nines).
     */
    private static final double SIX_NINES = 0.999999;
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
        probabilityMassFunctionCardinalities = new HashMap<SampledHypergeometricDistribution, Integer>();
        accumulatedSingleWinnerProbabilityMasses = new HashMap<Integer, BigDecimal>();
        singleWinnerProbabilityMasses = new HashMap<SampledHypergeometricDistribution, Double>();
        accumulatedPairProbabilityMasses = new HashMap<Set<Integer>, BigDecimal>();
        pairProbabilityMasses = new HashMap<Set<SampledHypergeometricDistribution>, Double>();
        relevantProbabilityMassFunctions = new ArrayList<SampledHypergeometricDistribution>();
        calculateCardinalities(probabilityMassFunctions);
        filterRelevantProbabilityFunctions(probabilityMassFunctions);
        SampledHypergeometricDistribution otherPmf = calculateProbabilityMassFunctionForOthers(populationSize,
                sampleSize);
        Map<SampledHypergeometricDistribution, List<Range>> ranges = calculateRanges(otherPmf);
        runSimulations(ranges, otherPmf, populationSize, requestedNumberOfIterations);
        convertAccumulatedProbabilityMassesToProbabilityMasses();
    }

    /**
     * Calculates how many times each probability mass function occurs in the input.
     *
     * @param probabilityMassFunctions The probability mass functions to base the sampled multivariate hypergeometric
     *                                 distribution on.
     */
    private void calculateCardinalities(final List<SampledHypergeometricDistribution> probabilityMassFunctions) {
        for (SampledHypergeometricDistribution probabilityMassFunction : probabilityMassFunctions) {
            if (probabilityMassFunctionCardinalities.containsKey(probabilityMassFunction)) {
                probabilityMassFunctionCardinalities.put(probabilityMassFunction,
                        probabilityMassFunctionCardinalities.get(probabilityMassFunction) + 1);
            } else {
                probabilityMassFunctionCardinalities.put(probabilityMassFunction, 1);
            }
        }
    }

    private SampledHypergeometricDistribution calculateProbabilityMassFunctionForOthers(final long populationSize,
            final long effectiveSampleSize) {
        long others = populationSize;
        // TODO: Should be calculated based on the numbers registered in the poll, not the medians from the PMFs
        for (SampledHypergeometricDistribution probabilityMassFunction : relevantProbabilityMassFunctions) {
            others -= probabilityMassFunction.getMedian().getMidpoint();
        }
        Long sampled = Math.round(((double) others * effectiveSampleSize) / populationSize);
        // TODO: Should be according to the precision (1, 0.5 or 0.1).
        long numberOfIterations = relevantProbabilityMassFunctions.iterator().next().getNumberOfSamples();
        return SampledHypergeometricDistributions.get(sampled, (long) effectiveSampleSize, numberOfIterations,
                populationSize);
    }

    private Map<SampledHypergeometricDistribution, List<Range>> calculateRanges(
            final SampledHypergeometricDistribution otherPmf) {
        Map<SampledHypergeometricDistribution, List<Range>> ranges = new HashMap<SampledHypergeometricDistribution, List<Range>>();
        ranges.put(otherPmf, otherPmf.getConfidenceIntervalKeyList(SIX_NINES));
        for (SampledHypergeometricDistribution probabilityMassFunction : relevantProbabilityMassFunctions) {
            if (!ranges.containsKey(probabilityMassFunction)) {
                ranges.put(probabilityMassFunction, probabilityMassFunction.getConfidenceIntervalKeyList(SIX_NINES));
            }
        }
        return ranges;
    }

    private void convertAccumulatedProbabilityMassesToProbabilityMasses() {
        BigDecimal sumOfProbabilityMasses = BigDecimal.ZERO;
        for (BigDecimal mass : accumulatedSingleWinnerProbabilityMasses.values()) {
            sumOfProbabilityMasses = sumOfProbabilityMasses.add(mass, MathContext.DECIMAL128);
        }
        for (BigDecimal mass : accumulatedPairProbabilityMasses.values()) {
            sumOfProbabilityMasses = sumOfProbabilityMasses.add(mass, MathContext.DECIMAL128);
        }
        sumOfProbabilityMasses = sumOfProbabilityMasses.divide(new BigDecimal(100), MathContext.DECIMAL128);
        for (Integer indexKey : accumulatedSingleWinnerProbabilityMasses.keySet()) {
            SampledHypergeometricDistribution key = relevantProbabilityMassFunctions.get(indexKey);
            if (singleWinnerProbabilityMasses.containsKey(key)) {
                singleWinnerProbabilityMasses.put(key,
                        singleWinnerProbabilityMasses.get(key) + accumulatedSingleWinnerProbabilityMasses.get(indexKey)
                                .divide(sumOfProbabilityMasses, MathContext.DECIMAL128).doubleValue());

            } else {
                singleWinnerProbabilityMasses.put(key, accumulatedSingleWinnerProbabilityMasses.get(indexKey)
                        .divide(sumOfProbabilityMasses, MathContext.DECIMAL128).doubleValue());
            }
        }
        Set<SampledHypergeometricDistribution> keys = singleWinnerProbabilityMasses.keySet();
        for (SampledHypergeometricDistribution key : keys) {
            singleWinnerProbabilityMasses.put(key,
                    singleWinnerProbabilityMasses.get(key) / probabilityMassFunctionCardinalities.get(key));
        }
        for (Set<Integer> key : accumulatedPairProbabilityMasses.keySet()) {
            Set<SampledHypergeometricDistribution> actualKey = new HashSet<SampledHypergeometricDistribution>();
            for (Integer i : key) {
                actualKey.add(relevantProbabilityMassFunctions.get(i));
            }
            if (pairProbabilityMasses.containsKey(actualKey)) {
                pairProbabilityMasses.put(actualKey,
                        pairProbabilityMasses.get(actualKey) + accumulatedPairProbabilityMasses.get(key)
                                .divide(sumOfProbabilityMasses, MathContext.DECIMAL128).doubleValue());
            } else {
                pairProbabilityMasses.put(actualKey, accumulatedPairProbabilityMasses.get(key)
                        .divide(sumOfProbabilityMasses, MathContext.DECIMAL128).doubleValue());
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
                    factor *= probabilityMassFunctionCardinalities.get(pmf);
                }
                pairProbabilityMasses.put(key, pairProbabilityMasses.get(key) / factor);
            }
        }
    }

    private void filterRelevantProbabilityFunctions(
            final List<SampledHypergeometricDistribution> probabilityMassFunctions) {
        if (probabilityMassFunctions.size() <= 2) {
            return;
        }
        List<Long> lowerBounds = new ArrayList<Long>();
        for (SampledHypergeometricDistribution probabilityMassFunction : probabilityMassFunctions) {
            lowerBounds.add(probabilityMassFunction.getConfidenceInterval(SIX_NINES).getLowerBound().getLowerBound());
        }
        Collections.sort(lowerBounds);
        Collections.reverse(lowerBounds);
        long lowerBound = lowerBounds.get(1);
        for (SampledHypergeometricDistribution probabilityMassFunction : probabilityMassFunctions) {
            if (probabilityMassFunction.getConfidenceInterval(SIX_NINES).getUpperBound()
                    .getUpperBound() >= lowerBound) {
                relevantProbabilityMassFunctions.add(probabilityMassFunction);
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
        Set<SampledHypergeometricDistribution> key = pmf1.equals(pmf2) ? Set.of(pmf1) : Set.of(pmf1, pmf2);
        if (pairProbabilityMasses.containsKey(key)) {
            return pairProbabilityMasses.get(key);
        } else {
            return 0D;
        }
    }

    private void runSimulations(final Map<SampledHypergeometricDistribution, List<Range>> rangesMap,
            final SampledHypergeometricDistribution otherPmf, final long populationSize,
            final long requestedNumberOfIterations) {
        long halfPopulationSize = populationSize / 2L;
        // TODO: Eliminate use of random
        Random random = new Random();
        numberOfIterations = 0;
        List<Range> otherRanges = rangesMap.get(otherPmf);
        long otherRangesUpperbound = otherRanges.get(otherRanges.size() - 1).getUpperBound();
        int numberOfRelevantProbabilityMassFunctions = relevantProbabilityMassFunctions.size();
        /**
         * Using SampledHypergeometricDistribution as a key is time consuming, hence we build a parallel list with
         * ranges.
         */
        List<List<Range>> rangesList = new ArrayList<List<Range>>();
        for (SampledHypergeometricDistribution probabilityMassFunction : relevantProbabilityMassFunctions) {
            rangesList.add(rangesMap.get(probabilityMassFunction));
        }
        while (numberOfIterations < requestedNumberOfIterations) {
            BigDecimal p = BigDecimal.ONE;
            long sumOfMidpoints = 0;
            Range firstRange = null;
            Range secondRange = null;
            Integer firstIndex = null;
            Integer secondIndex = null;
            for (int j = 0; j < numberOfRelevantProbabilityMassFunctions; j++) {
                SampledHypergeometricDistribution probabilityMassFunction = relevantProbabilityMassFunctions.get(j);
                List<Range> ranges = rangesList.get(j);
                Range range = ranges.get(random.nextInt(ranges.size()));
                sumOfMidpoints += range.getMidpoint();
                p = p.multiply(probabilityMassFunction.getProbabilityMass(range), MathContext.DECIMAL128);
                if (firstRange == null) {
                    firstRange = range;
                    firstIndex = j;
                } else if (secondRange == null) {
                    if (firstRange.compareTo(range) > 0) {
                        secondRange = range;
                        secondIndex = j;
                    } else {
                        secondRange = firstRange;
                        secondIndex = firstIndex;
                        firstRange = range;
                        firstIndex = j;
                    }
                } else if (firstRange.compareTo(range) < 0) {
                    secondRange = firstRange;
                    secondIndex = firstIndex;
                    firstRange = range;
                    firstIndex = j;
                } else if (secondRange.compareTo(range) < 0) {
                    secondRange = range;
                    secondIndex = j;
                }
            }
            if (sumOfMidpoints < populationSize) {
                Long o = populationSize - sumOfMidpoints;
                if (otherRangesUpperbound > o) {
                    Range otherRange = otherRanges.get(0);
                    for (Range r : otherRanges) {
                        if (r.getUpperBound() > o) {
                            otherRange = r;
                            break;
                        }
                    }
                    p = p.multiply(otherPmf.getProbabilityMass(otherRange), MathContext.DECIMAL128);
                    if (firstRange.getMidpoint() > halfPopulationSize) {
                        if (accumulatedSingleWinnerProbabilityMasses.containsKey(firstIndex)) {
                            accumulatedSingleWinnerProbabilityMasses.put(firstIndex, p.add(
                                    accumulatedSingleWinnerProbabilityMasses.get(firstIndex), MathContext.DECIMAL128));
                        } else {
                            accumulatedSingleWinnerProbabilityMasses.put(firstIndex, p);
                        }
                    } else {
                        Set<Integer> runoffPair = Set.of(firstIndex, secondIndex);
                        if (accumulatedPairProbabilityMasses.containsKey(runoffPair)) {
                            accumulatedPairProbabilityMasses.put(runoffPair,
                                    p.add(accumulatedPairProbabilityMasses.get(runoffPair), MathContext.DECIMAL128));
                        } else {
                            accumulatedPairProbabilityMasses.put(runoffPair, p);
                        }
                    }
                    numberOfIterations += 1;
                }
            }
        }
    }
}
