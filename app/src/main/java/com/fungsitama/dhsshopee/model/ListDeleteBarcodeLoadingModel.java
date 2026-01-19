package com.fungsitama.dhsshopee.model;

import java.util.Date;

public class ListDeleteBarcodeLoadingModel {

    private String id;
    private String qrcode;
    private String status;
    private String rowNumber;
    private Date createdAt;

    public ListDeleteBarcodeLoadingModel(String id, String qrcode, String status, String rowNumber, Date createdAt) {
        this.id = id;
        this.qrcode = qrcode;
        this.status = status;
        this.rowNumber = rowNumber;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
