package net.filipvanlaenen.asapop.parser;

import java.util.Map;
import java.util.regex.Pattern;

import net.filipvanlaenen.asapop.model.Scope;

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
    static final Pattern KEY_VALUES_PATTERN = Pattern
            .compile("^\\s*(" + KEY_VALUE_PATTERN + ")((\\s+" + KEY_VALUE_PATTERN + ")*)$");
    /**
     * The pattern to match the key and value of a metadata block.
     */
    static final Pattern METADATA_KEY_VALUE_PATTERN = Pattern.compile("^\\s*" + METADATA_MARKER_PATTERN + "("
            + METADATA_KEY_PATTERN + ")" + KEY_VALUE_SEPARATOR_PATTERN + "\\s*(.+?)\\s*$");
    /**
     * The pattern to match the key and value of a result block.
     */
    static final Pattern RESULT_KEY_VALUE_PATTERN = Pattern
            .compile("^\\s*(" + ELECTORAL_LIST_KEY_PATTERN + ")" + KEY_VALUE_SEPARATOR_PATTERN + "\\s*(.+?)\\s*$");
    /**
     * A map mapping string values as they can appear in the ROPF file to the scopes.
     */
    static final Map<String, Scope> STRING_VALUE_TO_SCOPE = Map.of("E", Scope.European, "N", Scope.National, "P1",
            Scope.PresidentialFirstRound);

    /**
     * Parses the code for a scope into a scope.
     *
     * @param value The value representing a scope.
     * @return The scope corresponding to the provide value.
     */
    static Scope parseScope(final String value) {
        return STRING_VALUE_TO_SCOPE.get(value);
    }

    /**
     * Checks whether a text matches a pattern.
     *
     * @param pattern The pattern to match against.
     * @param text    The text to match.
     * @return True if the text matches with the pattern, false otherwise.
     */
    static boolean textMatchesPattern(final Pattern pattern, final String text) {
        return pattern.matcher(text).matches();
    }
}
