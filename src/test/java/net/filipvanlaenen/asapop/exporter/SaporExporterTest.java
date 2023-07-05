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
import net.filipvanlaenen.asapop.model.DecimalNumber;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPollTestBuilder;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.yaml.AdditiveSaporMapping;
import net.filipvanlaenen.asapop.yaml.DirectSaporMapping;
import net.filipvanlaenen.asapop.yaml.SaporConfiguration;
import net.filipvanlaenen.asapop.yaml.SaporMapping;

/**
 * Unit tests on the <code>SaporExporter</code> class.
 */
public class SaporExporterTest {
    /**
     * The magic number thousand.
     */
    private static final int THOUSAND = 1000;
    /**
     * The magic number two thousand.
     */
    private static final int TWO_THOUSAND = 2000;
    /**
     * A date for the unit tests.
     */
    private static final LocalDate DATE = LocalDate.parse("2021-08-02");
    /**
     * A date or month for the unit tests.
     */
    private static final DateOrMonth DATE_OR_MONTH1 = DateOrMonth.parse("2021-08-01");
    /**
     * Another date or month for the unit tests.
     */
    private static final DateOrMonth DATE_OR_MONTH2 = DateOrMonth.parse("2021-08-02");
    /**
     * A third date or month for the unit tests.
     */
    private static final DateOrMonth DATE_OR_MONTH3 = DateOrMonth.parse("2021-09-01");
    /**
     * A fourth date or month for the unit tests.
     */
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
        return createDirectSaporMapping(source, target, null, null);
    }

    /**
     * Creates a direct SAPOR mapping.
     *
     * @param source    The source for the direct SAPOR mapping.
     * @param target    The target for the direct SAPOR mapping.
     * @param startDate The start date for the direct SAPOR mapping.
     * @param endDate   The end date for the direct SAPOR mapping.
     * @return A SAPOR mapping with a direct mapping from the source to the target.
     */
    private static SaporMapping createDirectSaporMapping(final String source, final String target,
            final String startDate, final String endDate) {
        SaporMapping saporMapping = new SaporMapping();
        DirectSaporMapping directSaporMapping = new DirectSaporMapping();
        directSaporMapping.setSource(source);
        directSaporMapping.setTarget(target);
        saporMapping.setDirectMapping(directSaporMapping);
        saporMapping.setStartDate(startDate);
        saporMapping.setEndDate(endDate);
        return saporMapping;
    }

    /**
     * Creates an additive SAPOR mapping.
     *
     * @param sources The sources for the additive SAPOR mapping.
     * @param target  The target for the additive SAPOR mapping.
     * @return A SAPOR mapping with an additive mapping from the sources to the target.
     */
    private static SaporMapping createAdditiveSaporMapping(final Set<String> sources, final String target) {
        SaporMapping saporMapping = new SaporMapping();
        AdditiveSaporMapping additiveSaporMapping = new AdditiveSaporMapping();
        additiveSaporMapping.setSources(sources);
        additiveSaporMapping.setTarget(target);
        saporMapping.setAdditiveMapping(additiveSaporMapping);
        return saporMapping;
    }

    /**
     * Returns the SAPOR body for an opinion poll with the lines sorted using the default SAPOR exporter.
     *
     * @param poll                      The opinion poll.
     * @param lowestSampleSize          The lowest sample size.
     * @param lowestEffectiveSampleSize The lowest effective sample size.
     * @return The SAPOR body with the lines sorted.
     */
    private String getSortedSaporBody(final OpinionPoll poll, final int lowestSampleSize,
            final int lowestEffectiveSampleSize) {
        return getSortedSaporBody(poll, lowestSampleSize, lowestEffectiveSampleSize, saporExporter);
    }

    /**
     * Returns the SAPOR body for an opinion poll with the lines sorted.
     *
     * @param poll                      The opinion poll.
     * @param lowestSampleSize          The lowest sample size.
     * @param lowestEffectiveSampleSize The lowest effective sample size.
     * @param exporter                  The SAPOR exporter.
     * @return The SAPOR body with the lines sorted.
     */
    private String getSortedSaporBody(final OpinionPoll poll, final int lowestSampleSize,
            final int lowestEffectiveSampleSize, final SaporExporter exporter) {
        StringBuilder sb = new StringBuilder();
        exporter.appendSaporBody(sb, poll, lowestSampleSize, lowestEffectiveSampleSize);
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
        saporConfiguration.setMapping(Set.of(createDirectSaporMapping("A", "Party A"),
                createDirectSaporMapping("B", "Party B"), createDirectSaporMapping("C", "Party C")));
        saporConfiguration.setArea("AR");
        try {
            saporExporter = new SaporExporter(saporConfiguration);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    /**
     * Creates a SAPOR exporter with different direct mappings in time.
     *
     * @return A SAPOR exporter with different direct mappings in time.
     */
    private static SaporExporter createShiftingSaporExporter() {
        SaporConfiguration saporConfiguration = new SaporConfiguration();
        saporConfiguration.setLastElectionDate("2020-12-06");
        saporConfiguration.setMapping(Set.of(createDirectSaporMapping("A", "Party A1", null, "2021-08-01"),
                createDirectSaporMapping("A", "Party A2", "2021-08-02", null),
                createDirectSaporMapping("B", "Party B")));
        saporConfiguration.setArea("AR");
        return new SaporExporter(saporConfiguration);
    }

    /**
     * Creates a SAPOR exporter with an additive mapping.
     *
     * @return A SAPOR exporter with an additive mapping.
     */
    private static SaporExporter createAdditiveSaporExporter() {
        SaporConfiguration saporConfiguration = new SaporConfiguration();
        saporConfiguration.setLastElectionDate("2020-12-06");
        saporConfiguration.setMapping(Set.of(createDirectSaporMapping("A", "Party A"),
                createAdditiveSaporMapping(Set.of("B", "C"), "Alliance B+C"),
                createAdditiveSaporMapping(Set.of("D", "E"), "Alliance D+E")));
        saporConfiguration.setArea("AR");
        return new SaporExporter(saporConfiguration);
    }

    /**
     * Verifies that the header can be created for an opinion poll without a commissioner.
     */
    @Test
    public void opinionPollWithoutCommissionerShouldProduceHeaderCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45").setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
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
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45").setPollingFirm("ACME")
                .addCommissioner("The Times").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
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
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45").setPollingFirm("ACME")
                .setFieldworkEnd(DATE_OR_MONTH2).build();
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
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "45").setPollingFirm("ACME")
                .setPublicationDate(DATE).build();
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
     * Verifies that the body can be created for an opinion poll when the sample size and result values are given.
     *
     * <code>•SS: 1000 A: 40 B: 30 C: 20</code>
     */
    @Test
    public void saporBodyCase01ShouldHandleSampleSizeAndResultValues() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").addResult("C", "20")
                .setSampleSize("1000").setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll when the sample size, the excluded and result values
     * are given.
     *
     * <code>•SS: 1250 •EX: 20 A: 40 B: 30 C: 20</code>
     */
    @Test
    public void saporBodyCase02ShouldHandleSampleSizeExcludedAndResultValues() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").addResult("C", "20")
                .setSampleSize("1250").setExcluded(DecimalNumber.parse("20")).setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll when the sample size, result values and others are
     * given.
     *
     * <code>•SS: 1000 A: 40 B: 30 C: 20 •O: 10</code>
     */
    @Test
    public void saporBodyCase03ShouldHandleSampleSizeResultValuesAndOthers() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").addResult("C", "20")
                .setOther("10").setSampleSize("1000").setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll when the sample size, the excluded, result values and
     * others are given.
     *
     * <code>•SS: 1250 •EX: 20 A: 40 B: 30 C: 20 •O: 10</code>
     */
    @Test
    public void saporBodyCase04ShouldHandleSampleSizeExcludedResultValuesAndOthers() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").addResult("C", "20")
                .setOther("10").setSampleSize("1250").setExcluded(DecimalNumber.parse("20")).setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll when the sample size, result values and others are
     * given with an implicit number of no responses.
     *
     * <code>•SS: 1250 A: 32 B: 24 C: 16 •O: 8</code>
     */
    @Test
    public void saporBodyCase05ShouldHandleSampleSizeResultValuesAndOthersWithImplicitNoResponses() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "32").addResult("B", "24").addResult("C", "16")
                .setOther("8").setSampleSize("1250").setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll when the sample size, the excluded, result values and
     * others are given with an implicit number of no responses.
     *
     * <code>•SS: 1250 •EX: 30 A: 32 B: 24 C: 16 •O: 8</code>
     */
    @Test
    public void saporBodyCase06ShouldHandleSampleSizeExcludedResultValuesAndOthersWithImplicitNoResponses() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "32").addResult("B", "24").addResult("C", "16")
                .setOther("8").setSampleSize("1250").setExcluded(DecimalNumber.parse("30")).setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll when the sample size, result values, others and no
     * responses are given.
     *
     * <code>•SS: 1250 A: 32 B: 24 C: 16 •O: 8 •N: 20</code>
     */
    @Test
    public void saporBodyCase07ShouldHandleSampleSizeResultValuesOthersAndNoResponses() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "32").addResult("B", "24").addResult("C", "16")
                .setOther("8").setNoResponses("20").setSampleSize("1250").setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll when the sample size, the excluded, result values,
     * others and no responses are given.
     *
     * <code>•SS: 1250 •EX: 30 A: 32 B: 24 C: 16 •O: 8 •N: 20</code>
     */
    @Test
    public void saporBodyCase08ShouldHandleSampleSizeExcludedResultValuesOthersAndNoResponses() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "32").addResult("B", "24").addResult("C", "16")
                .setOther("8").setNoResponses("20").setSampleSize("1250").setExcluded(DecimalNumber.parse("30"))
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll when the sample size, result values and no responses
     * are given.
     *
     * <code>•SS: 1250 A: 32 B: 24 C: 16 •N: 20</code>
     */
    @Test
    public void saporBodyCase09ShouldHandleSampleSizeResultValuesAndNoResponses() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "32").addResult("B", "24").addResult("C", "16")
                .setNoResponses("20").setSampleSize("1250").setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll when the sample size, the excluded, result values and
     * no responses are given.
     *
     * <code>•SS: 1250 •EX: 30 A: 32 B: 24 C: 16 •N: 20</code>
     */
    @Test
    public void saporBodyCase10ShouldHandleSampleSizeExcludedResultValuesAndNoResponses() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "32").addResult("B", "24").addResult("C", "16")
                .setNoResponses("20").setSampleSize("1250").setExcluded(DecimalNumber.parse("30"))
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll when the sample size, result values, others and no
     * responses are given but underflowing.
     *
     * <code>•SS: 1250 A: 8 B: 6 C: 4 •O: 2 •N: 5</code>
     */
    @Test
    public void saporBodyCase11ShouldHandleUnderflowingSampleSizeResultValuesOthersAndNoResponses() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "8").addResult("B", "6").addResult("C", "4")
                .setOther("2").setNoResponses("5").setSampleSize("1250").setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll when the sample size, the excluded, result values,
     * others and no responses are given but underflowing.
     *
     * <code>•SS: 1250 •EX: 30 A: 8 B: 6 C: 4 •O: 2 •N: 5</code>
     */
    @Test
    public void saporBodyCase12ShouldHandleUnderflowingSampleSizeExcludedResultValuesOthersAndNoResponses() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "8").addResult("B", "6").addResult("C", "4")
                .setOther("2").setNoResponses("5").setSampleSize("1250").setExcluded(DecimalNumber.parse("30"))
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll when the sample size, result values, others and no
     * responses are given but overflowing.
     *
     * <code>•SS: 1250 A: 40 B: 30 C: 20 •O: 10 •N: 25</code>
     */
    @Test
    public void saporBodyCase13ShouldHandleOverflowingSampleSizeResultValuesOthersAndNoResponses() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").addResult("C", "20")
                .setOther("10").setNoResponses("25").setSampleSize("1250").setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll when the sample size, the excluded, result values,
     * others and no responses are given but overflowing.
     *
     * <code>•SS: 1250 •EX: 30 A: 40 B: 30 C: 20 •O: 10 •N: 25</code>
     */
    @Test
    public void saporBodyCase14ShouldHandleOverflowingSampleSizeExcludedResultValuesOthersAndNoResponses() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").addResult("C", "20")
                .setOther("10").setNoResponses("25").setSampleSize("1250").setExcluded(DecimalNumber.parse("30"))
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll when the sample size, result values and others are
     * given but overflowing.
     *
     * <code>•SS: 1000 A: 80 B: 60 C: 40 •O: 20</code>
     */
    @Test
    public void saporBodyCase15ShouldHandleOverflowingSampleSizeResultValuesAndOthers() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "80").addResult("B", "60").addResult("C", "40")
                .setOther("20").setSampleSize("1000").setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll with a zero value.
     */
    @Test
    public void opinionPollWithZeroValueShouldProduceBodyCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "0").setSampleSize("1000")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=447\n");
        expected.append("Party A=550\n");
        expected.append("Party B=3");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll when the sample size is missing.
     */
    @Test
    public void opinionPollWithoutSampleSizeShouldProduceBodyCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=20\n");
        expected.append("Party A=550\n");
        expected.append("Party B=430");
        assertEquals(expected.toString(), getSortedSaporBody(poll, TWO_THOUSAND, THOUSAND));
    }

    /**
     * Verifies that the body can be created for an opinion poll when the sample size is missing but no responses is
     * present.
     */
    @Test
    public void opinionPollWithNoResponsesWithoutSampleSizeShouldProduceBodyCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "50").addResult("B", "20").setNoResponses("10")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=400\n");
        expected.append("Party A=1000\n");
        expected.append("Party B=400");
        assertEquals(expected.toString(), getSortedSaporBody(poll, TWO_THOUSAND, THOUSAND));
    }

    /**
     * Verifies that the body can be created for an opinion poll when the sample size is missing but excluded present.
     */
    @Test
    public void opinionPollWithExcludedWithoutSampleSizeShouldProduceBodyCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2)
                .setExcluded(DecimalNumber.parse("25")).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=30\n");
        expected.append("Party A=825\n");
        expected.append("Party B=645");
        assertEquals(expected.toString(), getSortedSaporBody(poll, TWO_THOUSAND, THOUSAND));
    }

    /**
     * Verifies that a mapping with a start date after the opinion poll's reference dated isn't used.
     */
    @Test
    public void mappingWithStartDateAfterOpinionPollShouldNotBeUsed() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").setSampleSize("1000")
                .setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH1).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=300\n");
        expected.append("Party A1=400\n");
        expected.append("Party B=300");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1, createShiftingSaporExporter()));
    }

    /**
     * Verifies that a mapping with an end date before the opinion poll's reference dated isn't used.
     */
    @Test
    public void mappingWithEndDateBeforeOpinionPollShouldNotBeUsed() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").setSampleSize("1000")
                .setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=300\n");
        expected.append("Party A2=400\n");
        expected.append("Party B=300");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1, createShiftingSaporExporter()));
    }

    /**
     * Verifies that an additive mapping is processed correctly.
     */
    @Test
    public void additiveMappingShouldBeProcessedCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").addResult("C", "20")
                .setSampleSize("1000").setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Alliance B+C=500\n");
        expected.append("Other=100\n");
        expected.append("Party A=400");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1, createAdditiveSaporExporter()));
    }

    /**
     * Verifies that an additive mapping requiring scaling is processed correctly.
     */
    @Test
    public void additiveMappingWithScalingShouldBeProcessedCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "400").addResult("B", "300")
                .addResult("C", "200").setOther("100").setSampleSize("1000").setPollingFirm("ACME")
                .setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Alliance B+C=500\n");
        expected.append("Other=100\n");
        expected.append("Party A=400");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1, createAdditiveSaporExporter()));
    }

    /**
     * Verifies that the entire content is produced correctly.
     */
    @Test
    public void opinionPollShouldProduceEntireContent() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setSampleSize("1000")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
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
        assertTrue(
                Set.of(expected1.toString(), expected2.toString()).contains(saporExporter.getSaporContent(poll, 1, 1)));
    }

    /**
     * Verifies that the file path is produced correctly.
     */
    @Test
    public void opinionPollShouldProduceFilePath() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setSampleSize("1000")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        assertEquals(Paths.get("2021-08-02-ACME.poll"), saporExporter.getSaporFilePath(poll));
    }

    /**
     * Verifies that the entire directory is produced correctly.
     */
    @Test
    public void opinionPollsShouldProduceDirectory() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setSampleSize("1000")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        OpinionPoll poll2 = new OpinionPollTestBuilder().addResult("A", "55").addResult("Z", "43").setSampleSize("1000")
                .setPollingFirm("BCME").setFieldworkStart(DATE_OR_MONTH3).setFieldworkEnd(DATE_OR_MONTH4).build();
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
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setSampleSize("1000")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        assertTrue(saporExporter.getSaporWarnings(poll).isEmpty());
    }

    /**
     * Verifies that a warning about a missing mapping is issued.
     */
    @Test
    public void opinionPollShouldProduceMissingSaporMappingWarning() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("Z", "43").setSampleSize("1000")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        assertEquals(Set.of(new MissingSaporMappingWarning(Set.of(ElectoralList.get("Z")))),
                saporExporter.getSaporWarnings(poll));
    }

    /**
     * Verifies that no warning about a missing mapping is issued over an electoral list covered by an additive mapping.
     */
    @Test
    public void noWarningAboutMissingMappingShouldBeIssuedOverElectoralListCoveredByAdditiveMapping() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("B", "55").addResult("C", "43").setSampleSize("1000")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        assertTrue(createAdditiveSaporExporter().getSaporWarnings(poll).isEmpty());
    }

    /**
     * Verifies that the export produces warnings.
     */
    @Test
    public void opinionPollsShouldProduceWarnings() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().addResult("A", "55").addResult("Q", "43").setSampleSize("1000")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        OpinionPoll poll2 = new OpinionPollTestBuilder().addResult("A", "55").addResult("Z", "43").setSampleSize("1000")
                .setPollingFirm("BCME").setFieldworkStart(DATE_OR_MONTH3).setFieldworkEnd(DATE_OR_MONTH4).build();
        OpinionPolls polls = new OpinionPolls(Set.of(poll1, poll2));
        SaporDirectory saporDirectory = saporExporter.export(polls);
        assertEquals(Set.of(new MissingSaporMappingWarning(Set.of(ElectoralList.get("Q"))),
                new MissingSaporMappingWarning(Set.of(ElectoralList.get("Z")))), saporDirectory.getWarnings());
    }
}
