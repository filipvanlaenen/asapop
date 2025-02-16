package net.filipvanlaenen.asapop.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Class implementing a line representing the definition of an electoral list.
 */
final class ElectoralListLine extends Line {
    /**
     * The integer number three.
     */
    private static final int THREE = 3;
    /**
     * The integer number four.
     */
    private static final int FOUR = 4;
    /**
     * The integer number eight.
     */
    private static final int EIGHT = 8;
    /**
     * The string for the pattern to match the ID of an electoral list.
     */
    private static final String ELECTORAL_LIST_ID_PATTERN = "\\p{Upper}{2}\\p{Digit}{3,6}";
    /**
     * The pattern to match an electoral list line.
     */
    private static final Pattern ELECTORAL_LIST_PATTERN = Pattern.compile("^\\s*" + ELECTORAL_LIST_KEY_PATTERN
            + KEY_VALUE_SEPARATOR_PATTERN + "\\s*" + ELECTORAL_LIST_ID_PATTERN + "(\\s+" + METADATA_MARKER_PATTERN
            + METADATA_KEY_PATTERN + KEY_VALUE_SEPARATOR_PATTERN + ".+?)+$");
    /**
     * The pattern to extract the key for the electoral list.
     */
    private static final Pattern KEY_EXTRACTION_PATTERN = Pattern.compile("^\\s*(" + ELECTORAL_LIST_KEY_PATTERN + ")"
            + KEY_VALUE_SEPARATOR_PATTERN + "\\s*(" + ELECTORAL_LIST_ID_PATTERN + ")((\\s+" + METADATA_MARKER_PATTERN
            + METADATA_KEY_PATTERN + KEY_VALUE_SEPARATOR_PATTERN + ".+?)+)$");

    /**
     * The abbreviation for the electoral list.
     */
    private String abbreviation;
    /**
     * The ID for the electoral list.
     */
    private final String id;
    /**
     * The key for the electoral list.
     */
    private final String key;
    /**
     * The map with the names for the electoral list.
     */
    private Map<String, String> names = new HashMap<String, String>();
    /**
     * The romanized abbreviation for the electoral list.
     */
    private String romanizedAbbreviation;

    /**
     * Private constructor taking the key for the electoral list as the parameter.
     *
     * @param key The key for the electoral list.
     * @param id  The ID for the electoral list.
     */
    private ElectoralListLine(final String key, final String id) {
        this.key = key;
        this.id = id;
    }

    /**
     * Returns the electoral list from this line.
     *
     * @return The electoral list from this line.
     */
    ElectoralList getElectoralList() {
        return ElectoralList.get(id);
    }

    /**
     * Returns the key for the electoral list in this line.
     *
     * @return The key for the electoral list in this line.
     */
    String getKey() {
        return key;
    }

    /**
     * Verifies whether a line is a definition of an electoral list line.
     *
     * @param line The line to check against the pattern of an electoral list line.
     * @return True if the line matches the pattern of an electoral list line, false otherwise.
     */
    static boolean isElectoralListLine(final String line) {
        return textMatchesPattern(ELECTORAL_LIST_PATTERN, line);
    }

    /**
     * Parses an electoral list line.
     *
     * @param line The line to parse.
     * @return An ElectoralListLine representing the line.
     */
    static ElectoralListLine parse(final Token token, final String line) {
        Matcher electoralListKeyMatcher = KEY_EXTRACTION_PATTERN.matcher(line);
        electoralListKeyMatcher.find();
        String key = electoralListKeyMatcher.group(1);
        String id = electoralListKeyMatcher.group(2);
        if (id.length() < EIGHT) {
            Laconic.LOGGER.logError("Electoral list ID %s is a non-permanent electoral list ID.", id, token);
        }
        String remainder = electoralListKeyMatcher.group(THREE);
        ElectoralListLine electoralListLine = new ElectoralListLine(key, id);
        while (!remainder.isEmpty()) {
            remainder = electoralListLine.parseKeyValue(remainder);
        }
        return electoralListLine;
    }

    /**
     * Processes a key and value from a part of an electoral list line.
     *
     * @param remainder The remainder of a line to parse a key and value from.
     * @return The unprocessed part of the line.
     */
    private String parseKeyValue(final String remainder) {
        Matcher keyValuesMatcher = KEY_VALUES_PATTERN.matcher(remainder);
        keyValuesMatcher.find();
        String keyValueBlock = keyValuesMatcher.group(1);
        processKeyValue(keyValueBlock);
        return keyValuesMatcher.group(FOUR);
    }

    /**
     * Processes a data block with information for an electoral list.
     *
     * @param keyValueString The data block to process.
     */
    private void processKeyValue(final String keyValueString) {
        Matcher keyValueMatcher = METADATA_KEY_VALUE_PATTERN.matcher(keyValueString);
        keyValueMatcher.find();
        String blockKey = keyValueMatcher.group(1);
        String value = keyValueMatcher.group(2);
        switch (blockKey) {
        case "A":
            abbreviation = value;
            break;
        case "R":
            romanizedAbbreviation = value;
            break;
        default:
            names.put(blockKey, value);
            break;
        }
    }

    /**
     * Updates the electoral list with the data in the line.
     */
    void updateElectoralList() {
        ElectoralList electoralList = ElectoralList.get(id);
        electoralList.setAbbreviation(abbreviation);
        electoralList.setRomanizedAbbreviation(romanizedAbbreviation);
        electoralList.setNames(names);
    }
}
