package com.ServiceToQADConnect.Dto.SyncData;

public class SyncDataRequest {

    private String site;
    private Long startSeq;
    private Integer maxRecord;

    public SyncDataRequest(String site, Long startSeq, Integer maxRecord) {
        this.site = site;
        this.startSeq = startSeq;
        this.maxRecord = maxRecord;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Long getStartSeq() {
        return startSeq;
    }

    public void setStartSeq(Long startSeq) {
        this.startSeq = startSeq;
    }

    public Integer getMaxRecord() {
        return maxRecord;
    }

    public void setMaxRecord(Integer maxRecord) {
        this.maxRecord = maxRecord;
    }
}