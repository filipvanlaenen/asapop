package net.filipvanlaenen.asapop.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Class representing an opinion poll.
 */
public final class OpinionPoll {
    /**
     * The list with alternative response scenarios.
     */
    private final List<ResponseScenario> alternativeResponseScenarios = new ArrayList<ResponseScenario>();
    /**
     * The area.
     */
    private String area;
    /**
     * The commissioners.
     */
    private final Set<String> commissioners;
    /**
     * The fieldwork end.
     */
    private DateOrMonth fieldworkEnd;
    /**
     * The fieldwork start.
     */
    private DateOrMonth fieldworkStart;
    /**
     * The main response scenario.
     */
    private ResponseScenario mainResponseScenario;
    /**
     * The result for other.
     */
    private ResultValue other;
    /**
     * The name of the polling firm.
     */
    private String pollingFirm;
    /**
     * The name of the polling firm partner.
     */
    private String pollingFirmPartner;
    /**
     * The publication date.
     */
    private LocalDate publicationDate;
    /**
     * The sample size.
     */
    private String sampleSize;
    /**
     * The sample size value.
     */
    private int sampleSizeValue;
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
        area = builder.area;
        commissioners = Collections.unmodifiableSet(builder.commissioners);
        fieldworkEnd = builder.fieldworkEnd;
        fieldworkStart = builder.fieldworkStart;
        mainResponseScenario = builder.responseScenarioBuilder.build();
        other = builder.other;
        pollingFirm = builder.pollingFirm;
        pollingFirmPartner = builder.pollingFirmPartner;
        publicationDate = builder.publicationDate;
        sampleSize = builder.sampleSize;
        sampleSizeValue = sampleSize == null ? 0 : Integer.parseInt(sampleSize);
        scope = builder.scope;
    }

    /**
     * Builder class.
     */
    public static class Builder {
        /**
         * The area.
         */
        private String area;
        /**
         * The commissioners.
         */
        private final Set<String> commissioners = new HashSet<String>();
        /**
         * The fieldwork end.
         */
        private DateOrMonth fieldworkEnd;
        /**
         * The fieldwork start.
         */
        private DateOrMonth fieldworkStart;
        /**
         * The result for other.
         */
        private ResultValue other;
        /**
         * The name of the polling firm.
         */
        private String pollingFirm;
        /**
         * The name of the polling firm partner.
         */
        private String pollingFirmPartner;
        /**
         * The publication date.
         */
        private LocalDate publicationDate;
        /**
         * The builder for the response scenario.
         */
        private ResponseScenario.Builder responseScenarioBuilder;
        /**
         * The sample size.
         */
        private String sampleSize;
        /**
         * The scope.
         */
        private String scope;

        /**
         * Default constructor.
         */
        public Builder() {
            responseScenarioBuilder = new ResponseScenario.Builder();
        }

        /**
         * Adds a commissioner.
         *
         * @param commissionerName The name of a commissioner.
         * @return This builder instance.
         */
        public Builder addCommissioner(final String commissionerName) {
            commissioners.add(commissionerName);
            return this;
        }

        /**
         * Adds a result value to the response scenario builder.
         *
         * @param electoralListKey The key of an electoral list.
         * @param resultValue      The result value.
         * @return This builder instance.
         */
        public Builder addResult(final String electoralListKey, final ResultValue resultValue) {
            responseScenarioBuilder.addResult(electoralListKey, resultValue);
            return this;
        }

        /**
         * Adds a result to the response scenario builder.
         *
         * @param electoralListKey The key of an electoral list.
         * @param wellformedResult The result value as a text, assumed to be well-formed.
         * @return This builder instance.
         */
        public Builder addWellformedResult(final String electoralListKey, final String wellformedResult) {
            return addResult(electoralListKey, new ResultValue(wellformedResult));
        }

        /**
         * Builds an opinion poll based on the current state of the builder instance.
         *
         * @return The resulting opinion poll.
         */
        public OpinionPoll build() {
            return new OpinionPoll(this);
        }

        /**
         * Sets the area.
         *
         * @param areaCode The area.
         * @return This builder instance.
         */
        public Builder setArea(final String areaCode) {
            this.area = areaCode;
            return this;
        }

        /**
         * Sets the fieldwork end date from a string.
         *
         * @param fieldworkEndString A string representing the fieldwork end date.
         * @return This builder instance.
         */
        public Builder setFieldworkEnd(final String fieldworkEndString) {
            this.fieldworkEnd = DateOrMonth.parse(fieldworkEndString);
            return this;
        }

        /**
         * Sets the fieldwork start date from a string.
         *
         * @param fieldworkStartString A string representing the fieldwork start date.
         * @return This builder instance.
         */
        public Builder setFieldworkStart(final String fieldworkStartString) {
            this.fieldworkStart = DateOrMonth.parse(fieldworkStartString);
            return this;
        }

        /**
         * Sets the result for other.
         *
         * @param otherString The result for other.
         * @return This builder instance.
         */
        public Builder setOther(final ResultValue otherString) {
            this.other = otherString;
            return this;
        }

        /**
         * Sets the polling firm.
         *
         * @param pollingFirmName The name of the polling firm.
         * @return This builder instance.
         */
        public Builder setPollingFirm(final String pollingFirmName) {
            this.pollingFirm = pollingFirmName;
            return this;
        }

        /**
         * Sets the polling firm partner.
         *
         * @param pollingFirmPartnerName The name of the polling firm partner.
         * @return This builder instance.
         */
        public Builder setPollingFirmPartner(final String pollingFirmPartnerName) {
            this.pollingFirmPartner = pollingFirmPartnerName;
            return this;
        }

        /**
         * Sets the publication date from a string.
         *
         * @param publicationDateString A string representing the publication date.
         * @return This builder instance.
         */
        public Builder setPublicationDate(final String publicationDateString) {
            this.publicationDate = LocalDate.parse(publicationDateString);
            return this;
        }

        /**
         * Sets the sample size.
         *
         * @param sampleSizeString The sample size as a string.
         * @return This builder instance.
         */
        public Builder setSampleSize(final String sampleSizeString) {
            this.sampleSize = sampleSizeString;
            return this;
        }

        /**
         * Sets the scope.
         *
         * @param scopeString The scope as a string.
         * @return This builder instance.
         */
        public Builder setScope(final String scopeString) {
            this.scope = scopeString;
            return this;
        }

        /**
         * Sets the result for other.
         *
         * @param otherString The result for other, assumed to be well-formed.
         * @return This builder instance.
         */
        public Builder setWellformedOther(final String otherString) {
            return setOther(new ResultValue(otherString));
        }
    }

    /**
     * Adds a response scenario to the list of alternative response scenarios.
     *
     * @param responseScenario The response scenario to add.
     */
    public void addAlternativeResponseScenario(final ResponseScenario responseScenario) {
        alternativeResponseScenarios.add(responseScenario);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof OpinionPoll) {
            OpinionPoll otherOpinionPoll = (OpinionPoll) obj;
            return otherOpinionPoll.alternativeResponseScenarios.equals(alternativeResponseScenarios)
                    && equalsOrBothNull(area, otherOpinionPoll.area)
                    && otherOpinionPoll.commissioners.equals(commissioners)
                    && equalsOrBothNull(fieldworkEnd, otherOpinionPoll.fieldworkEnd)
                    && equalsOrBothNull(fieldworkStart, otherOpinionPoll.fieldworkStart)
                    && otherOpinionPoll.mainResponseScenario.equals(mainResponseScenario)
                    && equalsOrBothNull(other, otherOpinionPoll.other)
                    && equalsOrBothNull(pollingFirm, otherOpinionPoll.pollingFirm)
                    && equalsOrBothNull(pollingFirmPartner, otherOpinionPoll.pollingFirmPartner)
                    && equalsOrBothNull(publicationDate, otherOpinionPoll.publicationDate)
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
        return obj1 == null && obj2 == null || obj1 != null && obj1.equals(obj2);
    }

    /**
     * Returns an unmodifiable list with the alternative response scenarios.
     *
     * @return An unmodifiable list with the alternative response scenarios.
     */
    public List<ResponseScenario> getAlternativeResponseScenarios() {
        return Collections.unmodifiableList(alternativeResponseScenarios);
    }

    /**
     * Returns the area.
     *
     * @return The area.
     */
    public String getArea() {
        return area;
    }

    /**
     * Returns the commissioners.
     *
     * @return The commissioners.
     */
    public Set<String> getCommissioners() {
        return commissioners;
    }

    /**
     * Returns the fieldwork end date.
     *
     * @return The fieldwork end date.
     */
    public DateOrMonth getFieldworkEnd() {
        return fieldworkEnd;
    }

    /**
     * Returns the fieldwork start date.
     *
     * @return The fieldwork start date.
     */
    public DateOrMonth getFieldworkStart() {
        return fieldworkStart;
    }

    /**
     * Returns the result for other.
     *
     * @return The result for other.
     */
    public ResultValue getOther() {
        return other;
    }

    /**
     * Returns the polling firm.
     *
     * @return The polling firm.
     */
    public String getPollingFirm() {
        return pollingFirm;
    }

    /**
     * Returns the polling firm partner.
     *
     * @return The polling firm partner.
     */
    public String getPollingFirmPartner() {
        return pollingFirmPartner;
    }

    /**
     * Returns the publication date.
     *
     * @return The publication date.
     */
    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    /**
     * Returns the result for an electoral list.
     *
     * @param electoralListKey The key of an electoral list.
     * @return The result for the electoral list.
     */
    public ResultValue getResult(final String electoralListKey) {
        return mainResponseScenario.getResult(electoralListKey);
    }

    /**
     * Returns the sample size.
     *
     * @return The sample size.
     */
    public String getSampleSize() {
        return sampleSize;
    }

    /**
     * Returns the sample size value.
     *
     * @return The sample size value.
     */
    public int getSampleSizeValue() {
        return sampleSizeValue;
    }

    /**
     * Returns the scope.
     *
     * @return The scope.
     */
    public String getScope() {
        return scope;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alternativeResponseScenarios, area, commissioners, fieldworkEnd, fieldworkStart,
                mainResponseScenario, other, pollingFirm, pollingFirmPartner, publicationDate, sampleSize, scope);
    }
}
