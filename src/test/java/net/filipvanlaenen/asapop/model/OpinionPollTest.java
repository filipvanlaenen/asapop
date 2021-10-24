package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the class <code>OpinionPoll</code>.
 */
public class OpinionPollTest {
    /**
     * The magic number 1000.
     */
    private static final int ONE_THOUSAND = 1000;

     /**
      * Verifies that the addCommissioner method in the builder class is wired correctly to the getCommissioners method.
      */
     @Test
     public void addCommissionerInBuilderShouldBeWiredCorrectlyToGetCommissioners() {
        OpinionPoll poll = new OpinionPoll.Builder().addCommissioner("The Times").build();
        Set<String> expected = new HashSet<String>();
        expected.add("The Times");
        assertEquals(expected, poll.getCommissioners());
     }

     /**
      * Verifies that the setArea method in the builder class is wired correctly to the getArea method.
      */
     @Test
     public void setAreaInBuilderShouldBeWiredCorrectlyToGetArea() {
        OpinionPoll poll = new OpinionPoll.Builder().setArea("AB").build();
        assertEquals("AB", poll.getArea());
     }

     /**
      * Verifies that the setFieldworkEnd method in the builder class is wired correctly to the getFieldworkEnd
      * method.
      */
     @Test
     public void setFieldworkEndInBuilderShouldBeWiredCorrectlyToGetFieldworkEnd() {
        OpinionPoll poll = new OpinionPoll.Builder().setFieldworkEnd("2021-07-27").build();
        assertEquals("2021-07-27", poll.getFieldworkEnd().toString());
     }

     /**
      * Verifies that the setFieldworkStart method in the builder class is wired correctly to the getFieldworkStart
      * method.
      */
     @Test
     public void setFieldworkStartInBuilderShouldBeWiredCorrectlyToGetFieldworkStart() {
        OpinionPoll poll = new OpinionPoll.Builder().setFieldworkStart("2021-07-27").build();
        assertEquals("2021-07-27", poll.getFieldworkStart().toString());
     }

     /**
      * Verifies that the setOther method in the builder class is wired correctly to the getOther method.
      */
     @Test
     public void setOtherInBuilderShouldBeWiredCorrectlyToGetOther() {
        OpinionPoll poll = new OpinionPoll.Builder().setWellformedOther("2").build();
        assertEquals("2", poll.getOther().getText());
     }

     /**
      * Verifies that the setPollingFirm method in the builder class is wired correctly to the getPollingFirm method.
      */
     @Test
     public void setPollingFirmInBuilderShouldBeWiredCorrectlyToGetPollingFirm() {
        OpinionPoll poll = new OpinionPoll.Builder().setPollingFirm("ACME").build();
        assertEquals("ACME", poll.getPollingFirm());
     }

     /**
      * Verifies that the setPublicationDate method in the builder class is wired correctly to the getPublicationDate
      * method.
      */
     @Test
     public void setPublicationDateInBuilderShouldBeWiredCorrectlyToGetPublicationDate() {
        OpinionPoll poll = new OpinionPoll.Builder().setPublicationDate("2021-07-27").build();
        assertEquals("2021-07-27", poll.getPublicationDate().toString());
     }

    /**
     * Verifies that the addResult method in the builder class is wired correctly to the getResult method.
     */
     @Test
     public void addResultInBuilderShouldBeWiredCorrectlyToGetResult() {
        OpinionPoll poll = new OpinionPoll.Builder().addWellformedResult("A", "55").build();
        assertEquals("55", poll.getResult("A").getText());
     }

     /**
      * Verifies that the setSampleSize method in the builder class is wired correctly to the getSampleSize
      * method.
      */
     @Test
     public void setSampleSizeInBuilderShouldBeWiredCorrectlyToGetSampleSize() {
        OpinionPoll poll = new OpinionPoll.Builder().setSampleSize("1000").build();
        assertEquals("1000", poll.getSampleSize());
     }

     /**
      * Verifies that the setSampleSize method in the builder class is wired correctly to the getSampleSizeValue
      * method.
      */
     @Test
     public void setSampleSizeInBuilderShouldBeWiredCorrectlyToGetSampleSizeValue() {
        OpinionPoll poll = new OpinionPoll.Builder().setSampleSize("1000").build();
        assertEquals(ONE_THOUSAND, poll.getSampleSizeValue());
     }

