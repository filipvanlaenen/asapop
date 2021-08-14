package net.filipvanlaenen.asapop.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class implementing a line representing the definition of an electoral list.
 */
final class ElectoralListLine extends Line {
    /**
     * The pattern to match an electoral list line.
     */
    private static final Pattern ELECTORAL_LIST_PATTERN = Pattern.compile("^\\s*" + ELECTORAL_LIST_KEY_PATTERN
                                                                                  + KEY_VALUE_SEPARATOR_PATTERN
                                                                                  + "(\\s+"
                                                                                  + METADATA_MARKER_PATTERN
                                                                                  + METADATA_KEY_PATTERN
                                                                                  + KEY_VALUE_SEPARATOR_PATTERN
                                                                                  + ".+?)+$");

    /**
     * Verifies whether a line is a definition of an electoral list line.
     *
     * @param line The line to check against the pattern of an electoral list line.
     * @return True if the line matches the pattern of an electoral list line, false otherwise.
     */
    static boolean isElectoralListLine(final String line) {
        return lineMatchesPattern(ELECTORAL_LIST_PATTERN, line);
    }

    /**
     * Parses an electoral list line.
     *
     * @param line The line to parse.
     * @return An ElectoralListLine representing the line.
     */
    static ElectoralListLine parse(final String line) {
        return new ElectoralListLine();
    }

    /**
     * Updates the electoral list which the lines refers to with all the information parsed from the line.
     */
    void updateElectoralList() {
    }
}
