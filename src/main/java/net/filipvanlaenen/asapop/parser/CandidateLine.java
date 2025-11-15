package net.filipvanlaenen.asapop.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.filipvanlaenen.asapop.model.Candidate;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Class implementing a line representing the definition of a candidate.
 */
final class CandidateLine extends Line {
    /**
     * The integer number three.
     */
    private static final int THREE = 3;
    /**
     * The integer number four.
     */
    private static final int FOUR = 4;
    /**
     * The string for the pattern to match the ID of a candidate.
     */
    private static final String CANDIDATE_ID_PATTERN = "\\p{Upper}{2}\\p{Digit}{4}\\p{Upper}";
    /**
     * The pattern to match a candidate line.
     */
    private static final Pattern CANDIDATE_PATTERN = Pattern.compile(
            "^\\s*" + CANDIDATE_KEY_PATTERN + KEY_VALUE_SEPARATOR_PATTERN + "\\s*" + CANDIDATE_ID_PATTERN + "(\\s+"
                    + METADATA_MARKER_PATTERN + METADATA_KEY_PATTERN + KEY_VALUE_SEPARATOR_PATTERN + ".+?)+$");
    /**
     * The pattern to extract the key for candidate.
     */
    private static final Pattern KEY_EXTRACTION_PATTERN = Pattern.compile("^\\s*(" + CANDIDATE_KEY_PATTERN + ")"
            + KEY_VALUE_SEPARATOR_PATTERN + "\\s*(" + CANDIDATE_ID_PATTERN + ")((\\s+" + METADATA_MARKER_PATTERN
            + METADATA_KEY_PATTERN + KEY_VALUE_SEPARATOR_PATTERN + ".+?)+)$");

    /**
     * The abbreviation for the candidate.
     */
    private String abbreviation;
    /**
     * The ID for the candidate.
     */
    private final String id;
    /**
     * The key for the candidate.
     */
    private final String key;
    /**
     * The name for the candidate.
     */
    private String name;
    /**
     * The romanized name for the candidate.
     */
    private String romanizedName;

    /**
     * Private constructor taking the key and the ID for the candidate as the parameter.
     *
     * @param key The key for the candidate.
     * @param id  The ID for the candidate.
     */
    private CandidateLine(final String key, final String id) {
        this.key = key;
        this.id = id;
    }

    /**
     * Returns the candidate from this line.
     *
     * @return The candidate from this line.
     */
    Candidate getCandidate() {
        return Candidate.get(id);
    }

    /**
     * Returns the key for the candidate in this line.
     *
     * @return The key for the candidate in this line.
     */
    String getKey() {
        return key;
    }

    /**
     * Verifies whether a line is a definition of a candidate line.
     *
     * @param line The line to check against the pattern of a candiate line.
     * @return True if the line matches the pattern of a candidate line, false otherwise.
     */
    static boolean isCandidateLine(final String line) {
        return textMatchesPattern(CANDIDATE_PATTERN, line);
    }

    /**
     * Parses a candidate line.
     *
     * @param token The Laconic logging token.
     * @param line  The line to parse.
     * @return A CandidateLine representing the line.
     */
    static CandidateLine parse(final Token token, final String line) {
        Matcher candidateKeyMatcher = KEY_EXTRACTION_PATTERN.matcher(line);
        candidateKeyMatcher.find();
        String key = candidateKeyMatcher.group(1);
        String id = candidateKeyMatcher.group(2);
        String remainder = candidateKeyMatcher.group(THREE);
        CandidateLine candidateLine = new CandidateLine(key, id);
        while (!remainder.isEmpty()) {
            remainder = candidateLine.parseKeyValue(token, remainder);
        }
        return candidateLine;
    }

    /**
     * Processes a key and value from a part of a candidate line.
     *
     * @param remainder The remainder of a line to parse a key and value from.
     * @return The unprocessed part of the line.
     */
    private String parseKeyValue(final Token token, final String remainder) {
        Matcher keyValuesMatcher = KEY_VALUES_PATTERN.matcher(remainder);
        keyValuesMatcher.find();
        String keyValueBlock = keyValuesMatcher.group(1);
        processKeyValue(token, keyValueBlock);
        return keyValuesMatcher.group(FOUR);
    }

    /**
     * Processes a data block with information for a candidate.
     *
     * @param keyValueString The data block to process.
     */
    private void processKeyValue(final Token token, final String keyValueString) {
        Matcher keyValueMatcher = METADATA_KEY_VALUE_PATTERN.matcher(keyValueString);
        keyValueMatcher.find();
        String blockKey = keyValueMatcher.group(1);
        String value = keyValueMatcher.group(2);
        switch (blockKey) {
        case "A":
            abbreviation = value;
            break;
        case "N":
            name = value;
            break;
        case "R":
            romanizedName = value;
            break;
        default:
            Laconic.LOGGER.logError("Unknown metadata key %s.", blockKey, token);
        }
    }

    /**
     * Updates the candidate with the data in the line.
     */
    void updateCandidate() {
        Candidate candidate = Candidate.get(id);
        candidate.setAbbreviation(abbreviation);
        candidate.setName(name);
        candidate.setRomanizedName(romanizedName);
    }
}
