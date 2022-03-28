package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>ResponseScenarioAnalysis</code> class.
 */
public class ResponseScenarioAnalysisTest {
    /**
     * Verifies that the getter method <code>getArea</code> is wired correctly to the setter method
     * <code>setArea</code>.
     */
    @Test
    public void getAreaShouldBeWiredCorrectlyToSetArea() {
        ResponseScenarioAnalysis responseScenarioAnalysis = new ResponseScenarioAnalysis();
        responseScenarioAnalysis.setArea("AREA");
        assertEquals("AREA", responseScenarioAnalysis.getArea());
    }

    /**
     * Verifies that the getter method <code>getOtherAnalysis</code> is wired correctly to the setter method
     * <code>setOtherAnalysis</code>.
     */
    @Test
    public void getOtherAnalysisShouldBeWiredCorrectlyToSetOtherAnalysis() {
        ResponseScenarioAnalysis responseScenarioAnalysis = new ResponseScenarioAnalysis();
        ResultAnalysis otherResultAnalysis = new ResultAnalysis();
        responseScenarioAnalysis.setOtherAnalysis(otherResultAnalysis);
        assertEquals(otherResultAnalysis, responseScenarioAnalysis.getOtherAnalysis());
    }

    /**
     * Verifies that the getter method <code>getResultAnalyses</code> is wired correctly to the setter method
     * <code>setResultAnalyses</code>.
     */
    @Test
    public void getResultAnalysesShouldBeWiredCorrectlyToSetResultAnalyses() {
        ResponseScenarioAnalysis responseScenarioAnalysis = new ResponseScenarioAnalysis();
        Map<String, ResultAnalysis> resultAnalyses = Map.of("A", new ResultAnalysis());
        responseScenarioAnalysis.setResultAnalyses(resultAnalyses);
        assertEquals(resultAnalyses, responseScenarioAnalysis.getResultAnalyses());
    }

    /**
     * Verifies that the getter method <code>getScope</code> is wired correctly to the setter method
     * <code>setScope</code>.
     */
    @Test
    public void getScopeShouldBeWiredCorrectlyToSetScope() {
        ResponseScenarioAnalysis responseScenarioAnalysis = new ResponseScenarioAnalysis();
        responseScenarioAnalysis.setScope("SCOPE");
        assertEquals("SCOPE", responseScenarioAnalysis.getScope());
    }

    /**
     * Verifies that the getter method <code>getFirstRoundAnalysis</code> is wired correctly to the setter method
     * <code>setFirstRoundAnalysis</code>.
     */
    @Test
    public void getFirstRoundAnalysisShouldBeWiredCorrectlyToSetFirstRoundAnalysis() {
        ResponseScenarioAnalysis responseScenarioAnalysis = new ResponseScenarioAnalysis();
        FirstRoundAnalysis firstRoundAnalysis = new FirstRoundAnalysis();
        responseScenarioAnalysis.setFirstRoundAnalysis(firstRoundAnalysis);
        assertEquals(firstRoundAnalysis, responseScenarioAnalysis.getFirstRoundAnalysis());
    }
}
