package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the class <code>ResponseScenario</code>.
 */
public class ResponseScenarioTest {
    /**
     * Verifies that the addResult method in the builder class is wired correctly to the getResult method.
     */
     @Test
     public void addResultInBuilderShouldBeWiredCorrectlyToGetResult() {
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").build();
        assertEquals("55", responseScenario.getResult("A"));
     }

    /**
     * Verifies that the setOther method in the builder class is wired correctly to the getOther method.
     */
     @Test
     public void setOtherInBuilderShouldBeWiredCorrectlyToGetOther() {
        ResponseScenario responseScenario = new ResponseScenario.Builder().setOther("5").build();
        assertEquals("5", responseScenario.getOther());
     }

     /**
      * Verifies that the setScope method in the builder class is wired correctly to the getScope
      * method.
      */
     @Test
     public void setScopeInBuilderShouldBeWiredCorrectlyToGetScope() {
        ResponseScenario responseScenario = new ResponseScenario.Builder().setScope("N").build();
        assertEquals("N", responseScenario.getScope());
     }

     /**
      * Verifies that a response scenario is not equal to null.
      */
     @Test
     public void aResponseScenarioShouldNotBeEqualToNull() {
        ResponseScenario responseScenario = new ResponseScenario.Builder().build();
        assertFalse(responseScenario.equals(null));
     }

     /**
      * Verifies that a response scenario is not equal to an object of another class, like a string.
      */
     @Test
     public void aResponseScenarioShouldNotBeEqualToString() {
        ResponseScenario responseScenario = new ResponseScenario.Builder().build();
        assertFalse(responseScenario.equals(""));
     }

     /**
      * Verifies that a response scenario is equal to itself.
      */
     @Test
     public void aResponseScenarioShouldBeEqualToItself() {
        ResponseScenario responseScenario = new ResponseScenario.Builder().build();
        assertEquals(responseScenario, responseScenario);
     }

     /**
      * Verifies that calling hashCode twice returns the same result.
      */
     @Test
     void callingHashCodeTwiceShouldReturnSameResult() {
        ResponseScenario responseScenario = new ResponseScenario.Builder().build();
        assertEquals(responseScenario.hashCode(), responseScenario.hashCode());
     }


     /**
      * Verifies that a response scenario is equal to another response scenario built from the same builder.
      */
     @Test
     public void aResponseScenarioShouldBeEqualToAnotherInstanceBuiltFromSameBuilder() {
        ResponseScenario.Builder builder = new ResponseScenario.Builder();
        assertEquals(builder.build(), builder.build());
     }

     /**
      * Verifies that calling hashCode on response scenarios built from the same builder returns the same result.
      */
     @Test
     void hashCodeShouldBeEqualForResponseScenariosBuiltFromSameBuilder() {
        ResponseScenario.Builder builder = new ResponseScenario.Builder();
        assertEquals(builder.build().hashCode(), builder.build().hashCode());
     }

     /**
      * Verifies that a response scenario is not equal to another response scenario with a different result.
      */
     @Test
     public void aResponseScenarioShouldNotBeEqualToAnotherResponseScenarioWithADifferentResult() {
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().addResult("A", "55").build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().addResult("A", "56").build();
        assertFalse(responseScenario1.equals(responseScenario2));
     }

     /**
      * Verifies that response scenarios have different hash codes if they have different results.
      */
     @Test
     public void aResponseScenarioShouldNotHaveSameHashCodeAsAnotherResponseScenarioWithADifferentResult() {
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().addResult("A", "55").build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().addResult("A", "56").build();
        assertFalse(responseScenario1.hashCode() == responseScenario2.hashCode());
     }

     /**
      * Verifies that a response scenario is not equal to another response scenario with a different result for others.
      */
     @Test
     public void aResponseScenarioShouldNotBeEqualToAnotherResponseScenarioWithADifferentOtherResult() {
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().setOther("5").build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().setOther("6").build();
        assertFalse(responseScenario1.equals(responseScenario2));
     }

     /**
      * Verifies that response scenarios have different hash codes if they have different results for other.
      */
     @Test
     public void aResponseScenarioShouldNotHaveSameHashCodeAsAnotherResponseScenarioWithADifferentOtherResult() {
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().setOther("5").build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().setOther("6").build();
        assertFalse(responseScenario1.hashCode() == responseScenario2.hashCode());
     }

