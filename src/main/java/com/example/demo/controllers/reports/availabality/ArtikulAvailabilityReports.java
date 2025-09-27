package com.example.demo.controllers.reports.availabality;

import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.models.DeliveryModel;
import com.example.demo.models.InvoiceModel;
import com.example.demo.services.RepoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class ArtikulAvailabilityReports<T> {

    @Autowired
    RepoService<T> repoService;

    @GetMapping(path = "/deliveries")
    public @ResponseBody ArrayList<T> getDeliveries(
            @RequestParam(value = "kontragent",required = false) String kontragent,
            @RequestParam(value = "invoiceByKontragent",required = false) String invoiceByKontragent,
            @RequestParam(value = "artikul",required = false) String artikul,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) throws SQLException {

        String command = "select artikul, quantity, med, value, kontragent, invoiceByKontragent, date, operator from DeliveryArtikulsDB2";
        int selectedCriterii = 0;
        String placeholder = "";
        if(kontragent != null) {
            placeholder = "where";
            command += String.format(" %s kontragent = '%s'", placeholder,kontragent);
            selectedCriterii++;
        }
        if(invoiceByKontragent != null) {
            placeholder = (selectedCriterii == 0) ? "where" : "and";
            command += String.format(" %s invoiceByKontragent = '%s'",placeholder,invoiceByKontragent);
            selectedCriterii++;
        }
        if(artikul != null) {
            placeholder = (selectedCriterii == 0) ? "where" : "and";
            command += String.format(" %s artikul = '%s'",placeholder,artikul);
            selectedCriterii++;
        }
        if(fromDate != null && toDate != null) {
            placeholder = (selectedCriterii == 0) ? "where" : "and";
            command += String.format(" %s date between Date('%s') and Date('%s')", placeholder,fromDate,toDate);
        }

        ArrayList<T> deliveries = new ArrayList<>();

        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    DeliveryModel deliveryModel = new DeliveryModel();

                    deliveryModel.setArtikul(resultSet.getString(1));
                    deliveryModel.setQuantity(resultSet.getString(2));
                    deliveryModel.setMed(resultSet.getString(3));
                    deliveryModel.setValue(resultSet.getString(4));
                    deliveryModel.setKontragent(resultSet.getString(5));
                    deliveryModel.setInvoiceByKontragent(resultSet.getString(6));
                    deliveryModel.setDate(resultSet.getString(7));
                    deliveryModel.setOperator(resultSet.getString(8));

                    deliveries.add((T) deliveryModel);
                }
            }
        });
        return deliveries;
    }



    @GetMapping(path = "/invoice_data_for_sales")
    public @ResponseBody ArrayList<T> getInvoiceDataForSale(
            @RequestParam(value = "fromDate") String fromDate,
            @RequestParam(value = "toDate") String toDate,
            @RequestParam(value = "artikul", required = false) String artikul) throws SQLException {

        HashMap<String,String> clientMap = new HashMap<>();

        String getEikCommand = "select firm, eik from FirmsTable";
        repoService.getResult(getEikCommand, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    String client = resultSet.getString(1);
                    String eik = resultSet.getString(2);
                    clientMap.put(client,eik != null ? eik : "");
                }
            }
        });
        ArrayList<T> sales = new ArrayList<>();
        String command = String.format("select InvoiceChildDB7.id, InvoiceChildDB7.client," +
                " InvoiceChildDB7.invoiceByKontragent," +
                " InvoiceChildDB7.kontragent, InvoiceChildDB7.artikul, InvoiceChildDB7.value," +
                "  InvoiceChildDB7.quantity, InvoiceChildDB7.med," +
                " InvoiceChildDB7.price, InvoiceChildDB7.sklad, InvoiceParentDB5.payment," +
                " InvoiceParentDB5.date from InvoiceChildDB7 ," +
                " InvoiceParentDB5  where InvoiceParentDB5.id = InvoiceChildDB7.id" +
                " and InvoiceParentDB5.date between Date('%s') and Date('%s')",fromDate,toDate);

        if(artikul != null) {
            command += String.format(" and  InvoiceChildDB7.artikul = " + "'%s'", artikul);
        }
        command += " order by CAST(date as DATE) desc";
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    InvoiceModel invoiceDataForSalesReports = new InvoiceModel();

                    String clientName = resultSet.getString(2);
                    invoiceDataForSalesReports.setId(resultSet.getString(1));
                    invoiceDataForSalesReports.setClient(clientName);
                    invoiceDataForSalesReports.setInvoiceByKontragent(resultSet.getString(3));
                    invoiceDataForSalesReports.setKontragent(resultSet.getString(4));
                    invoiceDataForSalesReports.setArtikul(resultSet.getString(5));
                    invoiceDataForSalesReports.setValue(resultSet.getString(6));
                    invoiceDataForSalesReports.setQuantity(resultSet.getString(7));
                    invoiceDataForSalesReports.setMed(resultSet.getString(8));
                    invoiceDataForSalesReports.setPrice(resultSet.getString(9));
                    invoiceDataForSalesReports.setSklad(resultSet.getString(10));
                    invoiceDataForSalesReports.setPayment(resultSet.getString(11));
                    invoiceDataForSalesReports.setDate(resultSet.getString(12));
                    invoiceDataForSalesReports.setEik(clientMap.get(clientName));

                    sales.add((T) invoiceDataForSalesReports);
                }
            }
        });
        return sales;
    }

    @GetMapping(path = "/delivery_data_for_sales")
    public @ResponseBody ArrayList<T> getDeliveryDataForSale(
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "artikul",required = false) String artikul, HttpServletResponse response) throws SQLException {
        ArrayList<T> sales = new ArrayList<>();
        String command = "select DeliveryArtikulsDB2.invoiceByKontragent, DeliveryArtikulsDB2.kontragent," +
                " DeliveryArtikulsDB2.date, DeliveryArtikulsDB2.artikul, DeliveryArtikulsDB2.value" +
                " from DeliveryArtikulsDB2";// where DeliveryArtikulsDB2.date between " + "Date('%s') and Date('%s')", fromDate, toDate);

        if(artikul != null) {
            command += String.format(" where DeliveryArtikulsDB2.artikul = '%s'", artikul);
        }
        command += " order by CAST(date as DATE) desc";
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                  while (resultSet.next()) {

                      DeliveryModel sale = new DeliveryModel();
                      sale.setInvoiceByKontragent(resultSet.getString(1));
                      sale.setKontragent(resultSet.getString(2));
                      sale.setDate(resultSet.getString(3));
                      sale.setArtikul(resultSet.getString(4));
                      sale.setValue(resultSet.getString(5));

                      sales.add((T) sale);
                  }
            }
        });

        response.addHeader("my-custom-header","tra la la");

        return sales;
    }

    @GetMapping(path = "/delivery_data_for_availability")
    public @ResponseBody ArrayList<T> getDeliveriesForReports(
            @RequestParam(value = "artikul",required = false) String artikul,
            @RequestParam(value = "fromDate") String fromDate,
            @RequestParam(value = "toDate") String toDate) throws SQLException {
        ArrayList<T> deliveries = new ArrayList<>();
        String command = String.format("select DeliveryArtikulsDB2.artikul, DeliveryArtikulsDB2.quantity, " +
                "DeliveryArtikulsDB2.value, DeliveryArtikulsDB2.date from " +
                "DeliveryArtikulsDB2 where DeliveryArtikulsDB2.date between Date('%s') and Date('%s')", fromDate, toDate);

        if(artikul != null) {
            command += String.format(" and DeliveryArtikulsDB2.artikul = '%s'", artikul);
        }
        command += " order by CAST(date as DATE) desc";
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    DeliveryModel deliveryModel = new DeliveryModel();
                    deliveryModel.setArtikul(resultSet.getString(1));
                    deliveryModel.setQuantity(resultSet.getString(2));
                    deliveryModel.setValue(resultSet.getString(3));
                    deliveryModel.setDate(resultSet.getString(4));

                    deliveries.add((T) deliveryModel);
                }
            }
        });
        return deliveries;
    }

    @GetMapping(path = "/invoice_data_for_availability")
    public @ResponseBody ArrayList<T> getInvoiceForPeriod(
            @RequestParam(value = "fromDate") String fromDate,
            @RequestParam(value = "toDate") String toDate,
            @RequestParam(value = "artikul", required = false) String artikul) throws SQLException {
        ArrayList<T> invoices = new ArrayList<>();
        String command = String.format("select InvoiceChildDB7.artikul, InvoiceChildDB7.quantity," +
                " InvoiceChildDB7.price, InvoiceParentDB5.date from InvoiceChildDB7 , " +
                "InvoiceParentDB5 where InvoiceParentDB5.id = InvoiceChildDB7.id " +
                "and InvoiceParentDB5.date between Date('%s') and Date('%s')", fromDate, toDate);

        if(artikul != null) {
            command += String.format(" and  InvoiceChildDB7.artikul = '%s'", artikul);
        }
        command += " order by CAST(date as DATE) desc";
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    InvoiceModel invoice = new InvoiceModel();
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
}
