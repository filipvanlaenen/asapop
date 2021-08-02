package net.filipvanlaenen.asapop.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

final class ResponseScenario {
    /**
     * The results.
     */
    private final Map<ElectoralList, String> results;

    /**
     * Constructor using a builder instance as its parameter.
     *
     * @param builder A builder instance.
     */
    private ResponseScenario(final Builder builder) {
        this.results = Collections.unmodifiableMap(builder.results);
    }

    /**
     * Builder class.
     */
    static class Builder {
        /**
         * The results.
         */
        private final Map<ElectoralList, String> results = new HashMap<ElectoralList, String>();

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
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ResponseScenario) {
            ResponseScenario otherResponseScenario = (ResponseScenario) obj;
            return otherResponseScenario.results.equals(results);
        } else {
            return false;
        }
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

    @Override
    public int hashCode() {
        return Objects.hash(results);
    }

    /**
     * Exports the response scenario as a string in the PSV file format for EOPAOD.
     *
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
        sb.append(opinionPoll.getScope() == null ? "N/A" : opinionPoll.getScope());
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
        sb.append(opinionPoll.getOther() == null ? "N/A" : opinionPoll.getOther());
        return sb.toString();
    }
}
