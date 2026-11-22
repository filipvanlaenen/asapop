package net.filipvanlaenen.asapop.parser;

import java.util.regex.Pattern;

/**
 * Class implementing a definition of two or more electoral lists together.
 */
public final class ElectoralListsLine extends Line {
    /**
     * The content of the line.
     */
    private final String content;
    /**
     * The string for the pattern to match the ID of an electoral list.
     */
    private static final String ELECTORAL_LIST_ID_PATTERN = "\\p{Upper}{2}\\p{Digit}{3,6}";
    /**
     * The pattern to match an electoral list line.
     */
    private static final Pattern ELECTORAL_LISTS_PATTERN = Pattern.compile("^\\s*" + ELECTORAL_LIST_KEY_PATTERN + "(\\+"
            + ELECTORAL_LIST_KEY_PATTERN + ")+" + KEY_VALUE_SEPARATOR_PATTERN + "\\s*" + ELECTORAL_LIST_ID_PATTERN
            + "(\\s+" + METADATA_MARKER_PATTERN + METADATA_KEY_PATTERN + KEY_VALUE_SEPARATOR_PATTERN + ".+?)+$");

    /**
     * Constructor taking the content of the line as its parameter.
     *
     * @param content The content of the line.
     */
    private ElectoralListsLine(final String content) {
        this.content = content;
    }

    /**
     * Returns the content of the line.
     *
     * @return The content of the line.
     */
    public String getContent() {
        return content;
    }

    /**
     * Validates the line against the format.
     *
     * @param line The line to check against the pattern.
     * @return True if the line matches the pattern, false otherwise.
     */
    static boolean isElectoralListsLine(final String line) {
        return textMatchesPattern(ELECTORAL_LISTS_PATTERN, line);
    }

    /**
     * Parses a line.
     *
     * @param line The line to be parsed.
     * @return An object representing the line.
     */
    static ElectoralListsLine parse(final String line) {
        return new ElectoralListsLine(line.strip());
    }
}
