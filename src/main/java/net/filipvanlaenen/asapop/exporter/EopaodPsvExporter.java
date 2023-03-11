package net.filipvanlaenen.asapop.exporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.ResultValue;
import net.filipvanlaenen.asapop.model.ResultValue.Precision;
import net.filipvanlaenen.asapop.model.Scope;

/**
 * Exporter to the EOPAOD PSV file format.
 */
public final class EopaodPsvExporter extends Exporter {
    /**
     * A map mapping scopes to PSV values.
     */
    static final Map<Scope, String> SCOPE_TO_PSV_STRING =
            Map.of(Scope.European, "E", Scope.National, "N", Scope.PresidentialFirstRound, "P");

    /**
     * Private constructor.
     */
    private EopaodPsvExporter() {
    }

    /**
     * Exports the opinion polls.
     *
     * @param opinionPolls        The opinion polls to export.
     * @param area                The area to filter opinion polls and response scenarios on.
     * @param electoralListIdSets A list with the IDs of keys for the electoral lists to be exported.
     * @return A string containing the opinion polls in the PSV file format for EOPAOD.
     */
    public static String export(final OpinionPolls opinionPolls, final String area,
            final List<Set<String>> electoralListIdSets) {
        StringBuffer sb = new StringBuffer();
        sb.append("Polling Firm | Commissioners | Fieldwork Start | Fieldwork End | Scope | Sample Size");
        sb.append(" | Participation | Precision");
        for (Set<String> electoralListIdSet : electoralListIdSets) {
            sb.append(" | ");
            sb.append(electoralListIdsToAbbreviations(electoralListIdSet));
        }
        sb.append(" | Other");
        for (OpinionPoll opinionPoll : sortOpinionPolls(opinionPolls.getOpinionPolls())) {
            String lines = export(opinionPoll, area, electoralListIdSets);
            if (lines != null) {
                sb.append("\n");
                sb.append(lines);
            }
        }
        return sb.toString();
    }

    /**
     * Exports the opinion poll.
     *
     * @param opinionPoll          The opinion poll to export.
     * @param area                 The area to filter opinion polls on.
     * @param electoralListKeySets A list with the sets of keys of the electoral lists to be exported.
     * @return A string containing the opinion poll in the PSV file format for EOPAOD.
     */
    static String export(final OpinionPoll opinionPoll, final String area,
            final List<Set<String>> electoralListKeySets) {
        List<String> lines = new ArrayList<String>();
        if (areaMatches(area, opinionPoll.getArea())) {
            List<String> elements = new ArrayList<String>();
            elements.add(exportPollingFirms(opinionPoll));
            elements.add(naIfNull(exportCommissioners(opinionPoll)));
            elements.addAll(exportDates(opinionPoll));
            elements.add(naIfNull(exportScope(opinionPoll.getScope())));
            elements.add(naIfNull(opinionPoll.getSampleSize()));
            elements.add(naIfNull(exportParticipationRate(opinionPoll.getMainResponseScenario(), opinionPoll)));
            Precision precision = calculatePrecision(opinionPoll, electoralListKeySets);
            elements.add(precision.toString());
            Double scale = opinionPoll.getScale();
            for (Set<String> electoralListKeySet : electoralListKeySets) {
                elements.add(naIfNull(opinionPoll.getResult(electoralListKeySet), precision, scale));
            }
            elements.add(naIfNull(opinionPoll.getOther(), precision, scale));
            lines.add(String.join(" | ", elements));
        }
        for (ResponseScenario responseScenario : opinionPoll.getAlternativeResponseScenarios()) {
            String responseScenarioLine = export(responseScenario, opinionPoll, area, electoralListKeySets);
            if (responseScenarioLine != null) {
                lines.add(responseScenarioLine);
            }
        }
        if (lines.isEmpty()) {
            return null;
        } else {
            return String.join("\n", lines);
        }
    }

    /**
     * Exports the response scenario.
     *
     * @param responseScenario     The response scenario to export.
     * @param opinionPoll          The opinion poll this response scenario relates to.
     * @param area                 The area to filter response scenarios on.
     * @param electoralListKeySets A list with the sets of keys of the electoral lists to be exported.
     * @return A string containing the response scenario in the PSV file format for EOPAOD.
     */
    static String export(final ResponseScenario responseScenario, final OpinionPoll opinionPoll, final String area,
            final List<Set<String>> electoralListKeySets) {
        if (!areaMatches(area, secondIfFirstNull(responseScenario.getArea(), opinionPoll.getArea()))) {
            return null;
        }
        List<String> elements = new ArrayList<String>();
        elements.add(exportPollingFirms(opinionPoll));
        elements.add(naIfNull(exportCommissioners(opinionPoll)));
        elements.addAll(exportDates(opinionPoll));
        elements.add(naIfNull(exportScope(secondIfFirstNull(responseScenario.getScope(), opinionPoll.getScope()))));
        elements.add(naIfNull(secondIfFirstNull(responseScenario.getSampleSize(), opinionPoll.getSampleSize())));
        elements.add(naIfNull(exportParticipationRate(responseScenario, opinionPoll)));
        Precision precision = calculatePrecision(responseScenario, electoralListKeySets);
        elements.add(precision.toString());
        Double scale = responseScenario.getScale();
        for (Set<String> electoralListKeySet : electoralListKeySets) {
            elements.add(naIfNull(responseScenario.getResult(electoralListKeySet), precision, scale));
        }
        elements.add(naIfNull(responseScenario.getOther(), precision, scale));
        return String.join(" | ", elements);
    }

    /**
     * Exports the scope to the terms used by the EOPAOD PSV format.
     *
     * @param scope The scope to convert.
     * @return A term used by EOPAOD PSV format for the scope.
     */
    private static String exportScope(final Scope scope) {
        return scope == null ? null : SCOPE_TO_PSV_STRING.get(scope);
    }

    /**
     * Returns the string if it isn't null, and the string "N/A" otherwise.
     *
     * @param s The string.
     * @return "N/A" if the string is null, and otherwise the string as provided.
     */
    private static String naIfNull(final String s) {
        return s == null ? "N/A" : s;
    }

    /**
     * Returns the result value's text if it isn't null, and the string "N/A" otherwise.
     *
     * @param resultValue The result value.
     * @param precision   The precision to be used.
     * @param scale       The scale to be used.
     * @return "N/A" if the string is null, and otherwise the string as provided.
     */
    private static String naIfNull(final ResultValue resultValue, final Precision precision, final Double scale) {
        if (resultValue == null) {
            return "N/A";
        } else if (scale == 1D) {
            return resultValue.getPrimitiveText();
        } else {
            return precision.getFormat().format(resultValue.getNominalValue() / scale);
        }
    }
}
