package net.filipvanlaenen.asapop.parser;

import java.util.regex.Pattern;

/**
 * Class implementing an empty line.
 */
public final class CommentLine extends Line {
    private final String comment;
    /**
     * The pattern to match a comment line.
     */
    private static final Pattern COMMENT_PATTERN = Pattern.compile("^\\s*‡.*$");

    private CommentLine(final String comment) {
        this.comment = comment;
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

    static CommentLine parse(final String line) {
        return new CommentLine(line.strip().substring(1).stripLeading());
    }

    public String getComment() {
        return comment;
    }
}
