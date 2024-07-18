package com.example.demo.models;

public class SaleReport2 {
    private String invoiceByKontragent;
    private String kontragent;
    private String date;
    private String artikul;
    private String value;

    public String getInvoiceByKontragent() {
        return invoiceByKontragent;
    }

    public void setInvoiceByKontragent(String invoiceByKontragent) {
        this.invoiceByKontragent = invoiceByKontragent;
    }

    public String getKontragent() {
        return kontragent;
    }

    public void setKontragent(String kontragent) {
        this.kontragent = kontragent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArtikul() {
        return artikul;
    }

    public void setArtikul(String artikul) {
        this.artikul = artikul;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
