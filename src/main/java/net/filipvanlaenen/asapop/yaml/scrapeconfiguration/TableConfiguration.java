package net.filipvanlaenen.asapop.yaml.scrapeconfiguration;

public class TableConfiguration {
    private String h2Content;
    private String h3Content;
    private String h4Content;
    private Integer tableIndex;
    private ColumnConfiguration[] columns;

    public ColumnConfiguration[] getColumns() {
        return columns;
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

    public Integer getTableIndex() {
        return tableIndex;
    }

    public void setColumns(final ColumnConfiguration[] columns) {
        this.columns = columns;
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

    public void setTableIndex(final Integer tableIndex) {
        this.tableIndex = tableIndex;
    }
}
