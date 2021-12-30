package net.filipvanlaenen.asapop.yaml;

import java.util.List;
import java.util.Set;

public class OpinionPollAnalysis {
    private Set<String> commissioners;
    private String fieldworkEnd;
    private String fieldworkStart;
    private String pollingFirm;
    private String pollingFirmPartner;
    private String publicationDate;
    private List<ResponseScenarioAnalysis> responseScenarioAnalyses;

    public Set<String> getCommissioners() {
        return commissioners;
    }

    public String getFieldworkEnd() {
        return fieldworkEnd;
    }

    public String getFieldworkStart() {
        return fieldworkStart;
    }

    public String getPollingFirm() {
        return pollingFirm;
    }

    public String getPollingFirmPartner() {
        return pollingFirmPartner;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public List<ResponseScenarioAnalysis> getResponseScenarioAnalyses() {
        return responseScenarioAnalyses;
    }

    public void setCommissioners(Set<String> commissioners) {
        this.commissioners = commissioners;
    }

    public void setFieldworkEnd(String fieldworkEnd) {
        this.fieldworkEnd = fieldworkEnd;
    }

    public void setFieldworkStart(String fieldworkStart) {
        this.fieldworkStart = fieldworkStart;
    }

    public void setPollingFirm(String pollingFirm) {
        this.pollingFirm = pollingFirm;
    }

    public void setPollingFirmPartner(String pollingFirmPartner) {
        this.pollingFirmPartner = pollingFirmPartner;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setResponseScenarioAnalyses(List<ResponseScenarioAnalysis> responseScenarioAnalyses) {
        this.responseScenarioAnalyses = responseScenarioAnalyses;
    }
}