     /**
      * Verifies that the setScope method in the builder class is wired correctly to the getScope
      * method.
      */
     @Test
     public void setScopeInBuilderShouldBeWiredCorrectlyToGetScope() {
        OpinionPoll poll = new OpinionPoll.Builder().setScope("N").build();
        assertEquals("N", poll.getScope());
     }


     /**
      * Verifies that by default, no alternative response scenarios are returned.
      */
     @Test
     public void byDefaultNoAlternativeResponseScenariosShouldBePresent() {
        OpinionPoll poll = new OpinionPoll.Builder().addCommissioner("The Times").build();
        assertTrue(poll.getAlternativeResponseScenarios().isEmpty());
     }

     /**
      * Verifies that when an alternative response scenario is added, it is also returned by the get method.
      */
     @Test
     public void getAlternativeResponseScenariosShouldBeWiredCorrectlyToAddResponseScenario() {
        OpinionPoll poll = new OpinionPoll.Builder().addCommissioner("The Times").build();
        ResponseScenario responseScenario = new ResponseScenario.Builder().addWellformedResult("A", "55").build();
        poll.addAlternativeResponseScenario(responseScenario);
        List<ResponseScenario> expected = new ArrayList<ResponseScenario>();
        expected.add(responseScenario);
        assertEquals(expected, poll.getAlternativeResponseScenarios());
     }

     /**
      * Verifies that an opinion poll is not equal to null.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToNull() {
        OpinionPoll poll = new OpinionPoll.Builder().build();
        assertFalse(poll.equals(null));
     }

     /**
      * Verifies that an opinion poll is not equal to an object of another class, like a string.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToString() {
        OpinionPoll poll = new OpinionPoll.Builder().build();
        assertFalse(poll.equals(""));
     }

     /**
      * Verifies that an opinion poll is equal to itself.
      */
     @Test
     public void anOpinionPollShouldBeEqualToItself() {
        OpinionPoll poll = new OpinionPoll.Builder().build();
        assertEquals(poll, poll);
     }

     /**
      * Verifies that calling hashCode twice returns the same result.
      */
     @Test
     void callingHashCodeTwiceShouldReturnSameResult() {
        OpinionPoll poll = new OpinionPoll.Builder().build();
        assertEquals(poll.hashCode(), poll.hashCode());
     }


     /**
      * Verifies that an opinion poll is equal to another opinion poll built from the same builder.
      */
     @Test
     public void anOpinionPollShouldBeEqualToAnotherInstanceBuiltFromSameBuilder() {
        OpinionPoll.Builder builder = new OpinionPoll.Builder();
        assertEquals(builder.build(), builder.build());
     }

     /**
      * Verifies that calling hashCode on opinion polls built from the same builder returns the same result.
      */
     @Test
     void hashCodeShouldBeEqualForOpinionPollsBuiltFromSameBuilder() {
        OpinionPoll.Builder builder = new OpinionPoll.Builder();
        assertEquals(builder.build().hashCode(), builder.build().hashCode());
     }

     /**
      * Verifies that an opinion poll is not equal to another opinion poll with a different commissioner.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentCommissioner() {
        OpinionPoll poll1 = new OpinionPoll.Builder().addCommissioner("The Times").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().addCommissioner("The Post").build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that opinion polls have different hash codes if they have different commissioners.
      */
     @Test
     public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentCommissioner() {
        OpinionPoll poll1 = new OpinionPoll.Builder().addCommissioner("The Times").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().addCommissioner("The Post").build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
     }

     /**
      * Verifies that an opinion poll is not equal to another opinion poll with a different fieldwork end.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentFieldworkEnd() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setFieldworkEnd("2021-07-27").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setFieldworkEnd("2021-07-28").build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that an opinion poll is not equal to another opinion poll missing the fieldwork end.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingTheFieldworkEnd() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setFieldworkEnd("2021-07-27").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that an opinion poll missing the fieldwork end is not equal to another opinion poll having one.
      */
     @Test
     public void anOpinionPollMissingFieldworkEndShouldNotBeEqualToAnotherOpinionPollHavingAFieldworkEnd() {
        OpinionPoll poll1 = new OpinionPoll.Builder().build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setFieldworkEnd("2021-07-27").build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that opinion polls have different hash codes if they have different fieldwork end.
      */
     @Test
     public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentFieldworkEnd() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setFieldworkEnd("2021-07-27").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setFieldworkEnd("2021-07-28").build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
     }

