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
}
