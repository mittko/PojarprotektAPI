package com.example.demo.models;

public class DeliveryModel {
    private String artikul;
    private String quantity;
    private String med;
    private String value;
    private String kontragent;
    private String invoiceByKontragent;
    private String date;
    private String operator;


    public String getArtikul() {
        return artikul;
    }

    public void setArtikul(String artikul) {
        this.artikul = artikul;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMed() {
        return med;
    }

    public void setMed(String med) {
        this.med = med;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKontragent() {
        return kontragent;
    }

    public void setKontragent(String kontragent) {
        this.kontragent = kontragent;
    }

    public String getInvoiceByKontragent() {
        return invoiceByKontragent;
    }

    public void setInvoiceByKontragent(String invoiceByKontragent) {
        this.invoiceByKontragent = invoiceByKontragent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
