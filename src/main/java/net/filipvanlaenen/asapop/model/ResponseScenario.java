package net.filipvanlaenen.asapop.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
    private final Map<ElectoralList, ResultValue> results;
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
        private final Map<ElectoralList, ResultValue> results = new HashMap<ElectoralList, ResultValue>();
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
         * @param electoralListKey The key of an electoral list.
         * @param resultValue      The result value.
         * @return This builder instance.
         */
        public Builder addResult(final String electoralListKey, final ResultValue resultValue) {
            results.put(ElectoralList.get(electoralListKey), resultValue);
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
         * Builds a response scenario based on the current state of the builder instance.
         *
         * @return The resulting response scenario.
         */
        public ResponseScenario build() {
            return new ResponseScenario(this);
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
     * Returns the electoral lists.
     *
     * @return The electoral lists.
     */
    public Set<ElectoralList> getElectoralLists() {
        return results.keySet();
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
     * Returns the result for an electoral list.
     *
     * @param electoralListKey The key of an electoral list.
     * @return The result for the electoral list.
     */
    public ResultValue getResult(final String electoralListKey) {
        return results.get(ElectoralList.get(electoralListKey));
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
