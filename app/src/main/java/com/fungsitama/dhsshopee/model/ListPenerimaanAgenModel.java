package com.fungsitama.dhsshopee.model;

import java.util.Date;

public class ListPenerimaanAgenModel {

    private String id;
    private String transNumber;
    private String idAgent;
    private String codeAgent;
    private String namaAgen;
    private String trMiId;
    private String koli;
    private String kg;
    private String qty;
    private String totalQrCode;
    private String description;
    private Date transactionDate;

    public ListPenerimaanAgenModel(String id, String transNumber, String idAgent, String codeAgent, String namaAgen, String trMiId, String koli, String kg, String qty, String totalQrCode, String description, Date transactionDate) {
        this.id = id;
        this.transNumber = transNumber;
        this.idAgent = idAgent;
        this.codeAgent = codeAgent;
        this.namaAgen = namaAgen;
        this.trMiId = trMiId;
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

    public String getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(String idAgent) {
        this.idAgent = idAgent;
    }

    public String getCodeAgent() {
        return codeAgent;
    }

    public void setCodeAgent(String codeAgent) {
        this.codeAgent = codeAgent;
    }

    public String getNamaAgen() {
        return namaAgen;
    }

    public void setNamaAgen(String namaAgen) {
        this.namaAgen = namaAgen;
    }

    public String getTrMiId() {
        return trMiId;
    }

    public void setTrMiId(String trMiId) {
        this.trMiId = trMiId;
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
