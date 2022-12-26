package net.filipvanlaenen.asapop.parser;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import net.filipvanlaenen.asapop.model.ResultValue;

/**
 * Class representing the a result value text.
 */
final class ResultValueText {
    /**
     * The pattern to match an opinion poll line.
     */
    private static final Pattern WELLFORMED_RESULT_VALUE_PATTERN = Pattern.compile("^<?\\d+(\\.\\d+)?$");
    /**
     * The result value.
     */
    private final ResultValue value;
    /**
     * The warnings.
     */
    private final Set<ParserWarning> warnings;

    /**
     * Constructor taking a result value and the warnings collected while parsing the text.
     *
     * @param value    The result value.
     * @param warnings The warnings collected while parsing.
     */
    private ResultValueText(final ResultValue value, final Set<ParserWarning> warnings) {
        this.value = value;
        this.warnings = warnings;
    }

    /**
     * Returns the result value represented by the result value text.
     *
     * @return The result value.
     */
    ResultValue getValue() {
        return value;
    }

    /**
     * Returns the warnings.
     *
     * @return The warnings.
     */
    Set<ParserWarning> getWarnings() {
        return Collections.unmodifiableSet(warnings);
    }

    /**
     * Parses a text into a result value text.
     *
     * @param value      The text representing the result value.
     * @param lineNumber The number of the line where the text occurs.
     * @return An instance representing the parsed text.
     */
    static ResultValueText parse(final String value, final int lineNumber) {
        Set<ParserWarning> warnings = new HashSet<ParserWarning>();
        if (!WELLFORMED_RESULT_VALUE_PATTERN.matcher(value).matches()) {
            warnings.add(new MalformedResultValueWarning(lineNumber, value));
        }
        return new ResultValueText(new ResultValue(value), warnings);
    }
}
