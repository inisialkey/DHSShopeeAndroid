package com.fungsitama.dhsshopee.model;

import java.util.Date;

public class ListNomorDODCModel {

    private String id;
    private String transNumber;
    private String codeVehicle;
    private String idDC;
    private String codeDC;
    private String nameDC;
    private String doNumber;
    private String koli;
    private String kg;
    private String qty;
    private String totalQrCode;
    private String description;
    private String status;
    private String namaSupir;
    private Date transactionDate;

    public ListNomorDODCModel(String id, String transNumber, String codeVehicle, String idDC, String codeDC, String nameDC, String doNumber, String koli, String kg, String qty, String totalQrCode, String description, String status, String namaSupir, Date transactionDate) {
        this.id = id;
        this.transNumber = transNumber;
        this.codeVehicle = codeVehicle;
        this.idDC = idDC;
        this.codeDC = codeDC;
        this.nameDC = nameDC;
        this.doNumber = doNumber;
        this.koli = koli;
        this.kg = kg;
        this.qty = qty;
        this.totalQrCode = totalQrCode;
        this.description = description;
        this.status = status;
        this.namaSupir = namaSupir;
        this.transactionDate = transactionDate;
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

    public String getCodeVehicle() {
        return codeVehicle;
    }

    public void setCodeVehicle(String codeVehicle) {
        this.codeVehicle = codeVehicle;
    }

    public String getIdDC() {
        return idDC;
    }

    public void setIdDC(String idDC) {
        this.idDC = idDC;
    }

    public String getCodeDC() {
        return codeDC;
    }

    public void setCodeDC(String codeDC) {
        this.codeDC = codeDC;
    }

    public String getNameDC() {
        return nameDC;
    }

    public void setNameDC(String nameDC) {
        this.nameDC = nameDC;
    }

    public String getDoNumber() {
        return doNumber;
    }

    public void setDoNumber(String doNumber) {
        this.doNumber = doNumber;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
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

    public String getNamaSupir() {
        return namaSupir;
    }

    public void setNamaSupir(String namaSupir) {
        this.namaSupir = namaSupir;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}
