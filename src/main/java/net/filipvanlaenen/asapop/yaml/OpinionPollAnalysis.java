package net.filipvanlaenen.asapop.yaml;

import java.util.Set;

/**
 * Class representing the opinion poll analysis element for the YAML file containing the analysis of the opinion polls
 * in an ROPF file.
 */
public class OpinionPollAnalysis {
    /**
     * The commissioners.
     */
    private Set<String> commissioners;
    /**
     * The end of the fieldwork period.
     */
    private String fieldworkEnd;
    /**
     * The start of the fieldwork period.
     */
    private String fieldworkStart;
    /**
     * The polling firm.
     */
    private String pollingFirm;
    /**
     * The polling firm partner.
     */
    private String pollingFirmPartner;
    /**
     * The publication date.
     */
    private String publicationDate;
    /**
     * The response scenario analyses.
     */
    private Set<ResponseScenarioAnalysis> responseScenarioAnalyses;

    /**
     * Returns the commissioners.
     *
     * @return The commissioners.
     */
    public Set<String> getCommissioners() {
        return commissioners;
    }

    /**
     * Returns the end of the fieldwork period.
     *
     * @return The end of the fieldwork period.
     */
    public String getFieldworkEnd() {
        return fieldworkEnd;
    }

    /**
     * Returns the start of the fieldwork period.
     *
     * @return The start of the fieldwork period.
     */
    public String getFieldworkStart() {
        return fieldworkStart;
    }

    /**
     * Returns the polling firm.
     *
     * @return The polling firm.
     */
    public String getPollingFirm() {
        return pollingFirm;
    }

    /**
     * Returns the polling firm partner.
     *
     * @return The polling firm partner.
     */
    public String getPollingFirmPartner() {
        return pollingFirmPartner;
    }

    /**
     * Returns the publication date.
     *
     * @return The publication date.
     */
    public String getPublicationDate() {
        return publicationDate;
    }

    /**
     * Returns the response scenario analyses.
     *
     * @return The response scenario analyses.
     */
    public Set<ResponseScenarioAnalysis> getResponseScenarioAnalyses() {
        return responseScenarioAnalyses;
    }

    /**
     * Sets the commissioners.
     *
     * @param commissioners The commissioners.
     */
    public void setCommissioners(final Set<String> commissioners) {
        this.commissioners = commissioners;
    }

    /**
     * Sets the end of the fieldwork period.
     *
     * @param fieldworkEnd The end of the fieldwork period.
     */
    public void setFieldworkEnd(final String fieldworkEnd) {
        this.fieldworkEnd = fieldworkEnd;
    }

    /**
     * Sets the start of the fieldwork period.
     *
     * @param fieldworkStart The start of the fieldwork period.
     */
    public void setFieldworkStart(final String fieldworkStart) {
        this.fieldworkStart = fieldworkStart;
    }

    /**
     * Sets the polling firm.
     *
     * @param pollingFirm The polling firm.
     */
    public void setPollingFirm(final String pollingFirm) {
        this.pollingFirm = pollingFirm;
    }

    /**
     * Sets the polling firm partner.
     *
     * @param pollingFirmPartner The polling firm partner.
     */
    public void setPollingFirmPartner(final String pollingFirmPartner) {
        this.pollingFirmPartner = pollingFirmPartner;
    }

    /**
     * Sets the publication date.
     *
     * @param publicationDate The publication date.
     */
    public void setPublicationDate(final String publicationDate) {
        this.publicationDate = publicationDate;
    }

    /**
     * Sets the response scenario analyses.
     *
     * @param responseScenarioAnalyses The response scenario analyses.
     */
    public void setResponseScenarioAnalyses(final Set<ResponseScenarioAnalysis> responseScenarioAnalyses) {
        this.responseScenarioAnalyses = responseScenarioAnalyses;
    }
}
