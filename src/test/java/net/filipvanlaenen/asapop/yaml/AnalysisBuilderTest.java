package net.filipvanlaenen.asapop.yaml;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.analysis.AnalysisEngine;
import net.filipvanlaenen.asapop.model.DateMonthOrYear;
import net.filipvanlaenen.asapop.model.ElectoralList;
import net.filipvanlaenen.asapop.model.OpinionPoll;
import net.filipvanlaenen.asapop.model.OpinionPollTestBuilder;
import net.filipvanlaenen.asapop.model.OpinionPolls;
import net.filipvanlaenen.asapop.model.ResponseScenario;
import net.filipvanlaenen.asapop.model.ResultValue;
import net.filipvanlaenen.asapop.model.Scope;

/**
 * Unit tests on the <code>AnalysisBuilder</code> class.
 */
public class AnalysisBuilderTest {
    /**
     * The magic number two thousand.
     */
    private static final long ONE_THOUSAND = 1000L;
    /**
     * The magic number eighty.
     */
    private static final int EIGHTY = 80;
    /**
     * Precision for floating point assertions.
     */
    private static final double DELTA = 0.01D;
    /**
     * Precision for floating point assertions.
     */
    private static final double LARGE_DELTA = 1D;
    /**
     * The magic number 50.
     */
    private static final double FLOAT_50 = 50D;
    /**
     * The magic number 0.60.
     */
    private static final double FLOAT_0_60 = 0.60D;
    /**
     * The magic number 1.04.
     */
    private static final double FLOAT_1_04 = 1.04D;
    /**
     * The magic number 1.69.
     */
    private static final double FLOAT_1_69 = 1.69D;
    /**
     * The area.
     */
    private static final String AREA = "AR";
    /**
     * The alternative area.
     */
    private static final String ALTERNATIVE_AREA = "BR";
    /**
     * The name of the commissioner.
     */
    private static final String COMMISSIONER_NAME = "The Times";
    /**
     * Electoral list AA001.
     */
    private static final Set<ElectoralList> ELECTORAL_LIST_AA001 = Set.of(ElectoralList.get("AA001"));
    /**
     * Electoral list AA002.
     */
    private static final Set<ElectoralList> ELECTORAL_LIST_AA002 = Set.of(ElectoralList.get("AA002"));
    /**
     * The first fieldwork end date.
     */
    private static final DateMonthOrYear FIELDWORK_END = DateMonthOrYear.parse("2022-01-19");
    /**
     * The fieldwork start date.
     */
    private static final DateMonthOrYear FIELDWORK_START = DateMonthOrYear.parse("2022-01-18");
    /**
     * The name of the polling firm for an opinion poll about a parliamentary election.
     */
    private static final String POLLING_FIRM_NAME_PARLIAMENT = "ACME-N";
    /**
     * The name of the polling firm for an opinion poll about a presidential election.
     */
    private static final String POLLING_FIRM_NAME_PRESIDENTIAL = "ACME-P";
    /**
     * The name of the polling firm partner.
     */
    private static final String POLLING_FIRM_PARTNER_NAME = "BCME";
    /**
     * The publication date.
     */
    private static final LocalDate PUBLICATION_DATE = LocalDate.parse("2022-01-20");
    /**
     * The size of the population (the number of voters for the first round of the French presidential election of
     * 2017).
     */
    private static final long POPULATION_SIZE = 10_001L;
    /**
     * An Analysis object to run the tests on.
     */
    private static Analysis analysis;
    /**
     * An OpinionPollAnalysis object to run the tests on.
     */
    private static OpinionPollAnalysis opinionPollAnalysis;
    /**
     * A main ResponseScenarioAnalysis object to run the tests on.
     */
    private static ResponseScenarioAnalysis mainResponseScenarioAnalysis;
    /**
     * An alternative ResponseScenarioAnalysis object to run the tests on.
     */
    private static ResponseScenarioAnalysis alternativeResponseScenarioAnalysis;
    /**
     * A ResultAnalysis object for an electoral list to run the tests on.
     */
    private static ResultAnalysis resultAnalysis;
    /**
     * A FirstRoundAnalysis object to run the tests on.
     */
    private static FirstRoundAnalysis firstRoundAnalysis;

