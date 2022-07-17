package com.kgaft.securemessengerappandroid.Database;

public class TableColumn {
    private String columnName;
    private String columnType;
    private String columnValue;

    public TableColumn() {
    }

    public TableColumn(String columnName, String columnType, String columnValue) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.columnValue = columnValue;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }
}
