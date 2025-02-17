package com.example.demo.controllers.invoice;

import com.example.demo.callbacks.PreparedStatementCallback;
import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.controllers.working_book.ArtikulInfo;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.*;
import com.example.demo.services.RepoService;
import com.example.demo.utils.DateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class InvoiceController<T> {

    @Autowired
    RepoService<T> service;

    @PostMapping("/insert_acquittance/{artikulTable}")
    public String insertAcquittance(@RequestBody AcquittanceModels body,
                                    @PathVariable("artikulTable") String artikulTable) throws SQLException {
        AcquittanceModel parentModel = body.getParentModel();

        String command = "select id from AcquittanceParentDB";

        final String[] numberAsString = new String[1];
        final int[] maxAcquittanceNumber = new int[1];

        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    numberAsString[0] = resultSet.getString(1);

                    int number = 0;
                    try {
                        number = Integer.parseInt(numberAsString[0]);
                    } catch (Exception e) {

                    }

                    if(number > maxAcquittanceNumber[0]) {
                        maxAcquittanceNumber[0] = number;
                    }
                }
            }
        });
        String nextNumberAsString =  String.format("%010d",maxAcquittanceNumber[0]+1);

        command = "insert into AcquittanceParentDB  values ('"
                + nextNumberAsString + "','" + parentModel.getValue() + "','" +
                parentModel.getClient() + "','" + parentModel.getSaller() + "','"
                + parentModel.getDate() + "')";;
        service.execute(command);

        command = "insert into AcquittanceChildDB " +
                " values ( ?, ?, ?, ?, ?, ?, ?, ?)";

        for(AcquittanceModel model : body.getChildModels()) {
            service.execute(command, new PreparedStatementCallback<T>() {
                @Override
                public void callback(PreparedStatement ps) throws SQLException {
                    ps.setString(1, nextNumberAsString);
                    ps.setString(2, model.getArtikul());
                    ps.setString(3, model.getMed());
                    ps.setString(4, model.getQuantity());
                    ps.setString(5, model.getPrice());
                    ps.setString(6, model.getValue());
                    ps.setString(7, model.getClient());
                    ps.setString(8,model.getSklad());
                    ps.executeUpdate();
                }
            });
        }
        for(AcquittanceModel model : body.getChildModels()) {
            String invoiceByKotragent = model.getInvoiceByKontragent();
            if(invoiceByKotragent != null) {
                command = "select artikul, quantity, client, invoice, date, sklad from " + artikulTable
                        + " where ( (client = "
                        + "'"
                        + model.getKontragent()
                        + "')"
                        + " and (invoice = '"
                        + model.getInvoiceByKontragent()
                        + "')"
                        + " and (artikul = "
                        + "'"
                        + model.getArtikul()
                        + "')"
                        + " and (sklad = "
                        + "'"
                        + model.getSklad()
                        + "')"
                        + " and (quantity > 0) )";

                ArrayList<ArtikulInfo> artikulsInfo = new ArrayList<>();
                service.getResult(command, new ResultSetCallback() {
                    @Override
                    public void result(ResultSet rs) throws SQLException {
                        while (rs.next()) {
                            ArtikulInfo art = new ArtikulInfo(rs.getString(1),
                                    rs.getString(2), rs.getString(3), rs.getString(4),
                                    rs.getString(5),rs.getString(6));
                            artikulsInfo.add(art);
                        }
                    }
                });

                double quantityToDecease = 0;
                try {
                    quantityToDecease = Double.parseDouble(model.getQuantity());
                } catch (Exception e) {}

                for(ArtikulInfo artikulInfo : artikulsInfo) {
                    command = "update " + artikulTable
                            + " set quantity = (quantity - ?) where (artikul = ? and client" +
                            " = ? and invoice = ? and sklad = ?)";//  and (quantity > 0)

                    if(quantityToDecease > artikulInfo.getQuantity()) {
                        service.execute(command, new PreparedStatementCallback<T>() {
                            @Override
                            public void callback(PreparedStatement ps) throws SQLException {
                                ps.setString(1, artikulInfo.getQuantity() + "");
                                ps.setString(2, artikulInfo.getArtikulName());
                                ps.setString(3, artikulInfo.getKontragent());
                                ps.setString(4, artikulInfo.getInvoiceByKontragent());
                                ps.setString(5,artikulInfo.getSklad());
                                ps.executeUpdate();
                            }
                        });
                        quantityToDecease -= artikulInfo.getQuantity();

                    } else if(quantityToDecease <= artikulInfo.getQuantity()) {

                        int finalQuantityToDecease = (int)quantityToDecease;

                        service.execute(command, new PreparedStatementCallback<T>() {
                            @Override
                            public void callback(PreparedStatement ps) throws SQLException {
                                ps.setString(1, finalQuantityToDecease + "");
                                ps.setString(2, artikulInfo.getArtikulName());
                                ps.setString(3, artikulInfo.getKontragent());
                                ps.setString(4, artikulInfo.getInvoiceByKontragent());
                                ps.setString(5, artikulInfo.getSklad());
                                ps.executeUpdate();
                            }
                        });

                        quantityToDecease = 0;
                        break;

                    }
                }


            }
        }
        return nextNumberAsString;
    }

    @PostMapping("/insert_proform")
    public String insertProform(@RequestBody InvoiceModels<T> body) throws SQLException {
        InvoiceModel parentModel = body.getParentInvoiceModel();
        String command = "select id from ProformParentDB";
        //"select max(integer(id)) from ProformParentDB where length(id) = 10";
        final String[] numberAsString = new String[1];
        final int[] maxNumber = new int[1];
        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    numberAsString[0] = resultSet.getString(1);

                    int number = 0;
                    try {
                        number = Integer.parseInt(numberAsString[0]);
                    } catch (Exception e) {

                    }

                    if(number > maxNumber[0]) {
                        maxNumber[0] = number;
                    }
                }
            }
        });
       String nextProformNumber = String.format("%010d",maxNumber[0]+1);

        command = "insert into ProformParentDB values ('" + nextProformNumber
                + "','" + parentModel.getPayment() + "','" + parentModel.getDiscount() + "','" + parentModel.getValue() + "','"
                + parentModel.getClient() + "','" + parentModel.getSaller() + "','" +
                parentModel.getDate()+ "','" + parentModel.getProtokol()
                + "')";

        service.execute(command);

        command = "insert into ProformChildDB2"
                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";;

        for(InvoiceModel model : body.getChildInvoiceModels()) {

            service.execute(command, new PreparedStatementCallback<T>() {
                @Override
                public void callback(PreparedStatement ps) throws SQLException {
                    ps.setString(1, nextProformNumber);
                    ps.setString(2, model.getMake());
                    ps.setString(3, model.getMed());
                    ps.setString(4, model.getQuantity());
                    ps.setString(5, model.getPrice());
                    ps.setString(6, model.getValue());
                    ps.setString(7, model.getClient());
                    ps.setString(8, model.getKontragent());
                    ps.setString(9, model.getInvoiceByKontragent());
                    ps.setString(10,model.getSklad());
                    ps.executeUpdate();
                }
            });
        }
        return nextProformNumber;
    }

    @PostMapping(path = "/insert_invoice/{isFiscalBon}")
    public String insertInvoice(@RequestBody InvoiceModels<T> body,@PathVariable boolean isFiscalBon) throws SQLException {
        InvoiceModel parentModel = body.getParentInvoiceModel();
        String command = "select id from InvoiceParentDB5";

        final String[] numberAsString = new String[1];
        final int[] maxNumber = new int[1];
        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    numberAsString[0] = resultSet.getString(1);

                    int number = 0;
                    try {
                        number = Integer.parseInt(numberAsString[0]);
                    } catch (Exception e) {

                    }

                    if(number > maxNumber[0]) {
                        maxNumber[0] = number;
                    }
                }
            }
        });

        String nextInvoiceNumber  = String.format("%010d",maxNumber[0]+1);

        command = "insert into InvoiceParentDB5 values ('" +
                (isFiscalBon ? DateManager.generateFiscalBonNumber() : nextInvoiceNumber)
                + "','" + parentModel.getPayment() + "','" + parentModel.getDiscount() + "','" + parentModel.getValue() + "','"
                + parentModel.getClient() + "','" + parentModel.getSaller() + "','" +
                parentModel.getDate()+ "','" + parentModel.getProtokol()
                + "')";

        service.execute(command);

        command = "insert into InvoiceChildDB7"
                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";;

                for(InvoiceModel model : body.getChildInvoiceModels()) {

                    service.execute(command, new PreparedStatementCallback<T>() {
                        @Override
                        public void callback(PreparedStatement ps) throws SQLException {
                            ps.setString(1,  (isFiscalBon ? DateManager.generateFiscalBonNumber() : nextInvoiceNumber));
                            ps.setString(2, model.getMake());
                            ps.setString(3, model.getMed());
                            ps.setString(4, model.getQuantity());
                            ps.setString(5, model.getPrice());
                            ps.setString(6, model.getValue());
                            ps.setString(7, model.getClient());
                            ps.setString(8, model.getKontragent());
                            ps.setString(9, model.getInvoiceByKontragent());
                            ps.setString(10,model.getSklad());

                            ps.executeUpdate();
                        }
                    });
                }

                // DECREASE ARTIKULS QUANTITY
                for(InvoiceModel model : body.getChildInvoiceModels()) {
                      String invoiceByKotragent = model.getInvoiceByKontragent();
                      if(invoiceByKotragent != null) {
                          command = "select artikul, quantity, client, invoice, date, sklad from ArtikulsDB"
                                  + " where ( (client = "
                                  + "'"
                                  + model.getKontragent()
                                  + "')"
                                  + " and (invoice = '"
                                  + model.getInvoiceByKontragent()
                                  + "')"
                                  + " and (artikul = "
                                  + "'"
                                  + model.getMake()
                                  + "')"
                                  + " and (sklad = "
                                  + "'"
                                  + model.getSklad()
                                  + "')"
                                  + " and (quantity > 0) )";

                          ArrayList<ArtikulInfo> artikulsInfo = new ArrayList<>();
                        service.getResult(command, new ResultSetCallback() {
                            @Override
                            public void result(ResultSet rs) throws SQLException {
                                while (rs.next()) {
                                    ArtikulInfo art = new ArtikulInfo(rs.getString(1),
                                            rs.getString(2), rs.getString(3), rs.getString(4),
                                            rs.getString(5),rs.getString(6));
                                    artikulsInfo.add(art);
                                }
                            }
                        });

                        double quantityToDecease = 0;
                        try {
                            quantityToDecease = Double.parseDouble(model.getQuantity());
                        }catch (Exception e){}

                        for(ArtikulInfo artikulInfo : artikulsInfo) {
                            command = "update ArtikulsDB"
                                    + " set quantity = (quantity - ?) where (artikul = ? and client" +
                                    " = ? and invoice = ? and sklad = ?)";//  and (quantity > 0)

                            if(quantityToDecease > artikulInfo.getQuantity()) {
                               service.execute(command, new PreparedStatementCallback<T>() {
                                   @Override
                                   public void callback(PreparedStatement ps) throws SQLException {
                                       ps.setString(1, artikulInfo.getQuantity() + "");
                                       ps.setString(2, artikulInfo.getArtikulName());
                                       ps.setString(3, artikulInfo.getKontragent());
                                       ps.setString(4, artikulInfo.getInvoiceByKontragent());
                                       ps.setString(5, artikulInfo.getSklad());
                                       ps.executeUpdate();
                                   }
                               });
                                quantityToDecease -= artikulInfo.getQuantity();

                            } else if(quantityToDecease <= artikulInfo.getQuantity()) {

                                int finalQuantityToDecease = (int)quantityToDecease;
                                service.execute(command, new PreparedStatementCallback<T>() {
                                    @Override
                                    public void callback(PreparedStatement ps) throws SQLException {
                                        ps.setString(1, finalQuantityToDecease + "");
                                        ps.setString(2, artikulInfo.getArtikulName());
                                        ps.setString(3, artikulInfo.getKontragent());
                                        ps.setString(4, artikulInfo.getInvoiceByKontragent());
                                        ps.setString(5,artikulInfo.getSklad());
                                        ps.executeUpdate();
                                    }
                                });

                                quantityToDecease = 0;
                                break;

                            }
                        }
                      }
                }

                return nextInvoiceNumber;
    }



    @GetMapping("/invoice_info")
    public @ResponseBody T getInvoiceInfo(@RequestParam("id") String id) throws SQLException, NotFoundException {
        String command = "select * from ProformParentDB where id = '" + id + "'";

        InvoiceModels<T> models = new InvoiceModels<>();
        InvoiceModel parentInvoiceModel = new InvoiceModel();
        List<T> childInvoiceModels = new ArrayList<>();


        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {

                    parentInvoiceModel.setId(resultSet.getString(1));
                    parentInvoiceModel.setPayment(resultSet.getString(2));
                    parentInvoiceModel.setDiscount(resultSet.getString(3));
                    parentInvoiceModel.setValue(resultSet.getString(4));
                    parentInvoiceModel.setClient(resultSet.getString(5));
                    parentInvoiceModel.setSaller(resultSet.getString(6));
                    parentInvoiceModel.setDate(resultSet.getString(7));

                    break;
                }
            }
        });

        command ="select ProformChildDB2.make, ProformChildDB2.med, ProformChildDB2.quantity,"
                + " ProformChildDB2.price, ProformChildDB2.value, ProformParentDB.discount,"
                + " ProformChildDB2.kontragent, ProformChildDB2.invoiceByKontragent , ProformChildDB2.sklad" +
                " from ProformParentDB , ProformChildDB2 where ProformParentDB.id = '"
                + id + "' and ProformChildDB2.id = '" + id + "'";



        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                  InvoiceModel model = new InvoiceModel();
                  model.setMake(resultSet.getString(1));
                  model.setMed(resultSet.getString(2));
                  model.setQuantity(resultSet.getString(3));
                  model.setPrice(resultSet.getString(4));
                  model.setValue(resultSet.getString(5));
                  model.setDiscount(resultSet.getString(6));
                  model.setKontragent(resultSet.getString(7));
                  model.setInvoiceByKontragent(resultSet.getString(8));
                  model.setSklad(resultSet.getString(9));
                  childInvoiceModels.add((T) model);
                }
            }
        });

        if(childInvoiceModels.size() > 0) {
            String client = parentInvoiceModel.getClient();

            Firm<T> firm = new Firm<T>();
            firm.setFirm(" ? ");
            firm.setDiscount(0);
            firm.setVat_registration("не");

            service.getResult(String.format("select firm, discount, vat_registration from FirmsTable where firm = '%s'",client), new ResultSetCallback() {
                @Override
                public void result(ResultSet resultSet) throws SQLException {
                    while (resultSet.next()) {
                        firm.setFirm(resultSet.getString(1));
                        firm.setDiscount(Integer.parseInt(resultSet.getString(2)));
                        firm.setVat_registration(resultSet.getString(3));
                        break;
                    }
                }
            });

            if(firm.getFirm() == null) {
                service.getResult(String.format("select name, discount from PersonsTable where name = '%s'", client), new ResultSetCallback() {
                    @Override
                    public void result(ResultSet resultSet) throws SQLException {
                        while (resultSet.next()) {
                            firm.setFirm(resultSet.getString(1));
                            firm.setDiscount(Integer.parseInt(resultSet.getString(2)));
                        }
                    }
                });
            }
            models.setFirm(firm);
            models.setParentInvoiceModel( parentInvoiceModel);
            models.setChildInvoiceModels((List<InvoiceModel>) childInvoiceModels);
        } else {
            throw new NotFoundException("Не е намерена такава проформа");
        }

        return (T) models;
    }

}
