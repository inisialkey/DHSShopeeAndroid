package com.fungsitama.dhsshopee.model;

import java.util.Date;

public class ListKendaraanModel {

    private String id;
    private String code;
    private String statusUsage;
    private Date stnkExpired;
    private Date kirExpired;

    public ListKendaraanModel(String id, String code, String statusUsage, Date stnkExpired, Date kirExpired) {
        this.id = id;
        this.code = code;
        this.statusUsage = statusUsage;
        this.stnkExpired = stnkExpired;
        this.kirExpired = kirExpired;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatusUsage() {
        return statusUsage;
    }

    public void setStatusUsage(String statusUsage) {
        this.statusUsage = statusUsage;
    }

    public Date getStnkExpired() {
        return stnkExpired;
    }

    public void setStnkExpired(Date stnkExpired) {
        this.stnkExpired = stnkExpired;
    }

    public Date getKirExpired() {
        return kirExpired;
    }

    public void setKirExpired(Date kirExpired) {
        this.kirExpired = kirExpired;
    }
}
