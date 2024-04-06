package com.example.demo;

import com.example.demo.exceptions.MyControllerAdvice;
import com.example.demo.requestbodies.DemoPostBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;

@Service
public class RepoService {

    @Autowired
    MyControllerAdvice myControllerAdvice;

    public Connection getNetworkConnection() {
        String networkConnection = "jdbc:derby://localhost:1527/D:/RealDB";
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
    public Connection getEmbeddedConnection() {
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

    public ArrayList<Object> getTEST() throws SQLException {

        String command = "select * from TEST3a";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData;
        ArrayList<Object> data = new ArrayList<>();


            connection = getEmbeddedConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(command);
            while (resultSet.next()) {
                DemoPostBody body = new DemoPostBody(resultSet.getInt(1),resultSet.getString(2));
                data.add(body);
                System.out.println();
            }
        return data;
    }

    public int insertTEST(int id, String  name) {
        String command = "insert into TEST3 values (" + id + ",'" + name +"')";
        Connection connection = null;
        Statement statement = null;
        int insert = 0;
        try {
            connection = getEmbeddedConnection();
            statement = connection.createStatement();
            insert = statement.executeUpdate(command);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return insert;
    }
}
