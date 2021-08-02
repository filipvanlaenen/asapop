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

}
