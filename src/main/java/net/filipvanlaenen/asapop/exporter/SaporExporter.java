package net.filipvanlaenen.asapop.exporter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.Scope;
import net.filipvanlaenen.asapop.model.Unit;
import net.filipvanlaenen.asapop.yaml.AdditiveSaporMapping;
import net.filipvanlaenen.asapop.yaml.AdditiveSplittingSaporMapping;
import net.filipvanlaenen.asapop.yaml.DirectSaporMapping;
import net.filipvanlaenen.asapop.yaml.EssentialEntriesSaporMapping;
import net.filipvanlaenen.asapop.yaml.SaporConfiguration;
import net.filipvanlaenen.asapop.yaml.SaporMapping;
import net.filipvanlaenen.asapop.yaml.SplittingSaporMapping;

/**
 * Exporter to the SAPOR files format.
 */
public class SaporExporter extends Exporter {
    /**
     * The magic number four.
     */
    private static final double FOUR = 4D;
    /**
     * The magic number one hundred.
     */
    private static final double ONE_HUNDRED = 100D;
    /**
     * The string for the pattern to match the separator between electoral list IDs.
     */
    static final String ELECTORAL_LIST_ID_SEPARATOR = "\\+";
    /**
     * The area as it should be exported to the SAPOR files.
     */
    private final String area;
    /**
     * The date of the last election.
     */
    private final LocalDate lastElectionDate;
    /**
     * The mapping to create the SAPOR bodies.
     */
    private final Set<SaporMapping> mapping;
    /**
     * A set with the mapped electoral list combinations.
     */
    private final Set<Set<ElectoralList>> mappedElectoralListCombinations;
    /**
     * The region.
     */
    private final String region;
    /**
     * The scope.
     */
    private final Scope scope;

    /**
     * Creates an exporter taking the SAPOR configuration as its parameter.
     *
     * @param saporConfiguration The SAPOR configuration.
     */
    public SaporExporter(final SaporConfiguration saporConfiguration) {
        this.area = saporConfiguration.getArea();
        this.lastElectionDate = LocalDate.parse(saporConfiguration.getLastElectionDate());
        this.mapping = saporConfiguration.getMapping();
        this.mappedElectoralListCombinations = calculateMappedElectoralListCombinations();
        this.scope = saporConfiguration.getScope() == null ? null : Scope.parse(saporConfiguration.getScope());
        this.region = saporConfiguration.getRegion();
    }