     /**
      * Verifies that a response scenario is not equal to another response scenario missing the result for others.
      */
     @Test
     public void aResponseScenarioShouldNotBeEqualToAnotherResponseScenarioMissingTheOtherResult() {
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().setOther("5").build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().build();
        assertFalse(responseScenario1.equals(responseScenario2));
     }

     /**
      * Verifies that a response scenario without a result for others is not equal to another response scenario with a
      * result for others.
      */
     @Test
     public void aResponseScenarioMissingOtherResutShouldNotBeEqualToAnotherResponseScenarioWithOtherResult() {
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().setOther("5").build();
        assertFalse(responseScenario1.equals(responseScenario2));
     }

     /**
      * Verifies that a response scenario is not equal to another response scenario with a different scope.
      */
     @Test
     public void aResponseScenarioShouldNotBeEqualToAnotherResponseScenarioWithADifferentScope() {
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().setScope("N").build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().setScope("E").build();
        assertFalse(responseScenario1.equals(responseScenario2));
     }

     /**
      * Verifies that a response scenario is not equal to another response scenario missing the scope.
      */
     @Test
     public void aResponseScenarioShouldNotBeEqualToAnotherResponseScenarioMissingTheScope() {
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().setScope("N").build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().build();
        assertFalse(responseScenario1.equals(responseScenario2));
     }

     /**
      * Verifies that response scenarios have different hash codes if they have different scopes.
      */
     @Test
     public void aResponseScenarioShouldNotHaveSameHashCodeAsAnotherResponseScenarioWithADifferentScope() {
        ResponseScenario responseScenario1 = new ResponseScenario.Builder().setScope("N").build();
        ResponseScenario responseScenario2 = new ResponseScenario.Builder().setScope("E").build();
        assertFalse(responseScenario1.hashCode() == responseScenario2.hashCode());
     }

     /**
      * Verifies the correct export of a simple response scenatio for an opinion poll with only a publication date to
      * the EOPAOD PSV file format.
      */
     @Test
     public void shouldExportSimpleResponseScenarioWithPublicationDateOnlyCorrectlyToEopaodPsvFormat() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "45")
                                                                          .build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | N/A | 55 | 45 | N/A";
        assertEquals(expected, responseScenario.toEopaodPsvString(poll, "A", "B"));
     }

     /**
      * Verifies the correct export of a simple response scenario for an opinion poll with a fieldwork period to the
      * EOPAOD PSV file format.
      */
     @Test
     public void shouldExportSimpleResponseScenarioWithFieldworkPeriodCorrectlyToEopaodPsvFormat() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setFieldworkStart("2021-08-01")
                                                    .setFieldworkEnd("2021-08-02").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "45")
                                                                          .build();
        String expected = "ACME | N/A | 2021-08-01 | 2021-08-02 | N/A | N/A | N/A | N/A | 55 | 45 | N/A";
        assertEquals(expected, responseScenario.toEopaodPsvString(poll, "A", "B"));
     }

     /**
      * Verifies the correct export of a simple response scenatio for an opinion poll with a result for other to
      * the EOPAOD PSV file format.
      */
     @Test
     public void shouldExportSimpleResponseScenarioWithOtherResultCorrectlyToEopaodPsvFormat() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "43")
                                                                          .setOther("2").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N/A | N/A | N/A | N/A | 55 | 43 | 2";
        assertEquals(expected, responseScenario.toEopaodPsvString(poll, "A", "B"));
     }

     /**
      * Verifies the correct export of a simple response scenatio for an opinion poll with the same scope to
      * the EOPAOD PSV file format.
      */
     @Test
     public void shouldExportSimpleResponseScenarioWithSameScopeCorrectlyToEopaodPsvFormat() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .setScope("N").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "43")
                                                                          .build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | N | N/A | N/A | N/A | 55 | 43 | N/A";
        assertEquals(expected, responseScenario.toEopaodPsvString(poll, "A", "B"));
     }

     /**
      * Verifies the correct export of a simple response scenatio for an opinion poll with a different scope to
      * the EOPAOD PSV file format.
      */
     @Test
     public void shouldExportSimpleResponseScenarioWithDifferentScopeCorrectlyToEopaodPsvFormat() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").setPublicationDate("2021-08-02")
                                                    .setScope("N").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addResult("A", "55").addResult("B", "43")
                                                                          .setScope("E").build();
        String expected = "ACME | N/A | 2021-08-02 | 2021-08-02 | E | N/A | N/A | N/A | 55 | 43 | N/A";
        assertEquals(expected, responseScenario.toEopaodPsvString(poll, "A", "B"));
     }
}
