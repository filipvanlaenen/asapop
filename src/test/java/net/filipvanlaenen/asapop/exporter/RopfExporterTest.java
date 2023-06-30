package net.filipvanlaenen.asapop.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.parser.RichOpinionPollsFile;

/**
 * Unit tests on the class <code>RopfExporter</code>.
 */
public class RopfExporterTest {
    /**
     * Verifies the correct export of a minimal opinion poll.
     */
    @Test
    public void shouldExportMinimalOpinionPoll() {
        RichOpinionPollsFile opinionPollsFile = RichOpinionPollsFile.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45",
                "A: AA001 •A:AP •EN: Apple Party", "B: AA002 •A:Bl");
        StringBuffer expected = new StringBuffer();
        expected.append("•PF: ACME •PD: 2021-07-27 AP: 55 BL: 45\n");
        expected.append("\n");
        expected.append("AP: AA001 •A: AP •EN: Apple Party\n");
        expected.append("BL: AA002 •A: Bl\n");
        assertEquals(expected.toString(), RopfExporter.export(opinionPollsFile));
    }

    /**
     * Verifies the correct export of an elaborate opinion poll.
     */
    @Test
    public void shouldExportElaborateOpinionPoll() {
        RichOpinionPollsFile opinionPollsFile =
                RichOpinionPollsFile.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45 C: 2 D: 2 E: 2",
                        "A: AA001 •A:AP •EN: Apple Party •NL: Appelpartij •EO: Pomo Partio •NO: Eplepartiet",
                        "B: AA002 •A:Bl •NL: Blauw", "C: AA003 •A:C", "D: AA004 •A:D", "E: AA005 •A:E");
        StringBuffer expected = new StringBuffer();
        expected.append("•PF: ACME •PD: 2021-07-27 AP: 55 BL: 45 C: 2 D: 2 E: 2\n");
        expected.append("\n");
        expected.append("AP: AA001 •A: AP •EN: Apple Party •EO: Pomo Partio •NL: Appelpartij •NO: Eplepartiet\n");
        expected.append("BL: AA002 •A: Bl                                   •NL: Blauw\n");
        expected.append("C:  AA003 •A: C\n");
        expected.append("D:  AA004 •A: D\n");
        expected.append("E:  AA005 •A: E\n");
        assertEquals(expected.toString(), RopfExporter.export(opinionPollsFile));
    }
}
