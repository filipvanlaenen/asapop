package net.filipvanlaenen.asapop.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing a response scenario.
 */
public final class ResponseScenario {
    /**
     * The area.
     */
    private String area;
    /**
     * The result for other.
     */
    private String other;
    /**
     * The results.
     */
    private final Map<ElectoralList, String> results;
    /**
     * The scope.
     */
    private String scope;

    /**
     * Constructor using a builder instance as its parameter.
     *
     * @param builder A builder instance.
     */
    private ResponseScenario(final Builder builder) {
        this.area = builder.area;
        this.other = builder.other;
        this.results = Collections.unmodifiableMap(builder.results);
        this.scope = builder.scope;
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
         * The result for other.
         */
        private String other;
        /**
         * The results.
         */
        private final Map<ElectoralList, String> results = new HashMap<ElectoralList, String>();
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
        public Builder addResult(final String electoralListKey, final String result) {
            results.put(ElectoralList.get(electoralListKey), result);
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
         * Sets the result for other.
         *
         * @param otherString The result for other.
         * @return This builder instance.
         */
        public Builder setOther(final String otherString) {
            this.other = otherString;
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
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ResponseScenario) {
            ResponseScenario otherResponseScenario = (ResponseScenario) obj;
            return equalsOrBothNull(area, otherResponseScenario.area)
                   && equalsOrBothNull(other, otherResponseScenario.other)
                   && otherResponseScenario.results.equals(results)
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
     * Returns the result for other.
     *
     * @return The result for other.
     */
    public String getOther() {
        return other;
    }

    /**
     * Returns the result for an electoral list.
     *
     * @param electoralListKey The key of an electoral list.
     * @return The result for the electoral list.
     */
    public String getResult(final String electoralListKey) {
        return results.get(ElectoralList.get(electoralListKey));
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
        return Objects.hash(area, other, results, scope);
    }
}
