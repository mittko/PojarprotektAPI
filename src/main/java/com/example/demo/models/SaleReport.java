package com.example.demo.models;

public class SaleReport {

    private String id;
    private String client;
    private String invoiceByKontragent;
    private String kontragent;
    private String artikul;
    private String med;
    private String quantity;
    private String price;
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

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

    public String getArtikul() {
        return artikul;
    }

    public void setArtikul(String artikul) {
        this.artikul = artikul;
    }

    public String getMed() {
        return med;
    }

    public void setMed(String med) {
        this.med = med;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
