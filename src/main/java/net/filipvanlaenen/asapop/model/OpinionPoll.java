package net.filipvanlaenen.asapop.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Class representing an opinion poll.
 */
final class OpinionPoll {
    /**
     * The commissioners.
     */
    private final Set<String> commissioners;
    /**
     * The fieldwork end.
     */
    private LocalDate fieldworkEnd;
    /**
     * The fieldwork start.
     */
    private LocalDate fieldworkStart;
    /**
     * The result for other.
     */
    private String other;
    /**
     * The name of the polling firm.
     */
    private String pollingFirm;
    /**
     * The publication date.
     */
    private LocalDate publicationDate;
    /**
     * The results.
     */
    private final Map<ElectoralList, String> results;
    /**
     * The sample size.
     */
    private String sampleSize;
    /**
     * The scope.
     */
    private String scope;

    /**
     * Constructor using a builder instance as its parameter.
     *
     * @param builder A builder instance.
     */
    private OpinionPoll(final Builder builder) {
        this.commissioners = Collections.unmodifiableSet(builder.commissioners);
        this.fieldworkEnd = builder.fieldworkEnd;
        this.fieldworkStart = builder.fieldworkStart;
        this.other = builder.other;
        this.pollingFirm = builder.pollingFirm;
        this.publicationDate = builder.publicationDate;
        this.results = Collections.unmodifiableMap(builder.results);
        this.sampleSize =  builder.sampleSize;
        this.scope = builder.scope;
    }

    /**
     * Builder class.
     */
    static class Builder {
        /**
         * The commissioners.
         */
        private final Set<String> commissioners = new HashSet<String>();
        /**
         * The fieldwork end.
         */
        private LocalDate fieldworkEnd;
        /**
         * The fieldwork start.
         */
        private LocalDate fieldworkStart;
        /**
         * The result for other.
         */
        private String other;
        /**
         * The name of the polling firm.
         */
        private String pollingFirm;
        /**
         * The publication date.
         */
        private LocalDate publicationDate;
        /**
         * The results.
         */
        private final Map<ElectoralList, String> results = new HashMap<ElectoralList, String>();
        /**
         * The sample size.
         */
        private String sampleSize;
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
         * Adds a commissioner.
         *
         * @param commissionerName The name of a commissioner.
         * @return This builder instance.
         */
        Builder addCommissioner(final String commissionerName) {
            commissioners.add(commissionerName);
            return this;
        }

        /**
         * Builds an opinion poll based on the current state of the builder instance.
         *
         * @return The resulting opinion poll.
         */
        OpinionPoll build() {
            return new OpinionPoll(this);
        }

        /**
         * Sets the fieldwork end date from a string.
         *
         * @param fieldworkEndString A string representing the fieldwork end date.
         * @return This builder instance.
         */
        Builder setFieldworkEnd(final String fieldworkEndString) {
            this.fieldworkEnd = LocalDate.parse(fieldworkEndString);
            return this;
        }

        /**
         * Sets the fieldwork start date from a string.
         *
         * @param fieldworkStartString A string representing the fieldwork start date.
         * @return This builder instance.
         */
        Builder setFieldworkStart(final String fieldworkStartString) {
            this.fieldworkStart = LocalDate.parse(fieldworkStartString);
            return this;
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
         * Sets the polling firm.
         *
         * @param pollingFirmName The name of the polling firm.
         * @return This builder instance.
         */
        Builder setPollingFirm(final String pollingFirmName) {
            this.pollingFirm = pollingFirmName;
            return this;
        }

        /**
         * Sets the publication date from a string.
         *
         * @param publicationDateString A string representing the publication date.
         * @return This builder instance.
         */
        Builder setPublicationDate(final String publicationDateString) {
            this.publicationDate = LocalDate.parse(publicationDateString);
            return this;
        }

        /**
         * Sets the sample size.
         *
         * @param sampleSizeString The sample size as a string.
         * @return This builder instance.
         */
        Builder setSampleSize(final String sampleSizeString) {
            this.sampleSize = sampleSizeString;
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
        if (obj instanceof OpinionPoll) {
            OpinionPoll otherOpinionPoll = (OpinionPoll) obj;
            return otherOpinionPoll.commissioners.equals(commissioners)
                   && equalsOrBothNull(fieldworkEnd, otherOpinionPoll.fieldworkEnd)
                   && equalsOrBothNull(fieldworkStart, otherOpinionPoll.fieldworkStart)
                   && equalsOrBothNull(other, otherOpinionPoll.other)
                   && equalsOrBothNull(pollingFirm, otherOpinionPoll.pollingFirm)
                   && equalsOrBothNull(publicationDate, otherOpinionPoll.publicationDate)
                   && otherOpinionPoll.results.equals(results)
                   && equalsOrBothNull(sampleSize, otherOpinionPoll.sampleSize)
                   && equalsOrBothNull(scope, otherOpinionPoll.scope);
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
        return obj1 == null && obj2 == null || obj1.equals(obj2);
    }

    /**
     * Returns the commissioners.
     *
     * @return The commissioners.
     */
    Set<String> getCommissioners() {
        return commissioners;
    }

    /**
     * Returns the fieldwork end date.
     *
     * @return The fieldwork end date.
     */
    LocalDate getFieldworkEnd() {
        return fieldworkEnd;
    }

    /**
     * Returns the fieldwork start date.
     *
     * @return The fieldwork start date.
     */
    LocalDate getFieldworkStart() {
        return fieldworkStart;
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
     * Returns the polling firm.
     *
     * @return The polling firm.
     */
    String getPollingFirm() {
        return pollingFirm;
    }

    /**
     * Returns the publication date.
     *
     * @return The publication date.
     */
    LocalDate getPublicationDate() {
        return publicationDate;
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
     * Returns the sample size.
     *
     * @return The sample size.
     */
    String getSampleSize() {
        return sampleSize;
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
        return Objects.hash(commissioners, fieldworkEnd, fieldworkStart, other, pollingFirm, publicationDate, results,
                            sampleSize, scope);
    }
}
