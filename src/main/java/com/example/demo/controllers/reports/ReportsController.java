package com.example.demo.controllers.reports;

import com.example.demo.callbacks.PreparedStatementCallback;
import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.exceptions.DublicateNumberException;
import com.example.demo.models.*;
import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

@RestController
public class ReportsController<T> {

    @Autowired
    RepoService<T> repoService;

    @GetMapping(path = "/service_orders")
    public @ResponseBody ArrayList<T> getServiceOrders(
    @RequestParam(value = "client", required = false) String client,
    @RequestParam(value = "number",required = false) String number,
    @RequestParam(value = "type", required = false) String type,
    @RequestParam(value = "wheight",required = false) String wheight,
    @RequestParam(value = "category",required = false) String category,
    @RequestParam(value = "brand",required = false) String brand,
    @RequestParam(value = "doing",required = false) String doing,
    @RequestParam(value = "serial",required = false) String serial,
    @RequestParam(value = "barcode",required = false) String barcode,
    @RequestParam(value = "fromDate",required = false) String fromDate,
    @RequestParam(value = "toDate",required = false) String toDate) throws SQLException {

        ArrayList<T> serviceOrders = new ArrayList<>();

        String command = "select client, type, wheight, barcod, serial, category, brand," +
                " T_O, P, HI, done, number, person, date , additional_data from ServiceTableDB";


        command += constructQueryWithPrams(client,number,type,wheight,category,brand,doing,
                serial,barcode,fromDate,toDate);

        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    ServiceOrderReports<T> serviceOrderReports = new ServiceOrderReports<T>();
                    serviceOrderReports.setClient(resultSet.getString(1));
                    serviceOrderReports.setType(resultSet.getString(2));
                    serviceOrderReports.setWheight(resultSet.getString(3));
                    serviceOrderReports.setBarcod(resultSet.getString(4));
                    serviceOrderReports.setSerial(resultSet.getString(5));
                    serviceOrderReports.setCategory(resultSet.getString(6));
                    serviceOrderReports.setBrand(resultSet.getString(7));
                    serviceOrderReports.setT_O(resultSet.getString(8));
                    serviceOrderReports.setP(resultSet.getString(9));
                    serviceOrderReports.setHI(resultSet.getString(10));
                    serviceOrderReports.setDone(resultSet.getString(11));
                    serviceOrderReports.setNumber(resultSet.getString(12));
                    serviceOrderReports.setPerson(resultSet.getString(13));
                    serviceOrderReports.setDate(resultSet.getString(14));
                    serviceOrderReports.setAdditional_data(resultSet.getString(15));
                    serviceOrders.add((T) serviceOrderReports);
                }
            }
        });
        return serviceOrders;
    }


    @GetMapping(path = "/protokols")
    public @ResponseBody ArrayList<T> getProtokols(
            @RequestParam(value = "client",required = false) String client,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "wheight", required = false) String wheight,
            @RequestParam(value = "barcod", required = false) String barcod,
            @RequestParam(value = "serial", required = false) String serial,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "doing", required = false) String doing,
            @RequestParam(value = "number", required = false) String number,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) throws SQLException {

        ArrayList<T> protokolReportList = new ArrayList<>();

        String command = "select client, type, wheight, barcod, serial, category, brand, " +
                "T_O, P, HI, parts, value, number, person, date, kontragent," +
                "invoiceByKontragent, additional_data, uptodate from ProtokolTableDB5";

        command += constructQueryWithPrams(client,number,type,wheight,category,brand,doing,
                serial,barcod, fromDate,toDate);
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
               while (resultSet.next()) {
                   ProtokolReports<T> protokolReports = new ProtokolReports<T>();
                   protokolReports.setClient(resultSet.getString(1));
                   protokolReports.setType(resultSet.getString(2));
                   protokolReports.setWheight(resultSet.getString(3));
                   protokolReports.setBarcod(resultSet.getString(4));
                   protokolReports.setSerial(resultSet.getString(5));
                   protokolReports.setCategory(resultSet.getString(6));
                   protokolReports.setBrand(resultSet.getString(7));
                   protokolReports.setT_O(resultSet.getString(8));
                   protokolReports.setP(resultSet.getString(9));
                   protokolReports.setHI(resultSet.getString(10));
                   protokolReports.setParts(resultSet.getString(11));
                   protokolReports.setValue(resultSet.getString(12));
                   protokolReports.setNumber(resultSet.getString(13));
                   protokolReports.setPerson(resultSet.getString(14));
                   protokolReports.setDate(resultSet.getString(15));
                   protokolReports.setKontragent(resultSet.getString(16));
                   protokolReports.setInvoiceByKontragent(resultSet.getString(17));
                   protokolReports.setAdditional_data(resultSet.getString(18));
                   protokolReports.setUptodate(resultSet.getString(19));

                   protokolReportList.add((T) protokolReports);
               }
            }
        });
        return protokolReportList;
    }

    @GetMapping(path = "/brack")
    public @ResponseBody ArrayList<T> getBrack(
            @RequestParam(value = "client", required = false) String client,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "wheight", required = false) String wheight,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "barcod", required = false) String barcod,
            @RequestParam(value = "serial", required = false) String serial,
            @RequestParam(value = "number", required = false) String number,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) throws SQLException {


        String command = "select client, type, wheight, brand, category, reasons, barcod, serial, number, tehnik, date from BrackTableDB2";

        command += constructQueryWithPrams(client,number,type,wheight,category,brand,null,
                serial,barcod, fromDate,toDate);

        ArrayList<T> brackList = new ArrayList<>();
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {

                    BrakReports brakReports = new BrakReports();
                    brakReports.setClient(resultSet.getString(1));
                    brakReports.setType(resultSet.getString(2));
                    brakReports.setWheight(resultSet.getString(3));
                    brakReports.setBrand(resultSet.getString(4));
                    brakReports.setCategory(resultSet.getString(5));
                    brakReports.setReasons(resultSet.getString(6));
                    brakReports.setBarcod(resultSet.getString(7));
                    brakReports.setSerial(resultSet.getString(8));
                    brakReports.setNumber(resultSet.getString(9));
                    brakReports.setTehnik(resultSet.getString(10));
                    brakReports.setDate(resultSet.getString(11));

                    brackList.add((T) brakReports);
                }
            }
        });
        return brackList;
    }

    @GetMapping(path = "/invoices")
    public @ResponseBody ArrayList<T> getInvoices(
            @RequestParam(value = "client", required = false) String client,
            @RequestParam(value = "invoice", required = false) String invoice,
            @RequestParam(value = "artikul", required = false) String artikul,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) throws SQLException {

        return getInvoiceData("InvoiceParentDB5","InvoiceChildDB7",client,invoice,artikul,
                fromDate,toDate);
    }

    @GetMapping(path = "/proforms")
    public @ResponseBody ArrayList<T> getProforms(
            @RequestParam(value = "client", required = false) String client,
            @RequestParam(value = "proform", required = false) String proform,
            @RequestParam(value = "artikul", required = false) String artikul,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) throws SQLException {

        return getInvoiceData("ProformParentDB","ProformChildDB2",client,proform,artikul,fromDate,toDate);
    }

    @GetMapping(path = "/acquittance")
    public @ResponseBody ArrayList<T> getAcquittance(
            @RequestParam(value = "client", required = false) String client,
            @RequestParam(value = "acquittance", required = false) String acquittance,
            @RequestParam(value = "artikul", required = false) String artikul,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) throws SQLException {

        ArrayList<T> acquittanceList = new ArrayList<>();

        String acquittanceParent = "AcquittanceParentDB";
        String acquittanceChild = "AcquittanceChildDB";
        String command =
                String.format("select * from %s , " +
                        "%s where %s.id = %s.id", acquittanceParent,acquittanceChild, acquittanceParent, acquittanceChild);

        command += constructQueryWithParamsForInvoice(acquittanceParent,acquittanceChild,client, acquittance,
                artikul, fromDate, toDate);

        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    AcquittanceReports acquittanceReports = new AcquittanceReports();

                    acquittanceReports.setId(resultSet.getString(1));
                    acquittanceReports.setValue(resultSet.getString(2));
                    acquittanceReports.setClient(resultSet.getString(3));
                    acquittanceReports.setSaller(resultSet.getString(4));
                    acquittanceReports.setDate(resultSet.getString(5));
                    acquittanceReports.set_id(resultSet.getString(6));
                    acquittanceReports.setArtikul(resultSet.getString(7));
                    acquittanceReports.setMed(resultSet.getString(8));
                    acquittanceReports.setQuantity(resultSet.getString(9));
                    acquittanceReports.setPrice(resultSet.getString(10));
                    acquittanceReports.set_value(resultSet.getString(11));
                    acquittanceReports.set_client(resultSet.getString(12));

                    acquittanceList.add((T) acquittanceReports);
                }
            }
        });
        return acquittanceList;
    }



    private ArrayList<T> getInvoiceData(String invoiceParent, String invoiceChild, String client,
                                        String invoice, String artikul, String fromDate, String toDate) throws SQLException {
        ArrayList<T> invoices = new ArrayList<>();

        String command =
                String.format("select * from %s , " +
                        "%s where %s.id = %s.id", invoiceParent,invoiceChild, invoiceParent, invoiceChild);

        command += constructQueryWithParamsForInvoice(invoiceParent,invoiceChild,client, invoice,
                artikul, fromDate, toDate);
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {

                while (resultSet.next()) {
                    InvoiceReports invoiceReports = new InvoiceReports();

                    invoiceReports.setId(resultSet.getString(1));
                    invoiceReports.setPayment(resultSet.getString(2));
                    invoiceReports.setDiscount(resultSet.getString(3));
                    invoiceReports.setValue(resultSet.getString(4));
                    invoiceReports.setClient(resultSet.getString(5));
                    invoiceReports.setSaller(resultSet.getString(6));
                    invoiceReports.setDate(resultSet.getString(7));
                    invoiceReports.setProtokol(resultSet.getString(8));

                    invoiceReports.set_id(resultSet.getString(9));
                    invoiceReports.setMake(resultSet.getString(10));
                    invoiceReports.setMed(resultSet.getString(11));
                    invoiceReports.setQuantity(resultSet.getString(12));
                    invoiceReports.setPrice(resultSet.getString(13));
                    invoiceReports.set_value(resultSet.getString(14));
                    invoiceReports.set_client(resultSet.getString(15));
                    invoiceReports.setKontragent(resultSet.getString(16));
                    invoiceReports.setInvoiceByKontragent(resultSet.getString(17));

                    invoices.add((T) invoiceReports);
                }
            }
        });
        return invoices;
    }
    @GetMapping(path = "/artikuls")
    public @ResponseBody ArrayList<T> getArtikuls() throws SQLException {
             ArrayList<T> artikuls = new ArrayList<>();
             String command = "select distinct artikul from ArtikulsDB order by artikul";
             repoService.getResult(command, new ResultSetCallback() {
                 @Override
                 public void result(ResultSet resultSet) throws SQLException {
                     while (resultSet.next()) {
                         ArtikulsReports artikulsReports = new ArtikulsReports();
                         artikulsReports.setArtikul(resultSet.getString(1));
                         artikuls.add((T) artikulsReports);
                     }
                 }
             });
             return artikuls;
    }
    @GetMapping(path = "/new_extinguishers")
    public @ResponseBody ArrayList<T> getNewExtinguishers() throws SQLException {
        ArrayList<T> newExtinguishers = new ArrayList<>();
        String command =  "select type from NewExtinguishersDB3 where quantitiy > 0";
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    NewExtinguishersReports extinguishersReports = new NewExtinguishersReports();
                    extinguishersReports.setType(resultSet.getString(1));
                    newExtinguishers.add((T) extinguishersReports);
                }
            }
        });
        return newExtinguishers;
    }

    @GetMapping(path = "/clients")
    public @ResponseBody ArrayList<T> getClients() throws SQLException {
        ArrayList<T> clients = new ArrayList<>();
        String command = "select firm, incorrectPerson from FirmsTable";
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    ClientReports clientReport = new ClientReports();
                    clientReport.setName(resultSet.getString(1));
                    clientReport.setIncorrectPerson(resultSet.getString(2));

                    clients.add((T) clientReport);
                }
            }
        });

        String command2 = "select name, incorrectPerson from PersonsTable";
        repoService.getResult(command2, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    ClientReports clientReports = new ClientReports();
                    clientReports.setName(resultSet.getString(1));
                    clientReports.setIncorrectPerson(resultSet.getString(2));

                    clients.add((T) clientReports);
                }
            }
        });
       return clients;
    }



    @PostMapping(path = "/insert_credit_note")
    public @ResponseBody String createCreditNotes(@RequestBody  BodyList bodyList) throws Exception {

        String invoice = bodyList.getList().get(0).getId();

        String nextCreditNoteId = "";
        HashSet<String> numberOfSet = new HashSet<>();
        String command = "select id from CreditNoteDB";
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    String numberAsString = resultSet.getString(1);
                    numberOfSet.add(numberAsString);
                }
            }
        });

        if(numberOfSet.contains(invoice)) {
            throw new DublicateNumberException("Вече има създадено кредитно известие за тази фактура");
        }

        for(CreditNoteBody body : bodyList.getList()) {
            command = "update ArtikulsDB"
                    + " set quantity = (quantity - ?) where (artikul = ? and client = ? and invoice  = ?)";//  and (quantity > 0)
            repoService.execute(command, new PreparedStatementCallback<T>() {
                @Override
                public void callback(PreparedStatement ps) throws SQLException {
                    ps.setString(1, String.valueOf(body.getQuantity()));
                    ps.setString(2, body.getArtikul());
                    ps.setString(3, body.getKontragent());
                    ps.setString(4, body.getInvoiceByKontragent());
                    ps.executeUpdate();
                }
            });

            command = "update InvoiceChildDB7 set quantity = ? " +
                    "where (artikul = ? and kontragent = ? and invoiceByKontragent  = ?) and (InvoiceChildDB7.id = ?)";
            repoService.execute(command, new PreparedStatementCallback<T>() {
                @Override
                public void callback(PreparedStatement ps) throws SQLException {
                    ps.setString(1, "0");
                    ps.setString(2, body.getArtikul());
                    ps.setString(3, body.getKontragent());
                    ps.setString(4, body.getInvoiceByKontragent());
                    ps.setString(5, body.getId());
                    ps.executeUpdate();
                }
            });

            nextCreditNoteId = String.valueOf(numberOfSet.size() + 1);

            command = "insert into CreditNoteDB values ('" + body.getId() + "','" + body.getPayment() + "','"
                    + body.getDiscount()
                    + "','" + body.getSum() + "','" + body.getClient() + "','" + body.getSaller() + "','" +
                    body.getDate() + "','" + body.getProtokol_id() +
                    "','" + body.getArtikul() + "','" + body.getMed() + "','" + body.getQuantity() + "','"
                    + body.getPrice() + "','" + body.getValue()
                    + "','" + body.getKontragent() + "','" + body.getInvoiceByKontragent() + "','" +
                    nextCreditNoteId + "','" + body.getCredit_note_date() + "')";

            repoService.execute(command);
        }

        return nextCreditNoteId;
    }


    @GetMapping(path = "/credit_notes")
    public @ResponseBody ArrayList<T> getCreditNotes(
            @RequestParam(value = "invoice",required = false) String invoice) throws SQLException {
        ArrayList<T> creditNotes = new ArrayList<>();
        String command = "select id ,payment ," +
                "discount , invoiceSum , client , saller, " +
                "date , protokol_id , artikul , med, " +
                "quantity , price , value , kontragent, " +
                "invoiceByKontragent, note_id, credit_note_date from CreditNoteDB";

        if(invoice != null) {
            command += String.format(" where id = '%s'",invoice);
        }
     //   command += " order by CAST(credit_note_date as DATE) desc";
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {

                    CreditNoteReports creditNote = new CreditNoteReports();
                    creditNote.setId(resultSet.getString(1));
                    creditNote.setPayment(resultSet.getString(2));
                    creditNote.setDiscount(resultSet.getString(3));
                    creditNote.setInvoiceSum(resultSet.getString(4));
                    creditNote.setClient(resultSet.getString(5));
                    creditNote.setSaller(resultSet.getString(6));
                    creditNote.setDate(resultSet.getString(7));
                    creditNote.setProtokol_id(resultSet.getString(8));
                    creditNote.setArtikul(resultSet.getString(9));
                    creditNote.setMed(resultSet.getString(10));
                    creditNote.setQuantity(resultSet.getString(11));
                    creditNote.setPrice(resultSet.getString(12));
                    creditNote.setValue(resultSet.getString(13));
                    creditNote.setKontragent(resultSet.getString(14));
                    creditNote.setInvoiceByKontragent(resultSet.getString(15));
                    creditNote.setNote_id(resultSet.getString(16));
                    creditNote.setCredit_note_date(resultSet.getString(17));

                    creditNotes.add((T) creditNote);
                }
            }
        });
        return creditNotes;
    }


    private String constructQueryWithParamsForInvoice(String invoiceParent, String invoiceChild,
                                                      String client, String invoice,
                                                      String artikul, String fromDate, String toDate) {
        String command = "";
        if(client != null) {
            command += String.format(" and %s.client = '%s'",invoiceParent, client);
        }
        if(invoice != null) {
            command += String.format(" and %s.id = '%s'",invoiceParent, invoice);
        }
        if(artikul != null) {
            command += String.format(" and %s.artikul = '%s'",invoiceChild, artikul);
        }
        if(fromDate != null && toDate != null) {
            command += String.format(" and %s.date between Date('%s') and Date('%s')", invoiceParent, fromDate, toDate);
        }
        command += " order by CAST(date as DATE) desc";
        return command;
    }

    private String constructQueryWithPrams(String client,
                                          String number,
                                         String type,
                                         String wheight,
                                         String category,
                                         String brand,
                                         String doing,
                                         String serial,
                                         String barcode,
                                         String fromDate,
                                         String toDate) {
        String command = "";
        int selectedCriterii = 0;
        if(client != null) {
            command += String.format(" where client = '%s'",client);// Димитър Александров
            selectedCriterii++;
        }
        if(number != null) {
            String placeholder = null;
            placeholder = (selectedCriterii == 0) ? "where" :  "and";
            command += String.format(" %s number = '%s'",placeholder,number); // 1000016357
            selectedCriterii++;
        }
        if(type != null) {
            String placeholder = null;
            placeholder = (selectedCriterii == 0) ? "where" :  "and";
            command += String.format(" %s type = '%s'",placeholder,type); // 1000016357
            selectedCriterii++;
        }
        if(wheight != null) {
            String placeholder = null;
            placeholder = (selectedCriterii == 0) ? "where" :  "and";
            command += String.format(" %s wheight = '%s'",placeholder,wheight); // 1000016357
            selectedCriterii++;
        }
        if(category != null) {
            String placeholder = null;
            placeholder = (selectedCriterii == 0) ? "where" :  "and";
            command += String.format(" %s category = '%s'",placeholder,category); // 1000016357
            selectedCriterii++;
        }
        if(brand != null) {
            String placeholder = null;
            placeholder = (selectedCriterii == 0) ? "where" :  "and";
            command += String.format(" %s brand = '%s'",placeholder,brand); // 1000016357
            selectedCriterii++;
        }
        if(doing != null) {
            String placeholder = null;
            placeholder = (selectedCriterii == 0) ? "where" :  "and";
            switch (doing) {
                case "ТО" ->
                        command += String.format(" %s %s",placeholder , "T_O != 'не' and P = 'не' and HI = 'не'");
                case "П" ->
                        command += String.format(" %s %s",placeholder,"P != 'не' and T_O = 'не' and HI = 'не'");
                case "ХИ" ->
                        command += String.format(" %s %s",placeholder , "HI != 'не' and T_O = 'не' and P = 'не'");
                case "ТО,П" ->
                        command += String.format(" %s %s",placeholder , "T_O != 'не' and P != 'не' and HI = 'не'");
                case "ТО,П,ХИ" ->
                        command += String.format(" %s %s",placeholder , " T_O != 'не' and P != 'не' and HI != 'не'");
            }
            selectedCriterii++;
        }
        if(serial != null) {
            String placeholder = null;
            placeholder = (selectedCriterii == 0) ? "where" :  "and";
            command += String.format(" %s serial = '%s'",placeholder,serial); // 52217
            selectedCriterii++;
        }
        if(barcode != null) {
            String placeholder = null;
            placeholder = (selectedCriterii == 0) ? "where" :  "and";
            command += String.format(" %s barcod = '%s'",placeholder,barcode); // 1000018166032
            selectedCriterii++;
        }

        if(fromDate != null & toDate != null) {
            String placeholder = null;
            placeholder = (selectedCriterii == 0) ? "where" :  "and";
            command += String.format(" %s date between Date('%s') and Date('%s')",placeholder,fromDate,toDate); // 1000018166032
        }

        command += " order by CAST(date as DATE) desc";
        return command;
    }






}
