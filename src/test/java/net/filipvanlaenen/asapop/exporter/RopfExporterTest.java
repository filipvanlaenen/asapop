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
}
