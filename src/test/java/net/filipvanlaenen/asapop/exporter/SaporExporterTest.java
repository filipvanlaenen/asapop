package net.filipvanlaenen.asapop.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.LaconicConfigurator;
import net.filipvanlaenen.asapop.model.DateMonthOrYear;
import net.filipvanlaenen.asapop.model.DecimalNumber;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPollTestBuilder;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.ResponseScenarioTestBuilder;
import net.filipvanlaenen.asapop.model.Scope;
import net.filipvanlaenen.asapop.model.Unit;
import net.filipvanlaenen.asapop.yaml.AdditiveSaporMapping;
import net.filipvanlaenen.asapop.yaml.AdditiveSplittingSaporMapping;
import net.filipvanlaenen.asapop.yaml.DirectSaporMapping;
import net.filipvanlaenen.asapop.yaml.EssentialEntriesSaporMapping;
import net.filipvanlaenen.asapop.yaml.SaporConfiguration;
import net.filipvanlaenen.asapop.yaml.SaporMapping;
import net.filipvanlaenen.asapop.yaml.SplittingSaporMapping;
import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.laconic.Laconic;
import net.filipvanlaenen.laconic.Token;

/**
 * Unit tests on the <code>SaporExporter</code> class.
 */
public class SaporExporterTest {
    /**
     * The magic number one half.
     */
    private static final double ONE_HALF = 0.5D;
    /**
     * The magic number thousand.
     */
    private static final int THOUSAND = 1000;
    /**
     * The magic number two thousand.
     */
    private static final int TWO_THOUSAND = 2000;
    /**
     * The date used as the last election date in the Sapor configuration.
     */
    private static final String LAST_ELECTION_DATE = "2020-12-06";
    /**
     * A date for the unit tests.
     */
    private static final LocalDate DATE = LocalDate.parse("2021-08-02");
    /**
     * A date or month for the unit tests.
     */
    private static final DateMonthOrYear DATE_OR_MONTH0 = DateMonthOrYear.parse("2020-05-01");
    /**
     * A date or month for the unit tests.
     */
    private static final DateMonthOrYear DATE_OR_MONTH1 = DateMonthOrYear.parse("2021-08-01");
    /**
     * Another date or month for the unit tests.
     */
    private static final DateMonthOrYear DATE_OR_MONTH2 = DateMonthOrYear.parse("2021-08-02");
    /**
     * A third date or month for the unit tests.
     */
    private static final DateMonthOrYear DATE_OR_MONTH3 = DateMonthOrYear.parse("2021-09-01");
    /**
     * A fourth date or month for the unit tests.
     */
    private static final DateMonthOrYear DATE_OR_MONTH4 = DateMonthOrYear.parse("2021-09-02");
    /**
     * A Laconic logging token for unit testing.
     */
    private static final Token TOKEN1 = Laconic.LOGGER.logMessage("Unit test SaporExporterTest (1).");
    /**
     * A Laconic logging token for unit testing.
     */
    private static final Token TOKEN2 = Laconic.LOGGER.logMessage("Unit test SaporExporterTest (2).");
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
        return createDirectSaporMapping(source, target, null, null, null);
    }

    /**
     * Creates a direct SAPOR mapping.
     *
     * @param source             The source for the direct SAPOR mapping.
     * @param target             The target for the direct SAPOR mapping.
     * @param compensationFactor The compensation factor for the direct SAPOR mapping.
     * @return A SAPOR mapping with a direct mapping from the source to the target.
     */
    private static SaporMapping createDirectSaporMapping(final String source, final String target,
            final Double compensationFactor) {
        return createDirectSaporMapping(source, target, null, null, compensationFactor);
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
        return createDirectSaporMapping(source, target, startDate, endDate, null);
    }

    /**
     * Creates a direct SAPOR mapping.
     *
     * @param source             The source for the direct SAPOR mapping.
     * @param target             The target for the direct SAPOR mapping.
     * @param startDate          The start date for the direct SAPOR mapping.
     * @param endDate            The end date for the direct SAPOR mapping.
     * @param compensationFactor The compensation factor for the direct SAPOR mapping.
     * @return A SAPOR mapping with a direct mapping from the source to the target.
     */
    private static SaporMapping createDirectSaporMapping(final String source, final String target,
            final String startDate, final String endDate, final Double compensationFactor) {
        SaporMapping saporMapping = new SaporMapping();
        DirectSaporMapping directSaporMapping = new DirectSaporMapping();
        directSaporMapping.setSource(source);
        directSaporMapping.setTarget(target);
        directSaporMapping.setCompensationFactor(compensationFactor);
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
     * Creates a splitting SAPOR mapping.
     *
     * @param source  The source for the splitting SAPOR mapping.
     * @param targets The targets with weights for the splitting SAPOR mapping.
     * @return A SAPOR mapping with a splitting SAPOR mapping from the source to the targets.
     */
    private static SaporMapping createSplittingSaporMapping(final String source, final Map<String, Integer> targets) {
        SaporMapping saporMapping = new SaporMapping();
        SplittingSaporMapping splittingSaporMapping = new SplittingSaporMapping();
        splittingSaporMapping.setSource(source);
        splittingSaporMapping.setTargets(targets);
        saporMapping.setSplittingMapping(splittingSaporMapping);
        return saporMapping;
    }

    /**
     * Creates an additive splitting SAPOR mapping.
     *
     * @param sources The sources for the additive splitting SAPOR mapping.
     * @param targets The targets with weights for the additive splitting SAPOR mapping.
     * @return A SAPOR mapping with a splitting SAPOR mapping from the source to the targets.
     */
    private static SaporMapping createAdditiveSplittingSaporMapping(final Set<String> sources,
            final Map<String, Integer> targets) {
        SaporMapping saporMapping = new SaporMapping();
        AdditiveSplittingSaporMapping additiveSplittingSaporMapping = new AdditiveSplittingSaporMapping();
        additiveSplittingSaporMapping.setSources(sources);
        additiveSplittingSaporMapping.setTargets(targets);
        saporMapping.setAdditiveSplittingMapping(additiveSplittingSaporMapping);
        return saporMapping;
    }

    /**
     * Creates an essential entries SAPOR mapping.
     *
     * @param residual The residual weight for the essential entries SAPOR mapping.
     * @param targets  The targets with weights for the essential entries SAPOR mapping.
     * @return A SAPOR mapping with an essential entries SAPOR mapping.
     */
    private static SaporMapping createEssentialEntriesSaporMapping(final Integer residual,
            final Map<String, Integer> targets) {
        SaporMapping saporMapping = new SaporMapping();
        EssentialEntriesSaporMapping essentialEntriesSaporMapping = new EssentialEntriesSaporMapping();
        essentialEntriesSaporMapping.setResidual(residual);
        essentialEntriesSaporMapping.setTargets(targets);
        saporMapping.setEssentialEntriesMapping(essentialEntriesSaporMapping);
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
        saporConfiguration.setLastElectionDate(LAST_ELECTION_DATE);
        saporConfiguration.setMapping(Set.of(createDirectSaporMapping("A", "Party A"),
                createDirectSaporMapping("B", "Party B"), createDirectSaporMapping("C", "Party C")));
        saporConfiguration.setArea("AR");
        saporConfiguration.setScope("N");
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
        saporConfiguration.setLastElectionDate(LAST_ELECTION_DATE);
        saporConfiguration.setMapping(Set.of(createDirectSaporMapping("A", "Party A1", null, "2021-08-01"),
                createDirectSaporMapping("A", "Party A2", "2021-08-02", null),
                createDirectSaporMapping("B", "Party B")));
        saporConfiguration.setArea("AR");
        return new SaporExporter(saporConfiguration);
    }

    /**
     * Creates a SAPOR exporter with a compensation factor.
     *
     * @return A SAPOR exporter with a compensation factor.
     */
    private static SaporExporter createSaporExporterWithCompensationFactor() {
        SaporConfiguration saporConfiguration = new SaporConfiguration();
        saporConfiguration.setLastElectionDate(LAST_ELECTION_DATE);
        saporConfiguration.setMapping(Set.of(createDirectSaporMapping("A", "Party A"),
                createDirectSaporMapping("B", "Party B"), createDirectSaporMapping("C", "Party C", ONE_HALF)));
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
        saporConfiguration.setLastElectionDate(LAST_ELECTION_DATE);
        saporConfiguration.setMapping(Set.of(createDirectSaporMapping("A", "Party A"),
                createAdditiveSaporMapping(Set.of("B", "C"), "Alliance B+C"),
                createAdditiveSaporMapping(Set.of("D", "E"), "Alliance D+E")));
        saporConfiguration.setArea("AR");
        return new SaporExporter(saporConfiguration);
    }

    /**
     * Creates a SAPOR exporter with an essential entries mapping.
     *
     * @return A SAPOR exporter with an essential entries mapping.
     */
    private static SaporExporter createEssentialEntriesSaporExporter() {
        SaporConfiguration saporConfiguration = new SaporConfiguration();
        saporConfiguration.setLastElectionDate(LAST_ELECTION_DATE);
        saporConfiguration
                .setMapping(Set.of(createDirectSaporMapping("A", "Party A"), createDirectSaporMapping("B", "Party B"),
                        createEssentialEntriesSaporMapping(1, Map.of("Party B", 1, "Party C", 1, "Party D", 2))));
        saporConfiguration.setArea("AR");
        return new SaporExporter(saporConfiguration);
    }

    /**
     * Creates a SAPOR exporter with a splitting mapping.
     *
     * @return A SAPOR exporter with a splitting mapping.
     */
    private static SaporExporter createSplittingSaporExporter() {
        SaporConfiguration saporConfiguration = new SaporConfiguration();
        saporConfiguration.setLastElectionDate(LAST_ELECTION_DATE);
        saporConfiguration.setMapping(Set.of(createDirectSaporMapping("A", "Party A"),
                createSplittingSaporMapping("B+C", Map.of("Party B", 2, "Party C", 1)),
                createSplittingSaporMapping("D+E", Map.of("Party D", 2, "Party E", 1))));
        saporConfiguration.setArea("AR");
        return new SaporExporter(saporConfiguration);
    }

    /**
     * Creates a SAPOR exporter with an additive splitting mapping.
     *
     * @return A SAPOR exporter with an additive splitting mapping.
     */
    private static SaporExporter createAdditiveSplittingSaporExporter() {
        SaporConfiguration saporConfiguration = new SaporConfiguration();
        saporConfiguration.setLastElectionDate(LAST_ELECTION_DATE);
        saporConfiguration.setMapping(Set.of(createDirectSaporMapping("A", "Party A"),
                createAdditiveSplittingSaporMapping(Set.of("B", "C"), Map.of("Party B", 2, "Party C", 1)),
                createAdditiveSplittingSaporMapping(Set.of("D", "E"), Map.of("Party D", 2, "Party E", 1))));
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
                .setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll given in numbers of seats when the sample size and
     * result values are given.
     *
     * <code>•U: S •SS: 1000 A: 40 B: 30 C: 20</code>
     */
    @Test
    public void saporBodyCaseU01ShouldHandleSampleSizeAndResultValues() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").addResult("C", "20")
                .setSampleSize("1000").setUnit(Unit.SEATS).setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1)
                .setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=1\n");
        expected.append("Party A=444\n");
        expected.append("Party B=333\n");
        expected.append("Party C=222");
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
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll given in numbers of seats when the sample size, the
     * excluded and result values are given.
     *
     * <code>•U: S •SS: 1250 •EX: 20 A: 40 B: 30 C: 20</code>
     */
    @Test
    public void saporBodyCaseU02ShouldHandleSampleSizeExcludedAndResultValues() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").addResult("C", "20")
                .setSampleSize("1250").setUnit(Unit.SEATS).setExcluded(DecimalNumber.parse("20")).setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=1\n");
        expected.append("Party A=444\n");
        expected.append("Party B=333\n");
        expected.append("Party C=222");
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
                .setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll given in numbers of seats when the sample size, result
     * values and others are given.
     *
     * <code>•U: S •SS: 1000 A: 40 B: 30 C: 20 •O: 10</code>
     */
    @Test
    public void saporBodyCaseU03ShouldHandleSampleSizeResultValuesAndOthers() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").addResult("C", "20")
                .setOther("10").setSampleSize("1000").setUnit(Unit.SEATS).setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
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
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll given in numbers of seats when the sample size, the
     * excluded, result values and others are given.
     *
     * <code>•U: S •SS: 1250 •EX: 20 A: 40 B: 30 C: 20 •O: 10</code>
     */
    @Test
    public void saporBodyCaseU04ShouldHandleSampleSizeExcludedResultValuesAndOthers() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").addResult("C", "20")
                .setOther("10").setSampleSize("1250").setUnit(Unit.SEATS).setExcluded(DecimalNumber.parse("20"))
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2)
                .setScope(Scope.NATIONAL).build();
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
                .setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll given in numbers of seats when the sample size, result
     * values and others are less than one hundred.
     *
     * <code>•U: S •SS: 1250 A: 32 B: 24 C: 16 •O: 8</code>
     */
    @Test
    public void saporBodyCaseU05ShouldHandleSampleSizeResultValuesAndOthersLessThanOneHundred() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "32").addResult("B", "24").addResult("C", "16")
                .setOther("8").setSampleSize("1250").setUnit(Unit.SEATS).setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=125\n");
        expected.append("Party A=500\n");
        expected.append("Party B=375\n");
        expected.append("Party C=250");
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
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll in numbers of seats when the sample size, the excluded,
     * result values and others are less than one hundred.
     *
     * <code>•U: S •SS: 1250 •EX: 30 A: 32 B: 24 C: 16 •O: 8</code>
     */
    @Test
    public void saporBodyCaseU06ShouldHandleSampleSizeExcludedResultValuesAndOthersLessThanOneHundred() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "32").addResult("B", "24").addResult("C", "16")
                .setOther("8").setSampleSize("1250").setUnit(Unit.SEATS).setExcluded(DecimalNumber.parse("30"))
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2)
                .setScope(Scope.NATIONAL).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=87\n");
        expected.append("Party A=350\n");
        expected.append("Party B=263\n");
        expected.append("Party C=175");
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
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
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
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2)
                .setScope(Scope.NATIONAL).build();
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
                .setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
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
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2)
                .setScope(Scope.NATIONAL).build();
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
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
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
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2)
                .setScope(Scope.NATIONAL).build();
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
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
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
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2)
                .setScope(Scope.NATIONAL).build();
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
                .setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=200");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1));
    }

    /**
     * Verifies that the body can be created for an opinion poll given in number of seats when the sample size, result
     * values and others are greater than one hundred.
     *
     * <code>•U: S •SS: 1000 A: 80 B: 60 C: 40 •O: 20</code>
     */
    @Test
    public void saporBodyCaseU15ShouldHandleSampleSizeResultValuesAndOthersGreaterThanOneHundred() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "80").addResult("B", "60").addResult("C", "40")
                .setOther("20").setSampleSize("1000").setUnit(Unit.SEATS).setPollingFirm("ACME")
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
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
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2)
                .setScope(Scope.NATIONAL).build();
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
                .setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
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
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2)
                .setScope(Scope.NATIONAL).build();
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
                .setExcluded(DecimalNumber.parse("25")).setScope(Scope.NATIONAL).build();
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
                .setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH1).setScope(Scope.NATIONAL).build();
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
                .setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=300\n");
        expected.append("Party A2=400\n");
        expected.append("Party B=300");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1, createShiftingSaporExporter()));
    }

    /**
     * Verifies that a mapping with a compensation factor is processed correctly.
     */
    @Test
    public void mappingWithCompensationFactorShouldBeProcessedCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").addResult("C", "20")
                .setSampleSize("1000").setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=100");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1, createSaporExporterWithCompensationFactor()));
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
     * Verifies that a splitting mapping is processed correctly.
     */
    @Test
    public void splittingMappingShouldBeProcessedCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "C", "50")
                .setSampleSize("1000").setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=333\n");
        expected.append("Party C=167");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1, createSplittingSaporExporter()));
    }

    /**
     * Verifies that a splitting mapping requiring scaling is processed correctly.
     */
    @Test
    public void splittingMappingWithScalingShouldBeProcessedCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "400").addResult("B", "C", "500").setOther("100")
                .setSampleSize("1000").setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=100\n");
        expected.append("Party A=400\n");
        expected.append("Party B=333\n");
        expected.append("Party C=167");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1, createSplittingSaporExporter()));
    }

    /**
     * Verifies that an additive splitting mapping is processed correctly.
     */
    @Test
    public void additiveSplittingMappingShouldBeProcessedCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "15").addResult("C", "15")
                .setSampleSize("1000").setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=300\n");
        expected.append("Party A=400\n");
        expected.append("Party B=200\n");
        expected.append("Party C=100");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1, createAdditiveSplittingSaporExporter()));
    }

    /**
     * Verifies that an additive splitting mapping requiring scaling is processed correctly.
     */
    @Test
    public void additiveSlittingMappingWithScalingShouldBeProcessedCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "400").addResult("B", "150")
                .addResult("C", "150").setOther("300").setSampleSize("1000").setPollingFirm("ACME")
                .setFieldworkEnd(DATE_OR_MONTH2).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=300\n");
        expected.append("Party A=400\n");
        expected.append("Party B=200\n");
        expected.append("Party C=100");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1, createAdditiveSplittingSaporExporter()));
    }

    /**
     * Verifies that a mapping with essential entries is processed correctly.
     */
    @Test
    public void essentialEntriesMappingShouldBeProcessedCorrectly() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "40").addResult("B", "30").setSampleSize("1000")
                .setPollingFirm("ACME").setFieldworkEnd(DATE_OR_MONTH2).setScope(Scope.NATIONAL).build();
        StringBuilder expected = new StringBuilder();
        expected.append("Other=75\n");
        expected.append("Party A=400\n");
        expected.append("Party B=300\n");
        expected.append("Party C=75\n");
        expected.append("Party D=150");
        assertEquals(expected.toString(), getSortedSaporBody(poll, 1, 1, createEssentialEntriesSaporExporter()));
    }

    /**
     * Verifies that the entire content is produced correctly.
     */
    @Test
    public void opinionPollShouldProduceEntireContent() {
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setSampleSize("1000")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2)
                .setScope(Scope.NATIONAL).build();
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
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2)
                .setScope(Scope.NATIONAL).build();
        assertEquals(Paths.get("2021-08-02-ACME.poll"), saporExporter.getSaporFilePath(poll));
    }

    /**
     * Verifies that the entire directory is produced correctly.
     */
    @Test
    public void opinionPollsShouldProduceDirectory() {
        OpinionPoll poll1 = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setSampleSize("1000")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2)
                .setScope(Scope.NATIONAL).build();
        OpinionPoll poll2 = new OpinionPollTestBuilder().addResult("A", "54").addResult("Z", "42").setSampleSize("1000")
                .setPollingFirm("BCME").setFieldworkStart(DATE_OR_MONTH3).setFieldworkEnd(DATE_OR_MONTH4)
                .setScope(Scope.EUROPEAN).build();
        ResponseScenario responseScenario = new ResponseScenarioTestBuilder().addResult("A", "55").addResult("Z", "43")
                .setSampleSize("1000").setScope(Scope.NATIONAL).build();
        poll2.addAlternativeResponseScenario(responseScenario);
        OpinionPoll poll3 = new OpinionPollTestBuilder().addResult("A", "55").addResult("Z", "43").setSampleSize("1000")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH0).setFieldworkEnd(DATE_OR_MONTH0)
                .setScope(Scope.NATIONAL).build();
        OpinionPoll poll4 = new OpinionPollTestBuilder().addResult("A", "55").addResult("Z", "43").setSampleSize("1000")
                .setPollingFirm("CCME").setFieldworkStart(DATE_OR_MONTH3).setFieldworkEnd(DATE_OR_MONTH4)
                .setScope(Scope.EUROPEAN).build();
        OpinionPolls polls = new OpinionPolls(Set.of(poll1, poll2, poll3, poll4));
        SaporDirectory saporDirectory = saporExporter.export(polls, TOKEN1, TOKEN2);
        assertEquals(2, saporDirectory.asMap().size());
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
    public void opinionPollShouldNotLogAnError() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token1 = Laconic.LOGGER.logMessage("Unit test SaporExporterTest.opinionPollShouldNotLogAnError (1).");
        Token token2 = Laconic.LOGGER.logMessage("Unit test SaporExporterTest.opinionPollShouldNotLogAnError (2).");
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("B", "43").setSampleSize("1000")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        saporExporter.checkSaporMappings(poll, token1, token2);
        assertTrue(outputStream.toString().isEmpty());
    }

    /**
     * Verifies that a warning about a missing mapping is issued.
     */
    @Test
    public void opinionPollShouldLogAnError() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token1 = Laconic.LOGGER.logMessage("Unit test SaporExporterTest.opinionPollShouldLogAnError (1).");
        Token token2 = Laconic.LOGGER.logMessage("Unit test SaporExporterTest.opinionPollShouldLogAnError (2).");
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("A", "55").addResult("Z", "43").setSampleSize("1000")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        saporExporter.checkSaporMappings(poll, token1, token2);
        String expected = "‡ ⬐ Unit test SaporExporterTest.opinionPollShouldLogAnError (1).\n"
                + "‡ ⬐ Unit test SaporExporterTest.opinionPollShouldLogAnError (2).\n"
                + "‡ SAPOR mapping missing for Z.\n";
        assertEquals(expected, outputStream.toString());
    }

    /**
     * Verifies that no warning about a missing mapping is issued over an electoral list covered by an additive mapping.
     */
    @Test
    public void shouldNotLogErrorOverElectoralListCoveredByAdditiveMapping() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token1 = Laconic.LOGGER.logMessage(
                "Unit test SaporExporterTest.shouldNotLogErrorOverElectoralListCoveredByAdditiveMapping (1).");
        Token token2 = Laconic.LOGGER.logMessage(
                "Unit test SaporExporterTest.shouldNotLogErrorOverElectoralListCoveredByAdditiveMapping (2).");
        OpinionPoll poll = new OpinionPollTestBuilder().addResult("B", "55").addResult("C", "43").setSampleSize("1000")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2).build();
        createAdditiveSaporExporter().checkSaporMappings(poll, token1, token2);
        assertTrue(outputStream.toString().isEmpty());
    }

    /**
     * Verifies that the export produces warnings.
     */
    @Test
    public void opinionPollsShouldLogErrors() {
        ByteArrayOutputStream outputStream = LaconicConfigurator.resetLaconicOutputStream();
        Token token1 = Laconic.LOGGER.logMessage("Unit test SaporExporterTest.opinionPollsShouldLogErrors (1).");
        Token token2 = Laconic.LOGGER.logMessage("Unit test SaporExporterTest.opinionPollsShouldLogErrors (2).");
        OpinionPoll poll1 = new OpinionPollTestBuilder().addResult("A", "55").addResult("Q", "43").setSampleSize("1000")
                .setPollingFirm("ACME").setFieldworkStart(DATE_OR_MONTH1).setFieldworkEnd(DATE_OR_MONTH2)
                .setScope(Scope.NATIONAL).build();
        OpinionPoll poll2 = new OpinionPollTestBuilder().addResult("A", "55").addResult("Z", "43").setSampleSize("1000")
                .setPollingFirm("BCME").setFieldworkStart(DATE_OR_MONTH3).setFieldworkEnd(DATE_OR_MONTH4)
                .setScope(Scope.NATIONAL).build();
        OpinionPolls polls = new OpinionPolls(Set.of(poll1, poll2));
        saporExporter.export(polls, token1, token2);
        String errorQ = "‡ ⬐ Unit test SaporExporterTest.opinionPollsShouldLogErrors (1).\n"
                + "‡ ⬐ Unit test SaporExporterTest.opinionPollsShouldLogErrors (2).\n"
                + "‡ SAPOR mapping missing for Q.\n";
        String errorZ = "‡ ⬐ Unit test SaporExporterTest.opinionPollsShouldLogErrors (1).\n"
                + "‡ ⬐ Unit test SaporExporterTest.opinionPollsShouldLogErrors (2).\n"
                + "‡ SAPOR mapping missing for Z.\n";
        assertTrue(Collection.of(errorQ + "\n" + errorZ, errorZ + "\n" + errorQ).contains(outputStream.toString()));
    }
}
