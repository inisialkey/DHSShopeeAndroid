package com.fungsitama.dhsshopee.model;

import java.util.Date;

public class ListNomorDOModel {

    private String id;
    private String trTrackingId;
    private String transNumber;
    private String doNumber;
    private String codeOrigin;
    private String nameOrigin;
    private String dcDestination;
    private String nameDestination;
    private String koli;
    private String kg;
    private String kty;
    private String totalQrCode;
    private String description;
    private String status;
    private String codeDriver;
    private String codeVehicle;
    private String namaSupir;
    private Date createdAt;

    public ListNomorDOModel(String id, String trTrackingId, String transNumber, String doNumber, String codeOrigin, String nameOrigin, String dcDestination, String nameDestination, String koli, String kg, String kty, String totalQrCode, String description, String status, String codeDriver, String codeVehicle, String namaSupir, Date createdAt) {
        this.id = id;
        this.trTrackingId = trTrackingId;
        this.transNumber = transNumber;
        this.doNumber = doNumber;
        this.codeOrigin = codeOrigin;
        this.nameOrigin = nameOrigin;
        this.dcDestination = dcDestination;
        this.nameDestination = nameDestination;
        this.koli = koli;
        this.kg = kg;
        this.kty = kty;
        this.totalQrCode = totalQrCode;
        this.description = description;
        this.status = status;
        this.codeDriver = codeDriver;
        this.codeVehicle = codeVehicle;
        this.namaSupir = namaSupir;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrTrackingId() {
        return trTrackingId;
    }

    public void setTrTrackingId(String trTrackingId) {
        this.trTrackingId = trTrackingId;
    }

    public String getTransNumber() {
        return transNumber;
    }

    public void setTransNumber(String transNumber) {
        this.transNumber = transNumber;
    }

    public String getDoNumber() {
        return doNumber;
    }

    public void setDoNumber(String doNumber) {
        this.doNumber = doNumber;
    }

    public String getCodeOrigin() {
        return codeOrigin;
    }

    public void setCodeOrigin(String codeOrigin) {
        this.codeOrigin = codeOrigin;
    }

    public String getNameOrigin() {
        return nameOrigin;
    }

    public void setNameOrigin(String nameOrigin) {
        this.nameOrigin = nameOrigin;
    }

    public String getDcDestination() {
        return dcDestination;
    }

    public void setDcDestination(String dcDestination) {
        this.dcDestination = dcDestination;
    }

    public String getNameDestination() {
        return nameDestination;
    }

    public void setNameDestination(String nameDestination) {
        this.nameDestination = nameDestination;
    }

    public String getKoli() {
        return koli;
    }

    public void setKoli(String koli) {
        this.koli = koli;
    }

    public String getKg() {
        return kg;
    }

    public void setKg(String kg) {
        this.kg = kg;
    }

    public String getKty() {
        return kty;
    }

    public void setKty(String kty) {
        this.kty = kty;
    }

    public String getTotalQrCode() {
        return totalQrCode;
    }

    public void setTotalQrCode(String totalQrCode) {
        this.totalQrCode = totalQrCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
