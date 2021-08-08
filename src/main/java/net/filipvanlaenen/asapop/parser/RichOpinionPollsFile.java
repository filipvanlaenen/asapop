package net.filipvanlaenen.asapop.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;

/**
 * Class implementing an ROPF file.
 */
public final class RichOpinionPollsFile {
    /**
     * The integer number three.
     */
    private static final int THREE = 3;
    /**
     * The opinion polls extracted from the file.
     */
    private final OpinionPolls opinionPolls;

    /**
     * Private constructor creating a new instance from a list of opinion polls.
     */
    private RichOpinionPollsFile(final Set<OpinionPoll> opinionPolls) {
        this.opinionPolls = new OpinionPolls(opinionPolls);
    }

    public OpinionPolls getOpinionPolls() {
        return opinionPolls;
    }

    /**
     * Parses a string array into an OpinionPolls instance.
     *
     * @param lines The multiline string to parse.
     * @return The OpinionPolls instance.
     */
    public static RichOpinionPollsFile parse(final String... lines) {
        Set<OpinionPoll> opinionPolls = new HashSet<OpinionPoll>();
        OpinionPoll lastOpinionPoll = null;
        for (String line : lines) {
            if (OpinionPollLine.isOpinionPollLine(line)) {
                OpinionPollLine opinionPollLine = OpinionPollLine.parse(line);
                lastOpinionPoll = opinionPollLine.getOpinionPoll();
                opinionPolls.add(lastOpinionPoll);
            } else if (ResponseScenarioLine.isResponseScenarioLine(line)) {
                ResponseScenarioLine responseScenarioLine = ResponseScenarioLine.parse(line);
                opinionPolls.remove(lastOpinionPoll);
                lastOpinionPoll.addAlternativeResponseScenario(responseScenarioLine.getResponseScenario());
                opinionPolls.add(lastOpinionPoll);
            }
        }
        return new RichOpinionPollsFile(opinionPolls);
    }
}