    /**
     * Initializes the test objects.
     */
    @BeforeAll
    public static void createAnalysisObject() {
        Set<OpinionPoll> opinionPollSet = new HashSet<OpinionPoll>();
        OpinionPoll opinionPoll = new OpinionPollTestBuilder().setSampleSize("1000")
                .setPollingFirm(POLLING_FIRM_NAME_PARLIAMENT).setPollingFirmPartner(POLLING_FIRM_PARTNER_NAME)
                .addCommissioner(COMMISSIONER_NAME).setFieldworkStart(FIELDWORK_START).setFieldworkEnd(FIELDWORK_END)
                .setPublicationDate(PUBLICATION_DATE).setArea(AREA).setScope(Scope.NATIONAL)
                .addResult(ELECTORAL_LIST_AA001, new ResultValue("1")).setOther(new ResultValue("2")).build();
        opinionPoll.addAlternativeResponseScenario(
                new ResponseScenario.Builder().setArea(ALTERNATIVE_AREA).setScope(Scope.EUROPEAN).build());
        opinionPollSet.add(opinionPoll);
        opinionPoll = new OpinionPollTestBuilder().setSampleSize("1000").setPollingFirm(POLLING_FIRM_NAME_PRESIDENTIAL)
                .setPollingFirmPartner(POLLING_FIRM_PARTNER_NAME).addCommissioner(COMMISSIONER_NAME)
                .setFieldworkStart(FIELDWORK_START).setFieldworkEnd(FIELDWORK_END).setPublicationDate(PUBLICATION_DATE)
                .setScope(Scope.PRESIDENTIAL_FIRST_ROUND).setArea(AREA)
                .addResult(ELECTORAL_LIST_AA001, new ResultValue("50"))
                .addResult(ELECTORAL_LIST_AA002, new ResultValue("30")).build();
        opinionPollSet.add(opinionPoll);
        OpinionPolls opinionPolls = new OpinionPolls(opinionPollSet);
        ElectionData electionData = new ElectionData();
        electionData.setPopulationSize(POPULATION_SIZE);
        AnalysisEngine engine = new AnalysisEngine(opinionPolls, electionData);
        engine.run(ONE_THOUSAND, ONE_THOUSAND);
        AnalysisBuilder builder = new AnalysisBuilder(engine);
        analysis = builder.build();
        if (analysis != null && analysis.getOpinionPollAnalyses() != null) {
            for (OpinionPollAnalysis opa : analysis.getOpinionPollAnalyses()) {
                if (opa != null && opa.getPollingFirm() != null) {
                    if (opa.getPollingFirm().equals(POLLING_FIRM_NAME_PARLIAMENT)) {
                        opinionPollAnalysis = opa;
                        if (opinionPollAnalysis != null && opinionPollAnalysis.getResponseScenarioAnalyses() != null) {
                            Iterator<ResponseScenarioAnalysis> rsai =
                                    opinionPollAnalysis.getResponseScenarioAnalyses().iterator();
                            ResponseScenarioAnalysis rsa = rsai.next();
                            if (rsa != null) {
                                if (Scope.NATIONAL.toString().equals(rsa.getScope())) {
                                    mainResponseScenarioAnalysis = rsa;
                                    alternativeResponseScenarioAnalysis = rsai.next();
                                } else {
                                    alternativeResponseScenarioAnalysis = rsa;
                                    mainResponseScenarioAnalysis = rsai.next();
                                }
                                resultAnalysis = mainResponseScenarioAnalysis.getResultAnalyses().get(Set.of("AA001"));
                            }
                        }
                    } else if (opa.getPollingFirm().equals(POLLING_FIRM_NAME_PRESIDENTIAL)
                            && opa.getResponseScenarioAnalyses() != null) {
                        ResponseScenarioAnalysis rsa = opa.getResponseScenarioAnalyses().iterator().next();
                        if (rsa != null) {
                            firstRoundAnalysis = rsa.getFirstRoundAnalysis();
                        }
                    }
                }
            }
        }
    }

