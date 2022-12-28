package net.filipvanlaenen.asapop.exporter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.yaml.DirectSaporMapping;
import net.filipvanlaenen.asapop.yaml.SaporConfiguration;
import net.filipvanlaenen.asapop.yaml.SaporMapping;

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
     * The string for the pattern to match the separator between electoral list keys.
     */
    static final String ELECTORAL_LIST_KEY_SEPARATOR = "\\+";
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
     * Creates an exporter taking the SAPOR configuration as its parameter.
     *
     * @param saporConfiguration The SAPOR configuration.
     */
    public SaporExporter(final SaporConfiguration saporConfiguration) {
        this.area = saporConfiguration.getArea();
        this.lastElectionDate = LocalDate.parse(saporConfiguration.getLastElectionDate());
        this.mapping = saporConfiguration.getMapping();
        this.mappedElectoralListCombinations = calculateMappedElectoralListCombinations();
    }

    /**
     * Appends the SAPOR body for an opinion poll to a StringBuilder.
     *
     * @param content     The StringBuilder to append the SAPOR body to.
     * @param opinionPoll The opinion poll.
     */
    void appendSaporBody(final StringBuilder content, final OpinionPoll opinionPoll) {
        int effectiveSampleSize = opinionPoll.getEffectiveSampleSize();
        ResponseScenario responseScenario = opinionPoll.getMainResponseScenario();
        double zeroValue = calculatePrecision(responseScenario).getValue() / FOUR;
        Map<Set<ElectoralList>, Double> actualValues = new HashMap<Set<ElectoralList>, Double>();
        double sumOfActualValues = 0D;
        for (Set<ElectoralList> electoralLists : responseScenario.getElectoralListSets()) {
            Set<String> electoralListKeys = ElectoralList.getKeys(electoralLists);
            double value = responseScenario.getResult(electoralListKeys).getNominalValue();
            double actualValue = value == 0D ? zeroValue : value;
            sumOfActualValues += actualValue;
            actualValues.put(electoralLists, actualValue);
        }
        double actualOtherValue = 0D;
        double calibration = 1D;
        boolean hasOther = responseScenario.getOther() != null;
        if (hasOther) {
            double otherValue = responseScenario.getOther().getNominalValue();
            actualOtherValue = otherValue == 0D ? zeroValue : otherValue;
            calibration = ONE_HUNDRED / (sumOfActualValues + actualOtherValue);
        }
        int remainder = effectiveSampleSize;
        for (SaporMapping map : mapping) {
            DirectSaporMapping directSaporMapping = map.getDirectMapping();
            Set<String> keys = new HashSet<String>(
                    Arrays.asList(directSaporMapping.getSource().split(ELECTORAL_LIST_KEY_SEPARATOR)));
            Set<ElectoralList> electoralLists = ElectoralList.get(keys);
            if (actualValues.containsKey(electoralLists)) {
                int sample = (int) Math
                        .round(actualValues.get(electoralLists) * effectiveSampleSize * calibration / ONE_HUNDRED);
                content.append(directSaporMapping.getTarget());
                content.append("=");
                content.append(sample);
                content.append("\n");
                remainder -= sample;
            }
        }
        content.append("Other=");
        // EQMU: Changing the conditional boundary below produces an equivalent mutant.
        content.append(remainder < 0 ? 0 : remainder);
        content.append("\n");
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
     * Calculate the set of electoral list combinations covered by the SAPOR mapping.
     *
     * @return A set with the electoral list combinations covered by the SAPOR mapping.
     */
    private Set<Set<ElectoralList>> calculateMappedElectoralListCombinations() {
        Set<Set<ElectoralList>> result = new HashSet<Set<ElectoralList>>();
        for (SaporMapping saporMapping : mapping) {
            DirectSaporMapping directSaporMapping = saporMapping.getDirectMapping();
            Set<String> keys = new HashSet<String>(
                    Arrays.asList(directSaporMapping.getSource().split(ELECTORAL_LIST_KEY_SEPARATOR)));
            result.add(ElectoralList.get(keys));
        }
        return result;
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
            if (lastElectionDate.isBefore(opinionPoll.getEndDate())) {
                result.put(getSaporFilePath(opinionPoll), getSaporContent(opinionPoll));
                result.addWarnings(getSaporWarnings(opinionPoll));
            }
        }
        return result;
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
     * Returns the content of the SAPOR file for an opinion poll.
     *
     * @param opinionPoll The opinion poll.
     * @return The content of the SAPOR file.
     */
    String getSaporContent(final OpinionPoll opinionPoll) {
        StringBuilder content = new StringBuilder();
        appendSaporHeader(content, opinionPoll);
        content.append("==\n");
        appendSaporBody(content, opinionPoll);
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
}