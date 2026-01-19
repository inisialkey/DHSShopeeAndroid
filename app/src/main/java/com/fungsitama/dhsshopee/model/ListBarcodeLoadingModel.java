package com.fungsitama.dhsshopee.model;

import java.util.Date;

public class ListBarcodeLoadingModel {

    private String id;
    private String qrcode;
    private String status;
    private String rowNumber;
    private String scan;
    private String manual;
    private String impor;
    private Date createdAt;

    public ListBarcodeLoadingModel(String id, String qrcode, String status, String rowNumber, String scan, String manual, String impor, Date createdAt) {
        this.id = id;
        this.qrcode = qrcode;
        this.status = status;
        this.rowNumber = rowNumber;
        this.scan = scan;
        this.manual = manual;
        this.impor = impor;
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

    public String getScan() {
        return scan;
    }

    public void setScan(String scan) {
        this.scan = scan;
    }

    public String getManual() {
        return manual;
    }

    public void setManual(String manual) {
        this.manual = manual;
    }

    public String getImpor() {
        return impor;
    }

    public void setImpor(String impor) {
        this.impor = impor;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
