package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class RepoService {
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

    public ArrayList<ArrayList<Object>> getTEST() {

        String command = "select * from TEST3";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData;
        ArrayList<ArrayList<Object>> data = new ArrayList<>();
        try {

            connection = getEmbeddedConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(command);
            while (resultSet.next()) {
                resultSetMetaData = resultSet.getMetaData();
                ArrayList<Object> list = new ArrayList<>();
                for(int column = 0;column < resultSetMetaData.getColumnCount();column++) {
                    System.out.print(resultSet.getString(column+1)+" ");
                    list.add(resultSet.getString(column+1));
                }
                data.add(list);
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(resultSet != null) {
                    resultSet.close();
                }
                if(statement != null) {
                    statement.close();
                }
                if(connection != null) {
                    connection.commit();
                }
            } catch (SQLException ignored) {}

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
