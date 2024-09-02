package com.example.demo.controllers.init;


import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.models.User;
import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/users")
    public @ResponseBody List<User<T>> getUsers() throws SQLException {
        String command = "select * from TeamDB";
        ArrayList<User<T>> users = new ArrayList<>();
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet rs) throws SQLException {
                while (rs.next()) {
                    User<T> user = new User<T>();
                    user.setUsser(rs.getString(1));
                    user.setPassword(rs.getString(2));
                    user.setService_Order(rs.getString(3));
                    user.setWorking_Book(rs.getString(4));
                    user.setInvoice(rs.getString(5));
                    user.setReports(rs.getString(6));
                    user.setNew_Ext(rs.getString(7));
                    user.setHidden_Menu(rs.getString(8));
                    user.setAcquittance(rs.getString(9));

                    users.add(user);
                }
            }
        });
        return users;
    }

    @PostMapping("/create_user")
    public int createUser(@RequestBody User user) throws SQLException {
        String command = "insert into TeamDB values ('" + user.getUsser()
                + "','" + user.getPassword() + "','" + user.getService_Order() + "','"
                + user.getWorking_Book() + "','" + user.getInvoice() + "','"
                + user.getReports() + "','" + user.getNew_Ext() + "','"
                + user.getHidden_Menu() +  "','" + user.getAcquittance() + "','" + "" +  "')";

        return repoService.execute(command);
    }

    @DeleteMapping("/delete_user/{user}")
    public int deleteUser(@PathVariable("user") String user) throws SQLException {
        String command = "delete from TeamDB where usser = " + "'"
                + user + "'";
        return repoService.execute(command);
    }

}