    /**
     * Verifies that an analysis object is built by the builder.
     */
    @Test
    public void analysisShouldBeNotNull() {
        assertNotNull(analysis);
    }

    /**
     * Verifies that the set with opinion poll analyses objects is built by the builder.
     */
    @Test
    public void opinionPollAnalysesShouldBeNotNull() {
        assertNotNull(analysis.getOpinionPollAnalyses());
    }

    /**
     * Verifies that an opinion poll analysis object is built by the builder.
     */
    @Test
    public void opinionPollAnalysisShouldBeNotNull() {
        assertNotNull(opinionPollAnalysis);
    }

    /**
     * Verifies that a response scenario analysis object is built by the builder.
     */
    @Test
    public void responseScenarioAnalysisShouldBeNotNull() {
        assertNotNull(mainResponseScenarioAnalysis);
    }

    /**
     * Verifies that a result analysis object is built by the builder.
     */
    @Test
    public void resultAnalysisShouldBeNotNull() {
        assertNotNull(resultAnalysis);
    }

    /**
     * Verifies that a first round analysis object is built by the builder.
     */
    @Test
    public void firstRoundAnalysisShouldBeNotNull() {
        assertNotNull(firstRoundAnalysis);
    }

    /**
     * Verifies that the median for an electoral list is set.
     */
    @Test
    public void resultAnalysisShouldContainTheMedianForTheElectoralList() {
        assertEquals(FLOAT_1_04, resultAnalysis.getMedian(), DELTA);
    }

    /**
     * Verifies that the 80 percent confidence interval for an electoral list is set.
     */
    @Test
    public void resultAnalysisShouldContainThe80PercentConfidenceIntervalForTheElectoralList() {
        assertEquals(FLOAT_0_60, resultAnalysis.getConfidenceIntervals().get(EIGHTY)[0], DELTA);
        assertEquals(FLOAT_1_69, resultAnalysis.getConfidenceIntervals().get(EIGHTY)[1], DELTA);
    }

    /**
     * Verifies that the analysis builder sets the area of an opinion poll.
     */
    @Test
    public void buildingAnAnalysisShouldSetTheAreaOfAnOpinionPoll() {
        assertNotNull(mainResponseScenarioAnalysis);
        assertEquals(AREA, mainResponseScenarioAnalysis.getArea());
    }

    /**
     * Verifies that the analysis builder sets the area of an alternative response scenario.
     */
    @Test
    public void buildingAnAnalysisShouldSetTheAreaOfAnAlternativeResponseScenario() {
        assertNotNull(alternativeResponseScenarioAnalysis);
        assertEquals(ALTERNATIVE_AREA, alternativeResponseScenarioAnalysis.getArea());
    }

    /**
     * Verifies that the analysis builder sets the commissioner.
     */
    @Test
    public void buildingAnAnalysisShouldSetTheCommissionerOfAnOpinionPoll() {
        assertNotNull(opinionPollAnalysis);
        assertEquals(Set.of(COMMISSIONER_NAME), opinionPollAnalysis.getCommissioners());
    }

    /**
     * Verifies that the analysis builder sets the fieldwork end.
     */
    @Test
    public void buildingAnAnalysisShouldSetTheFieldworkEndOfAnOpinionPoll() {
        assertNotNull(opinionPollAnalysis);
        assertEquals(FIELDWORK_END.toString(), opinionPollAnalysis.getFieldworkEnd());
    }

    /**
     * Verifies that the analysis builder sets the fieldwork start.
     */
    @Test
    public void buildingAnAnalysisShouldSetTheFieldworkStartOfAnOpinionPoll() {
        assertNotNull(opinionPollAnalysis);
        assertEquals(FIELDWORK_START.toString(), opinionPollAnalysis.getFieldworkStart());
    }

