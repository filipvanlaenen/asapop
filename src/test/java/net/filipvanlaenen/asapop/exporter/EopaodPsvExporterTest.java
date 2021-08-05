package net.filipvanlaenen.asapop.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.OpinionPolls;

/**
 * Unit tests on the class <code>EopaodPsvExporter</code>.
 */
public class EopaodPsvExporterTest {
    /**
     * Verifies the correct export of a minimal opinion poll.
     */
    @Test
    public void shouldExportMinimalOpinionPollToEopaodPsvFormat() {
        OpinionPolls opinionPolls = OpinionPolls.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45");
        StringBuffer expected = new StringBuffer();
        expected.append("Polling firm | Commissioners | Fieldwork Start | Fieldwork End | Scope | Sample Size");
        expected.append(" | Participation | Precision | A | B | Other\n");
        expected.append("ACME | N/A | 2021-07-27 | 2021-07-27 | N/A | N/A | N/A | N/A | 55 | 45 | N/A");
        assertEquals(expected.toString(), EopaodPsvExporter.export(opinionPolls, "A", "B"));
    }

    /**
     * Verifies the correct export of an opinion poll with an alternative response scenario.
     */
    @Test
    public void shouldExportOpinionPollWithAlternativeResponseScenarioToEopaodPsvFormat() {
        OpinionPolls opinionPolls = OpinionPolls.parse("•PF: ACME •PD: 2021-07-27 A:55 B:45", "& A:50 B:40 C:10");
        StringBuffer expected = new StringBuffer();
        expected.append("Polling firm | Commissioners | Fieldwork Start | Fieldwork End | Scope | Sample Size");
        expected.append(" | Participation | Precision | A | B | C | Other\n");
        expected.append("ACME | N/A | 2021-07-27 | 2021-07-27 | N/A | N/A | N/A | N/A | 55 | 45 | N/A | N/A\n");
        expected.append("ACME | N/A | 2021-07-27 | 2021-07-27 | N/A | N/A | N/A | N/A | 50 | 40 | 10 | N/A");
        assertEquals(expected.toString(), EopaodPsvExporter.export(opinionPolls, "A", "B", "C"));
    }
}