    /**
     * Appends the SAPOR body for an opinion poll to a StringBuilder.
     *
     * @param content                   The StringBuilder to append the SAPOR body to.
     * @param lowestSampleSize          The lowest sample size for the polling firm of this opinion poll.
     * @param lowestEffectiveSampleSize The lowest effective sample size for the polling firm of this opinion poll.
     * @param opinionPoll               The opinion poll.
     */
    void appendSaporBody(final StringBuilder content, final OpinionPoll opinionPoll, final Integer lowestSampleSize,
            final Integer lowestEffectiveSampleSize) {
        ResponseScenario responseScenario = getMatchingResponseScenario(opinionPoll);
        Integer calculationSampleSize = responseScenario.getSampleSizeValue();
        boolean unitIsSeats = Unit.SEATS == opinionPoll.getUnit();
        boolean hasNoResponses = responseScenario.getNoResponses() != null;
        boolean hasExcluded = responseScenario.getExcluded() != null;
        boolean hasOther = responseScenario.getOther() != null;
        boolean strictlyWithinRoundingError = responseScenario.isStrictlyWithinRoundingError();
        double sumOfActualValues = 0D;
        Map<Set<ElectoralList>, Double> actualValues = new HashMap<Set<ElectoralList>, Double>();
        double zeroValue = calculatePrecision(responseScenario).getValue() / FOUR;
        for (Set<ElectoralList> electoralLists : responseScenario.getElectoralListSets()) {
            Set<String> electoralListIds = ElectoralList.getIds(electoralLists);
            double value = responseScenario.getResult(electoralListIds).getNominalValue();
            double actualValue = value == 0D ? zeroValue : value;
            sumOfActualValues += actualValue;
            actualValues.put(electoralLists, actualValue);
        }
        double actualOtherValue = 0D;
        if (hasOther) {
            double otherValue = responseScenario.getOther().getNominalValue();
            actualOtherValue = otherValue == 0D ? zeroValue : otherValue;
        }
        double sumOfOtherAndActualValues = sumOfActualValues + actualOtherValue;
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        boolean hasImplicitlyNoResponses = !unitIsSeats && !hasNoResponses && hasOther && !strictlyWithinRoundingError
                && sumOfOtherAndActualValues < ONE_HUNDRED;
        if (calculationSampleSize == null) {
            if (hasNoResponses) {
                calculationSampleSize = lowestSampleSize;
            } else if (hasExcluded) {
                calculationSampleSize = (int) Math
                        .round(lowestSampleSize * (1F - responseScenario.getExcluded().value() / ONE_HUNDRED));
            } else {
                calculationSampleSize = lowestEffectiveSampleSize;
            }
        } else if (!hasNoResponses && !hasImplicitlyNoResponses && hasExcluded) {
            calculationSampleSize = (int) Math
                    .round(calculationSampleSize * (1F - responseScenario.getExcluded().value() / ONE_HUNDRED));
        }
        double actualNoResponsesValue = 0D;
        if (hasNoResponses) {
            double noResponsesValue = responseScenario.getNoResponses().getNominalValue();
            actualNoResponsesValue = noResponsesValue == 0D ? zeroValue : noResponsesValue;
        }
        double actualTotalSum = sumOfOtherAndActualValues + actualNoResponsesValue;
        double scale = 1D;
        if (unitIsSeats) {
            scale = ONE_HUNDRED / sumOfOtherAndActualValues;
        } else {
            // EQMU: Changing the conditional boundary below produces an equivalent mutant.
            if (actualTotalSum > ONE_HUNDRED) {
                scale = ONE_HUNDRED / actualTotalSum;
            }
            // EQMU: Changing the conditional boundary below produces an equivalent mutant.
            if (hasOther && hasNoResponses && actualTotalSum < ONE_HUNDRED) {
                scale = ONE_HUNDRED / actualTotalSum;
            }
        }
        int remainder = calculationSampleSize;
        if (hasNoResponses || hasImplicitlyNoResponses) {
            if (hasOther) {
                remainder = (int) Math.round(calculationSampleSize * sumOfOtherAndActualValues * scale / ONE_HUNDRED);
            } else {
                remainder =
                        (int) Math.round(calculationSampleSize * (ONE_HUNDRED - actualNoResponsesValue) / ONE_HUNDRED);
            }
        }
        for (SaporMapping map : mapping) {
            if (dateIsInMappingValidityPeriod(map, opinionPoll.getEndDate())) {
                remainder = processMapping(content, map.getDirectMapping(), actualValues, calculationSampleSize, scale,
                        remainder);
                remainder = processMapping(content, map.getAdditiveMapping(), actualValues, calculationSampleSize,
                        scale, remainder);
                remainder = processMapping(content, map.getAdditiveSplittingMapping(), actualValues,
                        calculationSampleSize, scale, remainder);
                remainder = processMapping(content, map.getSplittingMapping(), actualValues, calculationSampleSize,
                        scale, remainder);
            }
        }
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        if (remainder <= 0) {
            content.append("Other=0\n");
        } else {
            for (SaporMapping map : mapping) {
                if (dateIsInMappingValidityPeriod(map, opinionPoll.getEndDate())) {
                    remainder = processMapping(content, map.getEssentialEntriesMapping(), remainder);
                }
            }
            content.append("Other=");
            // EQMU: Changing the conditional boundary below produces an equivalent mutant.
            content.append(remainder < 0 ? 0 : remainder);
            content.append("\n");
        }

    }

