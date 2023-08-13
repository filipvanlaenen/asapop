package net.filipvanlaenen.asapop.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.filipvanlaenen.asapop.model.ResultValue.Precision;

/**
 * Class representing a response scenario.
 */
public final class ResponseScenario {
    /**
     * The magic number hundred.
     */
    private static final float HUNDRED = 100F;
    /**
     * The area.
     */
    private String area;
    /**
     * The effective sample size.
     */
    private Integer effectiveSampleSize;
    /**
     * The share of excluded responses.
     */
    private DecimalNumber excluded;
    /**
     * The result for no responses.
     */
    private ResultValue noResponses;
    /**
     * The result for other.
     */
    private ResultValue other;
    /**
     * The results.
     */
    private final Map<Set<ElectoralList>, ResultValue> results;
    /**
     * The sample size.
     */
    private SampleSize sampleSize;
    /**
     * The sample size value.
     */
    private Integer sampleSizeValue;
    /**
     * The scale.
     */
    private double scale;
    /**
     * The scope.
     */
    private Scope scope;
    /**
     * Whether the results add up to 100 percent within rounding error.
     */
    private boolean strictlyWithinRoundingError;
    /**
     * The verified sum.
     */
    private DecimalNumber verifiedSum;

    /**
     * Constructor using a builder instance as its parameter.
     *
     * @param builder A builder instance.
     */
    private ResponseScenario(final Builder builder) {
        area = builder.area;
        excluded = builder.excluded;
        noResponses = builder.noResponses;
        other = builder.other;
        results = Collections.unmodifiableMap(builder.results);
        sampleSize = builder.sampleSize;
        sampleSizeValue = sampleSize == null ? null : sampleSize.getMinimalValue();
        if (sampleSize != null) {
            if (excluded == null) {
                effectiveSampleSize = sampleSizeValue;
            } else {
                effectiveSampleSize = Math.round(sampleSizeValue * (1F - excluded.value() / HUNDRED));
            }
        }
        scale = builder.calculateScale();
        scope = builder.scope;
        strictlyWithinRoundingError = builder.resultsAddStrictlyUp();
        verifiedSum = builder.verifiedSum;
    }

    /**
     * Builder class.
     */
    public static class Builder {
        /**
         * The magic number one hundred.
         */
        private static final double ONE_HUNDRED = 100D;
        /**
         * The area.
         */
        private String area;
        /**
         * The share of excluded responses.
         */
        private DecimalNumber excluded;
        /**
         * The result for no responses.
         */
        private ResultValue noResponses;
        /**
         * The result for other.
         */
        private ResultValue other;
        /**
         * The results.
         */
        private final Map<Set<ElectoralList>, ResultValue> results = new HashMap<Set<ElectoralList>, ResultValue>();
        /**
         * The sample size.
         */
        private SampleSize sampleSize;
        /**
         * The scope.
         */
        private Scope scope;
        /**
         * The verified sum.
         */
        private DecimalNumber verifiedSum;

        /**
         * Adds a result.
         *
         * @param electoralLists The set of electoral lists.
         * @param resultValue    The result value.
         * @return This builder instance.
         */
        public Builder addResult(final Set<ElectoralList> electoralLists, final ResultValue resultValue) {
            results.put(electoralLists, resultValue);
            return this;
        }

        /**
         * Builds a response scenario based on the current state of the builder instance.
         *
         * @return The resulting response scenario.
         */
        public ResponseScenario build() {
            return new ResponseScenario(this);
        }

        /**
         * Calculates the scale. If the results add up, the scale is 1D, and otherwise the sum of all results.
         *
         * @return The scale.
         */
        Double calculateScale() {
            if (!resultsAddUp()) {
                return calculateResultsAndOtherSum() / ONE_HUNDRED;
            } else if (hasNoResponses()) {
                return 1D - noResponses.getNominalValue() / ONE_HUNDRED;
            } else {
                return 1D;
            }
        }

        /**
         * Calculates the sum of the results and other.
         *
         * @return The sum of the results and other.
         */
        private double calculateResultsAndOtherSum() {
            double sum = 0D;
            for (ResultValue resultValue : results.values()) {
                Double value = resultValue.getNominalValue();
                if (value != null) {
                    sum += value;
                }
            }
            if (hasOther()) {
                Double value = other.getNominalValue();
                if (value != null) {
                    sum += value;
                }
            }
            return sum;
        }

