package com.example.demo.controllers.sklad;

import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.models.ArtikulModel;
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
}
