package net.filipvanlaenen.asapop.exporter;

public abstract class CsvExporter extends Exporter {
    /**
     * Returns the string if it isn't null, and the empty string otherwise.
     *
     * @param s The string.
     * @return The empty string if the string is null, and otherwise the string as provided.
     */
    protected static String emptyIfNull(final String s) {
        return s == null ? "" : s;
    }

    /**
     * Escapes commas and quotes in a text block.
     *
     * @param text The text to process.
     * @return The original text properly escaped for commas and quotes according to the CSV standard.
     */
    protected static String escapeCommasAndQuotes(final String text) {
        if (text.contains(",") || text.contains("\"")) {
            return "\"" + text.replaceAll("\"", "\"\"") + "\"";
        } else {
            return text;
        }
    }
}
