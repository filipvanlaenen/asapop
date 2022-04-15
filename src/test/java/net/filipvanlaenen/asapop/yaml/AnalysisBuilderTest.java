package net.filipvanlaenen.asapop.yaml;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.analysis.AnalysisEngine;
import net.filipvanlaenen.asapop.model.OpinionPoll;
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
     * The magic number 0.50.
     */
    private static final double FLOAT_0_50 = 0.50D;
    /**
     * The magic number 0.60.
     */
    private static final double FLOAT_0_60 = 0.60D;
    /**
     * The magic number 1.05.
     */
    private static final double FLOAT_1_05 = 1.05D;
    /**
     * The magic number 1.60.
     */
    private static final double FLOAT_1_70 = 1.70D;
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
     * The first fieldwork end date.
     */
    private static final String FIELDWORK_END = "2022-01-19";
    /**
     * The fieldwork start date.
     */
    private static final String FIELDWORK_START = "2022-01-18";
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
    private static final String PUBLICATION_DATE = "2022-01-20";
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
        OpinionPoll opinionPoll = new OpinionPoll.Builder().setPollingFirm(POLLING_FIRM_NAME_PARLIAMENT)
                .setPollingFirmPartner(POLLING_FIRM_PARTNER_NAME).addCommissioner(COMMISSIONER_NAME)
                .setFieldworkStart(FIELDWORK_START).setFieldworkEnd(FIELDWORK_END).setPublicationDate(PUBLICATION_DATE)
                .setArea(AREA).setScope(Scope.National).setSampleSize("1000").addResult("A", new ResultValue("1"))
                .setOther(new ResultValue("2")).build();
        opinionPoll.addAlternativeResponseScenario(
                new ResponseScenario.Builder().setArea(ALTERNATIVE_AREA).setScope(Scope.European).build());
        opinionPollSet.add(opinionPoll);
        opinionPoll = new OpinionPoll.Builder().setPollingFirm(POLLING_FIRM_NAME_PRESIDENTIAL)
                .setPollingFirmPartner(POLLING_FIRM_PARTNER_NAME).addCommissioner(COMMISSIONER_NAME)
                .setFieldworkStart(FIELDWORK_START).setFieldworkEnd(FIELDWORK_END).setPublicationDate(PUBLICATION_DATE)
                .setScope(Scope.PresidentialFirstRound).setArea(AREA).setSampleSize("1000")
                .addResult("A", new ResultValue("50")).addResult("B", new ResultValue("30")).build();
        opinionPollSet.add(opinionPoll);
        OpinionPolls opinionPolls = new OpinionPolls(opinionPollSet);
        AnalysisEngine engine = new AnalysisEngine(opinionPolls, new ElectionData());
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
                                if (Scope.National.toString().equals(rsa.getScope())) {
                                    mainResponseScenarioAnalysis = rsa;
                                    alternativeResponseScenarioAnalysis = rsai.next();
                                } else {
                                    alternativeResponseScenarioAnalysis = rsa;
                                    mainResponseScenarioAnalysis = rsai.next();
                                }
                                resultAnalysis = mainResponseScenarioAnalysis.getResultAnalyses().get("A");
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
        assertEquals(FLOAT_1_05, resultAnalysis.getMedian(), DELTA);
    }

    /**
     * Verifies that the 80 percent confidence interval for an electoral list is set.
     */
    @Test
    public void resultAnalysisShouldContainThe80PercentConfidenceIntervalForTheElectoralList() {
        assertEquals(FLOAT_0_60, resultAnalysis.getConfidenceIntervals().get(EIGHTY)[0], DELTA);
        assertEquals(FLOAT_1_70, resultAnalysis.getConfidenceIntervals().get(EIGHTY)[1], DELTA);
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
        assertEquals(FIELDWORK_END, opinionPollAnalysis.getFieldworkEnd());
    }

    /**
     * Verifies that the analysis builder sets the fieldwork start.
     */
    @Test
    public void buildingAnAnalysisShouldSetTheFieldworkStartOfAnOpinionPoll() {
        assertNotNull(opinionPollAnalysis);
        assertEquals(FIELDWORK_START, opinionPollAnalysis.getFieldworkStart());
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
        assertEquals(PUBLICATION_DATE, opinionPollAnalysis.getPublicationDate());
    }

    /**
     * Verifies that the analysis builder sets the scope of an opinion poll.
     */
    @Test
    public void buildingAnAnalysisShouldSetTheScopeOfAnOpinionPoll() {
        assertNotNull(mainResponseScenarioAnalysis);
        assertEquals(Scope.National.toString(), mainResponseScenarioAnalysis.getScope());
    }

    /**
     * Verifies that the analysis builder sets the scope of an alternative response scenario.
     */
    @Test
    public void buildingAnAnalysisShouldSetTheScopeOfAnAlternativeResponseScenario() {
        assertNotNull(alternativeResponseScenarioAnalysis);
        assertEquals(Scope.European.toString(), alternativeResponseScenarioAnalysis.getScope());
    }

    /**
     * Verifies that the analysis builder sets the probability mass for a direct winner of the first round.
     */
    @Test
    public void buildingAnAnalysisSetsTheProbabilityMassForADirectWinnerOfAFirstRound() {
        FirstRoundResultProbabilityMass directWinner = null;
        for (FirstRoundResultProbabilityMass pm : firstRoundAnalysis.getProbabilityMassFunction()) {
            if (pm.getElectoralLists().equals(Set.of("A"))) {
                directWinner = pm;
            }
        }
        assertNotNull(directWinner);
        assertEquals(FLOAT_0_50, directWinner.getProbabilityMass(), DELTA);
    }

    /**
     * Verifies that the analysis builder sets the probability mass for a winner pair of the first round.
     */
    @Test
    public void buildingAnAnalysisSetsTheProbabilityMassForAWinnerPairOfAFirstRound() {
        FirstRoundResultProbabilityMass winnerPair = null;
        for (FirstRoundResultProbabilityMass pm : firstRoundAnalysis.getProbabilityMassFunction()) {
            if (pm.getElectoralLists().equals(Set.of("A", "B"))) {
                winnerPair = pm;
            }
        }
        assertNotNull(winnerPair);
        assertEquals(FLOAT_0_50, winnerPair.getProbabilityMass(), DELTA);
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
