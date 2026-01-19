package com.fungsitama.dhsshopee.model;

import java.util.Date;

public class ListNomorReferensiPengeluaranModel {

    private String id;
    private String transNumber;
    private String idDC;
    private String codeDC;
    private String namaDC;
    private String idAgent;
    private String namaAgen;
    private String referenceNumber;
    private String koli;
    private String kg;
    private String qty;
    private String totalQrCode;
    private String description;
    private Date transactionDate;

    public ListNomorReferensiPengeluaranModel(String id, String transNumber, String idDC, String codeDC, String namaDC, String idAgent, String namaAgen, String referenceNumber, String koli, String kg, String qty, String totalQrCode, String description, Date transactionDate) {
        this.id = id;
        this.transNumber = transNumber;
        this.idDC = idDC;
        this.codeDC = codeDC;
        this.namaDC = namaDC;
        this.idAgent = idAgent;
        this.namaAgen = namaAgen;
        this.referenceNumber = referenceNumber;
        this.koli = koli;
        this.kg = kg;
        this.qty = qty;
        this.totalQrCode = totalQrCode;
        this.description = description;
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

    public String getNamaDC() {
        return namaDC;
    }

    public void setNamaDC(String namaDC) {
        this.namaDC = namaDC;
    }

    public String getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(String idAgent) {
        this.idAgent = idAgent;
    }

    public String getNamaAgen() {
        return namaAgen;
    }

    public void setNamaAgen(String namaAgen) {
        this.namaAgen = namaAgen;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
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

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}
