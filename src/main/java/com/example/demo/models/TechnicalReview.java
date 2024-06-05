package com.example.demo.models;

public class TechnicalReview {
    String client;
    String type;
    String wheight;
    String T_O;
    String P;
    String HI;
    String number;
    String additional_data;

    public String getClient() {
        return client;
    }

    public String getType() {
        return type;
    }

    public String getWheight() {
        return wheight;
    }

    public String getT_O() {
        return T_O;
    }

    public String getP() {
        return P;
    }

    public String getHI() {
        return HI;
    }

    public String getNumber() {
        return number;
    }

    public String getAdditional_data() {
        return additional_data;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWheight(String wheight) {
        this.wheight = wheight;
    }

    public void setT_O(String t_O) {
        T_O = t_O;
    }

    public void setP(String p) {
        P = p;
    }

    public void setHI(String HI) {
        this.HI = HI;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setAdditional_data(String additional_data) {
        this.additional_data = additional_data;
    }

    @Override
    public String toString() {
        return "TechnicalReview{" +
                "client='" + client + '\'' +
                ", type='" + type + '\'' +
                ", wheight='" + wheight + '\'' +
                ", T_O='" + T_O + '\'' +
                ", P='" + P + '\'' +
                ", HI='" + HI + '\'' +
                ", number='" + number + '\'' +
                ", additional_data='" + additional_data + '\'' +
                '}';
    }
}
