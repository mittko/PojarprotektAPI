package com.example.demo.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.util.ArrayList;

public class DBManager {

    private Connection getNetworkConnection() {
        String networkConnection = "jdbc:derby://localhost:1527/D:/RealDBAPI";
        Connection connection;

        try {
            String driver = "org.apache.derby.jdbc.ClientDriver";
            try {
                Class.forName(driver).newInstance();
            } catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            connection = DriverManager.getConnection(networkConnection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }


    public static Connection getEmbeddedConnection() {
        String embeddedConnection = "jdbc:derby:D:/RealDB";
        Connection connection;
        try {
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            try {
                Class.forName(driver).newInstance();
            } catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            connection = DriverManager.getConnection(embeddedConnection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

}
