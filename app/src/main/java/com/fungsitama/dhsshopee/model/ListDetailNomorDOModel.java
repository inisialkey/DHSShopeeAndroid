package com.fungsitama.dhsshopee.model;

import java.util.Date;

public class ListDetailNomorDOModel {

    private String id;
    private String doId;
    private String doNumber;
    private String qrcode;
    private String status;
    private String codeDriver;
    private String codeVehicle;
    private String namaSupir;
    private String asal;
    private String tujuan;
    private Date createdAt;

    public ListDetailNomorDOModel(String id, String doId, String doNumber, String qrcode, String status, String codeDriver, String codeVehicle, String namaSupir, String asal, String tujuan, Date createdAt) {
        this.id = id;
        this.doId = doId;
        this.doNumber = doNumber;
        this.qrcode = qrcode;
        this.status = status;
        this.codeDriver = codeDriver;
        this.codeVehicle = codeVehicle;
        this.namaSupir = namaSupir;
        this.asal = asal;
        this.tujuan = tujuan;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoId() {
        return doId;
    }

    public void setDoId(String doId) {
        this.doId = doId;
    }

    public String getDoNumber() {
        return doNumber;
    }

    public void setDoNumber(String doNumber) {
        this.doNumber = doNumber;
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

    public String getCodeDriver() {
        return codeDriver;
    }

    public void setCodeDriver(String codeDriver) {
        this.codeDriver = codeDriver;
    }

    public String getCodeVehicle() {
        return codeVehicle;
    }

    public void setCodeVehicle(String codeVehicle) {
        this.codeVehicle = codeVehicle;
    }

    public String getNamaSupir() {
        return namaSupir;
    }

    public void setNamaSupir(String namaSupir) {
        this.namaSupir = namaSupir;
    }

    public String getAsal() {
        return asal;
    }

    public void setAsal(String asal) {
        this.asal = asal;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
