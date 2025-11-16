package net.filipvanlaenen.asapop.exporter;

import java.util.Set;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.ResultValue;
import net.filipvanlaenen.asapop.model.ResultValue.Precision;
import net.filipvanlaenen.asapop.model.SampleSize;
import net.filipvanlaenen.asapop.model.Scope;
import net.filipvanlaenen.asapop.model.Unit;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableMap;
import net.filipvanlaenen.kolektoj.ModifiableOrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedCollection;

/**
 * Exporter to the EOPAOD CSV file format.
 */
public final class EopaodCsvExporter extends Exporter {
    /**
     * The magic number one hundred.
     */
    private static final double ONE_HUNDRED = 100D;
    /**
     * A map mapping scopes to CSV values.
     */
    private static final Map<Scope, String> SCOPE_TO_CSV_STRING = createScopeToCsvMap();

    /**
     * Private constructor.
     */
    private EopaodCsvExporter() {
    }

    /**
     * Creates the map mapping the scopes to their CSV values.
     *
     * @return A map mapping the scopes to their CSV values.
     */
    private static Map<Scope, String> createScopeToCsvMap() {
        ModifiableMap<Scope, String> map = ModifiableMap.empty();
        map.add(null, null);
        map.add(Scope.EUROPEAN, "European");
        map.add(Scope.NATIONAL, "National");
        map.add(Scope.NATIONAL_TWO_PARTY_PREFERRED, "National Two-party-preferred");
        map.add(Scope.PRESIDENTIAL_FIRST_ROUND, "Presidential");
        map.add(Scope.REGIONAL, "Regional");
        return map;
    }

    /**
     * Returns the string if it isn't null, and the empty string otherwise.
     *
     * @param s The string.
     * @return The empty string if the string is null, and otherwise the string as provided.
     */
    private static String emptyIfNull(final String s) {
        return s == null ? "" : s;
    }

    /**
     * Escapes commas and quotes in a text block.
     *
     * @param text The text to process.
     * @return The original text properly escaped for commas and quotes according to the CSV standard.
     */
    private static String escapeCommasAndQuotes(final String text) {
        if (text.contains(",") || text.contains("\"")) {
            return "\"" + text.replaceAll("\"", "\"\"") + "\"";
        } else {
            return text;
        }
    }

