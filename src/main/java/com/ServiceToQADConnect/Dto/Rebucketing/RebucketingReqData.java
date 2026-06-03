package com.ServiceToQADConnect.Dto.Rebucketing;

public class RebucketingReqData {

    private String tt_xxintib_dom;
    private int    tt_xxintib_seq;
    private String tt_xxintib_datetime;
    private String tt_xxintib_transaction;
    private String tt_xxintib_keyvalue;
    private String tt_xxintib_status;
    private String tt_xxintib_data;
    private String tt_xxintib_source;

    public RebucketingReqData(String tt_xxintib_dom, int tt_xxintib_seq, String tt_xxintib_datetime,
            String tt_xxintib_transaction, String tt_xxintib_keyvalue, String tt_xxintib_status, String tt_xxintib_data,
            String tt_xxintib_source) {
        this.tt_xxintib_dom = tt_xxintib_dom;
        this.tt_xxintib_seq = tt_xxintib_seq;
        this.tt_xxintib_datetime = tt_xxintib_datetime;
        this.tt_xxintib_transaction = tt_xxintib_transaction;
        this.tt_xxintib_keyvalue = tt_xxintib_keyvalue;
        this.tt_xxintib_status = tt_xxintib_status;
        this.tt_xxintib_data = tt_xxintib_data;
        this.tt_xxintib_source = tt_xxintib_source;
    }

    public String getTt_xxintib_dom() {
        return tt_xxintib_dom;
    }

    public void setTt_xxintib_dom(String tt_xxintib_dom) {
        this.tt_xxintib_dom = tt_xxintib_dom;
    }

    public int getTt_xxintib_seq() {
        return tt_xxintib_seq;
    }

    public void setTt_xxintib_seq(int tt_xxintib_seq) {
        this.tt_xxintib_seq = tt_xxintib_seq;
    }

    public String getTt_xxintib_datetime() {
        return tt_xxintib_datetime;
    }

    public void setTt_xxintib_datetime(String tt_xxintib_datetime) {
        this.tt_xxintib_datetime = tt_xxintib_datetime;
    }

    public String getTt_xxintib_transaction() {
        return tt_xxintib_transaction;
    }

    public void setTt_xxintib_transaction(String tt_xxintib_transaction) {
        this.tt_xxintib_transaction = tt_xxintib_transaction;
    }

    public String getTt_xxintib_keyvalue() {
        return tt_xxintib_keyvalue;
    }

    public void setTt_xxintib_keyvalue(String tt_xxintib_keyvalue) {
        this.tt_xxintib_keyvalue = tt_xxintib_keyvalue;
    }

    public String getTt_xxintib_status() {
        return tt_xxintib_status;
    }

    public void setTt_xxintib_status(String tt_xxintib_status) {
        this.tt_xxintib_status = tt_xxintib_status;
    }

    public String getTt_xxintib_data() {
        return tt_xxintib_data;
    }

    public void setTt_xxintib_data(String tt_xxintib_data) {
        this.tt_xxintib_data = tt_xxintib_data;
    }

    public String getTt_xxintib_source() {
        return tt_xxintib_source;
    }

    public void setTt_xxintib_source(String tt_xxintib_source) {
        this.tt_xxintib_source = tt_xxintib_source;
    }
}