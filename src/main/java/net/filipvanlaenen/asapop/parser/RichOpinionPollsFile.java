package net.filipvanlaenen.asapop.parser;

import java.util.HashSet;
import java.util.Set;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;

/**
 * Class implementing an ROPF file.
 */
public final class RichOpinionPollsFile {
    /**
     * The opinion polls extracted from the file.
     */
    private final OpinionPolls opinionPolls;
    /**
     * The warnings.
     */
    private final Set<ParserWarning> warnings;

    /**
     * Private constructor creating a new instance from a set of opinion polls and the warnings collected while parsing.
     *
     * @param opinionPollsSet The set with the opinion polls.
     * @param warnings        The set with warnings collected while parsing.
     */
    private RichOpinionPollsFile(final Set<OpinionPoll> opinionPollsSet, final Set<ParserWarning> warnings) {
        this.opinionPolls = new OpinionPolls(opinionPollsSet);
        this.warnings = warnings;
    }

    /**
     * Returns the opinion polls.
     *
     * @return The opinion polls.
     */
    public OpinionPolls getOpinionPolls() {
        return opinionPolls;
    }

    /**
     * Returns the warnings.
     *
     * @return The warnings.
     */
    public Set<ParserWarning> getWarnings() {
        return warnings;
    }

    /**
     * Parses a string array into an RichOpinionPollsFile instance.
     *
     * @param lines The multiline string to parse.
     * @return The RichOpinionPollsFile instance.
     */
    public static RichOpinionPollsFile parse(final String... lines) {
        Set<OpinionPoll> opinionPolls = new HashSet<OpinionPoll>();
        Set<ParserWarning> warnings = new HashSet<ParserWarning>();
        OpinionPoll lastOpinionPoll = null;
        int lineNumber = 0;
        for (String line : lines) {
            lineNumber++;
            if (OpinionPollLine.isOpinionPollLine(line)) {
                OpinionPollLine opinionPollLine = OpinionPollLine.parse(line, lineNumber);
                lastOpinionPoll = opinionPollLine.getOpinionPoll();
                opinionPolls.add(lastOpinionPoll);
                warnings.addAll(opinionPollLine.getWarnings());
            } else if (ResponseScenarioLine.isResponseScenarioLine(line)) {
                ResponseScenarioLine responseScenarioLine = ResponseScenarioLine.parse(line, lineNumber);
                // Adding a response scenario to a poll changes its hash code, therefore it has to be removed from the
                // set before the change is made, and added again afterwards.
                opinionPolls.remove(lastOpinionPoll);
                lastOpinionPoll.addAlternativeResponseScenario(responseScenarioLine.getResponseScenario());
                opinionPolls.add(lastOpinionPoll);
                warnings.addAll(responseScenarioLine.getWarnings());
            } else if (ElectoralListLine.isElectoralListLine(line)) {
                ElectoralListLine electoralListLine = ElectoralListLine.parse(line);
                electoralListLine.updateElectoralList();
            } else if (!EmptyLine.isEmptyLine(line) && !CommentLine.isCommentLine(line)) {
                warnings.add(new UnrecognizedLineFormatWarning(lineNumber));
            }
        }
        return new RichOpinionPollsFile(opinionPolls, warnings);
    }
}
