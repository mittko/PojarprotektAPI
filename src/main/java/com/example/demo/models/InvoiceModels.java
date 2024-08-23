package com.example.demo.models;

import java.util.List;

public class InvoiceModels<T> {

    InvoiceModel parentInvoiceModel;
    List<InvoiceModel> childInvoiceModels;

    public InvoiceModel getParentInvoiceModel() {
        return parentInvoiceModel;
    }

    public void setParentInvoiceModel(InvoiceModel parentInvoiceModel) {
        this.parentInvoiceModel = parentInvoiceModel;
    }

    public List<InvoiceModel> getChildInvoiceModels() {
        return childInvoiceModels;
    }

    public void setChildInvoiceModels(List<InvoiceModel> childInvoiceModels) {
        this.childInvoiceModels = childInvoiceModels;
    }
}
