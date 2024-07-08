package com.example.demo.controllers.init;

import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.models.TechnicalReview;
import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


@RestController
public class GetTechnicalReview<T> {
    @Autowired
    public RepoService<T> repoService;

    @GetMapping(path="/tech_review")
    public @ResponseBody ArrayList<T> getTechnicalReview(@RequestParam("from") String from,
                                                                       @RequestParam("to") String to) throws SQLException {
        ArrayList<T> technicalReviewList = new ArrayList<>();
        repoService.getResult(String.format("select client, type, wheight, T_O, P, HI, number, additional_data from ProtokolTableDB5 "
                        + " where (T_O <> 'не' and T_O between Date('%s') and Date('%s') or P <> 'не' and P between Date('%s')" +
                        " and Date('%s') or  HI <> 'не' and HI between Date('%s') and Date('%s') ) and (uptodate is null)",
                from, to, from, to, from, to), new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
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
            }
        });
        return technicalReviewList;
    }
}
