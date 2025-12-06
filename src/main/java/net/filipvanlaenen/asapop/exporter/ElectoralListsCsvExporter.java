package net.filipvanlaenen.asapop.exporter;

import java.util.Comparator;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.SortedCollection;

/**
 * CSV exporter of the electoral lists.
 */
public final class ElectoralListsCsvExporter extends Exporter {
    /**
     * The integer number eight.
     */
    private static final int EIGHT = 8;

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
     * Exports the electoral lists.
     *
     * @return A string containing the electoral lists in a CSV file format.
     */
    public static String export() {
        StringBuffer sb = new StringBuffer();
        SortedCollection<ElectoralList> electoralLists = SortedCollection.of(new Comparator<ElectoralList>() {
            @Override
            public int compare(final ElectoralList el0, final ElectoralList el1) {
                return el0.getId().compareTo(el1.getId());
            }
        }, ElectoralList.getAll());
        int numberOfLanguages =
                electoralLists.stream().map(el -> el.getLanguageCodes().size()).max(Integer::compare).get();
        sb.append("ID,Abbreviation,Romanized Abbreviation");
        for (int i = 1; i <= numberOfLanguages; i++) {
            sb.append(",Language Code ");
            sb.append(i);
            sb.append(",Name ");
            sb.append(i);
        }
        sb.append("\n");
        for (ElectoralList electoralList : electoralLists) {
            String id = electoralList.getId();
            if (id.length() == EIGHT) {
                sb.append(electoralList.getId());
            }
            sb.append(",");
            sb.append(escapeCommasAndQuotes(electoralList.getAbbreviation()));
            sb.append(",");
            sb.append(escapeCommasAndQuotes(emptyIfNull(electoralList.getRomanizedAbbreviation())));
            Collection<String> languageCodes = electoralList.getLanguageCodes();
            for (String languageCode : languageCodes) {
                sb.append(",");
                sb.append(languageCode);
                sb.append(",");
                sb.append(escapeCommasAndQuotes(electoralList.getName(languageCode)));
            }
            for (int i = languageCodes.size(); i < numberOfLanguages; i++) {
                sb.append(",,");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
