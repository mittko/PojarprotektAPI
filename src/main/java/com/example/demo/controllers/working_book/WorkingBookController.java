package com.example.demo.controllers.working_book;

import com.example.demo.callbacks.PreparedStatementCallback;
import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.exceptions.NotFoundException;
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
                                                        @RequestParam(value = "serial_number",required = false) String serialNumber) throws SQLException, NotFoundException {

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

        final ServiceOrderModel<T>[] serviceOrderModel = new ServiceOrderModel[1];
        final boolean[] found = {false};
        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    found[0] = true;
                    serviceOrderModel[0] = new ServiceOrderModel<>();
                    serviceOrderModel[0].setClient(resultSet.getString(1));
                    serviceOrderModel[0].setType(resultSet.getString(2));
                    serviceOrderModel[0].setWheight(resultSet.getString(3));
                    serviceOrderModel[0].setBarcod(resultSet.getString(4));
                    serviceOrderModel[0].setSerial(resultSet.getString(5));
                    serviceOrderModel[0].setCategory(resultSet.getString(6));
                    serviceOrderModel[0].setBrand(resultSet.getString(7));
                    serviceOrderModel[0].setT_O(resultSet.getString(8));
                    serviceOrderModel[0].setP(resultSet.getString(9));
                    serviceOrderModel[0].setHI(resultSet.getString(10));
                    serviceOrderModel[0].setAdditional_data(resultSet.getString(11));

                }
            }
        });
        if(!found[0]) {
            throw new NotFoundException("Не е намерен такъв елемент");
        }
        return (T) serviceOrderModel[0];
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
    public @ResponseBody ProtokolInfo getProtokolInfo(@RequestParam("number") String number) throws SQLException, NotFoundException {
        String command = "select T_O, P, HI, client, type, wheight, category, parts, value, kontragent, invoiceByKontragent  "
                + "from ProtokolTableDB5 where number = '" + number + "'";
        ProtokolInfo protokolInfo = new ProtokolInfo();
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
        if(models.size() > 0) {
            String client = ((ProtokolModel)models.get(0)).getClient();

            Firm<T> firm = new Firm<T>();
            firm.setFirm(" ? ");
            firm.setDiscount(0);
            firm.setVat_registration("не");

            service.getResult(String.format("select firm, discount, vat_registration from FirmsTable where firm = '%s'",client), new ResultSetCallback() {
                @Override
                public void result(ResultSet resultSet) throws SQLException {
                    while (resultSet.next()) {
                        firm.setFirm(resultSet.getString(1));
                        firm.setDiscount(Integer.parseInt(resultSet.getString(2)));
                        firm.setVat_registration(resultSet.getString(3));
                        break;
                    }
                }
            });

            if(firm.getFirm() == null) {
                service.getResult(String.format("select name, discount from PersonsTable where name = '%s'", client), new ResultSetCallback() {
                    @Override
                    public void result(ResultSet resultSet) throws SQLException {
                        while (resultSet.next()) {
                            firm.setFirm(resultSet.getString(1));
                            firm.setDiscount(Integer.parseInt(resultSet.getString(2)));
                        }
                    }
                });
            }
            protokolInfo.setProtokolModels((List<ProtokolModel>) models);
            protokolInfo.setFirm(firm);
        } else {
            throw new NotFoundException("Не е намерен такъв протокол");
        }

        return protokolInfo;
    }

    @GetMapping("/get_protokol_number")
    public @ResponseBody String getProtokolNumber() throws SQLException {
        final int[] currentNumber = {0};
        service.getResult("select max(integer(number)) from ProtokolTableDB5 ", new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    currentNumber[0] = resultSet.getInt(1);
                    break;
                }
            }
        });
        return String.format("%07d",currentNumber[0]);
    }
}
