package com.example.demo.controllers.init;

import com.example.demo.models.TechnicalReview;
import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;


@RestController
public class GetTechnicalReview<T> {
    @Autowired
    public RepoService<T> repoService;
    @GetMapping(path = "/client_data")
    public @ResponseBody T getClientData(@RequestParam("client") String client) throws SQLException {
        return repoService.getData(String.format("select * from FirmsTable where firm = '%s'",client));
    }
    @GetMapping(path="/tech_review")
    public @ResponseBody ArrayList<T> getTechnicalReview(@RequestParam("from") String from,
                                                                       @RequestParam("to") String to) throws SQLException {

        return repoService.getDataArrays(String.format("select client, type, wheight, T_O, P, HI, number, additional_data from ProtokolTableDB5 "
                        + " where (T_O <> 'не' and T_O between Date('%s') and Date('%s') or P <> 'не' and P between Date('%s')" +
                        " and Date('%s') or  HI <> 'не' and HI between Date('%s') and Date('%s') ) and (uptodate is null)",
                from,to,from,to,from,to));
    }
}
