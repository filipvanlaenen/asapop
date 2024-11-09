package net.filipvanlaenen.asapop.parser;

import java.util.regex.Pattern;

import net.filipvanlaenen.asapop.model.ResultValue;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Class representing a result value text.
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
     * Constructor taking a result value.
     *
     * @param value The result value.
     */
    private ResultValueText(final ResultValue value) {
        this.value = value;
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
     * Parses a text into a result value text.
     *
     * @param value The text representing the result value.
     * @param token The Laconic logging token.
     * @return An instance representing the parsed text.
     */
    static ResultValueText parse(final String value, final Token token) {
        if (!WELLFORMED_RESULT_VALUE_PATTERN.matcher(value).matches()) {
            Laconic.LOGGER.logError("Malformed result value %s.", value, token);
        }
        return new ResultValueText(new ResultValue(value));
    }
}
