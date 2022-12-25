package net.filipvanlaenen.asapop.exporter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
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
     * Creates an exporter taking the SAPOR configuration as its parameter.
     *
     * @param saporConfiguration The SAPOR configuration.
     */
    public SaporExporter(final SaporConfiguration saporConfiguration) {
        this.area = saporConfiguration.getArea();
        this.lastElectionDate = LocalDate.parse(saporConfiguration.getLastElectionDate());
        this.mapping = saporConfiguration.getMapping();
    }

    /**
     * Appends the SAPOR body for an opinion poll to a StringBuilder.
     *
     * @param content     The StringBuilder to append the SAPOR body to.
     * @param opinionPoll The opinion poll.
     */
    private void appendSaporBody(final StringBuilder content, final OpinionPoll opinionPoll) {
        int effectiveSampleSize = opinionPoll.getEffectiveSampleSize(); // TODO: Should come from the scenario
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
            DirectSaporMapping dm = map.getDirectMapping();
            Set<ElectoralList> electoralLists = Set.of(ElectoralList.get(dm.getSource())); // TODO: Combinations
            if (actualValues.containsKey(electoralLists)) {
                int sample = (int) Math
                        .round(actualValues.get(electoralLists) * effectiveSampleSize * calibration / ONE_HUNDRED);
                content.append(dm.getTarget() + "=" + sample + "\n");
                remainder -= sample;
            }
        }
        // TODO: Warnings for unused result values
        content.append("Other=" + (remainder < 0 ? 0 : remainder) + "\n");
    }

    /**
     * Appends the SAPOR header for an opinion poll to a StringBuilder.
     *
     * @param content     The StringBuilder to append the SAPOR header to.
     * @param opinionPoll The opinion poll.
     */
    private void appendSaporHeader(final StringBuilder content, final OpinionPoll opinionPoll) {
        content.append("Type=Election\n");
        content.append("PollingFirm=" + exportPollingFirms(opinionPoll) + "\n");
        if (!opinionPoll.getCommissioners().isEmpty()) {
            content.append("Commissioners=" + exportCommissioners(opinionPoll) + "\n");
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
        content.append("FieldworkEnd=" + opinionPoll.getEndDate().toString() + "\n");
        content.append("Area=" + area + "\n");
    }

    /**
     * Exports the opinion polls to SAPOR files.
     *
     * @param opinionPolls The opinion polls.
     * @return A map with the SAPOR file paths and contents.
     */
    public Map<Path, String> export(final OpinionPolls opinionPolls) {
        Map<Path, String> result = new HashMap<Path, String>();
        for (OpinionPoll opinionPoll : opinionPolls.getOpinionPolls()) {
            if (lastElectionDate.isBefore(opinionPoll.getEndDate())) {
                result.put(getSaporFilePath(opinionPoll), getSaporContent(opinionPoll));
            }
        }
        return result;
    }

    /**
     * Returns the content of the SAPOR file for an opinion poll.
     *
     * @param opinionPoll The opinion poll.
     * @return The content of the SAPOR file.
     */
    private String getSaporContent(final OpinionPoll opinionPoll) {
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
    private Path getSaporFilePath(final OpinionPoll opinionPoll) {
        StringBuilder sb = new StringBuilder();
        sb.append(opinionPoll.getEndDate().toString());
        sb.append("-");
        sb.append(
                exportPollingFirms(opinionPoll).replaceAll("[ !\"#%&'\\(\\)\\*\\+,\\./:<=>\\?@\\[\\\\\\]\\{\\}]", ""));
        sb.append(".poll");
        return Paths.get(sb.toString());
    }
}