    /**
     * Appends the SAPOR header for an opinion poll to a StringBuilder.
     *
     * @param content     The StringBuilder to append the SAPOR header to.
     * @param opinionPoll The opinion poll.
     */
    void appendSaporHeader(final StringBuilder content, final OpinionPoll opinionPoll) {
        content.append("Type=Election\n");
        content.append("PollingFirm=");
        content.append(exportPollingFirms(opinionPoll));
        content.append("\n");
        if (!opinionPoll.getCommissioners().isEmpty()) {
            content.append("Commissioners=");
            content.append(exportCommissioners(opinionPoll));
            content.append("\n");
        }
        content.append("FieldworkStart=");
        if (opinionPoll.getFieldworkStart() != null) {
            content.append(opinionPoll.getFieldworkStart().getStart().toString());
        } else if (opinionPoll.getFieldworkEnd() != null) {
            content.append(opinionPoll.getFieldworkEnd().getStart().toString());
        } else {
            content.append(opinionPoll.getPublicationDate().toString());
        }
        content.append("\n");
        content.append("FieldworkEnd=");
        content.append(opinionPoll.getEndDate().toString());
        content.append("\n");
        content.append("Area=");
        content.append(area);
        content.append("\n");
    }

    /**
     * Converts a source string to an electoral list combination.
     *
     * @param source The source string to convert.
     * @return A set of electoral lists converted from the source string.
     */
    private Set<ElectoralList> asElectoralListCombination(final String source) {
        Set<String> ids = new HashSet<String>(Arrays.asList(source.split(ELECTORAL_LIST_ID_SEPARATOR)));
        return ElectoralList.get(ids);
    }

    /**
     * Calculate the set of electoral list combinations covered by the SAPOR mapping.
     *
     * @return A set with the electoral list combinations covered by the SAPOR mapping.
     */
    private Set<Set<ElectoralList>> calculateMappedElectoralListCombinations() {
        Set<Set<ElectoralList>> result = new HashSet<Set<ElectoralList>>();
        for (SaporMapping saporMapping : mapping) {
            DirectSaporMapping directSaporMapping = saporMapping.getDirectMapping();
            if (directSaporMapping != null) {
                result.add(asElectoralListCombination(directSaporMapping.getSource()));
            }
            AdditiveSaporMapping additiveSaporMapping = saporMapping.getAdditiveMapping();
            if (additiveSaporMapping != null) {
                for (String source : additiveSaporMapping.getSources()) {
                    result.add(asElectoralListCombination(source));
                }
            }
            AdditiveSplittingSaporMapping additiveSplittingSaporMapping = saporMapping.getAdditiveSplittingMapping();
            if (additiveSplittingSaporMapping != null) {
                for (String source : additiveSplittingSaporMapping.getSources()) {
                    result.add(asElectoralListCombination(source));
                }
            }
            SplittingSaporMapping splittingSaporMapping = saporMapping.getSplittingMapping();
            if (splittingSaporMapping != null) {
                result.add(asElectoralListCombination(splittingSaporMapping.getSource()));
            }
        }
        return result;
    }

    /**
     * Verifies that the date is within the validity period of the SAPOR mapping.
     *
     * @param map  The SAPOR mapping.
     * @param date The date.
     * @return True if the date is within the validity period of the SAPOR mapping.
     */
    private boolean dateIsInMappingValidityPeriod(final SaporMapping map, final LocalDate date) {
        return (map.getStartDate() == null || !date.isBefore(LocalDate.parse(map.getStartDate())))
                && (map.getEndDate() == null || !date.isAfter(LocalDate.parse(map.getEndDate())));
    }

    /**
     * Exports the opinion polls to SAPOR files.
     *
     * @param opinionPolls The opinion polls.
     * @return A map with the SAPOR file paths and contents.
     */
    public SaporDirectory export(final OpinionPolls opinionPolls) {
        SaporDirectory result = new SaporDirectory();
        for (OpinionPoll opinionPoll : opinionPolls.getOpinionPolls()) {
            if (lastElectionDate.isBefore(opinionPoll.getEndDate()) && hasMatchingResponseScenario(opinionPoll)) {
                String pollingFirm = opinionPoll.getPollingFirm();
                result.put(getSaporFilePath(opinionPoll),
                        getSaporContent(opinionPoll, opinionPolls.getLowestSampleSize(pollingFirm),
                                opinionPolls.getLowestEffectiveSampleSize(pollingFirm)));
                result.addWarnings(getSaporWarnings(opinionPoll));
            }
        }
        return result;
    }

