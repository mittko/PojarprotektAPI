package com.example.demo.models;

import java.util.List;

public class ServiceOrderBodyList<T> {

    public List<ServiceOrderModel<T>> getServiceOrders() {
        return serviceOrders;
    }

    public void setServiceOrders(List<ServiceOrderModel<T>> serviceOrders) {
        this.serviceOrders = serviceOrders;
    }

    private List<ServiceOrderModel<T>> serviceOrders;



}
