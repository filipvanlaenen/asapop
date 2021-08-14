package net.filipvanlaenen.asapop.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.parser.RichOpinionPollsFile;

/**
 * Unit tests on the class <code>EopaodCsvExporter</code>.
 */
public class EopaodCsvExporterTest {
    /**
     * Verifies the correct export of a minimal opinion poll.
     */
    @Test
    public void shouldExportMinimalOpinionPollToEopaodCsvFormat() {
        OpinionPolls opinionPolls = RichOpinionPollsFile.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45",
                                                               "A: •A:AP", "B: •A:BL").getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size");
        expected.append(",Sample Size Qualification,Participation,Precision,AP,BL,Other\n");
        expected.append("ACME,Not Available,2021-07-27,2021-07-27,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1,55,45,Not Available");
        assertEquals(expected.toString(), EopaodCsvExporter.export(opinionPolls, "A", "B"));
    }

    /**
     * Verifies the correct export of an opinion poll with an alternative response scenario.
     */
    @Test
    public void shouldExportOpinionPollWithAlternativeResponseScenarioToEopaodPsvFormat() {
        OpinionPolls opinionPolls = RichOpinionPollsFile.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45",
                                                               "& A:50 B:40 C:10", "A: •A:AP", "B: •A:BL", "C: •A:C")
                                                        .getOpinionPolls();
        StringBuffer expected = new StringBuffer();
        expected.append("Polling Firm,Commissioners,Fieldwork Start,Fieldwork End,Scope,Sample Size");
        expected.append(",Sample Size Qualification,Participation,Precision,AP,BL,C,Other\n");
        expected.append("ACME,Not Available,2021-07-27,2021-07-27,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1,55,45,Not Available,Not Available\n");
        expected.append("ACME,Not Available,2021-07-27,2021-07-27,Not Available,Not Available,Not Available");
        expected.append(",Not Available,1,50,40,10,Not Available");
        assertEquals(expected.toString(), EopaodCsvExporter.export(opinionPolls, "A", "B", "C"));
    }
}
