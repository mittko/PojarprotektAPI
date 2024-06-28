package com.example.demo.controllers.init;


import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class GetUser<T> {

    @Autowired
    public RepoService<T> repoService;

    @GetMapping(path = "/user_data")
    public @ResponseBody T getUser(@RequestParam("user") String user) throws SQLException {
          return repoService.getUser( "select usser, password, Service_Order, Working_Book,"
                  + " Invoice, Reports,"
                  + " New_Ext , Hidden_Menu, Acquittance from TeamDB " // ,
                  + " where usser = " + "'" + user + "'");
    }
}
