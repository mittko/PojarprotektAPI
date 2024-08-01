package com.example.demo.controllers.office;


import com.example.demo.callbacks.PreparedStatementCallback;
import com.example.demo.models.ServiceOrderBodyList;
import com.example.demo.models.ServiceOrderModel;
import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        return result[0];
    }
}
