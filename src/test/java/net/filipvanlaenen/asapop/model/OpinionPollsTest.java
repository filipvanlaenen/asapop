package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the class <code>OpinionPolls</code>.
 */
public class OpinionPollsTest {
    /**
     * Verifies that the getter method <code>getOpinionPolls</code> is wired correctly to the constructor.
     */
    @Test
    public void getOpinionPollsShouldBeWiredCorrectlyToTheConstructor() {
        OpinionPoll poll = new OpinionPoll.Builder().addCommissioner("The Times").build();
        Set<OpinionPoll> expected = new HashSet<OpinionPoll>();
        expected.add(poll);
        OpinionPolls opinionPolls = new OpinionPolls(expected);
        assertEquals(expected, opinionPolls.getOpinionPolls());
    }

    /**
     * Verifies that <code>getOpinionPolls</code> returns an UnmodifiableSet.
     */
    @Test
    public void getOpinionPollsReturnsAnUnmodifiableSet() {
        OpinionPoll poll = new OpinionPoll.Builder().addCommissioner("The Times").build();
        OpinionPoll otherPoll = new OpinionPoll.Builder().addCommissioner("The Post").build();
        Set<OpinionPoll> set = new HashSet<OpinionPoll>();
        set.add(poll);
        OpinionPolls opinionPolls = new OpinionPolls(set);
        assertThrows(UnsupportedOperationException.class, () -> {
            opinionPolls.getOpinionPolls().add(otherPoll);
        });
    }
}
