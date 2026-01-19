package com.fungsitama.dhsshopee.model;

public class ListDetailPengeluaranModel {

    private String id;
    private String trMiId;
    private String qrcode;
    private String status;

    public ListDetailPengeluaranModel(String id, String trMiId, String qrcode, String status) {
        this.id = id;
        this.trMiId = trMiId;
        this.qrcode = qrcode;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrMiId() {
        return trMiId;
    }

    public void setTrMiId(String trMiId) {
        this.trMiId = trMiId;
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
