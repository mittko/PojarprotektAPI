package com.example.demo.controllers.init;

import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;


@RestController
public class GetTechnicalReview {
    @Autowired
    public RepoService repoService;
    @GetMapping(path = "/client_data")
    public @ResponseBody ArrayList<Object> getClientData(@RequestParam("client") String client) throws SQLException {
        return repoService.getData(String.format("select * from FirmsTable where firm = '%s'",client));
    }
}
