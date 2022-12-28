package net.filipvanlaenen.asapop.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.model.DateOrMonth;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.yaml.DirectSaporMapping;
import net.filipvanlaenen.asapop.yaml.SaporConfiguration;
import net.filipvanlaenen.asapop.yaml.SaporMapping;

/**
 * Unit tests on the <code>SaporExporter</code> class.
 */
public class SaporExporterTest {
    private static final LocalDate DATE = LocalDate.parse("2021-08-02");
    private static final DateOrMonth DATE_OR_MONTH1 = DateOrMonth.parse("2021-08-01");
    private static final DateOrMonth DATE_OR_MONTH2 = DateOrMonth.parse("2021-08-02");
    private static final DateOrMonth DATE_OR_MONTH3 = DateOrMonth.parse("2021-09-01");
    private static final DateOrMonth DATE_OR_MONTH4 = DateOrMonth.parse("2021-09-02");
    /**
     * The SAPOR exporter to run the tests on.
     */
    private static SaporExporter saporExporter;

    /**
     * Creates a direct SAPOR mapping.
     *
     * @param source The source for the direct SAPOR mapping.
     * @param target The target for the direct SAPOR mapping.
     * @return A SAPOR mapping with a direct mapping from the source to the target.
     */
    private static SaporMapping createDirectSaporMapping(final String source, final String target) {
        SaporMapping saporMapping = new SaporMapping();
        DirectSaporMapping directSaporMapping = new DirectSaporMapping();
        directSaporMapping.setSource(source);
        directSaporMapping.setTarget(target);
        saporMapping.setDirectMapping(directSaporMapping);
        return saporMapping;
    }

    /**
     * Returns the SAPOR body for an opinion poll with the lines sorted.
     *
     * @param poll The opinion poll.
     * @return The SAPOR body with the lines sorted.
     */
    private String getSortedSaporBody(final OpinionPoll poll) {
        StringBuilder sb = new StringBuilder();
        saporExporter.appendSaporBody(sb, poll);
        String[] lines = sb.toString().split("\\n");
        Arrays.sort(lines);
        return String.join("\n", lines);
    }

    /**
     * Creates the SAPOR exporter to run the tests on.
     */
    @BeforeAll
    public static void createSaporExporter() {
        SaporConfiguration saporConfiguration = new SaporConfiguration();
        saporConfiguration.setLastElectionDate("2020-12-06");
        saporConfiguration
                .setMapping(Set.of(createDirectSaporMapping("A", "Party A"), createDirectSaporMapping("B", "Party B")));
        saporConfiguration.setArea("AR");
        saporExporter = new SaporExporter(saporConfiguration);
    }

