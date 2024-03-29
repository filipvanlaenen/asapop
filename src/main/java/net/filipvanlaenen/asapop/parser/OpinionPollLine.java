package net.filipvanlaenen.asapop.parser;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
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
     * @param line                The line to parse an opinion poll from.
     * @param electoralListKeyMap The map mapping keys to electoral lists.
     * @param lineNumber          The line number.
     * @return An OpinionPollLine representing the line.
     */
    static OpinionPollLine parse(final String line, final Map<String, ElectoralList> electoralListKeyMap,
            final int lineNumber) {
        OpinionPoll.Builder builder = new OpinionPoll.Builder();
        Set<ParserWarning> warnings = new HashSet<ParserWarning>();
        String remainder = line;
        while (!remainder.isEmpty()) {
            remainder = parseKeyValue(builder, warnings, remainder, electoralListKeyMap, lineNumber);
        }
        if (!builder.hasResults()) {
            warnings.add(new ResultsMissingWarning(lineNumber));
        }
        if (!builder.resultsAddUp()) {
            warnings.add(new ResultValuesNotAddingUpWithinRoundingErrorIntervalWarning(lineNumber));
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
     * @param builder             The opinion poll builder to build on.
     * @param warnings            The set to add any warnings too.
     * @param remainder           The remainder of a line to parse a key and value from.
     * @param electoralListKeyMap The map mapping keys to electoral lists.
     * @param lineNumber          The line number the data block.
     * @return The unprocessed part of the line.
     */
    private static String parseKeyValue(final OpinionPoll.Builder builder, final Set<ParserWarning> warnings,
            final String remainder, final Map<String, ElectoralList> electoralListKeyMap, final int lineNumber) {
        Matcher keyValuesMatcher = KEY_VALUES_PATTERN.matcher(remainder);
        keyValuesMatcher.find();
        String keyValueBlock = keyValuesMatcher.group(1);
        if (keyValueBlock.startsWith(METADATA_MARKER_PATTERN)) {
            processMetadata(builder, warnings, keyValueBlock, lineNumber);
        } else {
            processResultData(builder, warnings, keyValueBlock, electoralListKeyMap, lineNumber);
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
            if (builder.hasArea()) {
                warnings.add(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(lineNumber, key));
            } else {
                builder.setArea(value);
            }
            break;
        case "C":
            builder.addCommissioner(value);
            break;
        case "EX":
            if (builder.hasExcluded()) {
                warnings.add(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(lineNumber, key));
            } else if (textMatchesPattern(DECIMAL_NUMBER_PATTERN, value)) {
                DecimalNumber excluded = DecimalNumber.parse(value);
                builder.setExcluded(excluded);
            } else {
                warnings.add(new MalformedDecimalNumberWarning(lineNumber, key, value));
            }
            break;
        case "FE":
            if (builder.hasFieldworkEnd()) {
                warnings.add(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(lineNumber, key));
            } else if (textMatchesPattern(DATE_MONTH_OR_YEAR_PATTERN, value)) {
                DateMonthOrYear fieldworkEnd = DateMonthOrYear.parse(value);
                builder.setFieldworkEnd(fieldworkEnd);
            } else {
                warnings.add(new MalformedDateMonthOrYearWarning(lineNumber, key, value));
            }
            break;
        case "FS":
            if (builder.hasFieldworkStart()) {
                warnings.add(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(lineNumber, key));
            } else if (textMatchesPattern(DATE_MONTH_OR_YEAR_PATTERN, value)) {
                DateMonthOrYear fieldworkStart = DateMonthOrYear.parse(value);
                builder.setFieldworkStart(fieldworkStart);
            } else {
                warnings.add(new MalformedDateMonthOrYearWarning(lineNumber, key, value));
            }
            break;
        case "N":
            if (builder.hasNoResponses()) {
                warnings.add(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(lineNumber, key));
            } else {
                ResultValueText noResponse = ResultValueText.parse(value, lineNumber);
                warnings.addAll(noResponse.getWarnings());
                builder.setNoResponses(noResponse.getValue());
            }
            break;
        case "O":
            if (builder.hasOther()) {
                warnings.add(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(lineNumber, key));
            } else {
                ResultValueText other = ResultValueText.parse(value, lineNumber);
                warnings.addAll(other.getWarnings());
                builder.setOther(other.getValue());
            }
            break;
        case "PD":
            if (builder.hasPublicationDate()) {
                warnings.add(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(lineNumber, key));
            } else if (textMatchesPattern(DATE_PATTERN, value)) {
                LocalDate publicationDate = LocalDate.parse(value);
                builder.setPublicationDate(publicationDate);
            } else {
                warnings.add(new MalformedDateWarning(lineNumber, key, value));
            }
            break;
        case "PF":
            if (builder.hasPollingFirm()) {
                warnings.add(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(lineNumber, key));
            } else {
                builder.setPollingFirm(value);
            }
            break;
        case "PFP":
            if (builder.hasPollingFirmPartner()) {
                warnings.add(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(lineNumber, key));
            } else {
                builder.setPollingFirmPartner(value);
            }
            break;
        case "SC":
            if (builder.hasScope()) {
                warnings.add(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(lineNumber, key));
            } else {
                Scope scope = Scope.parse(value);
                if (scope == null) {
                    warnings.add(new UnknownMetadataValueWarning(lineNumber, "scope", value));
                } else {
                    builder.setScope(scope);
                }
            }
            break;
        case "SS":
            if (builder.hasSampleSize()) {
                warnings.add(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(lineNumber, key));
            } else if (textMatchesPattern(SAMPLE_SIZE_PATTERN, value)) {
                SampleSize sampleSize = SampleSize.parse(value);
                builder.setSampleSize(sampleSize);
            } else {
                warnings.add(new MalformedSampleSizeWarning(lineNumber, value));
            }
            break;
        case "U":
            if (builder.hasUnit()) {
                warnings.add(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(lineNumber, key));
            } else {
                Unit unit = Unit.parse(value);
                if (unit == null) {
                    warnings.add(new UnknownMetadataValueWarning(lineNumber, "unit", value));
                } else {
                    builder.setUnit(unit);
                }
            }
            break;
        case "VS":
            if (builder.hasVerifiedSum()) {
                warnings.add(new SingleValueMetadataKeyOccurringMoreThanOnceWarning(lineNumber, key));
            } else {
                try {
                    builder.setVerifiedSum(DecimalNumber.parse(value));
                } catch (NumberFormatException nfe) {
                    warnings.add(new MalformedDecimalNumberWarning(lineNumber, key, value));
                }
            }
            break;
        default:
            warnings.add(new UnknownMetadataKeyWarning(lineNumber, key));
        }
    }

    /**
     * Processes a data block with results for an opinion poll.
     *
     * @param builder             The opinion poll builder to build on.
     * @param warnings            The set to add any warnings too.
     * @param keyValueString      The data block to process.
     * @param electoralListKeyMap The map mapping keys to electoral lists.
     * @param lineNumber          The line number the data block.
     */
    private static void processResultData(final OpinionPoll.Builder builder, final Set<ParserWarning> warnings,
            final String keyValueString, final Map<String, ElectoralList> electoralListKeyMap, final int lineNumber) {
        Matcher keyValueMatcher = RESULT_KEY_VALUE_PATTERN.matcher(keyValueString);
        keyValueMatcher.find();
        String keysValue = keyValueMatcher.group(1);
        Set<String> keys = new HashSet<String>(Arrays.asList(keysValue.split(ELECTORAL_LIST_KEY_SEPARATOR)));
        Set<ElectoralList> electoralLists =
                keys.stream().map(key -> electoralListKeyMap.get(key)).collect(Collectors.toSet());
        ResultValueText value = ResultValueText.parse(keyValueMatcher.group(THREE), lineNumber);
        warnings.addAll(value.getWarnings());
        warnings.addAll(keys.stream().filter(Predicate.not(electoralListKeyMap::containsKey))
                .map(key -> new UnknownElectoralListKeyWarning(lineNumber, key)).collect(Collectors.toSet()));
        builder.addResult(electoralLists, value.getValue());
    }
}
