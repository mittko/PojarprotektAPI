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
}
