package com.example.demo.controllers.reports.availabality;

import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.models.DeliveryReports;
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
public class ArtikulAvailabilityReports<T> {

    @Autowired
    RepoService<T> repoService;

    @GetMapping(path = "/deliveries")
    public @ResponseBody ArrayList<T> getDeliveries() throws SQLException {
        ArrayList<T> deliveries = new ArrayList<>();
        String command = "select artikul, quantity, med, value, kontragent, invoiceByKontragent, date, operator from DeliveryArtikulsDB2";

        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    DeliveryReports deliveryReports = new DeliveryReports();

                    deliveryReports.setArtikul(resultSet.getString(1));
                    deliveryReports.setQuantity(resultSet.getString(2));
                    deliveryReports.setMed(resultSet.getString(3));
                    deliveryReports.setValue(resultSet.getString(4));
                    deliveryReports.setKontragent(resultSet.getString(5));
                    deliveryReports.setInvoiceByKontragent(resultSet.getString(6));
                    deliveryReports.setDate(resultSet.getString(7));
                    deliveryReports.setOperator(resultSet.getString(8));

                    deliveries.add((T) deliveryReports);
                }
            }
        });
        return deliveries;
    }

    @GetMapping(path = "/delivery_for_period")
    public @ResponseBody ArrayList<T> getDeliveriesForReports(
            @RequestParam(value = "artikul") String artikul,
            @RequestParam(value = "fromDate") String fromDate,
            @RequestParam(value = "toDate") String toDate) throws SQLException {
        ArrayList<T> deliveries = new ArrayList<>();
        String command = String.format("select DeliveryArtikulsDB2.artikul, DeliveryArtikulsDB2.quantity, " +
                "DeliveryArtikulsDB2.value, DeliveryArtikulsDB2.date from " +
                "DeliveryArtikulsDB2 where DeliveryArtikulsDB2.date between Date('%s') and Date('%s')", fromDate, toDate);

        command += String.format(" and DeliveryArtikulsDB2.artikul = '%s'", artikul);
        command += " order by CAST(date as DATE) desc";
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
               while (resultSet.next()) {
                   DeliveryReports deliveryReports = new DeliveryReports();
                   deliveryReports.setArtikul(resultSet.getString(1));
                   deliveryReports.setQuantity(resultSet.getString(2));
                   deliveryReports.setValue(resultSet.getString(3));
                   deliveryReports.setDate(resultSet.getString(4));

                   deliveries.add((T) deliveryReports);
               }
            }
        });
        return deliveries;
    }
}
