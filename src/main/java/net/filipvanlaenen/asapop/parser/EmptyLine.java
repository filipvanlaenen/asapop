package net.filipvanlaenen.asapop.parser;

import java.util.regex.Pattern;

/**
 * Class implementing an empty line.
 */
final class EmptyLine extends Line {
    /**
     * The pattern to match an empty line.
     */
    private static final Pattern EMPTY_PATTERN = Pattern.compile("^\\s*$");

    /**
     * Verifies whether a line is a empty.
     *
     * @param line The line to check against the pattern of an empty line.
     * @return True if the line matches the pattern of an empty line, false otherwise.
     */
    static boolean isEmptyLine(final String line) {
        return textMatchesPattern(EMPTY_PATTERN, line);
    }
}
