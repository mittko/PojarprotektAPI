package com.example.demo.services;

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

}
