package com.example.demo.callbacks;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class PreparedStatementCallback<T> {
    public abstract void callback(PreparedStatement ps) throws SQLException;
}
