package com.example.demo.models;

public class ArtikulModel {
   // artikul, quantity, med , value, client, invoice, date, operator, percentProfit, barcode
    private String artikul; // артикул
    private int quantity; // количество
    private String med; // м.ед
    private float price; // ед.цена
    private String date;
    private String invoice;// фактура
    private String kontragent; // контрагент});
    private String barcod; // баркод

    private String percentProfit;

    private String person;

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getArtikul() {
        return artikul;
    }

    public void setArtikul(String artikul) {
        this.artikul = artikul;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMed() {
        return med;
    }

    public void setMed(String med) {
        this.med = med;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getKontragent() {
        return kontragent;
    }

    public void setKontragent(String kontragent) {
        this.kontragent = kontragent;
    }

    public String getBarcod() {
        return barcod;
    }

    public void setBarcod(String barcod) {
        this.barcod = barcod;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPercentProfit() {
        return percentProfit;
    }

    public void setPercentProfit(String percentProfit) {
        this.percentProfit = percentProfit;
    }
}
