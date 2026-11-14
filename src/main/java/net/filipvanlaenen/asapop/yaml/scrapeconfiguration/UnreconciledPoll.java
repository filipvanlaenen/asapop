package net.filipvanlaenen.asapop.yaml.scrapeconfiguration;

public class UnreconciledPoll {
    private String date;
    private String pollingFirm;

    public String getDate() {
        return date;
    }

    public String getPollingFirm() {
        return pollingFirm;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public void setPollingFirm(final String pollingFirm) {
        this.pollingFirm = pollingFirm;
    }
}
