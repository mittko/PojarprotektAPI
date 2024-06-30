package com.example.demo.services;

import com.example.demo.db.DBManager;
import com.example.demo.exceptions.MyControllerAdvice;
import com.example.demo.models.Firm;
import com.example.demo.models.TechnicalReview;
import com.example.demo.models.User;
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
    public T getData(String command) throws SQLException {
        ResultSet resultSet = DBManager.getResultSet(command);
        Firm<T> firm = new Firm<T>();
        while (resultSet.next()) {

                firm.setFirm(resultSet.getString(1));
                firm.setCity(resultSet.getString(2));
                firm.setAddress(resultSet.getString(3));
                firm.setEik(resultSet.getString(4));
                firm.setMol(resultSet.getString(5));
                firm.setEmail(resultSet.getString(6));
                firm.setPerson(resultSet.getString(7));
                firm.setTelPerson(resultSet.getString(8));
                firm.setBank(resultSet.getString(9));
                firm.setBic(resultSet.getString(10));
                firm.setIban(resultSet.getString(11));
                firm.setDiscount(resultSet.getString(12));
                firm.setIncorrect_person(resultSet.getString(13));
                firm.setVat_registration(resultSet.getString(14));

                break;
            }
        return (T) firm;
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
