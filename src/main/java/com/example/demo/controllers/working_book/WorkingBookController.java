package com.example.demo.controllers.working_book;

import com.example.demo.callbacks.PreparedStatementCallback;
import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.models.PartsModel;
import com.example.demo.models.ProtokolModel;
import com.example.demo.models.ProtokolModelBodyList;
import com.example.demo.models.ServiceOrderModel;
import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

@RestController
public class WorkingBookController<T> {

    @Autowired
    RepoService<T> service;

    @GetMapping("/service_order_by_barcode")
    public @ResponseBody T getServiceOrderInfoByBarcode(@RequestParam(value = "barcode",required = false) String barcode,
                                                        @RequestParam(value = "serial_number",required = false) String serialNumber) throws SQLException {

        String command = "";
        if(barcode != null) {
            command = "select client, type, wheight, "
                    + "barcod, serial, category, brand, T_O, P, HI, additional_data" // ,
                    // допълнителни
                    // данни
                    + " from ServiceTableDB where barcod = '" + barcode + "' and done = 'не'";
        } else {
            command = "select client, type, wheight, "
                    + "barcod, serial, category, brand, T_O, P, HI, additional_data" // ,
                    // допълнителни
                    // данни
                    + " from ServiceTableDB where serial = '" + serialNumber + "' and done = 'не'";
        }

        ServiceOrderModel<T> serviceOrderModel = new ServiceOrderModel<>();
        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {

                    serviceOrderModel.setClient(resultSet.getString(1));
                    serviceOrderModel.setType(resultSet.getString(2));
                    serviceOrderModel.setWheight(resultSet.getString(3));
                    serviceOrderModel.setBarcod(resultSet.getString(4));
                    serviceOrderModel.setSerial(resultSet.getString(5));
                    serviceOrderModel.setCategory(resultSet.getString(6));
                    serviceOrderModel.setBrand(resultSet.getString(7));
                    serviceOrderModel.setT_O(resultSet.getString(8));
                    serviceOrderModel.setP(resultSet.getString(9));
                    serviceOrderModel.setHI(resultSet.getString(10));
                    serviceOrderModel.setAdditional_data(resultSet.getString(11));

                }
            }
        });
        return (T) serviceOrderModel;
    }

    @PostMapping(path = "/insert_protokol")
    public String insertProtokol(@RequestBody ProtokolModelBodyList body) throws SQLException {

        final int[] maxNumber = new int[1];
        String command = "select max(integer(number)) from ProtokolTableDB5 ";
        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                  while (resultSet.next()) {
                      maxNumber[0] = resultSet.getInt(1);
                      break;
                  }
            }
        });
        String nextProtokolNumber = String.valueOf(maxNumber[0]+1);

        for(ProtokolModel protokolModel : body.getList()) {
            command = "insert into ProtokolTableDB5" +
                    " values ('" + protokolModel.getClient() + "','" + protokolModel.getType() + "','"
                    + protokolModel.getWheight() + "','"
                    + protokolModel.getBarcod() + "','" + protokolModel.getSerial() + "','" +
                    protokolModel.getCategory() + "','" + protokolModel.getBrand() + "','" +
                    protokolModel.getT_O() + "','" + protokolModel.getP() + "','" + protokolModel.getHI() + "','"
                    + protokolModel.getParts() + "'," + protokolModel.getValue() + ",'" +
                    nextProtokolNumber + "','" + protokolModel.getPerson() + "','" +
                    protokolModel.getDate() +   "','" + "null" + "','" +  protokolModel.getKontragent()
                    + "','" + protokolModel.getInvoiceByKontragent() + "','" + protokolModel.getAdditional_data() + "')";

            service.execute(command);
        }

        for(ProtokolModel protokolModel : body.getList()) {
            command = "update ServiceTableDB"
                    + " set done = 'да' where barcod = '"+protokolModel.getBarcod()+"'";
            service.execute(command);
        }

        for(PartsModel partsModel : body.getParts()) {
           command =  "update PartsQuantityDB" +
                    " set quantity = (quantity - ?)  where part = ?";
           service.execute(command, new PreparedStatementCallback<T>() {
               @Override
               public void callback(PreparedStatement ps) throws SQLException {
                  ps.setFloat(1,partsModel.getValue());
                  ps.setString(2,partsModel.getKey());
                  ps.executeUpdate();
               }
           });
        }
        return nextProtokolNumber;
    }
}
