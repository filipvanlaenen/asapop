package net.filipvanlaenen.asapop.parser;

import java.util.regex.Pattern;

/**
 * Abstract class providing common functionality for all types of lines.
 */
abstract class Line {
    /**
     * The string for the pattern to match a metadata marker.
     */
    static final String METADATA_MARKER_PATTERN = "•";
    /**
     * The string for the pattern to match a metadata key.
     */
    static final String METADATA_KEY_PATTERN = "\\p{Upper}+";
    /**
     * The string for the pattern to match the key of an electoral list.
     */
    static final String ELECTORAL_LIST_KEY_PATTERN = "\\p{javaUpperCase}+";
    /**
     * The string for the pattern to match either a metadata key or a key of an electoral list.
     */
    private static final String KEY_PATTERN = "(" + METADATA_MARKER_PATTERN + METADATA_KEY_PATTERN + "|"
                                                  + ELECTORAL_LIST_KEY_PATTERN + ")";
    /**
     * The string for the pattern to match the separator between the key and the value.
     */
    static final String KEY_VALUE_SEPARATOR_PATTERN = ":";
    /**
     * The string for the pattern to match a key and a value.
     */
    static final String KEY_VALUE_PATTERN = KEY_PATTERN + KEY_VALUE_SEPARATOR_PATTERN + ".+?";
    /**
     * The pattern to match key and value blocks.
     */
    static final Pattern KEY_VALUES_PATTERN = Pattern.compile("^\\s*(" + KEY_VALUE_PATTERN
                                                                               + ")((\\s+"
                                                                               + KEY_VALUE_PATTERN
                                                                               + ")*)$");
    /**
     * The pattern to match the key and value of a metadata block.
     */
    static final Pattern METADATA_KEY_VALUE_PATTERN = Pattern.compile("^\\s*" + METADATA_MARKER_PATTERN
                                                                                      + "("
                                                                                      + METADATA_KEY_PATTERN
                                                                                      + ")"
                                                                                      + KEY_VALUE_SEPARATOR_PATTERN
                                                                                      + "\\s*(.+?)\\s*$");
    /**
     * The pattern to match the key and value of a result block.
     */
    static final Pattern RESULT_KEY_VALUE_PATTERN = Pattern.compile("^\\s*(" + ELECTORAL_LIST_KEY_PATTERN
                                                                                     + ")"
                                                                                     + KEY_VALUE_SEPARATOR_PATTERN
                                                                                     + "\\s*(.+?)\\s*$");
    /**
     * Checks whether a line matches a pattern.
     *
     * @param pattern The pattern to match against.
     * @param line The line to match.
     * @return True if the line matches with the pattern, false otherwise.
     */
    static boolean lineMatchesPattern(final Pattern pattern, final String line) {
        return pattern.matcher(line).matches();
    }
}