    /**
     * Returns an opinion poll's response scenario that matches the conditions to be exported. If the main response
     * scenario matches, it will be returned, and otherwise one of the matching alternative responses scenarios. If no
     * response scenarios match, <code>null</code> will be returned.
     *
     * @param opinionPoll The opinion poll for which to find a matching response scenario.
     * @return A matching response scenario, or <code>null</code>.
     */
    private ResponseScenario getMatchingResponseScenario(final OpinionPoll opinionPoll) {
        ResponseScenario mainResponseScenario = opinionPoll.getMainResponseScenario();
        String mainResponseScenarioArea =
                mainResponseScenario.getArea() == null ? opinionPoll.getArea() : mainResponseScenario.getArea();
        Scope mainResponseScenarioScope =
                mainResponseScenario.getScope() == null ? opinionPoll.getScope() : mainResponseScenario.getScope();
        if ((region == null || region.equals(mainResponseScenarioArea))
                && (scope == null || scope.equals(mainResponseScenarioScope))) {
            return opinionPoll.getMainResponseScenario();
        }
        for (ResponseScenario responseScenario : opinionPoll.getAlternativeResponseScenarios()) {
            String responseScenarioArea =
                    responseScenario.getArea() == null ? opinionPoll.getArea() : responseScenario.getArea();
            Scope responseScenarioScope =
                    responseScenario.getScope() == null ? opinionPoll.getScope() : responseScenario.getScope();
            if ((region == null || region.equals(responseScenarioArea))
                    && (scope == null || scope.equals(responseScenarioScope))) {
                return responseScenario;
            }
        }
        return null;
    }

    /**
     * Returns the content of the SAPOR file for an opinion poll.
     *
     * @param opinionPoll               The opinion poll.
     * @param lowestSampleSize          The lowest sample size for the polling firm of this opinion poll.
     * @param lowestEffectiveSampleSize The lowest effective sample size for the polling firm of this opinion poll.
     * @return The content of the SAPOR file.
     */
    String getSaporContent(final OpinionPoll opinionPoll, final Integer lowestSampleSize,
            final Integer lowestEffectiveSampleSize) {
        StringBuilder content = new StringBuilder();
        appendSaporHeader(content, opinionPoll);
        content.append("==\n");
        appendSaporBody(content, opinionPoll, lowestSampleSize, lowestEffectiveSampleSize);
        return content.toString();
    }

    /**
     * Return the path of the SAPOR file for an opinion poll.
     *
     * @param opinionPoll The opinion poll.
     * @return The path of the SAPOR file.
     */
    Path getSaporFilePath(final OpinionPoll opinionPoll) {
        StringBuilder sb = new StringBuilder();
        sb.append(opinionPoll.getEndDate());
        sb.append("-");
        sb.append(
                exportPollingFirms(opinionPoll).replaceAll("[ !\"#%&'\\(\\)\\*\\+,\\./:<=>\\?@\\[\\\\\\]\\{\\}]", ""));
        sb.append(".poll");
        return Paths.get(sb.toString());
    }

    /**
     * Returns the warnings encountered during the export of an opinion poll.
     *
     * @param opinionPoll The warnings encountered during the export of an opinion poll.
     * @return A set with exporter warnings for an opinion poll.
     */
    Set<ExporterWarning> getSaporWarnings(final OpinionPoll opinionPoll) {
        Set<ExporterWarning> warnings = new HashSet<ExporterWarning>();
        Set<Set<ElectoralList>> electoralLists = opinionPoll.getElectoralListSets();
        for (Set<ElectoralList> electoralList : electoralLists) {
            if (!mappedElectoralListCombinations.contains(electoralList)) {
                warnings.add(new MissingSaporMappingWarning(electoralList));
            }
        }
        return warnings;
    }

