package net.filipvanlaenen.asapop.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
         */
        Builder addResult(final String electoralList, final String result) {
            results.put(ElectoralList.get(electoralList), result);
            return this;
        }

        /**
         * Builds an opinion poll based on the current state of the builder instance.
         */
        OpinionPoll build() {
            return new OpinionPoll(this);
        }

        /**
         * Sets the polling firm.
         */
        Builder setPollingFirm(final String pollingFirm) {
            this.pollingFirm = pollingFirm;
            return this;
        }

        /**
         * Sets the publication date.
         */
        Builder setPublicationDate(final String publicationDate) {
            this.publicationDate = LocalDate.parse(publicationDate);
            return this;
        }
    }

    // TODO: Override hashcode also.
    // TODO: Handle null values.
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OpinionPoll) {
            OpinionPoll other = (OpinionPoll) obj;
            return other.pollingFirm.equals(pollingFirm) && other.publicationDate.equals(publicationDate) && other.results.equals(results);
        } else {
            return false;
        }
    }
}
