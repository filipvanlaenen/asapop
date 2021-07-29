package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the class <code>OpinionPoll</code>.
 */
public class OpinionPollTest {
    /**
     * Verifies that the addResult method in the builder class is wired correctly to the getResult method.
     */
     @Test
     public void addResultInBuilderShouldBeWiredCorrectlyToGetResult() {
        OpinionPoll poll = new OpinionPoll.Builder().addResult("A", "55").build();
        assertEquals("55", poll.getResult("A"));
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
      * Verifies that the setPublicationDate method in the builder class is wired correctly to the getPublicationDate method.
      */
     @Test
     public void setPublicationDateInBuilderShouldBeWiredCorrectlyToGetPublicationDate() {
        OpinionPoll poll = new OpinionPoll.Builder().setPublicationDate("2021-07-27").build();
        assertEquals("2021-07-27", poll.getPublicationDate().toString());
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
      * Verifies that an opinion poll is equal to another opinion poll built from the same builder.
      */
     @Test
     public void anOpinionPollShouldBeEqualToAnotherInstanceBuiltFromSameBuilder() {
        OpinionPoll.Builder builder = new OpinionPoll.Builder();
        assertEquals(builder.build(), builder.build());
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
      * Verifies that an opinion poll is not equal to another opinion poll with a different result.
      */
     @Test
     public void anOpinionPollShouldNotBeEqualToAnotherOpinionPollWithADifferentResult() {
        OpinionPoll poll1 = new OpinionPoll.Builder().addResult("A", "55").build();
        OpinionPoll poll2 = new OpinionPoll.Builder().addResult("A", "56").build();
        assertFalse(poll1.equals(poll2));
     }
}
