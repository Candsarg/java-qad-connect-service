package com.ServiceToQADConnect.Dto.MrpData;

public class MrpDataRequest {

    private String site;
    private String material;
    
    public MrpDataRequest(String site, String material) {
        this.site = site;
        this.material = material;
    }

    public String getSite() {
        return site;
    }
    public void setSite(String site) {
        this.site = site;
    }
    public String getMaterial() {
        return material;
    }
    public void setMaterial(String material) {
        this.material = material;
    }
}