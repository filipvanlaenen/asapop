package net.filipvanlaenen.asapop.yaml;

import java.util.Map;
import java.util.Set;

public class FirstRoundAnalysis {
    private Map<Set<String>, Double> probabilityMassFunction;

    public Map<Set<String>, Double> getProbabilityMassFunction() {
        return probabilityMassFunction;
    }

    public void setProbabilityMassFunction(final Map<Set<String>, Double> probabilityMassFunction) {
        this.probabilityMassFunction = probabilityMassFunction;
    }
}
