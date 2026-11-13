package net.filipvanlaenen.asapop.yaml.scrapeconfiguration;

public class ColumnConfiguration {
    private String columnType;
    private String headerContent;
    private int headerRow;
    private String resultKeys;
    private String separator;

    public String getColumnType() {
        return columnType;
    }

    public String getHeaderContent() {
        return headerContent;
    }

    public int getHeaderRow() {
        return headerRow;
    }

    public String getResultKeys() {
        return resultKeys;
    }

    public String getSeparator() {
        return separator;
    }

    public void setColumnType(final String columnType) {
        this.columnType = columnType;
    }

    public void setHeaderContent(final String headerContent) {
        this.headerContent = headerContent;
    }

    public void setHeaderRow(final int headerRow) {
        this.headerRow = headerRow;
    }

    public void setResultKeys(final String resultKeys) {
        this.resultKeys = resultKeys;
    }

    public void setSeparator(final String separator) {
        this.separator = separator;
    }
}
