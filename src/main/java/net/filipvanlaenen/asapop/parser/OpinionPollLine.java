package net.filipvanlaenen.asapop.parser;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.filipvanlaenen.asapop.model.DecimalNumber;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.Scope;

/**
 * Class implementing a line representing an opinion poll.
 */
final class OpinionPollLine extends Line {
    /**
     * The integer number three.
     */
    private static final int THREE = 3;
    /**
     * The integer number four.
     */
    private static final int FOUR = 4;
    /**
     * The pattern to match a decimal number.
     */
    private static final Pattern DECIMAL_NUMBER_PATTERN = Pattern.compile("^\\d+(\\.\\d+)?$");
    /**
     * The pattern to match an opinion poll line.
     */
    private static final Pattern OPINION_POLL_PATTERN = Pattern.compile("^\\s*" + METADATA_MARKER_PATTERN
            + METADATA_KEY_PATTERN + KEY_VALUE_SEPARATOR_PATTERN + ".+?(\\s+" + KEY_VALUE_PATTERN + ")+$");

    /**
     * The opinion poll represented by the line.
     */
    private final OpinionPoll opinionPoll;
    /**
     * The warnings.
     */
    private final Set<ParserWarning> warnings;

    /**
     * Private constructor taking the opinion poll as its parameter.
     *
     * @param opinionPoll The opinion poll represented by the line.
     * @param warnings    The warnings related to the line.
     */
    private OpinionPollLine(final OpinionPoll opinionPoll, final Set<ParserWarning> warnings) {
        this.opinionPoll = opinionPoll;
        this.warnings = warnings;
    }

    /**
     * Returns the opinion poll represented by this line.
     *
     * @return The opinion poll represented by this line.
     */
    OpinionPoll getOpinionPoll() {
        return opinionPoll;
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
     * Verifies whether a line is an opinion poll line.
     *
     * @param line The line to check against the pattern of an opinion poll line.
     * @return True if the line matches the pattern of an opinion poll line, false otherwise.
     */
    static boolean isOpinionPollLine(final String line) {
        return textMatchesPattern(OPINION_POLL_PATTERN, line);
    }

    /**
     * Parses an opinion poll line.
     *
     * @param line       The line to parse an opinion poll from.
     * @param lineNumber The line number.
     * @return An OpinionPollLine representing the line.
     */
    static OpinionPollLine parse(final String line, final int lineNumber) {
        OpinionPoll.Builder builder = new OpinionPoll.Builder();
        Set<ParserWarning> warnings = new HashSet<ParserWarning>();
        String remainder = line;
        while (!remainder.isEmpty()) {
            remainder = parseKeyValue(builder, warnings, remainder, lineNumber);
        }
        if (!builder.hasResults()) {
            warnings.add(new ResultsMissingWarning(lineNumber));
        }
        if (!builder.hasDates()) {
            warnings.add(new DatesMissingWarning(lineNumber));
        }
        if (!builder.hasPollingFirmOrCommissioner()) {
            warnings.add(new PollingFirmAndCommissionerMissingWarning(lineNumber));
        }
        return new OpinionPollLine(builder.build(), warnings);
    }

    /**
     * Processes a key and value from a part of an opinion poll line.
     *
     * @param builder    The opinion poll builder to build on.
     * @param warnings   The set to add any warnings too.
     * @param remainder  The remainder of a line to parse a key and value from.
     * @param lineNumber The line number the data block.
     * @return The unprocessed part of the line.
     */
    private static String parseKeyValue(final OpinionPoll.Builder builder, final Set<ParserWarning> warnings,
            final String remainder, final int lineNumber) {
        Matcher keyValuesMatcher = KEY_VALUES_PATTERN.matcher(remainder);
        keyValuesMatcher.find();
        String keyValueBlock = keyValuesMatcher.group(1);
        if (keyValueBlock.startsWith(METADATA_MARKER_PATTERN)) {
            processMetadata(builder, warnings, keyValueBlock, lineNumber);
        } else {
            processResultData(builder, warnings, keyValueBlock, lineNumber);
        }
        return keyValuesMatcher.group(FOUR);
    }

    /**
     * Processes a data block with metadata for an opinon poll.
     *
     * @param builder        The opinion poll builder to build on.
     * @param warnings       The set to add any warnings too.
     * @param keyValueString The data block to process.
     * @param lineNumber     The line number the data block.
     */
    private static void processMetadata(final OpinionPoll.Builder builder, final Set<ParserWarning> warnings,
            final String keyValueString, final int lineNumber) {
        Matcher keyValueMatcher = METADATA_KEY_VALUE_PATTERN.matcher(keyValueString);
        keyValueMatcher.find();
        String key = keyValueMatcher.group(1);
        String value = keyValueMatcher.group(2);
        switch (key) {
        case "A":
            builder.setArea(value);
            break;
        case "C":
            builder.addCommissioner(value);
            break;
        case "EX":
            if (textMatchesPattern(DECIMAL_NUMBER_PATTERN, value)) {
                DecimalNumber excluded = DecimalNumber.parse(value);
                builder.setExcluded(excluded);
            } else {
                warnings.add(new MalformedDecimalNumberWarning(lineNumber, key, value));
            }
            break;
        case "FE":
            builder.setFieldworkEnd(value);
            break;
        case "FS":
            builder.setFieldworkStart(value);
            break;
        case "N":
            ResultValueText noResponse = ResultValueText.parse(value, lineNumber);
            warnings.addAll(noResponse.getWarnings());
            builder.setNoResponses(noResponse.getValue());
            break;
        case "O":
            ResultValueText other = ResultValueText.parse(value, lineNumber);
            warnings.addAll(other.getWarnings());
            builder.setOther(other.getValue());
            break;
        case "PD":
            builder.setPublicationDate(value);
            break;
        case "PF":
            builder.setPollingFirm(value);
            break;
        case "PFP":
            builder.setPollingFirmPartner(value);
            break;
        case "SC":
            Scope scope = parseScope(value);
            if (scope == null) {
                warnings.add(new UnknownScopeValueWarning(lineNumber, value));
            } else {
                builder.setScope(scope);
            }
            break;
        case "SS":
            builder.setSampleSize(value);
            break;
        default:
            warnings.add(new UnknownMetadataKeyWarning(lineNumber, key));
        }
    }

    /**
     * Processes a data block with results for an opinion poll.
     *
     * @param builder        The opinion poll builder to build on.
     * @param warnings       The set to add any warnings too.
     * @param keyValueString The data block to process.
     * @param lineNumber     The line number the data block.
     */
    private static void processResultData(final OpinionPoll.Builder builder, final Set<ParserWarning> warnings,
            final String keyValueString, final int lineNumber) {
        Matcher keyValueMatcher = RESULT_KEY_VALUE_PATTERN.matcher(keyValueString);
        keyValueMatcher.find();
        String keysValue = keyValueMatcher.group(1);
        Set<String> keys = new HashSet<String>(Arrays.asList(keysValue.split(ELECTORAL_LIST_KEY_SEPARATOR)));
        ResultValueText value = ResultValueText.parse(keyValueMatcher.group(THREE), lineNumber);
        warnings.addAll(value.getWarnings());
        builder.addResult(keys, value.getValue());
    }
}
