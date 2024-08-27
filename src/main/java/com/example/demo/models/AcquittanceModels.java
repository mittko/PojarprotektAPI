package com.example.demo.models;

import java.util.ArrayList;

public class AcquittanceModels {

    private AcquittanceModel parentModel;

    private ArrayList<AcquittanceModel> childModels;

    public AcquittanceModel getParentModel() {
        return parentModel;
    }

    public void setParentModel(AcquittanceModel parentModel) {
        this.parentModel = parentModel;
    }

    public ArrayList<AcquittanceModel> getChildModels() {
        return childModels;
    }

    public void setChildModels(ArrayList<AcquittanceModel> childModels) {
        this.childModels = childModels;
    }
}
