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
     * The area.
     */
    private String area;
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
    private String sampleSize;
    /**
     * The scope.
     */
    private Scope scope;

    /**
     * Constructor using a builder instance as its parameter.
     *
     * @param builder A builder instance.
     */
    private ResponseScenario(final Builder builder) {
        area = builder.area;
        noResponses = builder.noResponses;
        other = builder.other;
        results = Collections.unmodifiableMap(builder.results);
        sampleSize = builder.sampleSize;
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
        private String sampleSize;
        /**
         * The scope.
         */
        private Scope scope;

        /**
         * Adds a result.
         *
         * @param electoralListKeys The keys of a set of electoral lists.
         * @param resultValue       The result value.
         * @return This builder instance.
         */
        public Builder addResult(final Set<String> electoralListKeys, final ResultValue resultValue) {
            results.put(ElectoralList.get(electoralListKeys), resultValue);
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
            return addWellformedResult(Set.of(electoralListKey), wellformedResult);
        }

        /**
         * Adds a result to the response scenario builder.
         *
         * @param electoralListKeys The keys of a set of electoral lists.
         * @param wellformedResult  The result value as a text, assumed to be well-formed.
         * @return This builder instance.
         */
        public Builder addWellformedResult(final Set<String> electoralListKeys, final String wellformedResult) {
            return addResult(electoralListKeys, new ResultValue(wellformedResult));
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
         * Returns whether an area has been registered in this builder instance.
         *
         * @return True if an area has been registered in this builder instance.
         */
        public boolean hasArea() {
            return area != null;
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
         * Verifies whether the results add up. The results add up if their sum is within the interval of rounding
         * errors, defined as 100 ± floor((n - 1) / 2) × precision..
         *
         * @return True if the sum of results is within the interval of rounding errors.
         */
        public boolean resultsAddUp() {
            int n = results.size() + (hasOther() ? 1 : 0) + (hasNoResponses() ? 1 : 0);
            Precision precision = Precision.highest(results.values());
            double sum = 0D;
            for (ResultValue resultValue : results.values()) {
                Double value = resultValue.getNominalValue();
                if (value != null) {
                    sum += value;
                }
            }
            if (hasOther()) {
                precision = Precision.highest(precision, other.getPrecision());
                Double value = other.getNominalValue();
                if (value != null) {
                    sum += value;
                }
            }
            if (hasNoResponses()) {
                precision = Precision.highest(precision, noResponses.getPrecision());
                Double value = noResponses.getNominalValue();
                if (value != null) {
                    sum += value;
                }
            }
            double epsilon = ((n - 1) / 2) * precision.getValue();
            return (100D - epsilon <= sum || !hasOther() || !hasNoResponses()) && sum <= 100D + epsilon;
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
         * Sets the result for no responses.
         *
         * @param noResponsesString The result for no responses, assumed to be well-formed.
         * @return This builder instance.
         */
        public Builder setWellformedNoResponses(final String noResponsesString) {
            return setNoResponses(new ResultValue(noResponsesString));
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

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ResponseScenario) {
            ResponseScenario otherResponseScenario = (ResponseScenario) obj;
            return equalsOrBothNull(area, otherResponseScenario.area)
                    && equalsOrBothNull(noResponses, otherResponseScenario.noResponses)
                    && equalsOrBothNull(other, otherResponseScenario.other)
                    && otherResponseScenario.results.equals(results)
                    && equalsOrBothNull(sampleSize, otherResponseScenario.sampleSize)
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
     * Returns the area.
     *
     * @return The area.
     */
    public String getArea() {
        return area;
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
     * @param electoralListKeys The keys of a set of electoral lists.
     * @return The result for the set of electoral lists.
     */
    public ResultValue getResult(final Set<String> electoralListKeys) {
        return results.get(ElectoralList.get(electoralListKeys));
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
    public String getSampleSize() {
        return sampleSize;
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
        return Objects.hash(area, noResponses, other, results, sampleSize, scope);
    }
}
