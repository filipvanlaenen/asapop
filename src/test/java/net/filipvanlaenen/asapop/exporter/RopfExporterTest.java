package net.filipvanlaenen.asapop.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.parser.RichOpinionPollsFile;
import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Unit tests on the class <code>RopfExporter</code>.
 */
public class RopfExporterTest {
    /**
     * A Laconic logging token for unit testing.
     */
    private static final Token TOKEN = Laconic.LOGGER.logMessage("Unit test RopfExporterTest.");

    /**
     * Verifies the correct export of a minimal opinion poll.
     */
    @Test
    public void shouldExportMinimalOpinionPoll() {
        RichOpinionPollsFile opinionPollsFile = RichOpinionPollsFile.parse(TOKEN, "•PF: ACME •PD: 2021-07-27 A:55 B:45",
                "A: AA001 •A:AP •EN: Apple Party", "B: AA002 •A:Bl");
        StringBuffer expected = new StringBuffer();
        expected.append("•PF: ACME •PD: 2021-07-27 AP: 55 BL: 45\n");
        expected.append("\n");
        expected.append("AP: AA001 •A: AP •EN: Apple Party\n");
        expected.append("BL: AA002 •A: Bl\n");
        assertEquals(expected.toString(),
                RopfExporter.export(opinionPollsFile, OrderedCollection.<Collection<String>>empty()));
    }

    /**
     * Verifies the correct export of an elaborate opinion poll.
     */
    @Test
    public void shouldExportElaborateOpinionPoll() {
        RichOpinionPollsFile opinionPollsFile = RichOpinionPollsFile.parse(TOKEN,
                "•PF: ACME •PFP: BCME •C: The Times •C: The Post •FS: 2021-07-25 •FE: 2021-07-26 •PD: 2021-07-27"
                        + " •SC: N •A: IO •SS: 1000 •EX: 10 •U: % A:55 B:45 C: 2 D: 2 E: 2 F: 2 •O: 2 •N: 3 •VS: 109",
                "•PF: ACME •PFP: BCME •C: The Times •C: The Post •FS: 2021-06-25 •FE: 2021-06-26 •PD: 2021-07-27"
                        + " •SC: N •A: IO •SS: 1000 •EX: 10 •U: % A:55 B:45 C: 2 D: 2 E: 2 F: 2 •ON: 5 •VS: 109",
                "A: AA001 •A:AP •EN: Apple Party •NL: Appelpartij •EO: Pomo Partio •NO: Eplepartiet",
                "B: AA002 •A:Bl •NL: Blauw", "C: AA003 •A:C", "D: AA004 •A:Δ •R:D", "E: AA005 •A:E", "F: AA006 •A:F");
        StringBuffer expected = new StringBuffer();
        expected.append(
                "•PF: ACME •PFP: BCME •C: The Post •C: The Times •FS: 2021-07-25 •FE: 2021-07-26 •PD: 2021-07-27"
                        + " •SC: N •A: IO •SS: 1000 •EX: 10 •U: % AP: 55 BL: 45 C: 2 E: 2 F: 2 Δ: 2 •O: 2 •N: 3"
                        + "        •VS: 109\n");
        expected.append(
                "•PF: ACME •PFP: BCME •C: The Post •C: The Times •FS: 2021-06-25 •FE: 2021-06-26 •PD: 2021-07-27"
                        + " •SC: N •A: IO •SS: 1000 •EX: 10 •U: % AP: 55 BL: 45 C: 2 E: 2 F: 2 Δ: 2            "
                        + " •ON: 5 •VS: 109\n");
        expected.append("\n");
        expected.append("AP: AA001 •A: AP       •EN: Apple Party •EO: Pomo Partio •NL: Appelpartij •NO: Eplepartiet\n");
        expected.append("BL: AA002 •A: Bl                                         •NL: Blauw\n");
        expected.append("C:  AA003 •A: C\n");
        expected.append("E:  AA005 •A: E\n");
        expected.append("F:  AA006 •A: F\n");
        expected.append("Δ:  AA004 •A: Δ  •R: D\n");
        assertEquals(expected.toString(),
                RopfExporter.export(opinionPollsFile, OrderedCollection.<Collection<String>>empty()));
    }

