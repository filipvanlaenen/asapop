package net.filipvanlaenen.asapop.exporter;

import net.filipvanlaenen.asapop.parser.RichOpinionPollsFile;

/**
 * Exporter to the ROPF file format.
 */
public final class RopfExporter extends Exporter {
    /**
     * Private constructor.
     */
    private RopfExporter() {
    }

    /**
     * Exports the rich opinion polls file.
     *
     * @param richOpinionPollsFile The rich opinion polls file to export.
     * @return A string representing the rich opinion polls file.
     */
    public static String export(final RichOpinionPollsFile richOpinionPollsFile) {
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        return sb.toString();
    }
}
