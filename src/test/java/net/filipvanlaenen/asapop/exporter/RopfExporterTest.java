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
        RichOpinionPollsFile opinionPollsFile = RichOpinionPollsFile.parse(
                "•PF: ACME •PFP: BCME •C: The Times •C: The Post •FS: 2021-07-25 •FE: 2021-07-26 •PD: 2021-07-27 •SC: N •A: IO •SS: 1000 •EX: 10 A:55 B:45 C: 2 D: 2 E: 2 •O: 2 •N: 3",
                "A: AA001 •A:AP •EN: Apple Party •NL: Appelpartij •EO: Pomo Partio •NO: Eplepartiet",
                "B: AA002 •A:Bl •NL: Blauw", "C: AA003 •A:C", "D: AA004 •A:Δ •R:D", "E: AA005 •A:E");
        StringBuffer expected = new StringBuffer();
        expected.append(
                "•PF: ACME •PFP: BCME •C: The Post •C: The Times •FS: 2021-07-25 •FE: 2021-07-26 •PD: 2021-07-27 •SC: N •A: IO •SS: 1000 •EX: 10 AP: 55 BL: 45 C: 2 E: 2 Δ: 2 •O: 2 •N: 3\n");
        expected.append("\n");
        expected.append("AP: AA001 •A: AP       •EN: Apple Party •EO: Pomo Partio •NL: Appelpartij •NO: Eplepartiet\n");
        expected.append("BL: AA002 •A: Bl                                         •NL: Blauw\n");
        expected.append("C:  AA003 •A: C\n");
        expected.append("E:  AA005 •A: E\n");
        expected.append("Δ:  AA004 •A: Δ  •R: D\n");
        assertEquals(expected.toString(), RopfExporter.export(opinionPollsFile));
    }

    /**
     * Verifies the correct export of an opinion poll with an alternative response scenario.
     */
    @Test
    public void shouldExportOpinionPollsWithAlternativeResponse() {
        RichOpinionPollsFile opinionPollsFile = RichOpinionPollsFile.parse(
                "•PF: ACME •C: The Times •FS: 2021-07-25 •FE: 2021-07-26 •SC: N •A: IO •SS: 1000 •EX: 10 A:55 B:45 •O: 2 •N: 3",
                "& •SC: E •A: OUV •SS: 12000 •EX: 19.5 A:50 B:40 C:5.2 •O: 1.2 •N: 2.1",
                "A: AA001 •A:AP •EN: Apple Party", "B: AA002 •A:Bl •NL: Blauw", "C: AA003 •A:C");
        StringBuffer expected = new StringBuffer();
        expected.append(
                "•PF: ACME •C: The Times •FS: 2021-07-25 •FE: 2021-07-26 •SC: N •A: IO  •SS: 1000  •EX: 10   AP: 55 BL: 45 •O: 2   •N: 3\n");
        expected.append(
                "&                                                       •SC: E •A: OUV •SS: 12000 •EX: 19.5 AP: 50 BL: 40 C: 5.2 •O: 1.2 •N: 2.1\n");
        expected.append("\n");
        expected.append("AP: AA001 •A: AP •EN: Apple Party\n");
        expected.append("BL: AA002 •A: Bl                  •NL: Blauw\n");
        expected.append("C:  AA003 •A: C\n");
        assertEquals(expected.toString(), RopfExporter.export(opinionPollsFile));
    }

    /**
     * Verifies that opinion polls are exported in chronological order.
     */
    @Test
    public void shouldExportOpinionPollsInChronologicalOrder() {
        RichOpinionPollsFile opinionPollsFile =
                RichOpinionPollsFile.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45", "•PF: ACME •PD: 2021-07-28 A:56 B:44",
                        "•PF: ACME •PD: 2021-07-29 A:57 B:43", "A: AA001 •A:AP •EN: Apple Party", "B: AA002 •A:Bl");
        StringBuffer expected = new StringBuffer();
        expected.append("•PF: ACME •PD: 2021-07-29 AP: 57 BL: 43\n");
        expected.append("•PF: ACME •PD: 2021-07-28 AP: 56 BL: 44\n");
        expected.append("•PF: ACME •PD: 2021-07-27 AP: 55 BL: 45\n");
        expected.append("\n");
        expected.append("AP: AA001 •A: AP •EN: Apple Party\n");
        expected.append("BL: AA002 •A: Bl\n");
        assertEquals(expected.toString(), RopfExporter.export(opinionPollsFile));
    }

    /**
     * Verifies that the metadata field widths are correct.
     */
    @Test
    public void shouldPadMetadataFieldsCorrectly() {
        RichOpinionPollsFile opinionPollsFile = RichOpinionPollsFile.parse(
                "•PF: ACME •FS: 2021-07-26 •FE: 2021-07-27 A:55 B:45", "•PF: ACME •PD: 2021-07-28 A:56 B:44",
                "•PF: Opinion Research •PD: 2021-07-29 A:57 B:43", "A: AA001 •A:AP •EN: Apple Party", "B: AA002 •A:Bl");
        StringBuffer expected = new StringBuffer();
        expected.append("•PF: Opinion Research                                 •PD: 2021-07-29 AP: 57 BL: 43\n");
        expected.append("•PF: ACME                                             •PD: 2021-07-28 AP: 56 BL: 44\n");
        expected.append("•PF: ACME             •FS: 2021-07-26 •FE: 2021-07-27                 AP: 55 BL: 45\n");
        expected.append("\n");
        expected.append("AP: AA001 •A: AP •EN: Apple Party\n");
        expected.append("BL: AA002 •A: Bl\n");
        assertEquals(expected.toString(), RopfExporter.export(opinionPollsFile));
    }

    /**
     * Verifies that the commissioners field width is correct.
     */
    @Test
    public void shouldPadCommissionnersFieldCorrectly() {
        RichOpinionPollsFile opinionPollsFile = RichOpinionPollsFile.parse(
                "•PF: ACME •C: The Times •PD: 2021-07-27 A:55 B:45", "•PF: ACME •PD: 2021-07-28 A:56 B:44",
                "•PF: Opinion Research •C: The Times •C: The Post •PD: 2021-07-29 A:57 B:43",
                "A: AA001 •A:AP •EN: Apple Party", "B: AA002 •A:Bl");
        StringBuffer expected = new StringBuffer();
        expected.append("•PF: Opinion Research •C: The Post •C: The Times •PD: 2021-07-29 AP: 57 BL: 43\n");
        expected.append("•PF: ACME                                        •PD: 2021-07-28 AP: 56 BL: 44\n");
        expected.append("•PF: ACME             •C: The Times              •PD: 2021-07-27 AP: 55 BL: 45\n");
        expected.append("\n");
        expected.append("AP: AA001 •A: AP •EN: Apple Party\n");
        expected.append("BL: AA002 •A: Bl\n");
        assertEquals(expected.toString(), RopfExporter.export(opinionPollsFile));
    }
}
