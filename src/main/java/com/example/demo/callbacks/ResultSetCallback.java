package com.example.demo.callbacks;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ResultSetCallback {
    public abstract void result(ResultSet resultSet) throws SQLException;

}
