package net.filipvanlaenen.asapop.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class representing a list of opinion polls.
 */
public final class OpinionPolls {
    /**
     * The overall lowest effective sample size.
     */
    private final Integer lowestEffectiveSampleSize;
    /**
     * The overall lowest sample size.
     */
    private final Integer lowestSampleSize;
    /**
     * A map containing the lowest effective sample sizes per polling firm.
     */
    private final Map<String, Integer> lowestEffectiveSampleSizes = new HashMap<String, Integer>();
    /**
     * A map containing the lowest sample sizes per polling firm.
     */
    private final Map<String, Integer> lowestSampleSizes = new HashMap<String, Integer>();
    /**
     * The set with the opinion polls.
     */
    private final Set<OpinionPoll> opinionPolls;

    /**
     * Constructor taking a set of opinion polls as its parameter.
     *
     * @param opinionPolls A set of opinion polls.
     */
    public OpinionPolls(final Set<OpinionPoll> opinionPolls) {
        this.opinionPolls = Collections.unmodifiableSet(opinionPolls);
        lowestSampleSize = calculateLowestSampleSize(false);
        lowestEffectiveSampleSize = calculateLowestSampleSize(true);
    }

    /**
     * Calculates the overall lowest sample size.
     *
     * @param effective Whether the sample sizes should be effective or not.
     * @return The lowest sample size.
     */
    private Integer calculateLowestSampleSize(final boolean effective) {
        return calculateLowestSampleSize(opinionPolls, effective);
    }

    /**
     * Calculates the overall lowest sample size for a collection of opinion polls.
     *
     * @param collection A collection of opinion polls.
     * @param effective  Whether the sample sizes should be effective or not.
     * @return The lowest sample size.
     */
    private Integer calculateLowestSampleSize(final Collection<OpinionPoll> collection, final boolean effective) {
        Set<Integer> sampleSizes =
                collection.stream().map(ol -> effective ? ol.getEffectiveSampleSize() : ol.getSampleSizeValue())
                        .filter(Objects::nonNull).collect(Collectors.toSet());
        return sampleSizes.isEmpty() ? null : Collections.min(sampleSizes);
    }

    /**
     * Calculates the lowest effective sample size registered for the polling firm.
     *
     * @param pollingFirm The polling firm.
     */
    private void calculateLowestEffectiveSampleSize(final String pollingFirm) {
        lowestEffectiveSampleSizes
                .put(pollingFirm,
                        calculateLowestSampleSize(opinionPolls.stream()
                                .filter(ol -> pollingFirm.equals(ol.getPollingFirm())).collect(Collectors.toSet()),
                                true));
    }

    /**
     * Calculates the lowest sample size registered for the polling firm.
     *
     * @param pollingFirm The polling firm.
     */
    private void calculateLowestSampleSize(final String pollingFirm) {
        lowestSampleSizes
                .put(pollingFirm,
                        calculateLowestSampleSize(opinionPolls.stream()
                                .filter(ol -> pollingFirm.equals(ol.getPollingFirm())).collect(Collectors.toSet()),
                                false));
    }

    /**
     * Returns the lowest effective sample size registered for the polling firm.
     *
     * @param pollingFirm The polling firm.
     * @return The lowest effective sample size registered for the polling firm.
     */
    public Integer getLowestEffectiveSampleSize(final String pollingFirm) {
        if (!lowestEffectiveSampleSizes.containsKey(pollingFirm)) {
            calculateLowestEffectiveSampleSize(pollingFirm);
        }
        Integer result = lowestEffectiveSampleSizes.get(pollingFirm);
        return result == null ? lowestEffectiveSampleSize : result;
    }

    /**
     * Returns the lowest sample size registered for the polling firm.
     *
     * @param pollingFirm The polling firm.
     * @return The lowest sample size registered for the polling firm.
     */
    public Integer getLowestSampleSize(final String pollingFirm) {
        if (!lowestSampleSizes.containsKey(pollingFirm)) {
            calculateLowestSampleSize(pollingFirm);
        }
        Integer result = lowestSampleSizes.get(pollingFirm);
        return result == null ? lowestSampleSize : result;
    }

    /**
     * Returns the most recent date.
     *
     * @return The most recent date.
     */
    public LocalDate getMostRecentDate() {
        LocalDate result = LocalDate.EPOCH;
        for (OpinionPoll opinionPoll : opinionPolls) {
            LocalDate endDate = opinionPoll.getEndDate();
            if (endDate.isAfter(result)) {
                result = endDate;
            }
        }
        return result;
    }

    /**
     * Returns the number of opinion polls.
     *
     * @return The number of opinion polls.
     */
    public int getNumberOfOpinionPolls() {
        return opinionPolls.size();
    }

    /**
     * Returns the number of opinion polls with an end date on or after the date.
     *
     * @param fromDate The cut-off date to count the number of opinion polls.
     * @return The number of opinion polls with an end date on or after the date.
     */
    public int getNumberOfOpinionPolls(final LocalDate fromDate) {
        int result = 0;
        for (OpinionPoll opinionPoll : opinionPolls) {
            if (!opinionPoll.getEndDate().isBefore(fromDate)) {
                result += 1;
            }
        }
        return result;
    }

    /**
     * Returns the number of response scenarios.
     *
     * @return The number of response scenarios.
     */
    public int getNumberOfResponseScenarios() {
        int result = 0;
        for (OpinionPoll opinionPoll : opinionPolls) {
            result += opinionPoll.getNumberOfResponseScenarios();
        }
        return result;
    }

    /**
     * Returns the number of response scenarios for opinion polls with an end date on or after the date.
     *
     * @param fromDate The cut-off date to count the number of response scenarios.
     * @return The number of response scenarios for opinion polls with an end date on or after the date.
     */
    public int getNumberOfResponseScenarios(final LocalDate fromDate) {
        int result = 0;
        for (OpinionPoll opinionPoll : opinionPolls) {
            if (!opinionPoll.getEndDate().isBefore(fromDate)) {
                result += opinionPoll.getNumberOfResponseScenarios();
            }
        }
        return result;
    }

    /**
     * Returns the number or result values.
     *
     * @return The number of result values.
     */
    public int getNumberOfResultValues() {
        int result = 0;
        for (OpinionPoll opinionPoll : opinionPolls) {
            result += opinionPoll.getNumberOfResultValues();
        }
        return result;
    }

    /**
     * Returns the number of result values for opinion polls with an end date on or after the date.
     *
     * @param fromDate The cut-off date to count the number of result values.
     * @return The number of result values for opinion polls with an end date on or after the date.
     */
    public int getNumberOfResultValues(final LocalDate fromDate) {
        int result = 0;
        for (OpinionPoll opinionPoll : opinionPolls) {
            if (!opinionPoll.getEndDate().isBefore(fromDate)) {
                result += opinionPoll.getNumberOfResultValues();
            }
        }
        return result;
    }

    /**
     * Returns the opinion polls.
     *
     * @return An unmodifiable set with the opinion polls.
     */
    public Set<OpinionPoll> getOpinionPolls() {
        return opinionPolls;
    }
}
