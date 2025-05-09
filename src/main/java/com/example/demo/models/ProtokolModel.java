package com.example.demo.models;

public class ProtokolModel {
    private String client;
    private String type;
    private String wheight;
    private String barcod;
    private String serial;
    private String category;
    private String brand;
    private String  T_O;
    private String P;
    private String HI;
    private String parts;
    private String value;
    private String number;
    private String person;
    private String date;
    private String kontragent;
    private String invoiceByKontragent;
    private String additional_data;
    private String uptodate;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWheight() {
        return wheight;
    }

    public void setWheight(String wheight) {
        this.wheight = wheight;
    }

    public String getBarcod() {
        return barcod;
    }

    public void setBarcod(String barcod) {
        this.barcod = barcod;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getT_O() {
        return T_O;
    }

    public void setT_O(String t_O) {
        T_O = t_O;
    }

    public String getP() {
        return P;
    }

    public void setP(String p) {
        P = p;
    }

    public String getHI() {
        return HI;
    }

    public void setHI(String HI) {
        this.HI = HI;
    }

    public String getParts() {
        return parts;
    }

    public void setParts(String parts) {
        this.parts = parts;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNumber() {
        return number != null ? number : client;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getAdditional_data() {
        return additional_data;
    }

    public void setAdditional_data(String additional_data) {
        this.additional_data = additional_data;
    }

    public String getUptodate() {
        return uptodate;
    }

    public void setUptodate(String uptodate) {
        this.uptodate = uptodate;
    }
}
