package net.filipvanlaenen.asapop.model;

import java.util.Set;

/**
 * Helper class extending the builder for ResponseScenario with some convenience methods for unit tests.
 */
public class ResponseScenarioTestBuilder extends ResponseScenario.Builder {
    /**
     * Adds a result value for an electoral list.
     *
     * @param id    The ID for an electoral list.
     * @param value The result value to be added for the electoral list.
     * @return This builder instance.
     */
    public ResponseScenarioTestBuilder addResult(final String id, final String value) {
        addResult(Set.of(ElectoralList.get(id)), new ResultValue(value));
        return this;
    }

    /**
     * Adds a result value for a combination of two electoral lists.
     *
     * @param id1   The ID for an electoral list.
     * @param id2   The ID for another electoral list.
     * @param value The result value to be added for the electoral list.
     * @return This builder instance.
     */
    public ResponseScenarioTestBuilder addResult(final String id1, final String id2, final String value) {
        addResult(Set.of(ElectoralList.get(id1), ElectoralList.get(id2)), new ResultValue(value));
        return this;
    }

    /**
     * Sets the result for no responses.
     *
     * @param noResponsesString The result for no responses, assumed to be well-formed.
     * @return This builder instance.
     */
    public ResponseScenarioTestBuilder setNoResponses(final String noResponsesString) {
        setNoResponses(new ResultValue(noResponsesString));
        return this;
    }

    /**
     * Sets the result for other.
     *
     * @param otherString The result for other, assumed to be well-formed.
     * @return This builder instance.
     */
    public ResponseScenarioTestBuilder setOther(final String otherString) {
        setOther(new ResultValue(otherString));
        return this;
    }
}
