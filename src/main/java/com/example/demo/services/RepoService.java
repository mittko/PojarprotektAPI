package com.example.demo.services;

import com.example.demo.db.DBManager;
import com.example.demo.exceptions.MyControllerAdvice;
import com.example.demo.models.TechnicalReview;
import com.example.demo.requestbodies.DemoPostBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;

@Service
public class RepoService {

    @Autowired
    MyControllerAdvice myControllerAdvice;


    public ArrayList<TechnicalReview> getDataArrays(String command) throws SQLException {
        ResultSet resultSet =  DBManager.getListOfObjectArrays(command);
        ArrayList<TechnicalReview> technicalReviewList = new ArrayList<>();
        while (resultSet.next()) {

                TechnicalReview technicalReview = new TechnicalReview();

                technicalReview.setClient(resultSet.getString(1));
                technicalReview.setType(resultSet.getString(2));
                technicalReview.setWheight(resultSet.getString(3));
                technicalReview.setT_O(resultSet.getString(4));
                technicalReview.setP(resultSet.getString(5));
                technicalReview.setHI(resultSet.getString(6));
                technicalReview.setNumber(resultSet.getString(7));
                technicalReview.setAdditional_data(resultSet.getString(8));

                technicalReviewList.add(technicalReview);
        }
        return technicalReviewList;
    }
    public ArrayList<Object> getData(String command) throws SQLException {
        return DBManager.getListOfObjects(command);
    }

    public ArrayList<Object> getTEST(String command) throws SQLException {

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData;
        ArrayList<Object> data = new ArrayList<>();


            connection = DBManager.getEmbeddedConnection();
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
            connection = DBManager.getEmbeddedConnection();
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