        /**
         * Calculates the sum.
         *
         * @return The sum.
         */
        private double calculateSum() {
            double sum = calculateResultsAndOtherSum();
            if (hasNoResponses()) {
                Double value = noResponses.getNominalValue();
                if (value != null) {
                    sum += value;
                }
            }
            return sum;
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
         * Returns whether excluded has been registered in this builder instance.
         *
         * @return True if excluded has been registered in this builder instance.
         */
        public boolean hasExcluded() {
            return excluded != null;
        }

        /**
         * Returns whether no responses has been registered in this builder instance.
         *
         * @return True if no responses has been registered in this builder instance.
         */
        public boolean hasNoResponses() {
            return noResponses != null;
        }

        /**
         * Returns whether other has been registered in this builder instance.
         *
         * @return True if other has been registered in this builder instance.
         */
        public boolean hasOther() {
            return other != null;
        }

        /**
         * Returns whether any results have been registered in this builder instance.
         *
         * @return True if at least one result has been registered in this builder instance.
         */
        public boolean hasResults() {
            return !results.isEmpty();
        }

        /**
         * Returns whether a sample size has been registered in this builder instance.
         *
         * @return True if a sample size has been registered in this builder instance.
         */
        public boolean hasSampleSize() {
            return sampleSize != null;
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
         * Returns whether a verified sum has been registered in this builder instance.
         *
         * @return True if a verified sum has been registered in this builder instance.
         */
        public boolean hasVerifiedSum() {
            return verifiedSum != null;
        }

        /**
         * Verifies whether the results add up. The results add up if their sum is equal to the verified sum, if one is
         * provided, or within the interval of rounding errors, defined as 100 ± floor((n - 1) / 2) × precision, or the
         * sum is below 100 and either other or no responses is missing.
         *
         * @return True if the sum of results is within the interval of rounding errors.
         */
        public boolean resultsAddUp() {
            return resultsAddUp(false);
        }

        /**
         * Verifies whether the results add up. The results add up if their sum is equal to the verified sum, if one is
         * provided, or within the interval of rounding errors, defined as 100 ± floor((n - 1) / 2) × precision. If they
         * don't need to add strictly up, the sum can be below 100 if other or no responses is missing.
         *
         * @param strictly True if the results should add up strictly.
         * @return True if the sum of results is within the interval of rounding errors.
         */
        private boolean resultsAddUp(final boolean strictly) {
            double sum = calculateSum();
            Precision precision = Precision.getHighestPrecision(results.values());
            if (hasOther()) {
                precision = Precision.highest(precision, other.getPrecision());
            }
            if (hasNoResponses()) {
                precision = Precision.highest(precision, noResponses.getPrecision());
            }
            if (hasVerifiedSum()) {
                // EQMU: Changing the conditional boundary below produces an equivalent mutant.
                return Math.abs(sum - verifiedSum.value()) < precision.getValue() / HUNDRED;
            } else {
                int n = results.size() + (hasOther() ? 1 : 0) + (hasNoResponses() ? 1 : 0);
                double epsilon = ((n - 1) / 2) * precision.getValue();
                return (ONE_HUNDRED - epsilon <= sum || !strictly && (!hasOther() || !hasNoResponses()))
                        && sum <= ONE_HUNDRED + epsilon;
            }
        }

        /**
         * Verifies whether the results add strictly up. The results add up if their sum is equal to the verified sum,
         * if one is provided, or within the interval of rounding errors, defined as 100 ± floor((n - 1) / 2) ×
         * precision.
         *
         * @return True if the sum of results is within the interval of rounding errors.
         */
        public boolean resultsAddStrictlyUp() {
            return resultsAddUp(true);
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
         * Sets the sample size.
         *
         * @param theSampleSize The sample size.
         * @return This builder instance.
         */
        public Builder setSampleSize(final SampleSize theSampleSize) {
            this.sampleSize = theSampleSize;
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
         * Sets the verified sum.
         *
         * @param theVerifiedSum The verified sum.
         * @return This builder instance.
         */
        public Builder setVerifiedSum(final DecimalNumber theVerifiedSum) {
            this.verifiedSum = theVerifiedSum;
            return this;
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ResponseScenario) {
            ResponseScenario otherResponseScenario = (ResponseScenario) obj;
            return equalsOrBothNull(area, otherResponseScenario.area)
                    && equalsOrBothNull(excluded, otherResponseScenario.excluded)
                    && equalsOrBothNull(noResponses, otherResponseScenario.noResponses)
                    && equalsOrBothNull(other, otherResponseScenario.other)
                    && otherResponseScenario.results.equals(results)
                    && equalsOrBothNull(sampleSize, otherResponseScenario.sampleSize)
                    && equalsOrBothNull(scope, otherResponseScenario.scope)
                    && equalsOrBothNull(verifiedSum, otherResponseScenario.verifiedSum);
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
     * Returns the area.
     *
     * @return The area.
     */
    public String getArea() {
        return area;
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
     * Returns the sets of electoral lists.
     *
     * @return The sets of electoral lists.
     */
    public Set<Set<ElectoralList>> getElectoralListSets() {
        return results.keySet();
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
     * Returns the result for no responses.
     *
     * @return The result for no responses.
     */
    public ResultValue getNoResponses() {
        return noResponses;
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
     * Returns the result for a set of electoral lists.
     *
     * @param electoralListIds The IDs of a set of electoral lists.
     * @return The result for the set of electoral lists.
     */
    public ResultValue getResult(final Set<String> electoralListIds) {
        return results.get(ElectoralList.get(electoralListIds));
    }

    /**
     * Returns all the results.
     *
     * @return All the results.
     */
    public Collection<ResultValue> getResults() {
        return results.values();
    }

    /**
     * Returns the sample size.
     *
     * @return The sample size.
     */
    public SampleSize getSampleSize() {
        return sampleSize;
    }

    /**
     * Returns the sample size value.
     *
     * @return The sample size value.
     */
    public Integer getSampleSizeValue() {
        return sampleSizeValue;
    }

    /**
     * Returns the scale.
     *
     * @return The scale.
     */
    public Double getScale() {
        return scale;
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
     * Returns the verified sum.
     *
     * @return The verified sum.
     */
    public DecimalNumber getVerifiedSum() {
        return verifiedSum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(area, excluded, noResponses, other, results, sampleSize, scope, verifiedSum);
    }

    /**
     * Returns true if the results add up to 100 percent within rounding error.
     *
     * @return True if the results add up to 100 percent within rounding error.
     */
    public boolean isStrictlyWithinRoundingError() {
        return strictlyWithinRoundingError;
    }
}
