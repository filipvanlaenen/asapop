package net.filipvanlaenen.asapop.yaml.scrapeconfiguration;

public class TableConfiguration {
    private ColumnConfiguration[] columns;
    private Integer defaultYear;
    private String[] excludedPollingFirms;
    private Integer footRows;
    private String h2Content;
    private String h3Content;
    private String h4Content;
    private String h5Content;
    private Integer headRows;
    private PollingFirmAliases[] pollingFirmAliases;
    private String scope;
    private Integer tableIndex;
    private UnreconciledPoll[] unreconciledPoll;

    public ColumnConfiguration[] getColumns() {
        return columns;
    }

    public Integer getDefaultYear() {
        return defaultYear;
    }

    public String[] getExcludedPollingFirms() {
        return excludedPollingFirms;
    }

    public Integer getFootRows() {
        return footRows;
    }

    public String getH2Content() {
        return h2Content;
    }

    public String getH3Content() {
        return h3Content;
    }

    public String getH4Content() {
        return h4Content;
    }

    public String getH5Content() {
        return h5Content;
    }

    public Integer getHeadRows() {
        return headRows;
    }

    public PollingFirmAliases[] getPollingFirmAliases() {
        return pollingFirmAliases;
    }

    public String getScope() {
        return scope;
    }

    public Integer getTableIndex() {
        return tableIndex;
    }

    public UnreconciledPoll[] getUnreconciledPolls() {
        return unreconciledPoll;
    }

    public void setColumns(final ColumnConfiguration[] columns) {
        this.columns = columns;
    }

    public void setDefaultYear(final Integer defaultYear) {
        this.defaultYear = defaultYear;
    }

    public void setExcludedPollingFirms(final String[] excludedPollingFirms) {
        this.excludedPollingFirms = excludedPollingFirms;
    }

    public void setFootRows(final Integer footRows) {
        this.footRows = footRows;
    }

    public void setH2Content(final String h2Content) {
        this.h2Content = h2Content;
    }

    public void setH3Content(final String h3Content) {
        this.h3Content = h3Content;
    }

    public void setH4Content(final String h4Content) {
        this.h4Content = h4Content;
    }

    public void setH5Content(final String h5Content) {
        this.h5Content = h5Content;
    }

    public void setHeadRows(final Integer headRows) {
        this.headRows = headRows;
    }

    public void setPollingFirmAliases(final PollingFirmAliases[] pollingFirmAliases) {
        this.pollingFirmAliases = pollingFirmAliases;
    }

    public void setScope(final String scope) {
        this.scope = scope;
    }

    public void setTableIndex(final Integer tableIndex) {
        this.tableIndex = tableIndex;
    }

    public void setUnreconciledPolls(final UnreconciledPoll[] unreconciledPolls) {
        this.unreconciledPoll = unreconciledPolls;
    }
}
