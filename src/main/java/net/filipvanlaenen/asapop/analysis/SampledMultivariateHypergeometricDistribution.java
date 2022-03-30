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

class SampledMultivariateHypergeometricDistribution {
    private final Map<SampledHypergeometricDistribution, Integer> probabilityMassFunctionCardinalities = new HashMap<SampledHypergeometricDistribution, Integer>();
    private final Map<Integer, BigDecimal> accumulatedSingleWinnerProbabilityMasses = new HashMap<Integer, BigDecimal>();
    private final Map<SampledHypergeometricDistribution, Double> singleWinnerProbabilityMasses = new HashMap<SampledHypergeometricDistribution, Double>();
    private final Map<Set<Integer>, BigDecimal> accumulatedPairProbabilityMasses = new HashMap<Set<Integer>, BigDecimal>();
    private final Map<Set<SampledHypergeometricDistribution>, Double> pairProbabilityMasses = new HashMap<Set<SampledHypergeometricDistribution>, Double>();
    private final List<SampledHypergeometricDistribution> relevantProbabilityMassFunctions = new ArrayList<SampledHypergeometricDistribution>();
    private long numberOfIterations;

    SampledMultivariateHypergeometricDistribution(List<SampledHypergeometricDistribution> probabilityMassFunctions,
            long populationSize, long effectiveSampleSize, final long numberOfIterations) {
        calculateCardinalities(probabilityMassFunctions);
        filterRelevantProbabilityFunctions(probabilityMassFunctions);
        SampledHypergeometricDistribution otherPmf = calculateProbabilityMassFunctionForOthers(populationSize,
                effectiveSampleSize, numberOfIterations);
        Map<SampledHypergeometricDistribution, List<Range>> ranges = calculateRanges(otherPmf);
        runSimulations(ranges, otherPmf, populationSize, numberOfIterations);
        convertAccumulatedProbabilityMassesToProbabilityMasses();
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

    private void runSimulations(Map<SampledHypergeometricDistribution, List<Range>> rangesMap,
            SampledHypergeometricDistribution otherPmf, long populationSize, final long requestedNumberOfIterations) {
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

    private Map<SampledHypergeometricDistribution, List<Range>> calculateRanges(
            SampledHypergeometricDistribution otherPmf) {
        Map<SampledHypergeometricDistribution, List<Range>> ranges = new HashMap<SampledHypergeometricDistribution, List<Range>>();
        ranges.put(otherPmf, otherPmf.getConfidenceIntervalKeyList(0.999999));
        for (SampledHypergeometricDistribution probabilityMassFunction : relevantProbabilityMassFunctions) {
            if (!ranges.containsKey(probabilityMassFunction)) {
                ranges.put(probabilityMassFunction, probabilityMassFunction.getConfidenceIntervalKeyList(0.999999));
            }
        }
        return ranges;
    }

    private SampledHypergeometricDistribution calculateProbabilityMassFunctionForOthers(long populationSize,
            long effectiveSampleSize, long numberOfIterations) {
        long others = populationSize;
        // TODO: Should be calculated based on the numbers registered in the poll, not the medians from the PMFs
        for (SampledHypergeometricDistribution probabilityMassFunction : relevantProbabilityMassFunctions) {
            others -= probabilityMassFunction.getMedian().getMidpoint();
        }
        Long sampled = Math.round(((double) others * effectiveSampleSize) / populationSize);
        // TODO: Should be according to the precision (1, 0.5 or 0.1).
        return SampledHypergeometricDistributions.get(sampled, (long) effectiveSampleSize, numberOfIterations,
                populationSize);
    }

    private void filterRelevantProbabilityFunctions(List<SampledHypergeometricDistribution> probabilityMassFunctions) {
        if (probabilityMassFunctions.size() <= 2) {
            return;
        }
        List<Long> lowerBounds = new ArrayList<Long>();
        for (SampledHypergeometricDistribution probabilityMassFunction : probabilityMassFunctions) {
            lowerBounds.add(probabilityMassFunction.getConfidenceInterval(0.999999).getLowerBound().getLowerBound());
        }
        Collections.sort(lowerBounds);
        Collections.reverse(lowerBounds);
        long lowerBound = lowerBounds.get(1);
        for (SampledHypergeometricDistribution probabilityMassFunction : probabilityMassFunctions) {
            if (probabilityMassFunction.getConfidenceInterval(0.999999).getUpperBound().getUpperBound() >= lowerBound) {
                relevantProbabilityMassFunctions.add(probabilityMassFunction);
            }
        }
    }

    private void calculateCardinalities(List<SampledHypergeometricDistribution> probabilityMassFunctions) {
        for (SampledHypergeometricDistribution probabilityMassFunction : probabilityMassFunctions) {
            if (probabilityMassFunctionCardinalities.containsKey(probabilityMassFunction)) {
                probabilityMassFunctionCardinalities.put(probabilityMassFunction,
                        probabilityMassFunctionCardinalities.get(probabilityMassFunction) + 1);
            } else {
                probabilityMassFunctionCardinalities.put(probabilityMassFunction, 1);
            }
        }
    }

    long getNumberOfIterations() {
        return numberOfIterations;
    }

    double getProbabilityMass(SampledHypergeometricDistribution of) {
        if (singleWinnerProbabilityMasses.containsKey(of)) {
            return singleWinnerProbabilityMasses.get(of);
        } else {
            return 0D;
        }
    }

    double getProbabilityMass(SampledHypergeometricDistribution of1, SampledHypergeometricDistribution of2) {
        Set<SampledHypergeometricDistribution> key = of1.equals(of2) ? Set.of(of1) : Set.of(of1, of2);
        if (pairProbabilityMasses.containsKey(key)) {
            return pairProbabilityMasses.get(key);
        } else {
            return 0D;
        }
    }

}
