package com.example.demo.controllers.office;


import com.example.demo.callbacks.PreparedStatementCallback;
import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.ProtokolModel;
import com.example.demo.models.ServiceOrderBodyList;
import com.example.demo.models.ServiceOrderModel;
import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class ServiceOrderController<T> {

    @Autowired
    RepoService<T> service;

    @PostMapping("/insert_service_order")
    public Integer insertServiceOrder(@RequestBody ServiceOrderBodyList<T> serviceOrderBodyList) throws SQLException {
        String command = "insert into ServiceTableDB" +
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
                                                    @RequestParam(value = "serial_number", required = false) String serialNumber) throws SQLException, NotFoundException {
        ProtokolModel protokolModel = new ProtokolModel();
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

        final boolean[] found = {false};

        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {

                    found[0] = true;
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
        if(!found[0]) {
            throw new NotFoundException("Не е намерен такъв елемент");
        }
        return (T) protokolModel;
    }

    @GetMapping("/next_serial_number")
    public @ResponseBody String getNextSerialNumber() throws SQLException {
       String command = "select integer(serial) from SerialTable";
        final int[] nextSerialNumber = {0};
       service.getResult(command, new ResultSetCallback() {
           @Override
           public void result(ResultSet resultSet) throws SQLException {
               while (resultSet.next()) {
                   nextSerialNumber[0] = resultSet.getInt(1);
                   break;
               }
           }
       });
       if(nextSerialNumber[0] == 0) {
           command = "insert into SerialTable values ('" + (nextSerialNumber[0]+1) + "')";
       } else {
           command = "update SerialTable set serial = '" + (nextSerialNumber[0]+1) + "'";
       }

       int result = service.execute(command);

       return String.format("%07d",nextSerialNumber[0]+1);
    }


    @GetMapping("/next_service_order_number")
    public @ResponseBody String getNextServiceOrderNumber() throws SQLException {
        String command = "select min(integer(so)) from SO_Table";
        final int[] currentSONumber = {0};
        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    currentSONumber[0] = resultSet.getInt(1);
                    break;
                }
            }
        });
       // currentSONumber[0] = 1000024034;
        String nextSoNumber = String.format("%09d",currentSONumber[0]+1);
        if(currentSONumber[0] == 0) {
            command = "insert into SO_Table values ('" + nextSoNumber + "')";
        } else {
            command = "update SO_Table set so = '" + nextSoNumber + "'";
        }
        service.execute(command);
        return nextSoNumber;
    }
}
