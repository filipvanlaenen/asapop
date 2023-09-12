package net.filipvanlaenen.asapop.parser;

import java.util.regex.Pattern;

/**
 * Class implementing an comment line.
 */
public final class CommentLine extends Line {
    /**
     * The content of the comment line.
     */
    private final String content;
    /**
     * The pattern to match a comment line.
     */
    private static final Pattern COMMENT_PATTERN = Pattern.compile("^\\s*‡.*$");

    /**
     * Constructor taking the content of the comment line as its parameter.
     *
     * @param content The content of the comment line.
     */
    private CommentLine(final String content) {
        this.content = content;
    }

    /**
     * Returns the content of the comment line.
     *
     * @return The content of the comment line.F
     */
    public String getContent() {
        return content;
    }

    /**
     * Verifies whether a line is a comment.
     *
     * @param line The line to check against the pattern of a comment line.
     * @return True if the line matches the pattern of a comment line, false otherwise.
     */
    static boolean isCommentLine(final String line) {
        return textMatchesPattern(COMMENT_PATTERN, line);
    }

    /**
     * Parses a line into a comment line object.
     *
     * @param line The line to be parsed.
     * @return A comment line object representing the comment line.
     */
    static CommentLine parse(final String line) {
        return new CommentLine(line.strip().substring(1).stripLeading());
    }
}
