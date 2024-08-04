package com.example.demo.controllers.office;


import com.example.demo.callbacks.PreparedStatementCallback;
import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.models.ProtokolModels;
import com.example.demo.models.ServiceOrderBodyList;
import com.example.demo.models.ServiceOrderModel;
import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class ServiceOrderController<T> {

    @Autowired
    RepoService<T> service;

    @PostMapping("/insert_service_order")
    public Integer insertServiceOrder(@RequestBody ServiceOrderBodyList<T> serviceOrderBodyList) throws SQLException {
        String command = "insert into  ServiceTableDB" +
                " (client, type, wheight, barcod, serial, category, brand, T_O, P, HI , done, number, person, date," +
                " additional_data) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        final int[] result = new int[1];
        for(ServiceOrderModel<T> serviceOrderModel : serviceOrderBodyList.getServiceOrders()) {
            service.execute(command, new PreparedStatementCallback<T>() {
                @Override
                public void callback(PreparedStatement ps) throws SQLException {


                    ps.setString(1, serviceOrderModel.getClient());
                    ps.setString(2, serviceOrderModel.getType());
                    ps.setString(3, serviceOrderModel.getWheight());
                    ps.setString(4, serviceOrderModel.getBarcod());
                    ps.setString(5, serviceOrderModel.getSerial());
                    ps.setString(6, serviceOrderModel.getCategory());
                    ps.setString(7, serviceOrderModel.getBrand());
                    ps.setString(8, serviceOrderModel.getT_O());
                    ps.setString(9, serviceOrderModel.getP());
                    ps.setString(10, serviceOrderModel.getHI());
                    ps.setString(11, serviceOrderModel.getDone());
                    ps.setString(12, serviceOrderModel.getNumber());
                    ps.setString(13, serviceOrderModel.getPerson());
                    ps.setString(14, serviceOrderModel.getDate());
                    ps.setString(15, serviceOrderModel.getAdditional_data());

                    result[0] = ps.executeUpdate();
                }
            });
        }

        for(String barcode : serviceOrderBodyList.getBarcodesToUpdateInProtokol()) {
            command = "update ProtokolTableDB5 set uptodate = ? where barcod = ?";
            service.execute(command, new PreparedStatementCallback<T>() {
                @Override
                public void callback(PreparedStatement ps) throws SQLException {
                    ps.setString(1,"not null");
                    ps.setString(2, barcode);
                    int update = ps.executeUpdate();
                }
            });
        }
        return result[0];
    }

    @GetMapping("/protokol_info_barcode")
    public @ResponseBody T getProtokolInfoByBarcode(@RequestParam(value = "barcode", required = false) String barcode,
                                                    @RequestParam(value = "serial_number", required = false) String serialNumber) throws SQLException {
        ProtokolModels<T> protokolModel = new ProtokolModels<>();
        String command;
        if(barcode != null) {
            command = "select client, type, wheight, "
                    + "barcod, serial, category, brand, T_O, P, HI, additional_data from ProtokolTableDB5" +
                    " where barcod = '" + barcode + "'";
        } else {
            command = "select client, type, wheight, "
                    + "barcod, serial, category, brand, T_O, P, HI, additional_data from ProtokolTableDB5" +
                    " where serial = '" + serialNumber + "'";
        }


        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {

                    protokolModel.setClient(resultSet.getString(1));
                    protokolModel.setType(resultSet.getString(2));
                    protokolModel.setWheight(resultSet.getString(3));
                    protokolModel.setBarcod(resultSet.getString(4));
                    protokolModel.setSerial(resultSet.getString(5));
                    protokolModel.setCategory(resultSet.getString(6));
                    protokolModel.setBrand(resultSet.getString(7));
                    protokolModel.setT_O(resultSet.getString(8));
                    protokolModel.setP(resultSet.getString(9));
                    protokolModel.setHI(resultSet.getString(10));
                    protokolModel.setAdditional_data(resultSet.getString(11));

                }
            }
        });
        return (T) protokolModel;
    }

    @GetMapping("/next_serial_number")
    public @ResponseBody String getNextSerialNumber() throws SQLException {
       String command = "select * from SerialTable";
        final String[] nextSerialNumber = {""};
       service.getResult(command, new ResultSetCallback() {
           @Override
           public void result(ResultSet resultSet) throws SQLException {
               while (resultSet.next()) {
                   nextSerialNumber[0] = resultSet.getString(1);
                   break;
               }
           }
       });
       int serialAsInt = Integer.parseInt(nextSerialNumber[0]);

       nextSerialNumber[0] = String.format("%07d",serialAsInt+1);

       command = "update SerialTable set serial = '" + nextSerialNumber[0] + "'";
       service.execute(command);

       return nextSerialNumber[0];
    }
}
