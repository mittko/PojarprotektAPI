package com.example.demo.services;

import com.example.demo.callbacks.PreparedStatementCallback;
import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.db.DBManager;
import com.example.demo.exceptions.MyControllerAdvice;
import com.example.demo.models.Firm;
import com.example.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;

import static com.example.demo.db.DBManager.getEmbeddedConnection;

@Service
public class RepoService<T> {

    public void getResult(String command, ResultSetCallback resultSetCallback) throws SQLException {
        try (Connection connection = getEmbeddedConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(command);
            resultSetCallback.result(resultSet);
        }
    }

    public int insert(String command) throws SQLException {
        int result;
       try(Connection connection = getEmbeddedConnection(); Statement statement = connection.createStatement()) {
         result = statement.executeUpdate(command);
       }
       return result;
    }

    public void insert(String command, PreparedStatementCallback<T> callback) throws SQLException {
        try(Connection connection = getEmbeddedConnection(); PreparedStatement statement = connection.prepareStatement(command)) {
            callback.callback(statement);
        }
    }

}
