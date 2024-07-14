package com.example.demo.controllers.reports;

import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.models.ServiceOrderReports;
import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class ReportsController<T> {

    @Autowired
    RepoService<T> repoService;

    @GetMapping(path = "/service_orders")
    public @ResponseBody ArrayList<T> getServiceOrders(
    @RequestParam(value = "client", required = false) String client,
    @RequestParam(value = "number",required = false) String number,
    @RequestParam(value = "type", required = false) String type,
    @RequestParam(value = "wheight",required = false) String wheight,
    @RequestParam(value = "category",required = false) String category,
    @RequestParam(value = "brand",required = false) String brand,
    @RequestParam(value = "doing",required = false) String doing,
    @RequestParam(value = "serial",required = false) String serial,
    @RequestParam(value = "barcode",required = false) String barcode,
    @RequestParam(value = "fromDate",required = false) String fromDate,
    @RequestParam(value = "toDate",required = false) String toDate) throws SQLException {

        ArrayList<T> serviceOrders = new ArrayList<>();

        String command = "select client, type, wheight, barcod, serial, category, brand," +
                " T_O, P, HI, done, number, person, date , additional_data from ServiceTableDB";
        int selectedCriterii = 0;
        if(client != null) {
            command += String.format(" where client = '%s'",client);// Димитър Александров
            selectedCriterii++;
        }
        if(number != null) {
            String placeholder = null;
            placeholder = (selectedCriterii == 0) ? "where" :  "and";
            command += String.format(" %s number = '%s'",placeholder,number); // 1000016357
            selectedCriterii++;
        }
        if(type != null) {
            String placeholder = null;
            placeholder = (selectedCriterii == 0) ? "where" :  "and";
            command += String.format(" %s type = '%s'",placeholder,type); // 1000016357
            selectedCriterii++;
        }
        if(wheight != null) {
            String placeholder = null;
            placeholder = (selectedCriterii == 0) ? "where" :  "and";
            command += String.format(" %s wheight = '%s'",placeholder,wheight); // 1000016357
            selectedCriterii++;
        }
        if(category != null) {
            String placeholder = null;
            placeholder = (selectedCriterii == 0) ? "where" :  "and";
            command += String.format(" %s category = '%s'",placeholder,category); // 1000016357
            selectedCriterii++;
        }
        if(brand != null) {
            String placeholder = null;
            placeholder = (selectedCriterii == 0) ? "where" :  "and";
            command += String.format(" %s brand = '%s'",placeholder,brand); // 1000016357
            selectedCriterii++;
        }
        if(doing != null) {
            String placeholder = null;
            placeholder = (selectedCriterii == 0) ? "where" :  "and";
            switch (doing) {
                case "ТО" ->
                        command += String.format(" %s %s",placeholder , "T_O != 'не' and P = 'не' and HI = 'не'");
                case "П" ->
                        command += String.format(" %s %s",placeholder,"P != 'не' and T_O = 'не' and HI = 'не'");
                case "ХИ" ->
                        command += String.format(" %s %s",placeholder , "HI != 'не' and T_O = 'не' and P = 'не'");
                case "ТО,П" ->
                        command += String.format(" %s %s",placeholder , "T_O != 'не' and P != 'не' and HI = 'не'");
                case "ТО,П,ХИ" ->
                        command += String.format(" %s %s",placeholder , " T_O != 'не' and P != 'не' and HI != 'не'");
            }
            selectedCriterii++;
        }
        if(serial != null) {
            String placeholder = null;
            placeholder = (selectedCriterii == 0) ? "where" :  "and";
            command += String.format(" %s serial = '%s'",placeholder,serial); // 52217
            selectedCriterii++;
        }
        if(barcode != null) {
            String placeholder = null;
            placeholder = (selectedCriterii == 0) ? "where" :  "and";
            command += String.format(" %s barcod = '%s'",placeholder,barcode); // 1000018166032
            selectedCriterii++;
        }
        if(fromDate != null & toDate != null) {
            String placeholder = null;
            placeholder = (selectedCriterii == 0) ? "where" :  "and";
            command += String.format(" %s date between " + "Date('%s') and Date('%s')",placeholder,fromDate,toDate); // 1000018166032
        }
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    ServiceOrderReports<T> serviceOrderReports = new ServiceOrderReports<T>();
                    serviceOrderReports.setClient(resultSet.getString(1));
                    serviceOrderReports.setType(resultSet.getString(2));
                    serviceOrderReports.setWheight(resultSet.getString(3));
                    serviceOrderReports.setBarcod(resultSet.getString(4));
                    serviceOrderReports.setSerial(resultSet.getString(5));
                    serviceOrderReports.setCategory(resultSet.getString(6));
                    serviceOrderReports.setBrand(resultSet.getString(7));
                    serviceOrderReports.setT_O(resultSet.getString(8));
                    serviceOrderReports.setP(resultSet.getString(9));
                    serviceOrderReports.setHI(resultSet.getString(10));
                    serviceOrderReports.setDone(resultSet.getString(11));
                    serviceOrderReports.setNumber(resultSet.getString(12));
                    serviceOrderReports.setPerson(resultSet.getString(13));
                    serviceOrderReports.setDate(resultSet.getString(14));
                    serviceOrderReports.setAdditional_data(resultSet.getString(15));
                    serviceOrders.add((T) serviceOrderReports);
                }
            }
        });
        return serviceOrders;
    }



}
