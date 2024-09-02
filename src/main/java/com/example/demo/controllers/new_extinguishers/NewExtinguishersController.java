package com.example.demo.controllers.new_extinguishers;

import com.example.demo.callbacks.PreparedStatementCallback;
import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.models.*;
import com.example.demo.services.RepoService;
import org.aspectj.weaver.ast.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class NewExtinguishersController<T> {

    @Autowired
    RepoService<T> service;

    @GetMapping("/extinguisher_shop")
    public @ResponseBody List<ExtinguisherModel> getExtinguishers() throws SQLException {
        ArrayList<ExtinguisherModel> models = new ArrayList<>();
        String command = "select * from NewExtinguishersDB3 where quantitiy > 0 order by CAST(date as DATE) desc";
        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    ExtinguisherModel model = new ExtinguisherModel();

                    model.setType(resultSet.getString(1));
                    model.setWheight(resultSet.getString(2));
                    model.setCategory(resultSet.getString(3));
                    model.setBrand(resultSet.getString(4));
                    model.setQuantity(resultSet.getString(5));
                    model.setPrice(resultSet.getString(6));
                    model.setInvoiceByKontragent(resultSet.getString(7));
                    model.setKontragent(resultSet.getString(8));
                    model.setDateString(resultSet.getString(9));
                    model.setSaller(resultSet.getString(10));
                    model.setPercentProfit(resultSet.getString(11));

                    models.add(model);

                }
            }
        });

        return models;
    }

    @PostMapping(path = "/insert_new_extinguisher")
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
                    " values ('" + protokolModel.getClient() + "','" + (protokolModel.getType() + " ( Нов )") + "','"
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



        for (ProtokolModel model : body.getList()) {

            if (model.getKontragent() != null) {
                // колона 7 е за контрагента ако е null или Пожарпротект
                // значи пожарогасителя е излезнал от работилницата
                // ako e null значи е направено обслужване в работилницата иначе e нов пожарогасител
                int quantityToDecrease = 1;// one row for every extinguisher
                String typeExtinguisher = model.getType();
                String wheight = model.getWheight();
                String category = model.getCategory();
                String brand = model.getBrand();
                String kontragent = model.getKontragent();
                String invoice = model.getInvoiceByKontragent();

                command = "select type, wheight, category, brand,"
                        + " quantitiy, invoice, client, date from NewExtinguishersDB3"
                        + " where ( (client = '"
                        + kontragent
                        + "') and (invoice = '"
                        + invoice
                        + "')"
                        + " and  (type = " + "'" + typeExtinguisher + "')"

                        + " and (wheight = " + "'" + wheight + "')"

                        + " and (category = " + "'" + category + "')"

                        /*+ " and (brand = " + "'" + brand + "')"*/

                        + " and (quantitiy > 0) )";

                        ArrayList<ExtinguisherModel> availableExtinguishers = new ArrayList<>();
                        service.getResult(command, new ResultSetCallback() {
                            @Override
                            public void result(ResultSet rs) throws SQLException {
                                while (rs.next()) {
                                    String typeT = rs.getString(1);
                                    String wheightT = rs.getString(2);
                                    String categoryT = rs.getString(3);
                                    String brandT = rs.getString(4);
                                    String quantityT = rs.getString(5);
                                    String invoiceT = rs.getString(6);
                                    String kontragentT = rs.getString(7);
                                    String dateT = rs.getString(8);
                                    ExtinguisherModel ext = new ExtinguisherModel(typeT, wheightT,
                                            categoryT, brandT, quantityT, invoiceT, kontragentT,
                                            dateT);

                                    availableExtinguishers.add(ext);
                                }
                            }
                        });

                for (ExtinguisherModel ext : availableExtinguishers) {

                    command = "update NewExtinguishersDB3"
                            + " set quantitiy = quantitiy - ? where (type = ? "
                            + "and wheight = ? and category = ? " //  and brand = ?
                            + "and invoice = ? and client = ?) and (quantitiy > 0)";
                    service.execute(command, new PreparedStatementCallback<T>() {
                        @Override
                        public void callback(PreparedStatement ps) throws SQLException {
                            int quantityToDecrease = 1;// one row for every extinguisher
                            ps.setString(1, quantityToDecrease + "");
                            ps.setString(2, ext.getType());
                            ps.setString(3, ext.getWheight());
                            ps.setString(4, ext.getCategory());
                            /*	ps.setString(5, brand);*/
                            ps.setString(5, ext.getInvoiceByKontragent());
                            ps.setString(6, ext.getKontragent());
                            ps.executeUpdate();
                        }
                    });
                    break;

                }
            }
        }
        return nextProtokolNumber;
    }

    @PostMapping("/create_new_extingusihser")
    public int createNewExtinguisher(@RequestBody ExtinguisherModel body) throws SQLException {
        String command = "insert into  NewExtinguishersDB3 values('" + body.getType() + "','"
                + body.getWheight() + "','" + body.getCategory() + "','" + body.getBrand() + "'," + body.getQuantity()
                + ",'" + body.getPrice() + "','" + body.getInvoiceByKontragent() + "','" + body.getKontragent() + "','"
                + body.getDateString() + "','" + body.getSaller() + "','" + body.getPercentProfit() + "')";

        service.execute(command);


        command = "insert into DeliveryArtikulsDB2 values ('" + (body.getType() + " ( Нов ) " + body.getWheight()) + "',"
                + body.getQuantity() + ",'"
                + "броя" + "','" + body.getPrice() + "','" + body.getKontragent() + "','"
                + body.getInvoiceByKontragent() + "','" + body.getDateString() + "','" + body.getSaller() + "')";

        service.execute(command);

        return 1;
    }

    @PutMapping("/update_extinguisher_quantity/{quantity}/{kontragent}/{invoiceByKontragent}/{type}/{weight}/{category}/{brand}")
    public int updateExtinguisherQuantity(@PathVariable("quantity") String quantity, @PathVariable("kontragent") String kontragent,
                                          @PathVariable("invoiceByKontragent") String invoiceByKontragent,
                                          @PathVariable("type") String type, @PathVariable("weight") String weight,
                                          @PathVariable("category") String category, @PathVariable("brand") String brand) throws SQLException {
        String command = "update NewExtinguishersDB3 set quantitiy = " + "(" + quantity
                + ")" + " where ( (client = '" + kontragent
                + "') and (invoice = '" + invoiceByKontragent
                + "') and (type = '" + type + "') and (wheight = '" + weight
                + "') and (category = '" + category + "') and (brand = '"
                + brand + "') )";

        return service.execute(command);
    }

    @PutMapping(path = "/update_extinguisher_price/{price}/{percentProfit}/{type}/{weight}/{category}/{brand}/{client}/{invoice}")
    public int updateExtinguisherPrice(@PathVariable("price") String price, @PathVariable("percentProfit") String percentProfit,
                                       @PathVariable("type") String type, @PathVariable("weight") String weight,
                                       @PathVariable("category") String category, @PathVariable("brand") String brand,
                                       @PathVariable("client") String kontragent, @PathVariable("invoice") String invoiceByKontragent) throws SQLException {
        String command = "update NewExtinguishersDB3 set price = '"
                + price + "' , percentProfit = '" + percentProfit
                + "' where type = '" + type + "' and wheight = '" + weight
                + "'" + " and category = '" + category + "'" + " and brand = '"
                + brand + "' and client = '" + kontragent + "' and invoice = '"
                + invoiceByKontragent + "'";

        return service.execute(command);
    }

    @DeleteMapping("/delete_extinguisher/{type}/{weight}/{category}/{brand}/{invoiceByKontragent}/{kontragent}")
    public int deleteExtinguisher(@PathVariable("type") String type, @PathVariable("weight") String weight,
                                  @PathVariable("category") String category, @PathVariable("brand") String brand,
                                  @PathVariable("invoiceByKontragent") String invoiceByKontragent,
                                  @PathVariable("kontragent") String kontragent) throws SQLException {
        String command = "delete from NewExtinguishersDB3 where type like '" + type + "'" + " and wheight like '"
                + weight + "'" + " and category like '" + category + "'"
                + " and brand like '" + brand + "'" + " and invoice = '"
                + invoiceByKontragent + "'" + " and client like '" + kontragent + "'" + "";

        service.execute(command);

        command = "delete from DeliveryArtikulsDB2 where artikul = ? and kontragent = ? and invoiceByKontragent = ?";
        service.execute(command, new PreparedStatementCallback<T>() {
            @Override
            public void callback(PreparedStatement ps) throws SQLException {
                ps.setString(1, type + " ( Нов ) " + weight);
                ps.setString(2, kontragent);
                ps.setString(3, invoiceByKontragent);
                ps.executeUpdate();// stat.getUpdateCount();
            }
        });
        return 1;
    }

}
