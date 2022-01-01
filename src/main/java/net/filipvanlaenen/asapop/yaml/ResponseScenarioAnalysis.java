package net.filipvanlaenen.asapop.yaml;

import java.util.Map;

public class ResponseScenarioAnalysis {
    private String area;
    private ResultAnalysis otherAnalysis;
    private Map<String, ResultAnalysis> resultAnalyses;
    private String scope;

    public String getArea() {
        return area;
    }

    public ResultAnalysis getOtherAnalysis() {
        return otherAnalysis;
    }

    public Map<String, ResultAnalysis> getResultAnalyses() {
        return resultAnalyses;
    }

    public String getScope() {
        return scope;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setOtherAnalysis(ResultAnalysis otherAnalysis) {
        this.otherAnalysis = otherAnalysis;
    }

    public void setResultAnalyses(Map<String, ResultAnalysis> resultAnalyses) {
        this.resultAnalyses = resultAnalyses;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
