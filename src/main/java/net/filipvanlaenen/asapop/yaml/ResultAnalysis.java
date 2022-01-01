package net.filipvanlaenen.asapop.yaml;

import java.util.Map;

public class ResultAnalysis {
    private Map<Integer, Float[]> confidenceIntervals;
    private Float median;

    public Map<Integer, Float[]> getConfidenceIntervals() {
        return confidenceIntervals;
    }

    public Float getMedian() {
        return median;
    }

    public void setMedian(Float median) {
        this.median = median;
    }

    public void setConfidenceIntervals(Map<Integer, Float[]> confidenceIntervals) {
        this.confidenceIntervals = confidenceIntervals;
    }
}
