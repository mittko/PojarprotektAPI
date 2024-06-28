package com.example.demo.models;

public class User<T> {
    private String usser;
    private String password;
    private String Service_Order;
    private String Working_Book;
    private String Invoice;
    private String Reports;
    private String New_Ext;
    private String Hidden_Menu;
    private String Acquittance;

    public void setUsser(String usser) {
        this.usser = usser;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setService_Order(String service_Order) {
        Service_Order = service_Order;
    }

    public void setWorking_Book(String working_Book) {
        Working_Book = working_Book;
    }

    public void setInvoice(String invoice) {
        Invoice = invoice;
    }

    public void setReports(String reports) {
        Reports = reports;
    }

    public void setNew_Ext(String new_Ext) {
        New_Ext = new_Ext;
    }

    public void setHidden_Menu(String hidden_Menu) {
        Hidden_Menu = hidden_Menu;
    }

    public void setAcquittance(String acquittance) {
        Acquittance = acquittance;
    }

    public String getUsser() {
        return usser;
    }

    public String getPassword() {
        return password;
    }

    public String getService_Order() {
        return Service_Order;
    }

    public String getWorking_Book() {
        return Working_Book;
    }

    public String getInvoice() {
        return Invoice;
    }

    public String getReports() {
        return Reports;
    }

    public String getNew_Ext() {
        return New_Ext;
    }

    public String getHidden_Menu() {
        return Hidden_Menu;
    }

    public String getAcquittance() {
        return Acquittance;
    }
}
