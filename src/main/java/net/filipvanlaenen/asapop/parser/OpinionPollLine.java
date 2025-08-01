package net.filipvanlaenen.asapop.parser;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.filipvanlaenen.asapop.model.DateMonthOrYear;
import net.filipvanlaenen.asapop.model.DecimalNumber;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.SampleSize;
import net.filipvanlaenen.asapop.model.Scope;
import net.filipvanlaenen.asapop.model.Unit;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

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
     * The pattern to match a date.
     */
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    /**
     * The pattern to match a date or month.
     */
    private static final Pattern DATE_MONTH_OR_YEAR_PATTERN = Pattern.compile("^\\d{4}(-\\d{2}(-\\d{2})?)?$");
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
     * Private constructor taking the opinion poll as its parameter.
     *
     * @param opinionPoll The opinion poll represented by the line.
     */
    private OpinionPollLine(final OpinionPoll opinionPoll) {
        this.opinionPoll = opinionPoll;
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
     * @param line                The line to parse an opinion poll from.
     * @param electoralListKeyMap The map mapping keys to electoral lists.
     * @param token               The Laconic logging token.
     * @return An OpinionPollLine representing the line.
     */
    static OpinionPollLine parse(final String line, final Map<String, ElectoralList> electoralListKeyMap,
            final Token token) {
        OpinionPoll.Builder builder = new OpinionPoll.Builder();
        String remainder = line;
        while (!remainder.isEmpty()) {
            remainder = parseKeyValue(builder, remainder, electoralListKeyMap, token);
        }
        if (!builder.hasResults()) {
            Laconic.LOGGER.logError("No results found.", token);
        }
        Token sumToken = Laconic.LOGGER.logMessage(token, "Total sum is %f.", builder.getSum());
        if (!builder.resultsAddUp()) {
            Laconic.LOGGER.logError("Results don’t add up within rounding error interval.", sumToken);
        }
        if (!builder.hasDates()) {
            Laconic.LOGGER.logError("No dates found.", token);
        }
        if (!builder.hasConsistentDates()) {
            Laconic.LOGGER.logError("Dates aren't consistent (FS ≤ FE ≤ PD).", token);
        }
        if (!builder.hasPollingFirmOrCommissioner()) {
            Laconic.LOGGER.logError("No polling firm or commissioner.", token);
        }
        if (builder.hasOtherAndNoResponses() && (builder.hasOther() || builder.hasNoResponses())) {
            Laconic.LOGGER.logError(
                    "Other and no responses (ON) shouldn’t be combined with other (O) and/or no responses (N).", token);
        }
        return new OpinionPollLine(builder.build());
    }

    /**
     * Processes a key and value from a part of an opinion poll line.
     *
     * @param builder             The opinion poll builder to build on.
     * @param remainder           The remainder of a line to parse a key and value from.
     * @param electoralListKeyMap The map mapping keys to electoral lists.
     * @param token               The Laconic logging token.
     * @return The unprocessed part of the line.
     */
    private static String parseKeyValue(final OpinionPoll.Builder builder, final String remainder,
            final Map<String, ElectoralList> electoralListKeyMap, final Token token) {
        Matcher keyValuesMatcher = KEY_VALUES_PATTERN.matcher(remainder);
        keyValuesMatcher.find();
        String keyValueBlock = keyValuesMatcher.group(1);
        if (keyValueBlock.startsWith(METADATA_MARKER_PATTERN)) {
            processMetadata(builder, keyValueBlock, token);
        } else {
            processResultData(builder, keyValueBlock, electoralListKeyMap, token);
        }
        return keyValuesMatcher.group(FOUR);
    }

    /**
     * Processes a data block with metadata for an opinon poll.
     *
     * @param builder        The opinion poll builder to build on.
     * @param keyValueString The data block to process.
     * @param token          The Laconic logging token.
     */
    private static void processMetadata(final OpinionPoll.Builder builder, final String keyValueString,
            final Token token) {
        Matcher keyValueMatcher = METADATA_KEY_VALUE_PATTERN.matcher(keyValueString);
        keyValueMatcher.find();
        String key = keyValueMatcher.group(1);
        String value = keyValueMatcher.group(2);
        Token keyToken = Laconic.LOGGER.logMessage(token, "Processing metadata field %s.", key);
        switch (key) {
        case "A":
            if (builder.hasArea()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else {
                builder.setArea(value);
            }
            break;
        case "C":
            builder.addCommissioner(value);
            break;
        case "EX":
            if (builder.hasExcluded()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else if (textMatchesPattern(DECIMAL_NUMBER_PATTERN, value)) {
                DecimalNumber excluded = DecimalNumber.parse(value);
                builder.setExcluded(excluded);
            } else {
                Laconic.LOGGER.logError("Malformed decimal number %s.", value, keyToken);
            }
            break;
        case "FE":
            if (builder.hasFieldworkEnd()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else if (textMatchesPattern(DATE_MONTH_OR_YEAR_PATTERN, value)) {
                DateMonthOrYear fieldworkEnd = DateMonthOrYear.parse(value);
                builder.setFieldworkEnd(fieldworkEnd);
            } else {
                Laconic.LOGGER.logError("Malformed date, month or year %s.", value, keyToken);
            }
            break;
        case "FS":
            if (builder.hasFieldworkStart()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else if (textMatchesPattern(DATE_MONTH_OR_YEAR_PATTERN, value)) {
                DateMonthOrYear fieldworkStart = DateMonthOrYear.parse(value);
                builder.setFieldworkStart(fieldworkStart);
            } else {
                Laconic.LOGGER.logError("Malformed date, month or year %s.", value, keyToken);
            }
            break;
        case "N":
            if (builder.hasNoResponses()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else {
                ResultValueText noResponse = ResultValueText.parse(value, keyToken);
                builder.setNoResponses(noResponse.getValue());
            }
            break;
        case "O":
            if (builder.hasOther()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else {
                ResultValueText other = ResultValueText.parse(value, keyToken);
                builder.setOther(other.getValue());
            }
            break;
        case "ON":
            if (builder.hasOtherAndNoResponses()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else {
                ResultValueText otherAndNoResponses = ResultValueText.parse(value, keyToken);
                builder.setOtherAndNoResponses(otherAndNoResponses.getValue());
            }
            break;
        case "PD":
            if (builder.hasPublicationDate()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else if (textMatchesPattern(DATE_PATTERN, value)) {
                LocalDate publicationDate = LocalDate.parse(value);
                builder.setPublicationDate(publicationDate);
            } else {
                Laconic.LOGGER.logError("Malformed date %s.", value, keyToken);
            }
            break;
        case "PF":
            if (builder.hasPollingFirm()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else {
                builder.setPollingFirm(value);
            }
            break;
        case "PFP":
            if (builder.hasPollingFirmPartner()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else {
                builder.setPollingFirmPartner(value);
            }
            break;
        case "SC":
            if (builder.hasScope()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else {
                Scope scope = Scope.parse(value);
                if (scope == null) {
                    Laconic.LOGGER.logError("Unknown metadata value %s.", value, keyToken);
                } else {
                    builder.setScope(scope);
                }
            }
            break;
        case "SS":
            if (builder.hasSampleSize()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else if (textMatchesPattern(SAMPLE_SIZE_PATTERN, value)) {
                SampleSize sampleSize = SampleSize.parse(value);
                builder.setSampleSize(sampleSize);
            } else {
                Laconic.LOGGER.logError("Malformed sample size %s.", value, keyToken);
            }
            break;
        case "U":
            if (builder.hasUnit()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else {
                Unit unit = Unit.parse(value);
                if (unit == null) {
                    Laconic.LOGGER.logError("Unknown metadata value %s.", value, keyToken);
                } else {
                    builder.setUnit(unit);
                }
            }
            break;
        case "VS":
            if (builder.hasVerifiedSum()) {
                Laconic.LOGGER.logError("Single value metadata key %s occurred more than once.", key, keyToken);
            } else {
                try {
                    builder.setVerifiedSum(DecimalNumber.parse(value));
                } catch (NumberFormatException nfe) {
                    Laconic.LOGGER.logError("Malformed decimal number %s.", value, keyToken);
                }
            }
            break;
        default:
            Laconic.LOGGER.logError("Unknown metadata key %s.", key, keyToken);
        }
    }

    /**
     * Processes a data block with results for an opinion poll.
     *
     * @param builder             The opinion poll builder to build on.
     * @param keyValueString      The data block to process.
     * @param electoralListKeyMap The map mapping keys to electoral lists.
     * @param token               The Laconic logging token.
     */
    private static void processResultData(final OpinionPoll.Builder builder, final String keyValueString,
            final Map<String, ElectoralList> electoralListKeyMap, final Token token) {
        Matcher keyValueMatcher = RESULT_KEY_VALUE_PATTERN.matcher(keyValueString);
        keyValueMatcher.find();
        String keysValue = keyValueMatcher.group(1);
        Token keysToken = Laconic.LOGGER.logMessage(token, "Processing result key %s.", keysValue);
        Set<String> keys = new HashSet<String>(Arrays.asList(keysValue.split(ELECTORAL_LIST_KEY_SEPARATOR)));
        Set<ElectoralList> electoralLists =
                keys.stream().map(key -> electoralListKeyMap.get(key)).collect(Collectors.toSet());
        ResultValueText value = ResultValueText.parse(keyValueMatcher.group(THREE), keysToken);
        for (String key : keys) {
            if (!electoralListKeyMap.containsKey(key)) {
                Laconic.LOGGER.logError("Unknown electoral list key %s.", key, keysToken);
            }
        }
        builder.addResult(electoralLists, value.getValue());
    }
}
