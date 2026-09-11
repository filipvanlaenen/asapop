package net.filipvanlaenen.asapop.yaml.scrapeconfiguration;

public class ColumnConfiguration {
    private String headerContent;
    private int headerRow;
    private String columnType;

    public String getHeaderContent() {
        return headerContent;
    }

    public int getHeaderRow() {
        return headerRow;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setHeaderContent(final String headerContent) {
        this.headerContent = headerContent;
    }

    public void setHeaderRow(final int headerRow) {
        this.headerRow = headerRow;
    }

    public void setColumnType(final String columnType) {
        this.columnType = columnType;
    }
}