    /**
     * Verifies that the header can be created for an opinion poll without a commissioner.
     */
    @Test
    public void opinionPollWithoutCommissionerShouldProduceHeaderCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).addWellformedResult("A", "55").addWellformedResult("B", "45").build();
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
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).addWellformedResult("A", "55")
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
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH2)
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
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate(DATE)
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
     * Verifies that the body can be created for an opinion poll.
     */
    @Test
    public void opinionPollShouldProduceBodyCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).setSampleSize("1000").addWellformedResult("A", "55")
                .addWellformedResult("B", "43").build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=20\n");
        expected.append("Party A=550\n");
        expected.append("Party B=430");
        assertEquals(expected.toString(), getSortedSaporBody(poll));
    }

    /**
     * Verifies that the body can be created for an opinion poll with a zero value.
     */
    @Test
    public void opinionPollWithZeroValueShouldProduceBodyCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).setSampleSize("1000").addWellformedResult("A", "55")
                .addWellformedResult("B", "0").build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=447\n");
        expected.append("Party A=550\n");
        expected.append("Party B=3");
        assertEquals(expected.toString(), getSortedSaporBody(poll));
    }

    /**
     * Verifies that the body can be created for an opinion poll with other and overflowing rounding.
     */
    @Test
    public void opinionPollWithOtherAndOverflowingRoundingShouldProduceBodyCorrectly() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).setSampleSize("1000").addWellformedResult("A", "55")
                .addWellformedResult("B", "45").setWellformedOther("1").build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=9\n");
        expected.append("Party A=545\n");
        expected.append("Party B=446");
        assertEquals(expected.toString(), getSortedSaporBody(poll));
    }

    /**
     * Verifies that the entire content is produced correctly.
     */
    @Test
    public void opinionPollShouldProduceEntireContent() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).setSampleSize("1000").addWellformedResult("A", "55")
                .addWellformedResult("B", "43").build();
        StringBuilder expected1 = new StringBuilder();
        expected1.append("Type=Election\n");
        expected1.append("PollingFirm=ACME\n");
        expected1.append("FieldworkStart=2021-08-01\n");
        expected1.append("FieldworkEnd=2021-08-02\n");
        expected1.append("Area=AR\n");
        expected1.append("==\n");
        StringBuilder expected2 = new StringBuilder();
        expected2.append(expected1.toString());
        expected1.append("Party A=550\n");
        expected1.append("Party B=430\n");
        expected1.append("Other=20\n");
        expected2.append("Party B=430\n");
        expected2.append("Party A=550\n");
        expected2.append("Other=20\n");
        assertTrue(Set.of(expected1.toString(), expected2.toString()).contains(saporExporter.getSaporContent(poll)));
    }

    /**
     * Verifies that the file path is produced correctly.
     */
    @Test
    public void opinionPollShouldProduceFilePath() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).setSampleSize("1000").addWellformedResult("A", "55")
                .addWellformedResult("B", "43").build();
        assertEquals(Paths.get("2021-08-02-ACME.poll"), saporExporter.getSaporFilePath(poll));
    }

    /**
     * Verifies that the entire directory is produced correctly.
     */
    @Test
    public void opinionPollsShouldProduceDirectory() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).setSampleSize("1000").addWellformedResult("A", "55")
                .addWellformedResult("B", "43").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setPollingFirm("BCME").setFieldworkStart(DATE_OR_MONTH3)
                .setFieldworkEnd(DATE_OR_MONTH4).setSampleSize("1000").addWellformedResult("A", "55")
                .addWellformedResult("Z", "43").build();
        OpinionPolls polls = new OpinionPolls(Set.of(poll1, poll2));
        SaporDirectory saporDirectory = saporExporter.export(polls);
        String actual = saporDirectory.asMap().get(Paths.get("2021-08-02-ACME.poll"));
        StringBuilder expected1 = new StringBuilder();
        expected1.append("Type=Election\n");
        expected1.append("PollingFirm=ACME\n");
        expected1.append("FieldworkStart=2021-08-01\n");
        expected1.append("FieldworkEnd=2021-08-02\n");
        expected1.append("Area=AR\n");
        expected1.append("==\n");
        StringBuilder expected2 = new StringBuilder();
        expected2.append(expected1.toString());
        expected1.append("Party A=550\n");
        expected1.append("Party B=430\n");
        expected1.append("Other=20\n");
        expected2.append("Party B=430\n");
        expected2.append("Party A=550\n");
        expected2.append("Other=20\n");
        assertTrue(Set.of(expected1.toString(), expected2.toString()).contains(actual));
    }

    /**
     * Verifies that no warnings are issued for an opinion poll without conversion problems.
     */
    @Test
    public void opinionPollShouldNotProduceWarnings() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).setSampleSize("1000").addWellformedResult("A", "55")
                .addWellformedResult("B", "43").build();
        assertTrue(saporExporter.getSaporWarnings(poll).isEmpty());
    }

    /**
     * Verifies that a warning about a missing mapping is issued.
     */
    @Test
    public void opinionPollShouldProduceMissingSaporMappingWarning() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).setSampleSize("1000").addWellformedResult("A", "55")
                .addWellformedResult("Z", "43").build();
        assertEquals(Set.of(new MissingSaporMappingWarning(Set.of(ElectoralList.get("Z")))),
                saporExporter.getSaporWarnings(poll));
    }

    /**
     * Verifies that the export produces warnings.
     */
    @Test
    public void opinionPollsShouldProduceWarnings() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).setSampleSize("1000").addWellformedResult("A", "55")
                .addWellformedResult("Q", "43").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setPollingFirm("BCME").setFieldworkStart(DATE_OR_MONTH3)
                .setFieldworkEnd(DATE_OR_MONTH4).setSampleSize("1000").addWellformedResult("A", "55")
                .addWellformedResult("Z", "43").build();
        OpinionPolls polls = new OpinionPolls(Set.of(poll1, poll2));
        SaporDirectory saporDirectory = saporExporter.export(polls);
        assertEquals(Set.of(new MissingSaporMappingWarning(Set.of(ElectoralList.get("Q"))),
                new MissingSaporMappingWarning(Set.of(ElectoralList.get("Z")))), saporDirectory.getWarnings());
    }
}
