package net.filipvanlaenen.asapop.yaml;

import java.util.Map;
import java.util.Set;

public class FirstRoundAnalysis {
    private Set<FirstRoundResultProbabilityMass> probabilityMassFunction;

    public Set<FirstRoundResultProbabilityMass> getProbabilityMassFunction() {
        return probabilityMassFunction;
    }

    public void setProbabilityMassFunction(final Set<FirstRoundResultProbabilityMass> probabilityMassFunction) {
        this.probabilityMassFunction = probabilityMassFunction;
    }
}
