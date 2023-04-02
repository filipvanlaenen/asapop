package net.filipvanlaenen.asapop.model;

public interface SampleSize {
    record ExactSampleSize(int value) implements SampleSize {
        public int getMinimalValue() {
            return value;
        }

        public String getText() {
            return Integer.toString(value);
        }
    }

    record MinimalSampleSize(int value) implements SampleSize {
        public int getMinimalValue() {
            return value;
        }

        public String getText() {
            return "≥" + Integer.toString(value);
        }
    }

    record SampleSizeRange(int lowerBound, int upperBound) implements SampleSize {
        public int getMinimalValue() {
            return lowerBound;
        }

        public String getText() {
            return Integer.toString(lowerBound) + "–" + Integer.toString(upperBound);
        }
    }

    public static SampleSize parse(String text) {
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

    int getMinimalValue();

    String getText();
}
