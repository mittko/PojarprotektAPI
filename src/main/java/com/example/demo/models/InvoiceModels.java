package com.example.demo.models;

import java.util.List;

public class InvoiceModels<T> {

    List<T> parentInvoiceModels;
    List<T> childInvoiceModels;

    public List<T> getParentInvoiceModels() {
        return parentInvoiceModels;
    }

    public void setParentInvoiceModels(List<T> parentInvoiceModels) {
        this.parentInvoiceModels = parentInvoiceModels;
    }

    public List<T> getChildInvoiceModels() {
        return childInvoiceModels;
    }

    public void setChildInvoiceModels(List<T> childInvoiceModels) {
        this.childInvoiceModels = childInvoiceModels;
    }
}
