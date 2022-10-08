package net.filipvanlaenen.asapop.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.ResultValue;
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
     * @param opinionPolls         The opinion polls to export.
     * @param area                 The area to filter opinion polls and response scenarios on.
     * @param electoralListKeySets A list with the sets of keys for the electoral lists to be exported.
     * @return A string containing the opinion polls in the PSV file format for EOPAOD.
     */
    public static String export(final OpinionPolls opinionPolls, final String area,
            final List<Set<String>> electoralListKeySets) {
        StringBuffer sb = new StringBuffer();
        sb.append("Polling Firm | Commissioners | Fieldwork Start | Fieldwork End | Scope | Sample Size");
        sb.append(" | Participation | Precision");
        for (Set<String> electoralListKeySet : electoralListKeySets) {
            sb.append(" | ");
            List<String> sortedKeys = new ArrayList<String>(electoralListKeySet);
            Collections.sort(sortedKeys);
            sb.append(String.join("+", sortedKeys));
        }
        sb.append(" | Other");
        for (OpinionPoll opinionPoll : sortOpinionPolls(opinionPolls.getOpinionPolls())) {
            String lines = export(opinionPoll, area, electoralListKeySets);
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
            elements.add(naIfNull(exportParticipationRate(opinionPoll)));
            elements.add(calculatePrecision(opinionPoll, electoralListKeySets));
            for (Set<String> electoralListKeySet : electoralListKeySets) {
                elements.add(naIfNull(opinionPoll.getResult(electoralListKeySet)));
            }
            elements.add(naIfNull(opinionPoll.getOther()));
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
        elements.add(naIfNull(exportParticipationRate(opinionPoll)));
        elements.add(calculatePrecision(responseScenario, electoralListKeySets));
        for (Set<String> electoralListKeySet : electoralListKeySets) {
            elements.add(naIfNull(responseScenario.getResult(electoralListKeySet)));
        }
        elements.add(naIfNull(responseScenario.getOther()));
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
     * @param s The result value.
     * @return "N/A" if the string is null, and otherwise the string as provided.
     */
    private static String naIfNull(final ResultValue s) {
        return s == null ? "N/A" : s.getPrimitiveText();
    }
}
