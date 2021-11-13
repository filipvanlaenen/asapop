package net.filipvanlaenen.asapop.parser;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import net.filipvanlaenen.asapop.model.ResultValue;

class ResultValueText {
    /**
     * The pattern to match an opinion poll line.
     */
    private static final Pattern WELLFORMED_RESULT_VALUE_PATTERN = Pattern.compile("^\\d+(\\.\\d+)?$");
    /**
     * The result value.
     */
    private final ResultValue value;
    /**
     * The warnings.
     */
    private final Set<Warning> warnings;

    private ResultValueText(final ResultValue value, final Set<Warning> warnings) {
        this.value = value;
        this.warnings = warnings;
    }

    ResultValue getValue() {
        return value;
    }

    /**
     * Returns the warnings.
     *
     * @return The warnings.
     */
    Set<Warning> getWarnings() {
        return Collections.unmodifiableSet(warnings);
    }

    static ResultValueText parse(final String value, final int lineNumber) {
        Set<Warning> warnings = new HashSet<Warning>();
        if (!WELLFORMED_RESULT_VALUE_PATTERN.matcher(value).matches()) {
            warnings.add(new MalformedResultValueWarning(lineNumber, value));
        }
        return new ResultValueText(new ResultValue(value), warnings);
    }
}
