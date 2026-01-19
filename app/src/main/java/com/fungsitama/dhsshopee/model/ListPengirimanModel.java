package com.fungsitama.dhsshopee.model;

import java.util.Date;

public class ListPengirimanModel {

    String id;
    String transNumber;
    Date transactionDate;
    String codeDriver;
    String codeVehicle;
    String namaSupir;
    String status;
    String qtyDO;
    String qtyBarcode;

    public ListPengirimanModel(String id, String transNumber, Date transactionDate, String codeDriver, String codeVehicle, String namaSupir, String status, String qtyDO, String qtyBarcode) {
        this.id = id;
        this.transNumber = transNumber;
        this.transactionDate = transactionDate;
        this.codeDriver = codeDriver;
        this.codeVehicle = codeVehicle;
        this.namaSupir = namaSupir;
        this.status = status;
        this.qtyDO = qtyDO;
        this.qtyBarcode = qtyBarcode;
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

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQtyDO() {
        return qtyDO;
    }

    public void setQtyDO(String qtyDO) {
        this.qtyDO = qtyDO;
    }

    public String getQtyBarcode() {
        return qtyBarcode;
    }

    public void setQtyBarcode(String qtyBarcode) {
        this.qtyBarcode = qtyBarcode;
    }
}
