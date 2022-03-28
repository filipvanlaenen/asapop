package net.filipvanlaenen.asapop.yaml;

import java.util.Set;

public class FirstRoundResultProbabilityMass {
    private Set<String> electoralLists;
    private Double probabilityMass;

    public Set<String> getElectoralLists() {
        return electoralLists;
    }

    public void setElectoralLists(Set<String> electoralLists) {
        this.electoralLists = electoralLists;
    }

    public Double getProbabilityMass() {
        return probabilityMass;
    }

    public void setProbabilityMass(Double probabilityMass) {
        this.probabilityMass = probabilityMass;
    }
}