    /**
     * Verifies that the analysis builder sets the polling firm of an opinion poll.
     */
    @Test
    public void buildingAnAnalysisShouldSetThePollingFirmOfAnOpinionPoll() {
        assertNotNull(opinionPollAnalysis);
        assertEquals(POLLING_FIRM_NAME_PARLIAMENT, opinionPollAnalysis.getPollingFirm());
    }

    /**
     * Verifies that the analysis builder sets the polling firm of an opinion poll.
     */
    @Test
    public void buildingAnAnalysisShouldSetThePollingFirmPartnerOfAnOpinionPoll() {
        assertNotNull(opinionPollAnalysis);
        assertEquals(POLLING_FIRM_PARTNER_NAME, opinionPollAnalysis.getPollingFirmPartner());
    }

    /**
     * Verifies that the analysis builder sets the publication date.
     */
    @Test
    public void buildingAnAnalysisShouldSetThePublicationDateOfAnOpinionPoll() {
        assertNotNull(opinionPollAnalysis);
        assertEquals(PUBLICATION_DATE.toString(), opinionPollAnalysis.getPublicationDate());
    }

    /**
     * Verifies that the analysis builder sets the scope of an opinion poll.
     */
    @Test
    public void buildingAnAnalysisShouldSetTheScopeOfAnOpinionPoll() {
        assertNotNull(mainResponseScenarioAnalysis);
        assertEquals(Scope.NATIONAL.toString(), mainResponseScenarioAnalysis.getScope());
    }

    /**
     * Verifies that the analysis builder sets the scope of an alternative response scenario.
     */
    @Test
    public void buildingAnAnalysisShouldSetTheScopeOfAnAlternativeResponseScenario() {
        assertNotNull(alternativeResponseScenarioAnalysis);
        assertEquals(Scope.EUROPEAN.toString(), alternativeResponseScenarioAnalysis.getScope());
    }

    /**
     * Verifies that the analysis builder sets the probability mass for a direct winner of the first round.
     */
    @Test
    public void buildingAnAnalysisSetsTheProbabilityMassForADirectWinnerOfAFirstRound() {
        FirstRoundResultProbabilityMass directWinner = null;
        for (FirstRoundResultProbabilityMass pm : firstRoundAnalysis.getProbabilityMassFunction()) {
            if (pm.getElectoralLists().equals(Set.of(Set.of("AA001")))) {
                directWinner = pm;
            }
        }
        assertNotNull(directWinner);
        assertEquals(FLOAT_50, directWinner.getProbabilityMass(), LARGE_DELTA);
    }

    /**
     * Verifies that the analysis builder sets the probability mass for a winner pair of the first round.
     */
    @Test
    public void buildingAnAnalysisSetsTheProbabilityMassForAWinnerPairOfAFirstRound() {
        FirstRoundResultProbabilityMass winnerPair = null;
        for (FirstRoundResultProbabilityMass pm : firstRoundAnalysis.getProbabilityMassFunction()) {
            if (pm.getElectoralLists().equals(Set.of(Set.of("AA001"), Set.of("AA002")))) {
                winnerPair = pm;
            }
        }
        assertNotNull(winnerPair);
        assertEquals(FLOAT_50, winnerPair.getProbabilityMass(), LARGE_DELTA);
    }

    /**
     * <code>nullOrToString</code> should return when <code>null</code> is passed as an argument.
     */
    @Test
    public void nullOrToStringShouldReturnNullWhenPassingNull() {
        assertNull(AnalysisBuilder.nullOrToString(null));
    }

    /**
     * <code>nullOrToString</code> should return the result of calling <code>toString</code> on the object passed as an
     * argument when it isn't <code>null</code>.
     */
    @Test
    public void nullOrToStringShouldReturnResultOfCallingToStringWhenPassingNonNullObject() {
        assertEquals("1", AnalysisBuilder.nullOrToString(1));
    }
}
