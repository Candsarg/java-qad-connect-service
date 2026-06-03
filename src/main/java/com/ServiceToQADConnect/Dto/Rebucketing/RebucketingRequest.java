package com.ServiceToQADConnect.Dto.Rebucketing;


import com.fasterxml.jackson.annotation.JsonProperty;

public class RebucketingRequest {

    private String site;
    @JsonProperty("RebucketingReqData")
    private RebucketingReqData[] rebucketData = {};

    public RebucketingRequest(String site, RebucketingReqData[] rebucketData) {
        this.site = site;
        this.rebucketData = rebucketData;
    }

    public String getSite() {
        return site;
    }
    public void setSite(String site) {
        this.site = site;
    }
    public RebucketingReqData[] getRebucketData() {
        return rebucketData;
    }
    public void setRebucketData(RebucketingReqData[] rebucketData) {
        this.rebucketData = rebucketData;
    }

}