    /**
     * Verifies the correct export of an opinion poll with an alternative response scenario.
     */
    @Test
    public void shouldExportOpinionPollsWithAlternativeResponse() {
        RichOpinionPollsFile opinionPollsFile = RichOpinionPollsFile.parse(TOKEN,
                "•PF: ACME •C: The Times •FS: 2021-07-25 •FE: 2021-07-26 •SC: N •A: IO •SS: 1000 •EX: 10 A:55 B:45"
                        + " •O: 2 •N: 3",
                "& •SC: N •A: OUV •SS: 800  A:50 B:40 C:10  •VS: 110",
                "& •SC: E •A: OUV •SS: 12000 •EX: 19.5 A:50 B:40 C:5.2 D: 5.2 E: 5.2 •O: 1.2 •N: 2.1 •VS: 112.1",
                "& •SC: P1 •A: OUV •SS: 12000 •EX: 19.5 A:50 B:40 C:5.2 D: 5.2 E: 5.2 •ON: 3.3 •VS: 112.1",
                "A: AA001 •A:AP •EN: Apple Party", "B: AA002 •A:Bl •NL: Blauw", "C: AA003 •A:C", "D: AA004 •A:D",
                "E: AA005 •A:E");
        StringBuffer expected = new StringBuffer();
        expected.append("•PF: ACME •C: The Times •FS: 2021-07-25 •FE: 2021-07-26 •SC: N  •A: IO  •SS: 1000  •EX: 10  "
                + " AP: 55 BL: 45                      •O: 2   •N: 3\n");
        expected.append("&                                                       •SC: N  •A: OUV •SS: 800            "
                + " AP: 50 BL: 40 C: 10                                         •VS: 110\n");
        expected.append("&                                                       •SC: E  •A: OUV •SS: 12000 •EX: 19.5"
                + " AP: 50 BL: 40 C: 5.2 D: 5.2 E: 5.2 •O: 1.2 •N: 2.1          •VS: 112.1\n");
        expected.append("&                                                       •SC: P1 •A: OUV •SS: 12000 •EX: 19.5"
                + " AP: 50 BL: 40 C: 5.2 D: 5.2 E: 5.2                 •ON: 3.3 •VS: 112.1\n");
        expected.append("\n");
        expected.append("AP: AA001 •A: AP •EN: Apple Party\n");
        expected.append("BL: AA002 •A: Bl                  •NL: Blauw\n");
        expected.append("C:  AA003 •A: C\n");
        expected.append("D:  AA004 •A: D\n");
        expected.append("E:  AA005 •A: E\n");
        assertEquals(expected.toString(),
                RopfExporter.export(opinionPollsFile, OrderedCollection.<Collection<String>>empty()));
    }

    /**
     * Verifies that opinion polls are exported in chronological order.
     */
    @Test
    public void shouldExportOpinionPollsInChronologicalOrder() {
        RichOpinionPollsFile opinionPollsFile = RichOpinionPollsFile.parse(TOKEN, "•PF: ACME •PD: 2021-07-27 A:55 B:45",
                "•PF: ACME •PD: 2021-07-28 A:56 B:44", "•PF: ACME •PD: 2021-07-29 A:57 B:43",
                "A: AA001 •A:AP •EN: Apple Party", "B: AA002 •A:Bl");
        StringBuffer expected = new StringBuffer();
        expected.append("•PF: ACME •PD: 2021-07-29 AP: 57 BL: 43\n");
        expected.append("•PF: ACME •PD: 2021-07-28 AP: 56 BL: 44\n");
        expected.append("•PF: ACME •PD: 2021-07-27 AP: 55 BL: 45\n");
        expected.append("\n");
        expected.append("AP: AA001 •A: AP •EN: Apple Party\n");
        expected.append("BL: AA002 •A: Bl\n");
        assertEquals(expected.toString(),
                RopfExporter.export(opinionPollsFile, OrderedCollection.<Collection<String>>empty()));
    }