    /**
     * Returns whether an opinion poll has a response scenario that matches the conditions to be exported.
     *
     * @param opinionPoll The opinion poll to test.
     * @return True if the opinion poll has a response scenario that matches the conditions to be exported.
     */
    private boolean hasMatchingResponseScenario(final OpinionPoll opinionPoll) {
        return getMatchingResponseScenario(opinionPoll) != null;
    }

    /**
     * Processes an additive mapping, writing the result to the content and returning an updated remainder.
     *
     * @param content               The StringBuilder to append the result of the mapping to.
     * @param additiveSaporMapping  The additive SAPOR mapping.
     * @param actualValues          A map with the actual values, i.e. either the nominal values or half the precision
     *                              for zero values.
     * @param calculationSampleSize The sample size to be used for the calculations.
     * @param scale                 The scale.
     * @param remainder             The remainder so far.
     * @return The updated remainder.
     */
    private int processMapping(final StringBuilder content, final AdditiveSaporMapping additiveSaporMapping,
            final Map<Set<ElectoralList>, Double> actualValues, final Integer calculationSampleSize, final double scale,
            final int remainder) {
        if (additiveSaporMapping == null) {
            return remainder;
        }
        double actualValue = 0D;
        boolean termPresent = false;
        for (String source : additiveSaporMapping.getSources()) {
            Set<ElectoralList> electoralLists = asElectoralListCombination(source);
            if (actualValues.containsKey(electoralLists)) {
                termPresent = true;
                actualValue += actualValues.get(electoralLists);
            }
        }
        if (termPresent) {
            int sample = (int) Math.round(actualValue * calculationSampleSize * scale / ONE_HUNDRED);
            content.append(additiveSaporMapping.getTarget());
            content.append("=");
            content.append(sample);
            content.append("\n");
            return remainder - sample;
        } else {
            return remainder;
        }
    }

    /**
     * Processes an additive splitting mapping, writing the result to the content and returning an updated remainder.
     *
     * @param content                       The StringBuilder to append the result of the mapping to.
     * @param additiveSplittingSaporMapping The additive splitting SAPOR mapping.
     * @param actualValues                  A map with the actual values, i.e. either the nominal values or half the
     *                                      precision for zero values.
     * @param calculationSampleSize         The sample size to be used for the calculations.
     * @param scale                         The scale.
     * @param remainder                     The remainder so far.
     * @return The updated remainder.
     */
    private int processMapping(final StringBuilder content,
            final AdditiveSplittingSaporMapping additiveSplittingSaporMapping,
            final Map<Set<ElectoralList>, Double> actualValues, final Integer calculationSampleSize, final double scale,
            final int remainder) {
        if (additiveSplittingSaporMapping == null) {
            return remainder;
        }
        double actualValue = 0D;
        boolean termPresent = false;
        for (String source : additiveSplittingSaporMapping.getSources()) {
            Set<ElectoralList> electoralLists = asElectoralListCombination(source);
            if (actualValues.containsKey(electoralLists)) {
                termPresent = true;
                actualValue += actualValues.get(electoralLists);
            }
        }
        if (termPresent) {
            double totalSample = actualValue * calculationSampleSize * scale / ONE_HUNDRED;
            int sumOfSamples = 0;
            Map<String, Integer> targets = additiveSplittingSaporMapping.getTargets();
            int sumOfWeights = targets.values().stream().reduce(0, Integer::sum);
            for (Entry<String, Integer> target : targets.entrySet()) {
                content.append(target.getKey());
                content.append("=");
                int sample = (int) Math.round(totalSample * target.getValue() / sumOfWeights);
                content.append(sample);
                sumOfSamples += sample;
                content.append("\n");
            }
            return remainder - sumOfSamples;
        } else {
            return remainder;
        }
    }

