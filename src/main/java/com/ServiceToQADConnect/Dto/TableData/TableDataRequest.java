package com.ServiceToQADConnect.Dto.TableData;

public class TableDataRequest {

    private String site;
    private String tableName;
    private String whereClause;
    private Integer maxRecord;

    public TableDataRequest(String site, String tableName, String whereClause, Integer maxRecord) {
        this.site = site;
        this.tableName = tableName;
        this.whereClause = whereClause;
        this.maxRecord = maxRecord;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

    public Integer getMaxRecord() {
        return maxRecord;
    }

    public void setMaxRecord(Integer maxRecord) {
        this.maxRecord = maxRecord;
    }
}