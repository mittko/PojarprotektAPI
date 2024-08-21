package com.example.demo.controllers.invoice;

import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.models.InvoiceModel;
import com.example.demo.models.InvoiceModels;
import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class InvoiceController<T> {

    @Autowired
    RepoService<T> service;

    @GetMapping("/invoice_info")
    public @ResponseBody T getInvoiceInfo(@RequestParam("id") String id) throws SQLException {
        String command = "select * from ProformParentDB where id = '" + id + "'";

        List<T> parentInvoiceModels = new ArrayList<>();

        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    InvoiceModel model = new InvoiceModel();
                    model.setId(resultSet.getString(1));
                    model.setPayment(resultSet.getString(2));
                    model.setDiscount(resultSet.getString(3));
                    model.setValue(resultSet.getString(4));
                    model.setClient(resultSet.getString(5));
                    model.setSaller(resultSet.getString(6));
                    model.setDate(resultSet.getString(7));

                    parentInvoiceModels.add((T) model);
                }
            }
        });

        command ="select ProformChildDB2.make, ProformChildDB2.med, ProformChildDB2.quantity,"
                + " ProformChildDB2.price, ProformChildDB2.value, ProformParentDB.discount,"
                + " ProformChildDB2.kontragent, ProformChildDB2.invoiceByKontragent " +
                "from ProformParentDB , ProformChildDB2 where ProformParentDB.id = '"
                + id + "' and ProformChildDB2.id = '" + id + "'";

        List<T> childInvoiceModels = new ArrayList<>();

        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                  InvoiceModel model = new InvoiceModel();
                  model.setMake(resultSet.getString(1));
                  model.setMed(resultSet.getString(2));
                  model.setQuantity(resultSet.getString(3));
                  model.setPrice(resultSet.getString(4));
                  model.setValue(resultSet.getString(5));
                  model.setDiscount(resultSet.getString(6));
                  model.setKontragent(resultSet.getString(7));
                  model.setInvoiceByKontragent(resultSet.getString(8));

                  childInvoiceModels.add((T) model);
                }
            }
        });

        InvoiceModels<T> models = new InvoiceModels<>();
        models.setParentInvoiceModels(parentInvoiceModels);
        models.setChildInvoiceModels(childInvoiceModels);
        return (T) models;
    }

}