    /**
     * Processes a direct mapping, writing the result to the content and returning an updated remainder.
     *
     * @param content               The StringBuilder to append the result of the mapping to.
     * @param directSaporMapping    The direct SAPOR mapping.
     * @param actualValues          A map with the actual values, i.e. either the nominal values or half the precision
     *                              for zero values.
     * @param calculationSampleSize The sample size to be used for the calculations.
     * @param scale                 The scale.
     * @param remainder             The remainder so far.
     * @return The updated remainder.
     */
    private int processMapping(final StringBuilder content, final DirectSaporMapping directSaporMapping,
            final Map<Set<ElectoralList>, Double> actualValues, final Integer calculationSampleSize, final double scale,
            final int remainder) {
        if (directSaporMapping == null) {
            return remainder;
        }
        Set<ElectoralList> electoralLists = asElectoralListCombination(directSaporMapping.getSource());
        if (actualValues.containsKey(electoralLists)) {
            int sample =
                    (int) Math.round(actualValues.get(electoralLists) * calculationSampleSize * scale / ONE_HUNDRED);
            content.append(directSaporMapping.getTarget());
            content.append("=");
            if (directSaporMapping.getCompensationFactor() == null) {
                content.append(sample);
            } else {
                content.append((int) Math.round(sample * directSaporMapping.getCompensationFactor()));
            }
            content.append("\n");
            return remainder - sample;
        } else {
            return remainder;
        }
    }

    /**
     * Processes an essential entries mapping, writing the result to the content and returning an updated remainder.
     *
     * @param content                      The StringBuilder to append the result of the mapping to.
     * @param essentialEntriesSaporMapping The essential entries SAPOR mapping.
     * @param remainder                    The remainder so far.
     * @return The updated remainder.
     */
    private int processMapping(final StringBuilder content,
            final EssentialEntriesSaporMapping essentialEntriesSaporMapping, final int remainder) {
        if (essentialEntriesSaporMapping == null) {
            return remainder;
        }
        int sumOfSamples = 0;
        String currentContent = content.toString();
        Set<Entry<String, Integer>> absentTargets = essentialEntriesSaporMapping.getTargets().entrySet().stream()
                .filter(k -> !currentContent.contains(k.getKey())).collect(Collectors.toSet());
        int sumOfWeights = absentTargets.stream().map(e -> e.getValue()).reduce(0, Integer::sum)
                + essentialEntriesSaporMapping.getResidual();
        for (Entry<String, Integer> target : absentTargets) {
            content.append(target.getKey());
            content.append("=");
            int sample = (int) Math.round(remainder * target.getValue() / sumOfWeights);
            content.append(sample);
            sumOfSamples += sample;
            content.append("\n");
        }
        return remainder - sumOfSamples;
    }

    /**
     * Processes a splitting mapping, writing the result to the content and returning an updated remainder.
     *
     * @param content               The StringBuilder to append the result of the mapping to.
     * @param splittingSaporMapping The splitting SAPOR mapping.
     * @param actualValues          A map with the actual values, i.e. either the nominal values or half the precision
     *                              for zero values.
     * @param calculationSampleSize The sample size to be used for the calculations.
     * @param scale                 The scale.
     * @param remainder             The remainder so far.
     * @return The updated remainder.
     */
    private int processMapping(final StringBuilder content, final SplittingSaporMapping splittingSaporMapping,
            final Map<Set<ElectoralList>, Double> actualValues, final Integer calculationSampleSize, final double scale,
            final int remainder) {
        if (splittingSaporMapping == null) {
            return remainder;
        }
        Set<ElectoralList> electoralLists = asElectoralListCombination(splittingSaporMapping.getSource());
        if (actualValues.containsKey(electoralLists)) {
            double totalSample = actualValues.get(electoralLists) * calculationSampleSize * scale / ONE_HUNDRED;
            int sumOfSamples = 0;
            Map<String, Integer> targets = splittingSaporMapping.getTargets();
            int sumOfWeights = targets.values().stream().reduce(0, Integer::sum);
            for (Entry<String, Integer> target : targets.entrySet()) {
                content.append(target.getKey());
                content.append("=");
                int sample = (int) Math.round(totalSample * target.getValue() / sumOfWeights);
                content.append(sample);
                sumOfSamples += sample;
                content.append("\n");
            }
            return remainder - sumOfSamples;
        } else {
            return remainder;
        }
    }
}
