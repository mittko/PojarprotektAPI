package com.example.demo.controllers.working_book;

import com.example.demo.callbacks.PreparedStatementCallback;
import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.models.*;
import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @PostMapping("/insert_brack")
    public String insertBrack(@RequestBody BrackModels body) throws SQLException {
        String command = "select max(integer(number)) from BrackTableDB2";
        final int[] maxNumber = new int[1];
        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    maxNumber[0] = resultSet.getInt(1);
                    break;
                }
            }
        });

        String nextBrackNumber  = String.format("%07d",maxNumber[0]+1);

        for(BrackModel model : body.getList()) {

            command = "insert into BrackTableDB2 values ('" + model.getClient() + "','" + model.getType() + "','"
                    + model.getWheight() + "','" + model.getBarcod() + "','" + model.getSerial() + "','"
                    + model.getCategory() + "','" + model.getBrand() + "','"
                    + model.getReasons() + "','" + nextBrackNumber + "','" +
                    model.getTehnik() + "','" + model.getDate() + "')";

            service.execute(command);

            command = "delete from ServiceTableDB where barcod = '" + model.getBarcod() + "'";

            service.execute(command);

            command = "delete from ProtokolTableDB5 where barcod = '" + model.getBarcod() + "'";

            service.execute(command);

        }


        return nextBrackNumber;
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
        String nextProtokolNumber = String.format("%07d",maxNumber[0]+1);

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

    @GetMapping("/protokol_info")
    public @ResponseBody List<T> getProtokolInfo(@RequestParam("number") String number) throws SQLException {
        String command = "select T_O, P, HI, client, type, wheight, category, parts, value, kontragent, invoiceByKontragent  "
                + "from ProtokolTableDB5 where number = '" + number + "'";
        List<T> models = new ArrayList<>();
        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    ProtokolModel model = new ProtokolModel();
                    model.setT_O(resultSet.getString(1));
                    model.setP(resultSet.getString(2));
                    model.setHI(resultSet.getString(3));
                    model.setClient(resultSet.getString(4));
                    model.setType(resultSet.getString(5));
                    model.setWheight(resultSet.getString(6));
                    model.setCategory(resultSet.getString(7));
                    model.setParts(resultSet.getString(8));
                    model.setValue(String.valueOf(resultSet.getDouble(9)));
                    model.setKontragent(resultSet.getString(10));
                    model.setInvoiceByKontragent(resultSet.getString(11));

                    models.add((T) model);
                }
            }
        });
        return models;
    }
}
