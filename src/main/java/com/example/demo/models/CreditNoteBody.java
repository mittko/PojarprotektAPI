package com.example.demo.models;

public class CreditNoteBody {
    private String id;
    private String payment;
    private int discount;
    private float sum;
    private String client;
    private String saller;
    private String date;
    private String protokol_id;
    private String artikul;
    private String med;
    private int quantity;
    private float price;
    private float value;
    private String kontragent;
    private String invoiceByKontragent;
   // private String note_id;
    private String credit_note_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getSaller() {
        return saller;
    }

    public void setSaller(String saller) {
        this.saller = saller;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProtokol_id() {
        return protokol_id;
    }

    public void setProtokol_id(String protokol_id) {
        this.protokol_id = protokol_id;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
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

    public String getCredit_note_date() {
        return credit_note_date;
    }

    public void setCredit_note_date(String credit_note_date) {
        this.credit_note_date = credit_note_date;
    }
}
