package com.fungsitama.dhsshopee.util;

public class DataHolder {
    //design pattern to share arguments between fragments and activities
    private static DataHolder dataHolder = null;

    private DataHolder() {
    }

    public static DataHolder getInstance() {
        if (dataHolder == null) {
            dataHolder = new DataHolder();
        }
        return dataHolder;
    }


    private String date;
    private String status;
    private String domestic;
    private String cw;
    private String rev;
    private String income;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDomestic() {
        return domestic;
    }

    public void setDomestic(String domestic) {
        this.domestic = domestic;
    }

    public String getCw() {
        return cw;
    }

    public void setCw(String cw) {
        this.cw = cw;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }


    /*public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }*/
}