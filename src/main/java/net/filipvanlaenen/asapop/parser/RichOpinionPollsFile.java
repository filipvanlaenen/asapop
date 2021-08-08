package net.filipvanlaenen.asapop.parser;

import java.util.ArrayList;
import java.util.List;
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

    private final OpinionPolls opinionPolls;

    private RichOpinionPollsFile(final List<OpinionPoll> opinionPolls) {
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
        List<OpinionPoll> opinionPolls = new ArrayList<OpinionPoll>();
        OpinionPoll lastOpinionPoll = null;
        for (String line : lines) {
            if (OpinionPollLine.isOpinionPollLine(line)) {
                OpinionPollLine opinionPollLine = OpinionPollLine.parse(line);
                lastOpinionPoll = opinionPollLine.getOpinionPoll();
                opinionPolls.add(lastOpinionPoll);
            } else if (ResponseScenarioLine.isResponseScenarioLine(line)) {
                lastOpinionPoll.addAlternativeResponseScenario(ResponseScenarioLine.parse(line));
            }
        }
        return new RichOpinionPollsFile(opinionPolls);
    }
}