    /**
     * Exports the opinion polls.
     *
     * @param opinionPolls          The opinion polls to export.
     * @param area                  The area to filter opinion polls and response scenarios on.
     * @param includeAreaAsNational The area to be included as national.
     * @param electoralListIdSets   A list with the sets of IDs for the electoral lists to be exported.
     * @return A string containing the opinion polls in the CSV file format for EOPAOD.
     */
    public static String export(final OpinionPolls opinionPolls, final String area, final String includeAreaAsNational,
            final OrderedCollection<Set<String>> electoralListIdSets, final OrderedCollection<String> candidateIds) {
        StringBuffer sb = new StringBuffer();
        sb.append("Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size");
        sb.append(",Sample Size Qualification,Participation,Precision");
        for (Set<String> electoralListIdSet : electoralListIdSets) {
            sb.append(",");
            sb.append(electoralListIdsToAbbreviations(electoralListIdSet));
        }
        for (String candidateId : candidateIds) {
            sb.append(",");
            sb.append(candidateIdToAbbreviation(candidateId));
        }
        sb.append(",Other\n");
        for (OpinionPoll opinionPoll : sortOpinionPolls(opinionPolls.getOpinionPolls())) {
            String lines = export(opinionPoll, area, includeAreaAsNational, electoralListIdSets, candidateIds);
            if (lines != null) {
                sb.append(lines);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Exports the opinion poll.
     *
     * @param opinionPoll           The opinion poll to export.
     * @param area                  The area to filter opinion polls on.
     * @param includeAreaAsNational The area to be included as national.
     * @param electoralListIdSets   A list with the sets of IDs of the electoral lists to be exported.
     * @return A string containing the opinion poll in the CSV file format for EOPAOD.
     */
    static String export(final OpinionPoll opinionPoll, final String area, final String includeAreaAsNational,
            final OrderedCollection<Set<String>> electoralListIdSets, final OrderedCollection<String> candidateIds) {
        ModifiableOrderedCollection<String> lines = ModifiableOrderedCollection.empty();
        if (areaMatches(area, includeAreaAsNational, opinionPoll.getArea())) {
            ModifiableOrderedCollection<String> elements = ModifiableOrderedCollection.empty();
            elements.add(escapeCommasAndQuotes(emptyIfNull(exportPollingFirms(opinionPoll))));
            elements.add(escapeCommasAndQuotes(emptyIfNull(exportCommissioners(opinionPoll))));
            elements.addAll(exportDates(opinionPoll));
            elements.add(notAvailableIfNull(SCOPE_TO_CSV_STRING.get(opinionPoll.getScope())));
            elements.add(notAvailableIfNull(opinionPoll.getSampleSizeValue()));
            elements.add(opinionPoll.getSampleSizeValue() == null ? "Not Available" : "Provided");
            elements.add(notAvailableIfNull(exportParticipationRatePercentage(opinionPoll)));
            Precision precision = calculatePrecision(opinionPoll, electoralListIdSets, candidateIds);
            Double scale = opinionPoll.getScale();
            if (Unit.SEATS == opinionPoll.getUnit()) {
                precision = Precision.TENTH;
                double numberOfSeats = opinionPoll.getMainResponseScenario().getSumOfResultsAndOther();
                elements.add(Precision.TENTH.getFormat().format(ONE_HUNDRED / numberOfSeats) + "%");
                scale = numberOfSeats / ONE_HUNDRED;
            } else {
                elements.add(precision + "%");
            }
            for (Set<String> electoralListIdSet : electoralListIdSets) {
                elements.add(percentageOrNotAvailable(opinionPoll.getResult(electoralListIdSet), precision, scale));
            }
            for (String candidateId : candidateIds) {
                elements.add(percentageOrNotAvailable(opinionPoll.getResult(candidateId), precision, scale));
            }
            elements.add(percentageOrNotAvailable(opinionPoll.getOther(), precision, scale));
            lines.add(String.join(",", elements));
        }
        for (ResponseScenario responseScenario : opinionPoll.getAlternativeResponseScenarios()) {
            String responseScenarioLine = export(responseScenario, opinionPoll, area, includeAreaAsNational,
                    electoralListIdSets, candidateIds);
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
     * @param responseScenario      The response scenario to export.
     * @param opinionPoll           The opinion poll this response scenario relates to.
     * @param area                  The area to filter response scenarios on.
     * @param includeAreaAsNational The area to be included as national.
     * @param electoralListIdSets   A list with the sets of IDs of the electoral lists to be exported.
     * @return A string containing the response scenario in the CSV file format for EOPAOD.
     */
    static String export(final ResponseScenario responseScenario, final OpinionPoll opinionPoll, final String area,
            final String includeAreaAsNational, final OrderedCollection<Set<String>> electoralListIdSets,
            final OrderedCollection<String> candidateIds) {
        if (!areaMatches(area, includeAreaAsNational,
                secondIfFirstNull(responseScenario.getArea(), opinionPoll.getArea()))) {
            return null;
        }
        ModifiableOrderedCollection<String> elements = ModifiableOrderedCollection.empty();
        elements.add(escapeCommasAndQuotes(emptyIfNull(exportPollingFirms(opinionPoll))));
        elements.add(escapeCommasAndQuotes(emptyIfNull(exportCommissioners(opinionPoll))));
        elements.addAll(exportDates(opinionPoll));
        elements.add(notAvailableIfNull(
                SCOPE_TO_CSV_STRING.get(secondIfFirstNull(responseScenario.getScope(), opinionPoll.getScope()))));
        SampleSize sampleSize = secondIfFirstNull(responseScenario.getSampleSize(), opinionPoll.getSampleSize());
        elements.add(sampleSize == null ? "Not Available" : Integer.toString(sampleSize.getMinimalValue()));
        elements.add(sampleSize == null ? "Not Available" : "Provided");
        elements.add(notAvailableIfNull(exportParticipationRatePercentage(responseScenario, opinionPoll)));
        Precision precision = calculatePrecision(responseScenario, electoralListIdSets, candidateIds);
        Double scale = responseScenario.getScale();
        if (Unit.SEATS == responseScenario.getUnit()) {
            precision = Precision.TENTH;
            double numberOfSeats = responseScenario.getSumOfResultsAndOther();
            elements.add(Precision.TENTH.getFormat().format(ONE_HUNDRED / numberOfSeats) + "%");
            scale = numberOfSeats / ONE_HUNDRED;
        } else {
            elements.add(precision + "%");
        }
        for (Set<String> electoralListIdSet : electoralListIdSets) {
            elements.add(percentageOrNotAvailable(responseScenario.getResult(electoralListIdSet), precision, scale));
        }
        for (String candidateId : candidateIds) {
            elements.add(percentageOrNotAvailable(opinionPoll.getResult(candidateId), precision, scale));
        }
        elements.add(percentageOrNotAvailable(responseScenario.getOther(), precision, scale));
        return String.join(",", elements);
    }

    /**
     * Exports the participation rate of the opinion poll as a percentage.
     *
     * @param opinionPoll The opinion poll for which to export the participation rate as a percentage.
     * @return A string representing the participation rate as a percentage.
     */
    private static String exportParticipationRatePercentage(final OpinionPoll opinionPoll) {
        String participationRate = exportParticipationRate(opinionPoll.getMainResponseScenario(), opinionPoll);
        if (participationRate == null) {
            return null;
        } else {
            return participationRate + "%";
        }
    }

    /**
     * Exports the participation rate of the response scenario as a percentage.
     *
     * @param responseScenario The response scenario for which to export the participation rate as a percentage.
     * @param opinionPoll      The opinion poll for which to export the participation rate as a percentage.
     * @return A string representing the participation rate as a percentage.
     */
    private static String exportParticipationRatePercentage(final ResponseScenario responseScenario,
            final OpinionPoll opinionPoll) {
        String participationRate = exportParticipationRate(responseScenario, opinionPoll);
        if (participationRate == null) {
            return null;
        } else {
            return participationRate + "%";
        }
    }

    /**
     * Returns the string representation of the object if it isn't null, and the string "Not Available" otherwise.
     *
     * @param object The object.
     * @return "Not Available" if the object is null, and otherwise its string representation.
     */
    private static String notAvailableIfNull(final Object object) {
        return object == null ? "Not Available" : object.toString();
    }

    /**
     * Returns the result value with a percentage sign added if it isn't null, and the string "Not Available" otherwise.
     *
     * @param resultValue The result value.
     * @param precision   The precision to be used.
     * @param scale       The scale to be used.
     * @return "Not Available" if the string is null, and otherwise the string with a percentage sign added.
     */
    private static String percentageOrNotAvailable(final ResultValue resultValue, final Precision precision,
            final Double scale) {
        if (resultValue == null) {
            return "Not Available";
        } else if (scale == 1D) {
            return resultValue.getPrimitiveText() + "%";
        } else {
            return precision.getFormat().format(resultValue.getNominalValue() / scale) + "%";
        }
    }
}
