package net.filipvanlaenen.asapop.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.HashMap;
import java.util.Map;

import net.filipvanlaenen.asapop.model.ElectoralList;

/**
 * Class implementing a line representing the definition of an electoral list.
 */
final class ElectoralListLine extends Line {
    /**
     * The integer number three.
     */
    private static final int THREE = 3;
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
     * The pattern to extract the key for the electoral list.
     */
    private static final Pattern KEY_EXTRACTION_PATTERN = Pattern.compile("^\\s*(" + ELECTORAL_LIST_KEY_PATTERN
                                                                                   + ")"
                                                                                   + KEY_VALUE_SEPARATOR_PATTERN
                                                                                   + "((\\s+"
                                                                                   + METADATA_MARKER_PATTERN
                                                                                   + METADATA_KEY_PATTERN
                                                                                   + KEY_VALUE_SEPARATOR_PATTERN
                                                                                   + ".+?)+)$");

    /**
     * The abbreviation for the electoral list.
     */
    private String abbreviation;
    /**
     * The key for the electoral list.
     */
    private final String key;
    /**
     * The map with the names for the electoral list.
     */
    private Map<String, String> names = new HashMap<String, String>();

    /**
     * Private constructor taking the key for the electoral list as the parameter.
     *
     * @param key The key for the electoral list.
     */
    private ElectoralListLine(final String key) {
        this.key = key;
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
    static ElectoralListLine parse(final String line) {
        Matcher electoralListKeyMatcher = KEY_EXTRACTION_PATTERN.matcher(line);
        electoralListKeyMatcher.find();
        String key = electoralListKeyMatcher.group(1);
        String remainder = electoralListKeyMatcher.group(2);
        ElectoralListLine electoralListLine = new ElectoralListLine(key);
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
        return keyValuesMatcher.group(THREE);
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
            case "A": abbreviation = value;
                break;
            default: names.put(blockKey, value);
                break;
        }
    }

    /**
     * Updates the electoral list with the data in the line.
     */
    void updateElectoralList() {
        ElectoralList electoralList = ElectoralList.get(key);
        electoralList.setAbbreviation(abbreviation);
        electoralList.setNames(names);
    }
}
