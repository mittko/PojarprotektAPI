package com.example.demo.controllers.init;


import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.models.User;
import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class GetUser<T> {

    @Autowired
    public RepoService<T> repoService;

    @GetMapping(path = "/user_data")
    public @ResponseBody T getUser(@RequestParam("user") String user) throws SQLException {
          User<T> user1 = new User();
          repoService.getResult("select usser, password, Service_Order, Working_Book,"
                  + " Invoice, Reports,"
                  + " New_Ext , Hidden_Menu, Acquittance from TeamDB " // ,
                  + " where usser = " + "'" + user + "'", new ResultSetCallback() {
              @Override
              public void result(ResultSet rs) throws SQLException {
                  while (rs.next()) {
                      user1.setUsser(rs.getString(1));
                      user1.setPassword(rs.getString(2));
                      user1.setService_Order(rs.getString(3));
                      user1.setWorking_Book(rs.getString(4));
                      user1.setInvoice(rs.getString(5));
                      user1.setReports(rs.getString(6));
                      user1.setNew_Ext(rs.getString(7));
                      user1.setHidden_Menu(rs.getString(8));
                      user1.setAcquittance(rs.getString(9));
                  }
              }
          });
          return (T) user1;
    }
}
