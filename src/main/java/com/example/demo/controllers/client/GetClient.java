package com.example.demo.controllers.client;

import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.models.Firm;
import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class GetClient<T> {

    @Autowired
    public RepoService<T> service;

    @GetMapping(path = "/client_data")
    public @ResponseBody T getClientData(@RequestParam("client") String client) throws SQLException {
        Firm<T> firm = new Firm<T>();
        service.getResult(String.format("select * from FirmsTable where firm = '%s'",client), new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    firm.setFirm(resultSet.getString(1));
                    firm.setCity(resultSet.getString(2));
                    firm.setAddress(resultSet.getString(3));
                    firm.setEik(resultSet.getString(4));
                    firm.setMol(resultSet.getString(5));
                    firm.setEmail(resultSet.getString(6));
                    firm.setPerson(resultSet.getString(7));
                    firm.setTelPerson(resultSet.getString(8));
                    firm.setBank(resultSet.getString(9));
                    firm.setBic(resultSet.getString(10));
                    firm.setIban(resultSet.getString(11));
                    firm.setDiscount(resultSet.getString(12));
                    firm.setIncorrect_person(resultSet.getString(13));
                    firm.setVat_registration(resultSet.getString(14));

                    break;
                }
            }
        });
        return (T) firm;
    }

}
