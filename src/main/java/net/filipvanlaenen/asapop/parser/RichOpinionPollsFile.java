package net.filipvanlaenen.asapop.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Class implementing an ROPF file.
 */
public final class RichOpinionPollsFile {
    /**
     * The opinion polls extracted from the file.
     */
    private final OpinionPolls opinionPolls;
    /**
     * A list with the comment lines.
     */
    private final List<CommentLine> commentLines;

    /**
     * Private constructor creating a new instance from a set of opinion polls and the warnings collected while parsing.
     *
     * @param opinionPollsSet The set with the opinion polls.
     * @param commentLines    A list with the comment lines.
     * @param warnings        The set with warnings collected while parsing.
     */
    private RichOpinionPollsFile(final Set<OpinionPoll> opinionPollsSet, final List<CommentLine> commentLines) {
        this.opinionPolls = new OpinionPolls(opinionPollsSet);
        this.commentLines = commentLines;
    }

    /**
     * Returns the comment lines.
     *
     * @return The comment lines.
     */
    public List<CommentLine> getCommentLines() {
        return commentLines;
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
     * Parses a string array into an RichOpinionPollsFile instance.
     *
     * @param lines The multiline string to parse.
     * @return The RichOpinionPollsFile instance.
     */
    public static RichOpinionPollsFile parse(final Token fileToken, final String... lines) {
        Set<OpinionPoll> opinionPolls = new HashSet<OpinionPoll>();
        List<CommentLine> commentLines = new ArrayList<CommentLine>();
        Map<String, ElectoralList> electoralListKeyMap = new HashMap<String, ElectoralList>();
        OpinionPoll lastOpinionPoll = null;
        int lineNumber = 0;
        for (String line : lines) {
            lineNumber++;
            Token token = Laconic.LOGGER.logMessage(fileToken, "Parsing line number %d.", lineNumber);
            if (ElectoralListLine.isElectoralListLine(line)) {
                Laconic.LOGGER.logMessage("Line is recognized as an electoral list line.", token);
                ElectoralListLine electoralListLine = ElectoralListLine.parse(line);
                electoralListLine.updateElectoralList();
                electoralListKeyMap.put(electoralListLine.getKey(), electoralListLine.getElectoralList());
            }
        }
        lineNumber = 0;
        for (String line : lines) {
            lineNumber++;
            Token token = Laconic.LOGGER.logMessage(fileToken, "Parsing line number %d.", lineNumber);
            if (OpinionPollLine.isOpinionPollLine(line)) {
                Laconic.LOGGER.logMessage("Line is recognized as an opinion poll line.", token);
                OpinionPollLine opinionPollLine = OpinionPollLine.parse(line, electoralListKeyMap, token);
                lastOpinionPoll = opinionPollLine.getOpinionPoll();
                opinionPolls.add(lastOpinionPoll);
            } else if (ResponseScenarioLine.isResponseScenarioLine(line)) {
                Laconic.LOGGER.logMessage("Line is recognized as a response scenario line.", token);
                ResponseScenarioLine responseScenarioLine =
                        ResponseScenarioLine.parse(line, electoralListKeyMap, token);
                // Adding a response scenario to a poll changes its hash code, therefore it has to be removed from the
                // set before the change is made, and added again afterwards.
                opinionPolls.remove(lastOpinionPoll);
                lastOpinionPoll.addAlternativeResponseScenario(responseScenarioLine.getResponseScenario());
                opinionPolls.add(lastOpinionPoll);
            } else if (CommentLine.isCommentLine(line)) {
                Laconic.LOGGER.logMessage("Line is recognized as a comment line.", token);
                commentLines.add(CommentLine.parse(line));
            } else if (!ElectoralListLine.isElectoralListLine(line) && !EmptyLine.isEmptyLine(line)) {
                Laconic.LOGGER.logError("Line doesn't have a recognized line format.", token);
            }
        }
        return new RichOpinionPollsFile(opinionPolls, commentLines);
    }
}
