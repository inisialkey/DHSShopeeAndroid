package com.fungsitama.dhsshopee.model;

import java.io.Serializable;
import java.util.Date;

public class ListGambarModel implements Serializable {
    private String url;

    public ListGambarModel() {
    }

    public ListGambarModel(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
