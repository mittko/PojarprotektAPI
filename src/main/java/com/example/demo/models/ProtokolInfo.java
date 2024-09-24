package com.example.demo.models;

import java.util.List;

public class ProtokolInfo {

    private Firm firm;
    private List<ProtokolModel> protokolModels;

    public Firm getFirm() {
        return firm;
    }

    public void setFirm(Firm firm) {
        this.firm = firm;
    }

    public List<ProtokolModel> getProtokolModels() {
        return protokolModels;
    }

    public void setProtokolModels(List<ProtokolModel> protokolModels) {
        this.protokolModels = protokolModels;
    }
}
