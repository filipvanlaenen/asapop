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
    private DateMonthOrYear fieldworkEnd;
    /**
     * The fieldwork start.
     */
    private DateMonthOrYear fieldworkStart;
    /**
     * The main response scenario.
     */
    private ResponseScenario mainResponseScenario;
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
        fieldworkEnd = builder.fieldworkEnd;
        fieldworkStart = builder.fieldworkStart;
        mainResponseScenario = builder.responseScenarioBuilder.build();
        pollingFirm = builder.pollingFirm;
        pollingFirmPartner = builder.pollingFirmPartner;
        publicationDate = builder.publicationDate;
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
        private DateMonthOrYear fieldworkEnd;
        /**
         * The fieldwork start.
         */
        private DateMonthOrYear fieldworkStart;
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
         * @param electoralLists The set of electoral lists.
         * @param resultValue    The result value.
         * @return This builder instance.
         */
        public Builder addResult(final Set<ElectoralList> electoralLists, final ResultValue resultValue) {
            responseScenarioBuilder.addResult(electoralLists, resultValue);
            return this;
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
         * Returns the sum.
         *
         * @return The sum.
         */
        public double getSum() {
            return responseScenarioBuilder.getSum();
        }

        /**
         * Returns whether an area has been registered in this builder instance.
         *
         * @return True if an area has been registered in this builder instance.
         */
        public boolean hasArea() {
            return area != null;
        }

        /**
         * Returns whether the dates are consistent. The dates are consistent when the fieldwork start is before or
         * equal to the fieldwork end, and when the fieldwork start and end are before or equal to the publication date.
         *
         * @return True if the dates are consistent.
         */
        public boolean hasConsistentDates() {
            if (fieldworkStart == null) {
                if (fieldworkEnd == null) {
                    return publicationDate != null;
                } else if (publicationDate == null) {
                    return true;
                } else {
                    return !fieldworkEnd.getStart().isAfter(publicationDate);
                }
            } else {
                if (fieldworkEnd == null) {
                    if (publicationDate == null) {
                        return true;
                    } else {
                        return !fieldworkStart.getStart().isAfter(publicationDate);
                    }
                } else {
                    if (publicationDate == null) {
                        return !fieldworkStart.getStart().isAfter(fieldworkEnd.getEnd());
                    } else {
                        return !fieldworkStart.getStart().isAfter(fieldworkEnd.getEnd())
                                && !fieldworkStart.getStart().isAfter(publicationDate)
                                && !fieldworkEnd.getStart().isAfter(publicationDate);
                    }
                }
            }
        }

        /**
         * Returns whether any date has been registered in this builder instance.
         *
         * @return True if at least one date has been registered in this builder instance.
         */
        public boolean hasDates() {
            return hasFieldworkEnd() || hasFieldworkStart() || hasPublicationDate();
        }

        /**
         * Returns whether excluded has been registered in this builder instance.
         *
         * @return True if excluded has been registered in this builder instance.
         */
        public boolean hasExcluded() {
            return responseScenarioBuilder.hasExcluded();
        }

        /**
         * Returns whether a fieldwork end has been registered in this builder instance.
         *
         * @return True if a fieldwork end has been registered in this builder instance.
         */
        public boolean hasFieldworkEnd() {
            return fieldworkEnd != null;
        }

        /**
         * Returns whether a fieldwork start has been registered in this builder instance.
         *
         * @return True if a fieldwork start has been registered in this builder instance.
         */
        public boolean hasFieldworkStart() {
            return fieldworkStart != null;
        }

        /**
         * Returns whether no responses has been registered in this builder instance.
         *
         * @return True if no responses has been registered in this builder instance.
         */
        public boolean hasNoResponses() {
            return responseScenarioBuilder.hasNoResponses();
        }

        /**
         * Returns whether other has been registered in this builder instance.
         *
         * @return True if other has been registered in this builder instance.
         */
        public boolean hasOther() {
            return responseScenarioBuilder.hasOther();
        }

        /**
         * Returns whether other and no responses combined has been registered in this builder instance.
         *
         * @return True if other and no responses combined has been registered in this builder instance.
         */
        public boolean hasOtherAndNoResponses() {
            return responseScenarioBuilder.hasOtherAndNoResponses();
        }

        /**
         * Returns whether a polling firm has been registered in this builder instance.
         *
         * @return True if a polling firm has been registered in this builder instance.
         */
        public boolean hasPollingFirm() {
            return pollingFirm != null;
        }

        /**
         * Returns whether a polling firm and/or a commissioner has been registered in this builder instance.
         *
         * @return True if a polling firm or a commissioner has been registered in this builder instance.
         */
        public boolean hasPollingFirmOrCommissioner() {
            return hasPollingFirm() || !commissioners.isEmpty();
        }

        /**
         * Returns whether a polling firm partner has been registered in this builder instance.
         *
         * @return True if a polling firm partner has been registered in this builder instance.
         */
        public boolean hasPollingFirmPartner() {
            return pollingFirmPartner != null;
        }

        /**
         * Returns whether a publication date has been registered in this builder instance.
         *
         * @return True if a publication date has been registered in this builder instance.
         */
        public boolean hasPublicationDate() {
            return publicationDate != null;
        }

        /**
         * Returns whether any results have been registered in this builder instance.
         *
         * @return True if at least one result has been registered in this builder instance.
         */
        public boolean hasResults() {
            return responseScenarioBuilder.hasResults();
        }

        /**
         * Returns whether a sample size has been registered in this builder instance.
         *
         * @return True if a sample size has been registered in this builder instance.
         */
        public boolean hasSampleSize() {
            return responseScenarioBuilder.hasSampleSize();
        }

        /**
         * Returns whether a scope has been registered in this builder instance.
         *
         * @return True if a scope has been registered in this builder instance.
         */
        public boolean hasScope() {
            return scope != null;
        }

        /**
         * Returns whether a unit has been registered in this builder instance.
         *
         * @return True if a unit has been registered in this builder instance.
         */
        public boolean hasUnit() {
            return responseScenarioBuilder.hasUnit();
        }

        /**
         * Returns whether a verified sum has been registered in this builder instance.
         *
         * @return True if a verified sum has been registered in this builder instance.
         */
        public boolean hasVerifiedSum() {
            return responseScenarioBuilder.hasVerifiedSum();
        }

        /**
         * Verifies whether the results add up. The results add up if their sum is equal to the verified sum, if one is
         * provided, or within the interval of rounding errors, or the sum is below 100 and either other or other and no
         * responses is missing.
         *
         * @return True if the sum of results is within the interval of rounding errors.
         */
        public boolean resultsAddUp() {
            return responseScenarioBuilder.resultsAddUp();
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
            responseScenarioBuilder.setExcluded(excludedShare);
            return this;
        }

        /**
         * Sets the fieldwork end date.
         *
         * @param fieldworkEndDate The fieldwork end date.
         * @return This builder instance.
         */
        public Builder setFieldworkEnd(final DateMonthOrYear fieldworkEndDate) {
            this.fieldworkEnd = fieldworkEndDate;
            return this;
        }

        /**
         * Sets the fieldwork start date.
         *
         * @param fieldworkStartDate The fieldwork start date.
         * @return This builder instance.
         */
        public Builder setFieldworkStart(final DateMonthOrYear fieldworkStartDate) {
            this.fieldworkStart = fieldworkStartDate;
            return this;
        }

        /**
         * Sets the number of no responses.
         *
         * @param noResponsesString The result for other.
         * @return This builder instance.
         */
        public Builder setNoResponses(final ResultValue noResponsesString) {
            responseScenarioBuilder.setNoResponses(noResponsesString);
            return this;
        }

        /**
         * Sets the result for other.
         *
         * @param otherString The result for other.
         * @return This builder instance.
         */
        public Builder setOther(final ResultValue otherString) {
            responseScenarioBuilder.setOther(otherString);
            return this;
        }

        /**
         * Sets the result for other and no responses combined.
         *
         * @param otherAndNoResponsesString The result for other and no responses combined.
         * @return This builder instance.
         */
        public Builder setOtherAndNoResponses(final ResultValue otherAndNoResponsesString) {
            responseScenarioBuilder.setOtherAndNoResponses(otherAndNoResponsesString);
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
         * Sets the publication date.
         *
         * @param thePublicationDate The publication date.
         * @return This builder instance.
         */
        public Builder setPublicationDate(final LocalDate thePublicationDate) {
            this.publicationDate = thePublicationDate;
            return this;
        }

        /**
         * Sets the sample size.
         *
         * @param sampleSize The sample size.
         * @return This builder instance.
         */
        public Builder setSampleSize(final SampleSize sampleSize) {
            responseScenarioBuilder.setSampleSize(sampleSize);
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
         * Sets the unit.
         *
         * @param theUnit The unit.
         * @return This builder instance.
         */
        public Builder setUnit(final Unit theUnit) {
            responseScenarioBuilder.setUnit(theUnit);
            return this;
        }

        /**
         * Sets the verified sum.
         *
         * @param theVerifiedSum The verified sum.
         * @return This builder instance.
         */
        public Builder setVerifiedSum(final DecimalNumber theVerifiedSum) {
            responseScenarioBuilder.setVerifiedSum(theVerifiedSum);
            return this;
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
                    && equalsOrBothNull(pollingFirm, otherOpinionPoll.pollingFirm)
                    && equalsOrBothNull(pollingFirmPartner, otherOpinionPoll.pollingFirmPartner)
                    && equalsOrBothNull(publicationDate, otherOpinionPoll.publicationDate)
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
        return mainResponseScenario.getEffectiveSampleSize();
    }

    /**
     * Returns the sets of electoral lists.
     *
     * @return The sets of electoral lists.
     */
    public Set<Set<ElectoralList>> getElectoralListSets() {
        return mainResponseScenario.getElectoralListSets();
    }

    /**
     * Returns the end date for sorting.
     *
     * @return The end date of the opinion poll, either the fieldwork end date or the publication date.
     */
    public LocalDate getEndDate() {
        if (fieldworkEnd == null) {
            return publicationDate;
        } else {
            return fieldworkEnd.getEnd();
        }
    }

    /**
     * Returns the share of excluded responses.
     *
     * @return The share of excluded responses.
     */
    public DecimalNumber getExcluded() {
        return mainResponseScenario.getExcluded();
    }

    /**
     * Returns the fieldwork end date.
     *
     * @return The fieldwork end date.
     */
    public DateMonthOrYear getFieldworkEnd() {
        return fieldworkEnd;
    }

    /**
     * Returns the fieldwork start date.
     *
     * @return The fieldwork start date.
     */
    public DateMonthOrYear getFieldworkStart() {
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
     * Returns the result for no responses.
     *
     * @return The result for no responses.
     */
    public ResultValue getNoResponses() {
        return mainResponseScenario.getNoResponses();
    }

    /**
     * Returns the number of response scenarios.
     *
     * @return The number of response scenarios.
     */
    public int getNumberOfResponseScenarios() {
        return 1 + alternativeResponseScenarios.size();
    }

    /**
     * Returns the number of result values.
     *
     * @return The number of result values.
     */
    public int getNumberOfResultValues() {
        int result = mainResponseScenario.getResults().size();
        for (ResponseScenario responseScenario : alternativeResponseScenarios) {
            result += responseScenario.getResults().size();
        }
        return result;
    }

    /**
     * Returns the result for other.
     *
     * @return The result for other.
     */
    public ResultValue getOther() {
        return mainResponseScenario.getOther();
    }

    /**
     * Returns the result for other and no responses combined.
     *
     * @return The result for other and no responses combined.
     */
    public ResultValue getOtherAndNoResponses() {
        return mainResponseScenario.getOtherAndNoResponses();
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
     * Returns the result for a set of electoral lists.
     *
     * @param electoralListIds The IDs of a set of electoral lists.
     * @return The result for the set of electoral lists.
     */
    public ResultValue getResult(final Set<String> electoralListIds) {
        return mainResponseScenario.getResult(electoralListIds);
    }

    /**
     * Returns the sample size.
     *
     * @return The sample size.
     */
    public SampleSize getSampleSize() {
        return mainResponseScenario.getSampleSize();
    }

    /**
     * Returns the sample size value.
     *
     * @return The sample size value.
     */
    public Integer getSampleSizeValue() {
        return mainResponseScenario.getSampleSizeValue();
    }

    /**
     * Returns the scale.
     *
     * @return The scale.
     */
    public Double getScale() {
        return mainResponseScenario.getScale();
    }

    /**
     * Returns the scope.
     *
     * @return The scope.
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * Returns the unit.
     *
     * @return The unit.
     */
    public Unit getUnit() {
        return mainResponseScenario.getUnit();
    }

    /**
     * Returns the verified sum.
     *
     * @return The verified sum.
     */
    public DecimalNumber getVerifiedSum() {
        return mainResponseScenario.getVerifiedSum();
    }

    @Override
    public int hashCode() {
        return Objects.hash(alternativeResponseScenarios, area, commissioners, fieldworkEnd, fieldworkStart,
                mainResponseScenario, pollingFirm, pollingFirmPartner, publicationDate, scope);
    }
}
