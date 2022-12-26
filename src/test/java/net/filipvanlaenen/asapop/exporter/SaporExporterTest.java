package net.filipvanlaenen.asapop.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.yaml.SaporConfiguration;

/**
 * Unit tests on the <code>SaporExporter</code> class.
 */
public class SaporExporterTest {
    /**
     * The sapor exporter to run the tests on.
     */
    private static SaporExporter saporExporter;

    /**
     * Creates the sapor exporter to run the tests on.
     */
    @BeforeAll
    public static void createSaporExporter() {
        SaporConfiguration saporConfiguration = new SaporConfiguration();
        saporConfiguration.setLastElectionDate("2020-12-06");
        saporConfiguration.setMapping(Collections.EMPTY_SET);
        saporConfiguration.setArea("AR");
        saporExporter = new SaporExporter(saporConfiguration);
    }

    /**
     * Verifies that the header can be created for an opinion poll without a commissioner.
     */
    @Test
    public void opinionPollWithoutCommissionerShouldProduceHeaderCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-08-01")
                .setFieldworkEnd("2021-08-02").addWellformedResult("A", "55").addWellformedResult("B", "45").build();
        StringBuilder sb = new StringBuilder();
        saporExporter.appendSaporHeader(sb, poll);
        StringBuilder expected = new StringBuilder();
        expected.append("Type=Election\n");
        expected.append("PollingFirm=ACME\n");
        expected.append("FieldworkStart=2021-08-01\n");
        expected.append("FieldworkEnd=2021-08-02\n");
        expected.append("Area=AR\n");
        assertEquals(expected.toString(), sb.toString());
    }

    /**
     * Verifies that the header can be created for an opinion poll with a commissioner.
     */
    @Test
    public void opinionPollWithCommissionerShouldProduceHeaderCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").addCommissioner("The Times")
                .setFieldworkStart("2021-08-01").setFieldworkEnd("2021-08-02").addWellformedResult("A", "55")
                .addWellformedResult("B", "45").build();
        StringBuilder sb = new StringBuilder();
        saporExporter.appendSaporHeader(sb, poll);
        StringBuilder expected = new StringBuilder();
        expected.append("Type=Election\n");
        expected.append("PollingFirm=ACME\n");
        expected.append("Commissioners=The Times\n");
        expected.append("FieldworkStart=2021-08-01\n");
        expected.append("FieldworkEnd=2021-08-02\n");
        expected.append("Area=AR\n");
        assertEquals(expected.toString(), sb.toString());
    }

    /**
     * Verifies that the header can be created for an opinion poll without a fieldwork start date.
     */
    @Test
    public void opinionPollWithoutFieldworkStartShouldProduceHeaderCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd("2021-08-02")
                .addWellformedResult("A", "55").addWellformedResult("B", "45").build();
        StringBuilder sb = new StringBuilder();
        saporExporter.appendSaporHeader(sb, poll);
        StringBuilder expected = new StringBuilder();
        expected.append("Type=Election\n");
        expected.append("PollingFirm=ACME\n");
        expected.append("FieldworkStart=2021-08-02\n");
        expected.append("FieldworkEnd=2021-08-02\n");
        expected.append("Area=AR\n");
        assertEquals(expected.toString(), sb.toString());
    }

    /**
     * Verifies that the header can be created for an opinion poll with a publication date only.
     */
    @Test
    public void opinionPollWithPublicationDateOnlyShouldProduceHeaderCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                .addWellformedResult("A", "55").addWellformedResult("B", "45").build();
        StringBuilder sb = new StringBuilder();
        saporExporter.appendSaporHeader(sb, poll);
        StringBuilder expected = new StringBuilder();
        expected.append("Type=Election\n");
        expected.append("PollingFirm=ACME\n");
        expected.append("FieldworkStart=2021-08-02\n");
        expected.append("FieldworkEnd=2021-08-02\n");
        expected.append("Area=AR\n");
        assertEquals(expected.toString(), sb.toString());
    }
}
