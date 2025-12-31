package net.filipvanlaenen.asapop.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.filipvanlaenen.asapop.model.Candidate;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Class implementing an ROPF file.
 */
public final class RichOpinionPollsFile {
    /**
     * The opinion polls in the file.
     */
    private final Collection<OpinionPoll> opinionPolls;
    /**
     * The opinion polls extracted from the file.
     */
    @Deprecated
    private final OpinionPolls opinionPollsDeprecated;
    /**
     * A list with the comment lines.
     */
    private final List<CommentLine> commentLines;

    /**
     * Private constructor creating a new instance from a set of opinion polls and the warnings collected while parsing.
     *
     * @param opinionPollsSet The set with the opinion polls.
     * @param commentLines    A list with the comment lines.
     */
    private RichOpinionPollsFile(final Set<OpinionPoll> opinionPollsSet, final List<CommentLine> commentLines) {
        this.opinionPollsDeprecated = new OpinionPolls(opinionPollsSet);
        this.opinionPolls = Collection.of(opinionPollsSet.toArray(new OpinionPoll[] {}));
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
     * Returns a collection with all the opinion polls.
     *
     * @return A collection with all the opinion polls.
     */
    public Collection<OpinionPoll> getOpinionPolls() {
        return Collection.of(opinionPolls);
    }

    /**
     * Returns the opinion polls.
     *
     * @return The opinion polls.
     */
    @Deprecated
    public OpinionPolls getOpinionPollsDeprecated() {
        return opinionPollsDeprecated;
    }

    /**
     * Parses a string array into an RichOpinionPollsFile instance.
     *
     * @param fileToken The Laconic logging token.
     * @param lines     The multiline string to parse.
     * @return The RichOpinionPollsFile instance.
     */
    public static RichOpinionPollsFile parse(final Token fileToken, final String... lines) {
        Set<OpinionPoll> opinionPolls = new HashSet<OpinionPoll>();
        List<CommentLine> commentLines = new ArrayList<CommentLine>();
        Map<String, ElectoralList> electoralListKeyMap = new HashMap<String, ElectoralList>();
        Map<String, Candidate> candidateKeyMap = new HashMap<String, Candidate>();
        OpinionPoll lastOpinionPoll = null;
        int lineNumber = 0;
        for (String line : lines) {
            lineNumber++;
            Token token = Laconic.LOGGER.logMessage(fileToken, "Parsing line number %d.", lineNumber);
            if (ElectoralListLine.isElectoralListLine(line)) {
                Laconic.LOGGER.logMessage("Line is recognized as an electoral list line.", token);
                ElectoralListLine electoralListLine = ElectoralListLine.parse(token, line);
                electoralListLine.updateElectoralList();
                electoralListKeyMap.put(electoralListLine.getKey(), electoralListLine.getElectoralList());
            } else if (CandidateLine.isCandidateLine(line)) {
                Laconic.LOGGER.logMessage("Line is recognized as a candidate line.", token);
                CandidateLine candidateLine = CandidateLine.parse(token, line);
                candidateLine.updateCandidate();
                candidateKeyMap.put(candidateLine.getKey(), candidateLine.getCandidate());
            }
        }
        lineNumber = 0;
        for (String line : lines) {
            lineNumber++;
            Token token = Laconic.LOGGER.logMessage(fileToken, "Parsing line number %d.", lineNumber);
            if (OpinionPollLine.isOpinionPollLine(line)) {
                Laconic.LOGGER.logMessage("Line is recognized as an opinion poll line.", token);
                OpinionPollLine opinionPollLine =
                        OpinionPollLine.parse(line, electoralListKeyMap, candidateKeyMap, token);
                lastOpinionPoll = opinionPollLine.getOpinionPoll();
                opinionPolls.add(lastOpinionPoll);
            } else if (ResponseScenarioLine.isResponseScenarioLine(line)) {
                Laconic.LOGGER.logMessage("Line is recognized as a response scenario line.", token);
                ResponseScenarioLine responseScenarioLine =
                        ResponseScenarioLine.parse(line, electoralListKeyMap, candidateKeyMap, token);
                // Adding a response scenario to a poll changes its hash code, therefore it has to be removed from the
                // set before the change is made, and added again afterwards.
                opinionPolls.remove(lastOpinionPoll);
                lastOpinionPoll.addAlternativeResponseScenario(responseScenarioLine.getResponseScenario());
                opinionPolls.add(lastOpinionPoll);
            } else if (CommentLine.isCommentLine(line)) {
                Laconic.LOGGER.logMessage("Line is recognized as a comment line.", token);
                commentLines.add(CommentLine.parse(line));
            } else if (!ElectoralListLine.isElectoralListLine(line) && !CandidateLine.isCandidateLine(line)
                    && !EmptyLine.isEmptyLine(line)) {
                Laconic.LOGGER.logError("Line doesn't have a recognized line format.", token);
            }
        }
        return new RichOpinionPollsFile(opinionPolls, commentLines);
    }
}
