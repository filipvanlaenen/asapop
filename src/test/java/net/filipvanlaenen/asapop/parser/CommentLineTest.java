package net.filipvanlaenen.asapop.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>CommentLine</code> class.
 */
public class CommentLineTest {
    /**
     * Verifies that the <code>isCommentLine</code> method can detect a comment line.
     */
    @Test
    public void isCommentLineShouldDetectCommentLine() {
        assertTrue(CommentLine.isCommentLine("‡"));
    }

    /**
     * Verifies that the <code>isCommentLine</code> method can detect a comment line with some text.
     */
    @Test
    public void isCommentLineShouldDetectCommentLineWithText() {
        assertTrue(CommentLine.isCommentLine("‡ Foo"));
    }

    /**
     * Verifies that the <code>isCommentLine</code> method can detect a line that isn't a comment.
     */
    @Test
    public void isCommentLineShouldDetectNonCommentLine() {
        assertFalse(CommentLine.isCommentLine("Foo"));
    }
}
