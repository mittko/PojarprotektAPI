package com.example.demo.controllers.init;

import com.example.demo.services.RepoService;
import com.example.demo.requestbodies.DemoPostBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


@RestController
public class RepoController {

    @Autowired
    public RepoService repoService;

    @GetMapping(path = "/all")
    public @ResponseBody ArrayList<Object> getAllUsers2() throws SQLException {
        return repoService.getTEST("");

    }

    @GetMapping(path="/tech_review")
    public @ResponseBody ArrayList<Object[]> getTechnicalReview(@RequestParam("from") String from,
    @RequestParam("to") String to) throws SQLException {

        return repoService.getDataArrays(String.format("select client, type, wheight, T_O, P, HI, number, additional_data from ProtokolTableDB5 "
                + " where (T_O <> 'не' and T_O between Date('%s') and Date('%s') or P <> 'не' and P between Date('%s')" +
                " and Date('%s') or  HI <> 'не' and HI between Date('%s') and Date('%s') ) and (uptodate is null)",
                from,to,from,to,from,to));
    }

//    @GetMapping(path = "/client_data")
//    public @ResponseBody ArrayList<Object> getClientData(@RequestParam("client") String client) throws SQLException {
//        return repoService.getData(String.format("select * from FirmsTable where firm = '%s'",client));
//    }

    /*In Postman. under Body, select raw and choose JSON from the drop down menu that appears.
    Then write the JSON that is the request body.
    You can't use form-data or x-www-form-urlencoded with
    @RequestBody, they are used when the binding is @ModelAttribute.*/
    @PostMapping(path = "/add")
    public Map<String,Boolean> add(@RequestBody DemoPostBody body) {
        repoService.insertTEST(body.getId(), body.getName());
        return Collections.singletonMap("added",true);
    }
}