    /**
     * Verifies that the metadata field widths are correct.
     */
    @Test
    public void shouldPadMetadataFieldsCorrectly() {
        RichOpinionPollsFile opinionPollsFile = RichOpinionPollsFile.parse(TOKEN,
                "•PF: ACME •FS: 2021-07-26 •FE: 2021-07-27 A:55 B:45", "•PF: ACME •PD: 2021-07-28 A:56 B:43 •O: 1",
                "•PF: Opinion Research •PD: 2021-07-29 A:57 B:42.9", "A: AA001 •A:AP •EN: Apple Party",
                "B: AA002 •A:Bl");
        StringBuffer expected = new StringBuffer();
        expected.append("•PF: Opinion Research                                 •PD: 2021-07-29 AP: 57 BL: 42.9\n");
        expected.append(
                "•PF: ACME                                             •PD: 2021-07-28 AP: 56 BL: 43   •O: 1\n");
        expected.append("•PF: ACME             •FS: 2021-07-26 •FE: 2021-07-27                 AP: 55 BL: 45\n");
        expected.append("\n");
        expected.append("AP: AA001 •A: AP •EN: Apple Party\n");
        expected.append("BL: AA002 •A: Bl\n");
        assertEquals(expected.toString(),
                RopfExporter.export(opinionPollsFile, OrderedCollection.<Collection<String>>empty()));
    }

    /**
     * Verifies that the metadata field U width is correct if it occurs in a response scenario only.
     */
    @Test
    public void shouldPadMetadataFieldUFromResponseScenarioCorrectly() {
        RichOpinionPollsFile opinionPollsFile =
                RichOpinionPollsFile.parse(TOKEN, "•PF: ACME •FS: 2021-07-26 •FE: 2021-07-27 A:55 B:45",
                        "& •U: % A:54 B:45 •O: 1 ", "A: AA001 •A:AP •EN: Apple Party", "B: AA002 •A:Bl");
        StringBuffer expected = new StringBuffer();
        expected.append("•PF: ACME •FS: 2021-07-26 •FE: 2021-07-27       AP: 55 BL: 45\n");
        expected.append("&                                         •U: % AP: 54 BL: 45 •O: 1\n");
        expected.append("\n");
        expected.append("AP: AA001 •A: AP •EN: Apple Party\n");
        expected.append("BL: AA002 •A: Bl\n");
        assertEquals(expected.toString(),
                RopfExporter.export(opinionPollsFile, OrderedCollection.<Collection<String>>empty()));
    }

    /**
     * Verifies that the commissioners field width is correct.
     */
    @Test
    public void shouldPadCommissionnersFieldCorrectly() {
        RichOpinionPollsFile opinionPollsFile = RichOpinionPollsFile.parse(TOKEN,
                "•PF: ACME •C: The Times •PD: 2021-07-27 A:55 B:45", "•PF: ACME •PD: 2021-07-28 A:56 B:44",
                "•PF: Opinion Research •C: The Times •C: The Post •C: The Mail •PD: 2021-07-29 A:57 B:43",
                "A: AA001 •A:AP •EN: Apple Party", "B: AA002 •A:Bl");
        StringBuffer expected = new StringBuffer();
        expected.append(
                "•PF: Opinion Research •C: The Mail •C: The Post •C: The Times •PD: 2021-07-29 AP: 57 BL: 43\n");
        expected.append(
                "•PF: ACME                                                     •PD: 2021-07-28 AP: 56 BL: 44\n");
        expected.append(
                "•PF: ACME             •C: The Times                           •PD: 2021-07-27 AP: 55 BL: 45\n");
        expected.append("\n");
        expected.append("AP: AA001 •A: AP •EN: Apple Party\n");
        expected.append("BL: AA002 •A: Bl\n");
        assertEquals(expected.toString(),
                RopfExporter.export(opinionPollsFile, OrderedCollection.<Collection<String>>empty()));
    }

