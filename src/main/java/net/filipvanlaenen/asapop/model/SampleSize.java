package net.filipvanlaenen.asapop.model;

/**
 * Interface defining a sample size.
 */
public interface SampleSize {
    /**
     * Record class for exact sample sizes.
     *
     * @param value The exact value of the sample size.
     */
    record ExactSampleSize(int value) implements SampleSize {
        @Override
        public int getMinimalValue() {
            return value;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    /**
     * Record class for minimal sample sizes.
     *
     * @param value The minimal value of the sample size.
     */
    record MinimalSampleSize(int value) implements SampleSize {
        @Override
        public int getMinimalValue() {
            return value;
        }

        @Override
        public String toString() {
            return "≥" + value;
        }
    }

    /**
     * Record class for a sample size range.
     *
     * @param lowerBound The lower bound of the sample size range.
     * @param upperBound The upper bound of the sample size range.
     */
    record SampleSizeRange(int lowerBound, int upperBound) implements SampleSize {
        @Override
        public int getMinimalValue() {
            return lowerBound;
        }

        @Override
        public String toString() {
            return lowerBound + "–" + upperBound;
        }
    }

    /**
     * Parses a text into a sample size.
     *
     * @param text The text to parse.
     * @return Returns a sample size instance.
     */
    static SampleSize parse(final String text) {
        if (text.startsWith("≥")) {
            return new MinimalSampleSize(Integer.parseInt(text.substring(1)));
        } else if (text.contains("–")) {
            int index = text.indexOf("–");
            return new SampleSizeRange(Integer.parseInt(text.substring(0, index)),
                    Integer.parseInt(text.substring(index + 1)));
        } else {
            return new ExactSampleSize(Integer.parseInt(text));
        }
    }

    /**
     * Returns the minimal value for the sample size.
     *
     * @return The minimal value for the sample size.
     */
    int getMinimalValue();
}
