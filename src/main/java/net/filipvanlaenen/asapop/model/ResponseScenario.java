package net.filipvanlaenen.asapop.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing a response scenario.
 */
final class ResponseScenario {
    /**
     * The result for other.
     */
    private String other;
    /**
     * The results.
     */
    private final Map<ElectoralList, String> results;
    /**
     * The scope.
     */
    private String scope;

    /**
     * Constructor using a builder instance as its parameter.
     *
     * @param builder A builder instance.
     */
    private ResponseScenario(final Builder builder) {
        this.other = builder.other;
        this.results = Collections.unmodifiableMap(builder.results);
        this.scope = builder.scope;
    }

    /**
     * Builder class.
     */
    static class Builder {
        /**
         * The result for other.
         */
        private String other;
        /**
         * The results.
         */
        private final Map<ElectoralList, String> results = new HashMap<ElectoralList, String>();
        /**
         * The scope.
         */
        private String scope;

        /**
         * Adds a result.
         *
         * @param electoralListKey The key of an electoral list.
         * @param result The result.
         * @return This builder instance.
         */
        Builder addResult(final String electoralListKey, final String result) {
            results.put(ElectoralList.get(electoralListKey), result);
            return this;
        }

        /**
         * Builds a response scenario based on the current state of the builder instance.
         *
         * @return The resulting response scenario.
         */
        ResponseScenario build() {
            return new ResponseScenario(this);
        }

        /**
         * Sets the result for other.
         *
         * @param otherString The result for other.
         * @return This builder instance.
         */
        Builder setOther(final String otherString) {
            this.other = otherString;
            return this;
        }

        /**
         * Sets the scope.
         *
         * @param scopeString The scope as a string.
         * @return This builder instance.
         */
        Builder setScope(final String scopeString) {
            this.scope = scopeString;
            return this;
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ResponseScenario) {
            ResponseScenario otherResponseScenario = (ResponseScenario) obj;
            return equalsOrBothNull(other, otherResponseScenario.other)
                   && otherResponseScenario.results.equals(results)
                   && equalsOrBothNull(scope, otherResponseScenario.scope);
        } else {
            return false;
        }
    }

    /**
     * Returns true if both objects are equal or both are null.
     *
     * @param obj1 The first object.
     * @param obj2 The second object.
     * @return True if both objects are equal or both are null, false otherwise.
     */
    private boolean equalsOrBothNull(final Object obj1, final Object obj2) {
        return obj1 == null && obj2 == null || obj1 != null && obj1.equals(obj2);
    }

    /**
     * Returns the result for other.
     *
     * @return The result for other.
     */
    String getOther() {
        return other;
    }

    /**
     * Returns the result for an electoral list.
     *
     * @param electoralListKey The key of an electoral list.
     * @return The result for the electoral list.
     */
    String getResult(final String electoralListKey) {
        return results.get(ElectoralList.get(electoralListKey));
    }

    /**
     * Returns the scope.
     *
     * @return The scope.
     */
    String getScope() {
        return scope;
    }

    @Override
    public int hashCode() {
        return Objects.hash(other, results, scope);
    }

    /**
     * Exports the response scenario as a string in the PSV file format for EOPAOD.
     *
     * @param opinionPoll The opinion poll this response scenario relates to.
     * @param electoralListKeys An array with the keys of the electoral lists to be exported.
     * @return A string containing the response scenario in the PSV file format for EOPAOD.
     */
    String toEopaodPsvString(final OpinionPoll opinionPoll, final String... electoralListKeys) {
        StringBuffer sb = new StringBuffer();
        sb.append(opinionPoll.getPollingFirm());
        sb.append(" | ");
        sb.append(opinionPoll.getCommissioners().isEmpty() ? "N/A" : String.join(", ", opinionPoll.getCommissioners()));
        sb.append(" | ");
        if (opinionPoll.getFieldworkStart() == null) {
            if (opinionPoll.getFieldworkEnd() == null) {
                sb.append(opinionPoll.getPublicationDate().toString());
                sb.append(" | ");
                sb.append(opinionPoll.getPublicationDate().toString());
            } else {
                sb.append(opinionPoll.getFieldworkEnd().toString());
                sb.append(" | ");
                sb.append(opinionPoll.getFieldworkEnd().toString());
            }
        } else {
            if (opinionPoll.getFieldworkEnd() == null) {
                sb.append(opinionPoll.getFieldworkStart().toString());
                sb.append(" | ");
                sb.append(opinionPoll.getPublicationDate().toString());
            } else {
                sb.append(opinionPoll.getFieldworkStart().toString());
                sb.append(" | ");
                sb.append(opinionPoll.getFieldworkEnd().toString());
            }
        }
        sb.append(" | ");
        sb.append(scope == null ? opinionPoll.getScope() == null ? "N/A" : opinionPoll.getScope() : scope);
        sb.append(" | ");
        sb.append(opinionPoll.getSampleSize() == null ? "N/A" : opinionPoll.getSampleSize());
        sb.append(" | ");
        sb.append("N/A");
        sb.append(" | ");
        sb.append("N/A");
        sb.append(" | ");
        for (String electoralListKey : electoralListKeys) {
            String result = getResult(electoralListKey);
            sb.append(result == null ? "N/A" : result);
            sb.append(" | ");
        }
        sb.append(other == null ? "N/A" : other);
        return sb.toString();
    }
}
