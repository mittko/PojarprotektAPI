package com.example.demo.controllers.new_extinguishers;

import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.models.ExtinguisherModel;
import com.example.demo.models.NewExtinguishersModel;
import com.example.demo.services.RepoService;
import org.aspectj.weaver.ast.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
}
