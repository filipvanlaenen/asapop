package net.filipvanlaenen.asapop.scraper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.OrderedCollection;

/**
 * Class representing a cell in a table.
 */
class Cell {
    /**
     * Pattern string for a single quote.
     */
    private static final String SINGLE_QUOTE = Pattern.quote("'");
    /**
     * Pattern string for any character but a single quote.
     */
    private static final String NOT_SINGLE_QUOTE = "[^" + SINGLE_QUOTE + "]";
    /**
     * Pattern string for any character but a single quote or a greater than sign.
     */
    private static final String NOT_SINGLE_QUOTE_OR_GREATER_THAN = "[^" + Pattern.quote("'>") + "]";
    /**
     * Pattern string for XML arguments.
     */
    private static final String XML_ARGUMENTS = "(\\s(" + NOT_SINGLE_QUOTE_OR_GREATER_THAN + "*" + SINGLE_QUOTE
            + NOT_SINGLE_QUOTE + "*" + SINGLE_QUOTE + ")*" + NOT_SINGLE_QUOTE_OR_GREATER_THAN + "*)?";
    /**
     * Pattern string for a greater than sign in a single quote XML argument.
     */
    private static final String GREATER_THAN_IN_SINGLE_QUOTE_XML_ARGUMENT = "(\\s[\\w-]+" + Pattern.quote("=")
            + SINGLE_QUOTE + NOT_SINGLE_QUOTE_OR_GREATER_THAN + "*)>(" + NOT_SINGLE_QUOTE + "*" + SINGLE_QUOTE + ")";
    /**
     * The pattern matching a greater than sign.
     */
    private final static Pattern GREATER_THAN_PATTERN = Pattern.compile(Pattern.quote(">"));
    /**
     * The pattern matching the colspan argument of a cell.
     */
    private static final Pattern COLSPAN_PATTERN =
            Pattern.compile(Pattern.quote("colspan=\"") + "(\\d+)" + Pattern.quote("\""), Pattern.CASE_INSENSITIVE);
    /**
     * The pattern matching the rowspan argument of a cell.
     */
    private static final Pattern ROWSPAN_PATTERN =
            Pattern.compile(Pattern.quote("rowspan=\"") + "(\\d+)" + Pattern.quote("\""), Pattern.CASE_INSENSITIVE);
    /**
     * Value object for an empty cell.
     */
    static final Cell EMPTY = new Cell("<td></td>");
    /**
     * The content of the cell.
     */
    private final String content;
    /**
     * The text as displayed, possibly over multiple lines.
     */
    private String displayText;
    /**
     * The first line of the display text.
     */
    private String firstLineOfDisplayText;
    /**
     * The display text without newline characters.
     */
    private String flatDisplayText;
    /**
     * The name of the image file displayed in the cell.
     */
    private String image;

    /**
     * Constructor taking the content as its parameter.
     *
     * @param content The content of the cell.
     */
    Cell(final String content) {
        this.content = content;
    }

