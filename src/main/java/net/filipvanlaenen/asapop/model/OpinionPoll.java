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
     * The magic number hundred.
     */
    private static final float HUNDRED = 100F;
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
     * The share of excluded responses.
     */
    private DecimalNumber excluded;
    /**
     * The fieldwork end.
     */
    private DateOrMonth fieldworkEnd;
    /**
     * The fieldwork start.
     */
    private DateOrMonth fieldworkStart;
    /**
     * The effective sample size.
     */
    private Integer effectiveSampleSize;
    /**
     * The main response scenario.
     */
    private ResponseScenario mainResponseScenario;
    /**
     * The result for no responses.
     */
    private ResultValue noResponses;
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
    private Scope scope;

    /**
     * Constructor using a builder instance as its parameter.
     *
     * @param builder A builder instance.
     */
    private OpinionPoll(final Builder builder) {
        area = builder.area;
        commissioners = Collections.unmodifiableSet(builder.commissioners);
        excluded = builder.excluded;
        fieldworkEnd = builder.fieldworkEnd;
        fieldworkStart = builder.fieldworkStart;
        mainResponseScenario = builder.responseScenarioBuilder.build();
        noResponses = builder.noResponses;
        other = builder.other;
        pollingFirm = builder.pollingFirm;
        pollingFirmPartner = builder.pollingFirmPartner;
        publicationDate = builder.publicationDate;
        sampleSize = builder.sampleSize;
        sampleSizeValue = sampleSize == null ? 0 : Integer.parseInt(sampleSize);
        if (sampleSize != null) {
            if (excluded == null) {
                effectiveSampleSize = sampleSizeValue;
            } else {
                effectiveSampleSize = Math.round(sampleSizeValue * (1F - excluded.getValue() / HUNDRED));
            }
        }
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
         * The share of excluded responses.
         */
        private DecimalNumber excluded;
        /**
         * The fieldwork end.
         */
        private DateOrMonth fieldworkEnd;
        /**
         * The fieldwork start.
         */
        private DateOrMonth fieldworkStart;
        /**
         * The result for no responses.
         */
        private ResultValue noResponses;
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
        private Scope scope;

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
         * Sets the share of excluded responses.
         *
         * @param excludedShare The share of the excluded responses.
         * @return This build instance.
         */
        public Builder setExcluded(final DecimalNumber excludedShare) {
            this.excluded = excludedShare;
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
         * Sets the number of no responses.
         *
         * @param noResponsesString The result for other.
         * @return This builder instance.
         */
        public Builder setNoResponses(final ResultValue noResponsesString) {
            this.noResponses = noResponsesString;
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
         * @param theScope The scope.
         * @return This builder instance.
         */
        public Builder setScope(final Scope theScope) {
            this.scope = theScope;
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
                    && equalsOrBothNull(excluded, otherOpinionPoll.excluded)
                    && equalsOrBothNull(fieldworkEnd, otherOpinionPoll.fieldworkEnd)
                    && equalsOrBothNull(fieldworkStart, otherOpinionPoll.fieldworkStart)
                    && otherOpinionPoll.mainResponseScenario.equals(mainResponseScenario)
                    && equalsOrBothNull(noResponses, otherOpinionPoll.noResponses)
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
     * Returns the effective sample size. The effective sample size is the sample size minus the excluded responses.
     *
     * @return The effective sample size.
     */
    public Integer getEffectiveSampleSize() {
        return effectiveSampleSize;
    }

    /**
     * Returns the electoral lists.
     *
     * @return The electoral lists.
     */
    public Set<ElectoralList> getElectoralLists() {
        return mainResponseScenario.getElectoralLists();
    }

    /**
     * Returns the share of excluded responses.
     *
     * @return The share of excluded responses.
     */
    public DecimalNumber getExcluded() {
        return excluded;
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
     * Returns the main response scenario.
     *
     * @return The main response scenario.
     */
    public ResponseScenario getMainResponseScenario() {
        return mainResponseScenario;
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
    public Scope getScope() {
        return scope;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alternativeResponseScenarios, area, commissioners, excluded, fieldworkEnd, fieldworkStart,
                mainResponseScenario, noResponses, other, pollingFirm, pollingFirmPartner, publicationDate, sampleSize,
                scope);
    }
}
