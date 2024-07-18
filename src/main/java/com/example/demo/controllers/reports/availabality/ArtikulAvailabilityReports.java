package com.example.demo.controllers.reports.availabality;

import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.models.DeliveryReports;
import com.example.demo.models.InvoiceForPeriodReports;
import com.example.demo.models.SaleReport;
import com.example.demo.models.SaleReport2;
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

    @GetMapping(path = "/invoice_for_period")
    public @ResponseBody ArrayList<T> getInvoiceForPeriod(
            @RequestParam(value = "fromDate") String fromDate,
            @RequestParam(value = "toDate") String toDate,
            @RequestParam(value = "artikul") String artikul) throws SQLException {
        ArrayList<T> invoices = new ArrayList<>();
        String command = String.format("select InvoiceChildDB7.artikul, InvoiceChildDB7.quantity," +
                " InvoiceChildDB7.price, InvoiceParentDB5.date from InvoiceChildDB7 , " +
                "InvoiceParentDB5 where InvoiceParentDB5.id = InvoiceChildDB7.id " +
                "and InvoiceParentDB5.date between Date('%s') and Date('%s')", fromDate, toDate);
        command += String.format(" and  InvoiceChildDB7.artikul = '%s'", artikul);
        command += " order by CAST(date as DATE) desc";
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
               while (resultSet.next()) {
                   InvoiceForPeriodReports invoice = new InvoiceForPeriodReports();
                   invoice.setArtikul(resultSet.getString(1));
                   invoice.setQuantity(resultSet.getString(2));
                   invoice.setPrice(resultSet.getString(3));
                   invoice.setDate(resultSet.getString(4));

                   invoices.add((T) invoice);
               }
            }
        });
        return invoices;
    }

    @GetMapping(path = "/sales")
    public @ResponseBody ArrayList<T> getSales(
            @RequestParam(value = "fromDate") String fromDate,
            @RequestParam(value = "toDate") String toDate,
            @RequestParam(value = "artikul") String artikul) throws SQLException {
        ArrayList<T> sales = new ArrayList<>();
        String command = String.format("select InvoiceChildDB7.id, InvoiceChildDB7.client, InvoiceChildDB7.invoiceByKontragent," +
                " InvoiceChildDB7.kontragent, InvoiceChildDB7.artikul, InvoiceChildDB7.med, InvoiceChildDB7.quantity," +
                " InvoiceChildDB7.price, InvoiceParentDB5.date from InvoiceChildDB7 ," +
                " InvoiceParentDB5  where InvoiceParentDB5.id = InvoiceChildDB7.id" +
                " and InvoiceParentDB5.date between Date('%s') and Date('%s')",fromDate,toDate);

        command += String.format(" and  InvoiceChildDB7.artikul = " + "'%s'",artikul);
        command += " order by CAST(date as DATE) desc";
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    SaleReport saleReport = new SaleReport();

                    saleReport.setId(resultSet.getString(1));
                    saleReport.setClient(resultSet.getString(2));
                    saleReport.setInvoiceByKontragent(resultSet.getString(3));
                    saleReport.setKontragent(resultSet.getString(4));
                    saleReport.setArtikul(resultSet.getString(5));
                    saleReport.setMed(resultSet.getString(6));
                    saleReport.setQuantity(resultSet.getString(7));
                    saleReport.setPrice(resultSet.getString(8));
                    saleReport.setDate(resultSet.getString(9));

                    sales.add((T) saleReport);
                }
            }
        });
        return sales;
    }

    @GetMapping(path = "/sales2")
    public @ResponseBody ArrayList<T> getSales2(
      @RequestParam(value = "fromDate") String fromDate,
      @RequestParam(value = "toDate") String toDate,
      @RequestParam(value = "artikul") String artikul) throws SQLException {
        ArrayList<T> sales = new ArrayList<>();
        String command = String.format("select DeliveryArtikulsDB2.invoiceByKontragent, DeliveryArtikulsDB2.kontragent," +
                " DeliveryArtikulsDB2.date, DeliveryArtikulsDB2.artikul, DeliveryArtikulsDB2.value" +
                " from DeliveryArtikulsDB2 where DeliveryArtikulsDB2.date between " + "Date('%s') and Date('%s')", fromDate, toDate);
        command += String.format(" and DeliveryArtikulsDB2.artikul = '%s'",artikul);
        command += " order by CAST(date as DATE) desc";
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                  while (resultSet.next()) {

                      SaleReport2 sale = new SaleReport2();
                      sale.setInvoiceByKontragent(resultSet.getString(1));
                      sale.setKontragent(resultSet.getString(2));
                      sale.setDate(resultSet.getString(3));
                      sale.setArtikul(resultSet.getString(4));
                      sale.setValue(resultSet.getString(5));

                      sales.add((T) sale);
                  }
            }
        });
        return sales;
    }
}