    /**
     * Returns the colspan of the cell, either the specified value, or 1 if absent.
     *
     * @return The colspan of the cell, either the specified value, or 1 if absent.
     */
    int getColspan() {
        Matcher matcher = COLSPAN_PATTERN.matcher(content);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            return 1;
        }
    }

    /**
     * Returns the display text, i.e. the content without any HTML mark-up.
     *
     * @return The display text.
     */
    String getDisplayText() {
        if (displayText == null) {
            String fragment = content;
            String newFragment = fragment.replaceAll(GREATER_THAN_IN_SINGLE_QUOTE_XML_ARGUMENT, "$1&gt;$2");
            while (!newFragment.equals(fragment)) {
                fragment = newFragment;
                newFragment = fragment.replaceAll(GREATER_THAN_IN_SINGLE_QUOTE_XML_ARGUMENT, "$1&gt;$2");
            }
            fragment = fragment.substring(Scraper.findFirstOccurrenceOf(fragment, 0, GREATER_THAN_PATTERN) + 1,
                    fragment.length() - 5);
            fragment = removeTagsButKeepContent(fragment, "a");
            fragment = removeTagsButKeepContent(fragment, "b");
            fragment = replaceTags(fragment, "br", "\n");
            fragment = removeTags(fragment, "img");
            fragment = removeTags(fragment, "link");
            fragment = removeTagsButKeepContent(fragment, "small");
            fragment = removeTagsButKeepContent(fragment, "span");
            fragment = removeTagsWithContent(fragment, "sup");
            fragment = removeTags(fragment, "wbr");
            displayText = fragment.trim();
        }
        return displayText;
    }

    /**
     * Returns the first line of the display text.
     *
     * @return The first line of the display text.
     */
    String getFirstLineOfDisplayText() {
        if (firstLineOfDisplayText == null) {
            firstLineOfDisplayText = getDisplayText().split("\n")[0];
        }
        return firstLineOfDisplayText;
    }

    /**
     * Returns the display text as a single line.
     *
     * @return The display text as a single line.
     */
    String getFlatDisplayText() {
        if (flatDisplayText == null) {
            flatDisplayText = getDisplayText().replaceAll("\\s+", " ");
        }
        return flatDisplayText;
    }

    String getImage() {
        if (image == null) {
            OrderedCollection<String> images = Scraper.extractEmptyTags(content, "img");
            Pattern pattern = Pattern.compile(Pattern.quote("resource=\"https://en.wikipedia.org/wiki/File:") + "([^"
                    + Pattern.quote("\"") + "]+)" + Pattern.quote("\""), Pattern.CASE_INSENSITIVE);
            for (String img : images) {
                Matcher matcher = pattern.matcher(img);
                if (matcher.find()) {
                    image = matcher.group(1);
                }
            }
        }
        return image;
    }

    /**
     * Returns the rowspan of the cell, either the specified value, or 1 if absent.
     *
     * @return The rowspan of the cell, either the specified value, or 1 if absent.
     */
    int getRowspan() {
        Matcher matcher = ROWSPAN_PATTERN.matcher(content);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            return 1;
        }
    }

    boolean isEmpty() {
        return Collection.of("", "–", "—N/a", "?").contains(getFlatDisplayText());
    }

    private String replaceTags(final String fragment, String tag, String result) {
        Pattern single = Pattern.compile(Pattern.quote("<") + tag + XML_ARGUMENTS + Pattern.quote("/>"),
                Pattern.CASE_INSENSITIVE);
        Matcher m = single.matcher(fragment);
        return m.replaceAll(result);
    }

    private String removeTags(final String fragment, String tag) {
        return replaceTags(fragment, tag, "");
    }

    private String removeTagsButKeepContent(final String fragment, String tagPattern) {
        Pattern opening = Pattern.compile(Pattern.quote("<") + tagPattern + XML_ARGUMENTS + Pattern.quote(">"),
                Pattern.CASE_INSENSITIVE);
        Pattern closing =
                Pattern.compile(Pattern.quote("</") + tagPattern + Pattern.quote(">"), Pattern.CASE_INSENSITIVE);
        Matcher m = opening.matcher(fragment);
        String fragment2 = m.replaceAll("");
        Matcher n = closing.matcher(fragment2);
        return n.replaceAll("");
    }

    private String removeTagsWithContent(final String fragment, String tagPattern) {
        Pattern opening = Pattern.compile(Pattern.quote("<") + tagPattern + XML_ARGUMENTS + Pattern.quote(">"),
                Pattern.CASE_INSENSITIVE);
        Pattern closing =
                Pattern.compile(Pattern.quote("</") + tagPattern + Pattern.quote(">"), Pattern.CASE_INSENSITIVE);
        String result = fragment;
        Matcher m = opening.matcher(result);
        Matcher n = closing.matcher(result);
        while (m.find()) {
            n.find();
            result = result.substring(0, m.start()) + result.substring(n.end());
            m = opening.matcher(result);
            n = closing.matcher(result);
        }
        return result;
    }
}