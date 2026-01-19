package com.fungsitama.dhsshopee.model;

public class ListDetailPenerimaanAgenModel {

    private String id;
    private String trAgentId;
    private String qrcode;
    private String status;

    public ListDetailPenerimaanAgenModel(String id, String trAgentId, String qrcode, String status) {
        this.id = id;
        this.trAgentId = trAgentId;
        this.qrcode = qrcode;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrAgentId() {
        return trAgentId;
    }

    public void setTrAgentId(String trAgentId) {
        this.trAgentId = trAgentId;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