     /**
      * Verifies that an opinion poll is not equal to another opinion poll with a different fieldwork start.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentFieldworkStart() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setFieldworkStart("2021-07-27").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setFieldworkStart("2021-07-28").build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that an opinion poll is not equal to another opinion poll missing the fieldwork start.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingTheFieldworkStart() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setFieldworkStart("2021-07-27").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that opinion polls have different hash codes if they have different fieldwork start.
      */
     @Test
     public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentFieldworkStart() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setFieldworkStart("2021-07-27").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setFieldworkStart("2021-07-28").build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
     }

     /**
      * Verifies that an opinion poll is not equal to another opinion poll with a different result for other.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentResultForOther() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setWellformedOther("2").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setWellformedOther("3").build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that opinion polls have different hash codes if they have different result for other.
      */
     @Test
     public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentResultForOther() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setWellformedOther("2").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setWellformedOther("3").build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
     }

     /**
      * Verifies that an opinion poll is not equal to another opinion poll missing the result for other.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingTheResultForOther() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setWellformedOther("2").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that an opinion poll is not equal to another opinion poll with a different polling firm.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentPollingFirm() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPollingFirm("ACME").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setPollingFirm("BCME").build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that opinion polls have different hash codes if they have different polling firms.
      */
     @Test
     public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentPollingFirm() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPollingFirm("ACME").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setPollingFirm("BCME").build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
     }

     /**
      * Verifies that an opinion poll is not equal to another opinion poll missing the polling firm.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingThePollingFirm() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPollingFirm("ACME").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that an opinion poll is not equal to another opinion poll with a different publication date.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentPublicationDate() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPublicationDate("2021-07-27").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setPublicationDate("2021-07-28").build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that an opinion poll is not equal to another opinion poll missing the publication date.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingThePublicationDate() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPublicationDate("2021-07-27").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that opinion polls have different hash codes if they have different publication dates.
      */
     @Test
     public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentPublicationDate() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setPublicationDate("2021-07-27").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setPublicationDate("2021-07-28").build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
     }

     /**
      * Verifies that an opinion poll is not equal to another opinion poll with a different result.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentResult() {
        OpinionPoll poll1 = new OpinionPoll.Builder().addWellformedResult("A", "55").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().addWellformedResult("A", "56").build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that opinion polls have different hash codes if they have different results.
      */
     @Test
     public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentResult() {
        OpinionPoll poll1 = new OpinionPoll.Builder().addWellformedResult("A", "55").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().addWellformedResult("A", "56").build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
     }

     /**
      * Verifies that an opinion poll is not equal to another opinion poll with a different sample size.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentSampleSize() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setSampleSize("1000").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setSampleSize("800").build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that an opinion poll is not equal to another opinion poll missing the sample size.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingTheSampleSize() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setSampleSize("1000").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that opinion polls have different hash codes if they have different sample sizes.
      */
     @Test
     public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentSampleSize() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setSampleSize("1000").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setSampleSize("800").build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
     }

     /**
      * Verifies that an opinion poll is not equal to another opinion poll with a different scope.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentScope() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setScope("N").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setScope("E").build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that an opinion poll is not equal to another opinion poll missing the scope.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingTheScope() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setScope("N").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that opinion polls have different hash codes if they have different scopes.
      */
     @Test
     public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentScope() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setScope("N").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setScope("E").build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
     }

     /**
      * Verifies that an opinion poll is not equal to another opinion poll with a different area.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentArea() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setArea("AB").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setArea("YZ").build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that an opinion poll is not equal to another opinion poll missing the area.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollMissingTheArea() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setArea("AB").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that opinion polls have different hash codes if they have different areas.
      */
     @Test
     public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentArea() {
        OpinionPoll poll1 = new OpinionPoll.Builder().setArea("AB").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().setArea("YZ").build();
        assertFalse(poll1.hashCode() == poll2.hashCode());
     }
}
