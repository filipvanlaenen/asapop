package net.filipvanlaenen.asapop.scraper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scraper {
    public static boolean containsHeaderWithText(final String page, final String text) {
        return containsTagPatternWithText(page, "h[1-4]", text);
    }

    public static boolean containsTagPatternWithText(final String page, final String tagPattern, final String text) {
        Pattern startPattern = Pattern.compile(Pattern.quote("<") + tagPattern, Pattern.CASE_INSENSITIVE);
        Pattern tagClosingPattern = Pattern.compile(Pattern.quote(">"));
        Pattern endPattern = Pattern.compile(Pattern.quote("</") + tagPattern, Pattern.CASE_INSENSITIVE);
        Pattern textPattern = Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE);
        int i = 0;
        while (i >= 0) {
            int tagStart = findFirstOccurrenceOf(page, i, startPattern);
            if (tagStart == -1) {
                return false;
            } else {
                int tagStartClosing = findFirstOccurrenceOf(page, tagStart, tagClosingPattern);
                int tagEnd = findFirstOccurrenceOf(page, tagStart, endPattern);
                String content = page.substring(tagStartClosing, tagEnd);
                Matcher matcher = textPattern.matcher(content);
                if (matcher.find()) {
                    return true;
                }
                i = tagEnd + 1;
            }
        }
        return false;
    }

    public static boolean containsTagWithText(final String page, final String tag, final String text) {
        return containsTagPatternWithText(page, Pattern.quote(tag), text);
    }

    private static int findFirstOccurrenceOf(final String page, final int fromIndex, final Pattern pattern) {
        Matcher matcher = pattern.matcher(page);
        if (matcher.find(fromIndex)) {
            return matcher.start();
        } else {
            return -1;
        }
    }
}
