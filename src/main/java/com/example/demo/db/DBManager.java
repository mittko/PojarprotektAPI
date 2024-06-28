package com.example.demo.db;

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
        String embeddedConnection = "jdbc:derby:D:/RealDBAPI";
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

    public static ResultSet getResultSet(String command) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getEmbeddedConnection();
            statement = connection.createStatement();
        }finally {
//            if(statement != null) {

//                statement.close();
//            }
//            if(connection != null) {
//                connection.close();
//            }
        }

        return statement != null ? statement.executeQuery(command) : null;
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