    /**
     * Verifies the correct export of an opinion poll with list combinations.
     */
    @Test
    public void shouldSortCombinedElectoralListsAlphabetically() {
        RichOpinionPollsFile opinionPollsFile = RichOpinionPollsFile.parse(TOKEN,
                "•PF: ACME •PD: 2021-07-27 A:55 B+F+C+E+D:45", "A: AA001 •A:AP •EN: Apple Party", "B: AA002 •A:B",
                "C: AA003 •A:C", "D: AA004 •A:D", "E: AA005 •A:E", "F: AA006 •A:F");
        StringBuffer expected = new StringBuffer();
        expected.append("•PF: ACME •PD: 2021-07-27 AP: 55 B+C+D+E+F: 45\n");
        expected.append("\n");
        expected.append("AP: AA001 •A: AP •EN: Apple Party\n");
        expected.append("B:  AA002 •A: B\n");
        expected.append("C:  AA003 •A: C\n");
        expected.append("D:  AA004 •A: D\n");
        expected.append("E:  AA005 •A: E\n");
        expected.append("F:  AA006 •A: F\n");
        assertEquals(expected.toString(),
                RopfExporter.export(opinionPollsFile, OrderedCollection.<Collection<String>>empty()));
    }

    /**
     * Verifies that keys are produced when the abbreviation doesn't directly produce a valid key.
     */
    @Test
    public void shouldProduceAlternativeKeys() {
        RichOpinionPollsFile opinionPollsFile = RichOpinionPollsFile.parse(TOKEN, "•PF: ACME •PD: 2021-07-27 A:55 B:45",
                "A: AA001 •A:+ •EN: Apple Party", "B: AA002 •A:3S •EN: 3 Stars", "C: AA003 •A:C", "D: AA004 •A:D",
                "E: AA005 •A:E", "F: AA006 •A:F");
        StringBuffer expected = new StringBuffer();
        expected.append("•PF: ACME •PD: 2021-07-27 AA001: 55 N3S: 45\n");
        expected.append("\n");
        expected.append("AA001: AA001 •A: +  •EN: Apple Party\n");
        expected.append("N3S:   AA002 •A: 3S •EN: 3 Stars\n");
        assertEquals(expected.toString(),
                RopfExporter.export(opinionPollsFile, OrderedCollection.<Collection<String>>empty()));
    }

    /**
     * Verifies the correct export of an opinion poll when some of the electoral lists are preordered from the command
     * line.
     */
    @Test
    public void shouldIncludePreorderingIntoSortingOfElectoralLists() {
        RichOpinionPollsFile opinionPollsFile =
                RichOpinionPollsFile.parse(TOKEN, "•PF: ACME •PD: 2021-07-27 A:40 B:30 C+D:20 E+F:10", "A: AA001 •A:A",
                        "B: AA002 •A:B", "C: AA003 •A:C", "D: AA004 •A:D", "E: AA005 •A:E", "F: AA006 •A:F");
        StringBuffer expected = new StringBuffer();
        expected.append("•PF: ACME •PD: 2021-07-27 B: 30 E+F: 10 A: 40 C+D: 20\n");
        expected.append("\n");
        expected.append("A: AA001 •A: A\n");
        expected.append("B: AA002 •A: B\n");
        expected.append("C: AA003 •A: C\n");
        expected.append("D: AA004 •A: D\n");
        expected.append("E: AA005 •A: E\n");
        expected.append("F: AA006 •A: F\n");
        assertEquals(expected.toString(), RopfExporter.export(opinionPollsFile,
                OrderedCollection.of(Collection.of("AA002"), Collection.of("AA005", "AA006"), Collection.of("AA007"))));
    }
}
