package net.filipvanlaenen.asapop.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing an opinion poll.
 */
final class OpinionPoll {
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
     * Constructor using a builder instance as its parameter.
     *
     * @param builder A builder instance.
     */
    private OpinionPoll(final Builder builder) {
        this.pollingFirm = builder.pollingFirm;
        this.publicationDate = builder.publicationDate;
        this.results = Collections.unmodifiableMap(builder.results);
    }

    /**
     * Builder class.
     */
    static class Builder {
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
         * Builds an opinion poll based on the current state of the builder instance.
         *
         * @return The resulting opinion poll.
         */
        OpinionPoll build() {
            return new OpinionPoll(this);
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
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof OpinionPoll) {
            OpinionPoll other = (OpinionPoll) obj;
            return equalsOrBothNull(pollingFirm, other.pollingFirm)
                   && equalsOrBothNull(publicationDate, other.publicationDate)
                   && other.results.equals(results);
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

    @Override
    public int hashCode() {
        return Objects.hash(pollingFirm, publicationDate, results);
    }
}
