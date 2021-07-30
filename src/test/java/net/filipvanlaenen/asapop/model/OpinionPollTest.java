package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the class <code>OpinionPoll</code>.
 */
public class OpinionPollTest {
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
        OpinionPoll poll = new OpinionPoll.Builder().addResult("A", "55").build();
        assertEquals("55", poll.getResult("A"));
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
        OpinionPoll poll1 = new OpinionPoll.Builder().addResult("A", "55").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().addResult("A", "56").build();
        assertFalse(poll1.equals(poll2));
     }

     /**
      * Verifies that opinion polls have different hash codes if they have different results.
      */
     @Test
     public void anOpinionPollShouldNotHaveSameHashCodeAsAnotherOpinionPollWithADifferentResult() {
        OpinionPoll poll1 = new OpinionPoll.Builder().addResult("A", "55").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().addResult("A", "56").build();
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
}
