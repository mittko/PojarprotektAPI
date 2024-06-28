package com.example.demo.services;

import com.example.demo.db.DBManager;
import com.example.demo.exceptions.MyControllerAdvice;
import com.example.demo.models.TechnicalReview;
import com.example.demo.models.User;
import com.example.demo.requestbodies.DemoPostBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;

@Service
public class RepoService<T> {

    @Autowired
    MyControllerAdvice myControllerAdvice;

    public ArrayList<T> getDataArrays(String command) throws SQLException {
        ResultSet resultSet =  DBManager.getResultSet(command);
        ArrayList<T> technicalReviewList = new ArrayList<>();
        while (resultSet.next()) {

                TechnicalReview<T> technicalReview = new TechnicalReview<T>();

                technicalReview.setClient(resultSet.getString(1));
                technicalReview.setType(resultSet.getString(2));
                technicalReview.setWheight(resultSet.getString(3));
                technicalReview.setT_O(resultSet.getString(4));
                technicalReview.setP(resultSet.getString(5));
                technicalReview.setHI(resultSet.getString(6));
                technicalReview.setNumber(resultSet.getString(7));
                technicalReview.setAdditional_data(resultSet.getString(8));



                technicalReviewList.add((T) technicalReview);
        }
        return technicalReviewList;
    }
    public ArrayList<Object> getData(String command) throws SQLException {
        return DBManager.getListOfObjects(command);
    }

    public T getUser(String command) throws SQLException {
        ResultSet rs = DBManager.getResultSet(command);
        User user = new User();
        while (rs.next()) {
            user.setUsser(rs.getString(1));
            user.setPassword(rs.getString(2));
            user.setService_Order(rs.getString(3));
            user.setWorking_Book(rs.getString(4));
            user.setInvoice(rs.getString(5));
            user.setReports(rs.getString(6));
            user.setNew_Ext(rs.getString(7));
            user.setHidden_Menu(rs.getString(8));
            user.setAcquittance(rs.getString(9));
        }
        return (T) user;
    }


}
