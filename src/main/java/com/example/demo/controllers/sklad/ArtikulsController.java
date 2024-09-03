package com.example.demo.controllers.sklad;

import com.example.demo.callbacks.PreparedStatementCallback;
import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.models.ArtikulModel;
import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ArtikulsController<T> {

    @Autowired
    RepoService<T> service;

    @GetMapping(path = "/artikuls_data")
    public @ResponseBody List<T> getArtikuls(@RequestParam("grey") boolean grey,
                                             @RequestParam(value = "order_by_date") boolean order_by_date) throws SQLException {
        String table = grey ? "GreyArtikulsDB" : "ArtikulsDB" ;
        String orderBy = order_by_date ? "CAST(date as DATE) desc" : "artikul";
        String command = String.format("select * from %s order by %s",table,orderBy);
        ArrayList<T> artikuls = new ArrayList<>();
        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    ArtikulModel model = new ArtikulModel();

                    model.setArtikul(resultSet.getString(1));
                    model.setQuantity(Integer.parseInt(resultSet.getString(2)));
                    model.setMed(resultSet.getString(3));
                    model.setPrice(Float.parseFloat(resultSet.getString(4).replace(",",".")));
                    model.setInvoice(resultSet.getString(5));
                    model.setKontragent(resultSet.getString(6));
                    model.setDate(resultSet.getString(7));
                    model.setPerson(resultSet.getString(8));
                    model.setPercentProfit(resultSet.getString(9));

                    artikuls.add((T) model);
                }
            }
        });
        return artikuls;
    }

    @PostMapping(path = "/insert_artikul")
    public int insertArtikul(@RequestBody ArtikulModel body) throws SQLException {

        // insert into available (sklad)
        String command = "insert into ArtikulsDB values ('" + body.getArtikul() + "',"
                + body.getQuantity() + ",'" + body.getMed()
                + "','" + body.getPrice() + "','" + body.getInvoice() + "','" + body.getKontragent() + "','"
                + body.getDate() + "','" + body.getPerson() + "','" + body.getPercentProfit() + "','" + body.getBarcod() +  "')";

        service.execute(command);

        // insert into delivery
        command = "insert into DeliveryArtikulsDB2"
                + " values ('" + body.getArtikul() + "'," + body.getQuantity() + ",'" + body.getMed() + "','"
                + body.getPrice() + "','" + body.getKontragent() + "','" + body.getInvoice() + "','" + body.getDate()
                + "','" + body.getPerson() + "')";

        service.execute(command);

        return 1;
    }

    @DeleteMapping(path = "/delete_artikul/{artikul}/{kontragent}/{invoiceByKontragent}")
    public int deleteArtikul(@PathVariable("artikul") String artikul, @PathVariable("kontragent") String kontragent,
                             @PathVariable("invoiceByKontragent") String invoiceByKontragent) throws SQLException {

        // delete from available (sklad)
        String command = "delete from ArtikulsDB"
                + " where artikul = ? and client = ? and invoice = ?";
        service.execute(command, new PreparedStatementCallback<T>() {
            @Override
            public void callback(PreparedStatement ps) throws SQLException {
                ps.setString(1, artikul);
                ps.setString(2, kontragent);
                ps.setString(3, invoiceByKontragent);
                ps.executeUpdate();
            }
        });

        // delete from delivery
        command = "delete from DeliveryArtikulsDB2"
                + " where artikul = ? and kontragent = ? and invoiceByKontragent = ?";
        service.execute(command, new PreparedStatementCallback<T>() {
            @Override
            public void callback(PreparedStatement ps) throws SQLException {
                ps.setString(1, artikul);
                ps.setString(2, kontragent);
                ps.setString(3, invoiceByKontragent);
                ps.executeUpdate();
            }
        });
        return 0;
    }

    @PutMapping("/rename_artikul/{oldName}/{newName}")
    public int renameArtikul(@PathVariable("oldName") String oldName, @PathVariable("newName") String newName) throws SQLException {
     String command = "update ArtikulsDB set artikul = '" // artikul
             + newName + "' where artikul = '" + oldName + "'";

     service.execute(command);

     command = "update DeliveryArtikulsDB2 set artikul = '" // artikul
                + newName + "' where artikul = '" + oldName + "'";

     service.execute(command);

     command = "update InvoiceChildDB7 set artikul = '" // artikul
                + newName + "' where artikul = '" + oldName + "'";

     service.execute(command);

     command = "update ProformChildDB2 set make = '" // artikul
                + newName + "' where make = '" + oldName + "'";

     service.execute(command);

     command = "update AcquittanceChildDB set artikul = '" // artikul
                + newName + "' where artikul = '" + oldName + "'";

     service.execute(command);

     return 1;

    }

    @PutMapping("/edit_artikul_quantity/{artiukl}/{kontragent}/{invoiceByKontragent}/{newQuantity}")
    public int editArtikulQuantity(@PathVariable("artiukl") String artikul, @PathVariable("kontragent") String kontragent,
                                   @PathVariable("invoiceByKontragent") String invoiceByKontragent,
                                   @PathVariable("newQuantity") String newQuantity) throws SQLException {
      String command = "update ArtikulsDB"
              + " set quantity = ? where (artikul = ? and client = ? and invoice = ?)";
        final int[] update = new int[1];
      service.execute(command, new PreparedStatementCallback<T>() {
          @Override
          public void callback(PreparedStatement ps) throws SQLException {
              ps.setString(1, newQuantity);
              ps.setString(2, artikul);
              ps.setString(3, kontragent);
              ps.setString(4, invoiceByKontragent);
              update[0] = ps.executeUpdate();
          }
      });

      return update[0];
    }

    @PutMapping("/edit_artikul_price/{newValue}/{percentProfit}/{artikul}/{kontragent}/{invoiceByKontragent}")
    public int editArtikulPrice(@PathVariable("newValue") String newValue, @PathVariable("percentProfit") String percentProfit,
                                @PathVariable("artikul") String artikul, @PathVariable("kontragent") String kontragent,
                                @PathVariable("invoiceByKontragent") String invoiceByKontragent) throws SQLException {
        String command = "update ArtikulsDB set value = '"
                + newValue + "', percentProfit = '" + percentProfit
                + "' where (artikul = '" + artikul + "' and client = '"
                + kontragent + "' and invoice = '" + invoiceByKontragent + "')";

        return service.execute(command);
    }


    @PutMapping("/update_parts_price/{price}/{part}/{type}/{wheight}/{category}")
    public int updatePartsPrice(@PathVariable("price") String price, @PathVariable("part") String part,
                                @PathVariable("type") String type, @PathVariable("wheight") String wheight,
                                @PathVariable("category") String category) throws SQLException {
        String update = "update PartsTableDB set price = " + price  + " where " + "part = "
                + "'" + part + "'" + " and " + "type = " + "'" + type + "'" + " and " + "wheight = "
                + "'" + wheight + "'" + " and " + "category = " + "'" + category + "'";

        return service.execute(update);
    }


    @GetMapping("/get_part_price")
    public double getPartPrice(@RequestParam("part") String part, @RequestParam("type") String type,
                               @RequestParam("category") String category, @RequestParam("weight") String weight) throws SQLException {
        String command = "select price from PartsTableDB where part = '" + part + "'"
                + " and type = '" + type + "'" + " and category = '" + category + "'" +
                " and wheight = '" + weight + "'";

        final double[] price = {0.0d};
        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                   price[0] = resultSet.getDouble(1);
                   break;
                }
            }
        });
        return price[0];
    }

    @GetMapping(path = "/artikul_value/{table}")
    public double getArtikulValue(@PathVariable("table") String table, @RequestParam("artikul") String artikul) throws SQLException {
        // table DeliveryArtikulsDB2 or ArtikulsDB
        String command = "select max(double(value)) from " + table
               + " where artikul = '" + artikul + "'";
        final double[] maxValue = new double[1];
       service.getResult(command, new ResultSetCallback() {
           @Override
           public void result(ResultSet resultSet) throws SQLException {
               while (resultSet.next()) {
                   maxValue[0] = resultSet.getDouble(1);
                   break;
               }
           }
       });
       return maxValue[0];
    }

    @GetMapping("/extinguisher_value/{type}/{weight}/{category}/{brand}")
    public double getExtinguisherValue(@PathVariable("type") String type,
                                       @PathVariable("weight") String weight,
                                       @PathVariable("category") String category,
                                       @PathVariable("brand") String brand) throws SQLException {
        String command = "select max(double(price)) from NewExtinguishersDB3"
                + " where type = '" + type + "' and wheight =  '" + weight
                + "' and category = '" + category + "' and brand =  '" + brand
                + "'";
        final double[] maxValue = new double[1];
        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    maxValue[0] = resultSet.getDouble(1);
                    break;
                }
            }
        });
        return maxValue[0];
    }
}
