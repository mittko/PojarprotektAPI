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
import java.util.HashMap;
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
                    ServiceOrderModel<T> serviceOrderReports = new ServiceOrderModel<T>();
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


    @GetMapping(path = "/diary")
    public @ResponseBody T getDiaryInfo(@RequestParam("fromDate") String fromDate,
                                                   @RequestParam("toDate") String toDate) throws SQLException {
        ArrayList<T> data = new ArrayList<>();
        String command = "select * from ProtokolTableDB5" +
                " where date between Date('"+fromDate+"') and Date('"
                +toDate+"') order by number";
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    ProtokolModel protokolModel = new ProtokolModel();
                    protokolModel.setClient(resultSet.getString(1));
                    protokolModel.setType(resultSet.getString(2));
                    protokolModel.setWheight(resultSet.getString(3));
                    protokolModel.setBarcod(resultSet.getString(4));
                    protokolModel.setSerial(resultSet.getString(5));
                    protokolModel.setCategory(resultSet.getString(6));
                    protokolModel.setBrand(resultSet.getString(7));
                    protokolModel.setT_O(resultSet.getString(8));
                    protokolModel.setP(resultSet.getString(9));
                    protokolModel.setHI(resultSet.getString(10));
                    protokolModel.setParts(resultSet.getString(11));
                    protokolModel.setValue(resultSet.getString(12));
                    protokolModel.setNumber(resultSet.getString(13));
                    protokolModel.setPerson(resultSet.getString(14));
                    protokolModel.setDate("01.06.2016");//(resultSet.getString(15));
                    protokolModel.setKontragent(resultSet.getString(16));
                    protokolModel.setInvoiceByKontragent(resultSet.getString(17));
                    protokolModel.setAdditional_data(resultSet.getString(18));
                    protokolModel.setUptodate(resultSet.getString(19));

                    data.add((T) protokolModel);
                }
            }
        });


        // get invoice numbers accordingly protokol numbers
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for(T t : data) {
           ProtokolModel model = (ProtokolModel) t;
            command = "select id, date from InvoiceParentDB5"
                    + " where protokol = '" + model.getNumber() + "'";
            repoService.getResult(command, new ResultSetCallback() {
                @Override
                public void result(ResultSet resultSet) throws SQLException {
                    while (resultSet.next()) {
                        hashMap.put(model.getNumber(),
                                resultSet.getString(1) + " /" + resultSet.getString(2) + "/");
                    }
                }
            });
        }

        // get client info
        HashMap<String,ClientInfo> clientModelHashMap = new HashMap<>();
        for(T t : data) {
            ProtokolModel model = (ProtokolModel) t;

            command = "select city, telPerson from FirmsTable"
                    + " where firm = '" + model.getClient() + "'";
            repoService.getResult(command, new ResultSetCallback() {
                @Override
                public void result(ResultSet resultSet) throws SQLException {
                    while (resultSet.next()) {
                        ClientInfo clientInfo = new ClientInfo();
                        clientInfo.setCity(resultSet.getString(1));
                        clientInfo.setTel(resultSet.getString(2));
                      clientModelHashMap.put(model.getClient(), clientInfo);
                    }
                }
            });
            command = "select tel from PersonsTable"
                    + " where name = '" + model.getClient() + "'";
            repoService.getResult(command, new ResultSetCallback() {
                @Override
                public void result(ResultSet resultSet) throws SQLException {
                    while (resultSet.next()) {
                        ClientInfo clientInfo = new ClientInfo();
                        clientInfo.setCity("");
                        clientInfo.setTel(resultSet.getString(1));
                        clientModelHashMap.put(model.getClient(), clientInfo);
                    }
                }
            });
        }
        DiaryModel<T> diaryModel = new DiaryModel<T>();
        diaryModel.setProtokolModels((ArrayList<ProtokolModel>) data);
        diaryModel.setInvoiceNumbers(hashMap);
        diaryModel.setClientModelHashMap(clientModelHashMap);
        return (T) diaryModel;
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
                "T_O, P, HI, parts, value, number,  person, date, kontragent, " +
                "invoiceByKontragent, additional_data, uptodate from ProtokolTableDB5";

        command += constructQueryWithPrams(client,number,type,wheight,category,brand,doing,
                serial,barcod, fromDate,toDate);
        repoService.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
               while (resultSet.next()) {
                   ProtokolModel protokolModel = new ProtokolModel();
                   protokolModel.setClient(resultSet.getString(1));
                   protokolModel.setType(resultSet.getString(2));
                   protokolModel.setWheight(resultSet.getString(3));
                   protokolModel.setBarcod(resultSet.getString(4));
                   protokolModel.setSerial(resultSet.getString(5));
                   protokolModel.setCategory(resultSet.getString(6));
                   protokolModel.setBrand(resultSet.getString(7));
                   protokolModel.setT_O(resultSet.getString(8));
                   protokolModel.setP(resultSet.getString(9));
                   protokolModel.setHI(resultSet.getString(10));
                   protokolModel.setParts(resultSet.getString(11));
                   protokolModel.setValue(resultSet.getString(12));
                   protokolModel.setNumber(resultSet.getString(13));
                   protokolModel.setPerson(resultSet.getString(14));
                   protokolModel.setDate("01.06.2016");//(resultSet.getString(15));
                   protokolModel.setKontragent(resultSet.getString(16));
                   protokolModel.setInvoiceByKontragent(resultSet.getString(17));
                   protokolModel.setAdditional_data(resultSet.getString(18));
                   protokolModel.setUptodate(resultSet.getString(19));

                   protokolReportList.add((T) protokolModel);
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

                    BrackModel brackModel = new BrackModel();
                    brackModel.setClient(resultSet.getString(1));
                    brackModel.setType(resultSet.getString(2));
                    brackModel.setWheight(resultSet.getString(3));
                    brackModel.setBrand(resultSet.getString(4));
                    brackModel.setCategory(resultSet.getString(5));
                    brackModel.setReasons(resultSet.getString(6));
                    brackModel.setBarcod(resultSet.getString(7));
                    brackModel.setSerial(resultSet.getString(8));
                    brackModel.setNumber(resultSet.getString(9));
                    brackModel.setTehnik(resultSet.getString(10));
                    brackModel.setDate(resultSet.getString(11));

                    brackList.add((T) brackModel);
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
                    AcquittanceModel acquittanceModel = new AcquittanceModel();

                    acquittanceModel.setId(resultSet.getString(1));
                    acquittanceModel.setValue(resultSet.getString(2));
                    acquittanceModel.setClient(resultSet.getString(3));
                    acquittanceModel.setSaller(resultSet.getString(4));
                    acquittanceModel.setDate(resultSet.getString(5));
                    acquittanceModel.set_id(resultSet.getString(6));
                    acquittanceModel.setArtikul(resultSet.getString(7));
                    acquittanceModel.setMed(resultSet.getString(8));
                    acquittanceModel.setQuantity(resultSet.getString(9));
                    acquittanceModel.setPrice(resultSet.getString(10));
                    acquittanceModel.set_value(resultSet.getString(11));
                    acquittanceModel.set_client(resultSet.getString(12));

                    acquittanceList.add((T) acquittanceModel);
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
                    InvoiceModel invoiceModel = new InvoiceModel();

                    invoiceModel.setId(resultSet.getString(1));
                    invoiceModel.setPayment(resultSet.getString(2));
                    invoiceModel.setDiscount(resultSet.getString(3));
                    invoiceModel.setValue(resultSet.getString(4));
                    invoiceModel.setClient(resultSet.getString(5));
                    invoiceModel.setSaller(resultSet.getString(6));
                    invoiceModel.setDate(resultSet.getString(7));
                    invoiceModel.setProtokol(resultSet.getString(8));

                    invoiceModel.set_id(resultSet.getString(9));
                    invoiceModel.setMake(resultSet.getString(10));
                    invoiceModel.setMed(resultSet.getString(11));
                    invoiceModel.setQuantity(resultSet.getString(12));
                    invoiceModel.setPrice(resultSet.getString(13));
                    invoiceModel.set_value(resultSet.getString(14));
                    invoiceModel.set_client(resultSet.getString(15));
                    invoiceModel.setKontragent(resultSet.getString(16));
                    invoiceModel.setInvoiceByKontragent(resultSet.getString(17));

                    invoices.add((T) invoiceModel);
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
                    NewExtinguishersModel extinguishersReports = new NewExtinguishersModel();
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
                    ClientModel clientReport = new ClientModel();
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
                    ClientModel clientModel = new ClientModel();
                    clientModel.setName(resultSet.getString(1));
                    clientModel.setIncorrectPerson(resultSet.getString(2));

                    clients.add((T) clientModel);
                }
            }
        });
       return clients;
    }



    @PostMapping(path = "/insert_credit_note")
    public @ResponseBody String createCreditNotes(@RequestBody CreditNoteBodyList bodyList) throws Exception {

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

                    CreditNoteModel creditNote = new CreditNoteModel();
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
