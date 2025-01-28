package com.example.demo.models;

public class ArtikulSimpleModel {

    private String artikul;
    private String sklad;

    public ArtikulSimpleModel(String artikul, String sklad) {
        this.artikul = artikul;
        this.sklad = sklad;
    }

    public String getArtikul() {
        return artikul;
    }

    public void setArtikul(String artikul) {
        this.artikul = artikul;
    }

    public String getSklad() {
        return sklad;
    }

    public void setSklad(String sklad) {
        this.sklad = sklad;
    }
}
