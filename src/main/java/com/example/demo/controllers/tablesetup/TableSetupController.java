package com.example.demo.controllers.tablesetup;

import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
public class TableSetupController {

    @Autowired
    RepoService service;

    @PostMapping("/create_barcode_table")
    private int createBarcodeTable() throws SQLException {
        String command = "create table ARTIKUL_BARCODE3 (barcode varchar(20), artikul varchar(200), primary key (barcode))";
        return service.execute(command);
    }
}
