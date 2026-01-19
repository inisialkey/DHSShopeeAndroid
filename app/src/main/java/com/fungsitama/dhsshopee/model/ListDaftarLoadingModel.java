package com.fungsitama.dhsshopee.model;

import java.util.Date;

public class ListDaftarLoadingModel {

    private String id;
    private String transNumber;
    private String nameDriver;
    private String codeVehicle;
    private String nameOrigin;
    private String nameDestination;
    private String nameTransit;
    private String status;
    private String qrcode;
    private String loadType;
    private Date transactionDate;
    private Date createdAt;
    private Date unloadingAt;

    public ListDaftarLoadingModel(String id, String transNumber, String nameDriver, String codeVehicle, String nameOrigin, String nameDestination, String nameTransit, String status, String qrcode, String loadType, Date transactionDate, Date createdAt, Date unloadingAt) {
        this.id = id;
        this.transNumber = transNumber;
        this.nameDriver = nameDriver;
        this.codeVehicle = codeVehicle;
        this.nameOrigin = nameOrigin;
        this.nameDestination = nameDestination;
        this.nameTransit = nameTransit;
        this.status = status;
        this.qrcode = qrcode;
        this.loadType = loadType;
        this.transactionDate = transactionDate;
        this.createdAt = createdAt;
        this.unloadingAt = unloadingAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransNumber() {
        return transNumber;
    }

    public void setTransNumber(String transNumber) {
        this.transNumber = transNumber;
    }

    public String getNameDriver() {
        return nameDriver;
    }

    public void setNameDriver(String nameDriver) {
        this.nameDriver = nameDriver;
    }

    public String getCodeVehicle() {
        return codeVehicle;
    }

    public void setCodeVehicle(String codeVehicle) {
        this.codeVehicle = codeVehicle;
    }

    public String getNameOrigin() {
        return nameOrigin;
    }

    public void setNameOrigin(String nameOrigin) {
        this.nameOrigin = nameOrigin;
    }

    public String getNameDestination() {
        return nameDestination;
    }

    public void setNameDestination(String nameDestination) {
        this.nameDestination = nameDestination;
    }

    public String getNameTransit() {
        return nameTransit;
    }

    public void setNameTransit(String nameTransit) {
        this.nameTransit = nameTransit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUnloadingAt() {
        return unloadingAt;
    }

    public void setUnloadingAt(Date unloadingAt) {
        this.unloadingAt = unloadingAt;
    }
}
