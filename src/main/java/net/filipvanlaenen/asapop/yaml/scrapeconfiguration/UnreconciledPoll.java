package net.filipvanlaenen.asapop.yaml.scrapeconfiguration;

public class UnreconciledPoll {
    private String endDate;
    private String pollingFirm;

    public String getEndDate() {
        return endDate;
    }

    public String getPollingFirm() {
        return pollingFirm;
    }

    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    public void setPollingFirm(final String pollingFirm) {
        this.pollingFirm = pollingFirm;
    }
}
