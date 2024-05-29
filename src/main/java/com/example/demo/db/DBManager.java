package com.example.demo.db;

import java.sql.*;
import java.util.ArrayList;

public class DBManager {

    private Connection getNetworkConnection() {
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

    public static ArrayList<Object[]> getListOfObjectArrays(String command) throws SQLException {
        ArrayList<Object[]> listOfArr = new ArrayList<>();
        Connection connection;
        Statement statement;
        ResultSet resultSet;
        ResultSetMetaData resultSetMetaData;

        connection = getEmbeddedConnection();
        statement = connection.createStatement();
        resultSet = statement.executeQuery(command);

        while (resultSet.next()) {
            resultSetMetaData = resultSet.getMetaData();
            ArrayList<String> objects = new ArrayList<>();
            for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                objects.add(resultSet.getString(i+1));
            }
            listOfArr.add(objects.toArray());
        }
        return listOfArr;
    }
    public static ArrayList<Object> getListOfObjects(String command) throws SQLException {
        ArrayList<Object> listOfObjects = new ArrayList<>();
        Connection connection;
        Statement statement;
        ResultSet resultSet;
        ResultSetMetaData resultSetMetaData;

        connection = getEmbeddedConnection();
        statement = connection.createStatement();
        resultSet = statement.executeQuery(command);

        while (resultSet.next()) {
            resultSetMetaData = resultSet.getMetaData();
            for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                listOfObjects.add(resultSet.getString(i+1));
            }
        }
        return listOfObjects;
    }
}
