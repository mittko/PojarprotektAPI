package com.example.demo.models;

import java.util.ArrayList;
import java.util.HashMap;

public class DiaryModel<T> {
    private ArrayList<ProtokolModel> protokolModels;
    private HashMap<String,String> invoiceNumbers;

    private HashMap<String, ClientInfo> clientModelHashMap = new HashMap<>();

    public void setProtokolModels(ArrayList<ProtokolModel> models) {
        this.protokolModels = models;
    }
    public void setInvoiceNumbers(HashMap<String,String> map) {
        this.invoiceNumbers = map;
    }

    public ArrayList<ProtokolModel> getProtokolModels() {
        return protokolModels;
    }

    public HashMap<String, String> getInvoiceNumbers() {
        return invoiceNumbers;
    }

    public HashMap<String, ClientInfo> getClientModelHashMap() {
        return clientModelHashMap;
    }

    public void setClientModelHashMap(HashMap<String, ClientInfo> clientModelHashMap) {
        this.clientModelHashMap = clientModelHashMap;
    }
}
