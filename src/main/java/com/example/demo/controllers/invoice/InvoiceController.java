package com.example.demo.controllers.invoice;

import com.example.demo.callbacks.PreparedStatementCallback;
import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.controllers.working_book.ArtikulInfo;
import com.example.demo.models.AcquittanceModel;
import com.example.demo.models.AcquittanceModels;
import com.example.demo.models.InvoiceModel;
import com.example.demo.models.InvoiceModels;
import com.example.demo.services.RepoService;
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

        String command = "select max(integer(id)) from AcquittanceParentDB where length(id) = 10";

        final int[] maxAcquittanceNumber = new int[1];
        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    maxAcquittanceNumber[0] = resultSet.getInt(1);
                    break;
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
                " values ( ?, ?, ?, ?, ?, ?, ?)";

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
                    ps.executeUpdate();
                }
            });
        }
        for(AcquittanceModel model : body.getChildModels()) {
            String invoiceByKotragent = model.getInvoiceByKontragent();
            if(invoiceByKotragent != null) {
                command = "select artikul, quantity, client, invoice, date from " + artikulTable
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
                        + " and (quantity > 0) )";

                ArrayList<ArtikulInfo> artikulsInfo = new ArrayList<>();
                service.getResult(command, new ResultSetCallback() {
                    @Override
                    public void result(ResultSet rs) throws SQLException {
                        while (rs.next()) {
                            ArtikulInfo art = new ArtikulInfo(rs.getString(1),
                                    rs.getString(2), rs.getString(3), rs.getString(4),
                                    rs.getString(5));
                            artikulsInfo.add(art);
                        }
                    }
                });

                int quantityToDecease = Integer.parseInt(model.getQuantity());

                for(ArtikulInfo artikulInfo : artikulsInfo) {
                    command = "update " + artikulTable
                            + " set quantity = (quantity - ?) where (artikul = ? and client" +
                            " = ? and invoice = ?)";//  and (quantity > 0)

                    if(quantityToDecease > artikulInfo.getQuantity()) {
                        service.execute(command, new PreparedStatementCallback<T>() {
                            @Override
                            public void callback(PreparedStatement ps) throws SQLException {
                                ps.setString(1, artikulInfo.getQuantity() + "");
                                ps.setString(2, artikulInfo.getArtikulName());
                                ps.setString(3, artikulInfo.getKontragent());
                                ps.setString(4, artikulInfo.getInvoiceByKontragent());
                                ps.executeUpdate();
                            }
                        });
                        quantityToDecease -= artikulInfo.getQuantity();

                    } else if(quantityToDecease <= artikulInfo.getQuantity()) {

                        int finalQuantityToDecease = quantityToDecease;
                        service.execute(command, new PreparedStatementCallback<T>() {
                            @Override
                            public void callback(PreparedStatement ps) throws SQLException {
                                ps.setString(1, finalQuantityToDecease + "");
                                ps.setString(2, artikulInfo.getArtikulName());
                                ps.setString(3, artikulInfo.getKontragent());
                                ps.setString(4, artikulInfo.getInvoiceByKontragent());
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
        String command = "select max(integer(id)) from ProformParentDB where length(id) = 10";
        final int[] maxNumber = new int[1];
        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    maxNumber[0] = resultSet.getInt(1);
                    break;
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
                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";;

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
                    ps.executeUpdate();
                }
            });
        }
        return nextProformNumber;
    }

    @PostMapping(path = "/insert_invoice")
    public String insertInvoice(@RequestBody InvoiceModels<T> body) throws SQLException {
        InvoiceModel parentModel = body.getParentInvoiceModel();
        String command = "select max(integer(id)) from InvoiceParentDB5 where length(id) = 10";
        final int[] maxNumber = new int[1];
        service.getResult(command, new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    maxNumber[0] = resultSet.getInt(1);
                    break;
                }
            }
        });
        String nextInvoiceNumber  = String.format("%010d",maxNumber[0]+1);

        command = "insert into InvoiceParentDB5 values ('" + nextInvoiceNumber
                + "','" + parentModel.getPayment() + "','" + parentModel.getDiscount() + "','" + parentModel.getValue() + "','"
                + parentModel.getClient() + "','" + parentModel.getSaller() + "','" +
                parentModel.getDate()+ "','" + parentModel.getProtokol()
                + "')";

        service.execute(command);

        command = "insert into InvoiceChildDB7"
                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";;

                for(InvoiceModel model : body.getChildInvoiceModels()) {

                    service.execute(command, new PreparedStatementCallback<T>() {
                        @Override
                        public void callback(PreparedStatement ps) throws SQLException {
                            ps.setString(1, nextInvoiceNumber);
                            ps.setString(2, model.getMake());
                            ps.setString(3, model.getMed());
                            ps.setString(4, model.getQuantity());
                            ps.setString(5, model.getPrice());
                            ps.setString(6, model.getValue());
                            ps.setString(7, model.getClient());
                            ps.setString(8, model.getKontragent());
                            ps.setString(9, model.getInvoiceByKontragent());
                            ps.executeUpdate();
                        }
                    });
                }

                for(InvoiceModel model : body.getChildInvoiceModels()) {
                      String invoiceByKotragent = model.getInvoiceByKontragent();
                      if(invoiceByKotragent != null) {
                          command = "select artikul, quantity, client, invoice, date from ArtikulsDB"
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
                                  + " and (quantity > 0) )";

                          ArrayList<ArtikulInfo> artikulsInfo = new ArrayList<>();
                        service.getResult(command, new ResultSetCallback() {
                            @Override
                            public void result(ResultSet rs) throws SQLException {
                                while (rs.next()) {
                                    ArtikulInfo art = new ArtikulInfo(rs.getString(1),
                                            rs.getString(2), rs.getString(3), rs.getString(4),
                                            rs.getString(5));
                                    artikulsInfo.add(art);
                                }
                            }
                        });

                        int quantityToDecease = Integer.parseInt(model.getQuantity());

                        for(ArtikulInfo artikulInfo : artikulsInfo) {
                            command = "update ArtikulsDB"
                                    + " set quantity = (quantity - ?) where (artikul = ? and client" +
                                    " = ? and invoice = ?)";//  and (quantity > 0)

                            if(quantityToDecease > artikulInfo.getQuantity()) {
                               service.execute(command, new PreparedStatementCallback<T>() {
                                   @Override
                                   public void callback(PreparedStatement ps) throws SQLException {
                                       ps.setString(1, artikulInfo.getQuantity() + "");
                                       ps.setString(2, artikulInfo.getArtikulName());
                                       ps.setString(3, artikulInfo.getKontragent());
                                       ps.setString(4, artikulInfo.getInvoiceByKontragent());
                                       ps.executeUpdate();
                                   }
                               });
                                quantityToDecease -= artikulInfo.getQuantity();

                            } else if(quantityToDecease <= artikulInfo.getQuantity()) {

                                int finalQuantityToDecease = quantityToDecease;
                                service.execute(command, new PreparedStatementCallback<T>() {
                                    @Override
                                    public void callback(PreparedStatement ps) throws SQLException {
                                        ps.setString(1, finalQuantityToDecease + "");
                                        ps.setString(2, artikulInfo.getArtikulName());
                                        ps.setString(3, artikulInfo.getKontragent());
                                        ps.setString(4, artikulInfo.getInvoiceByKontragent());
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
    public @ResponseBody T getInvoiceInfo(@RequestParam("id") String id) throws SQLException {
        String command = "select * from ProformParentDB where id = '" + id + "'";

        InvoiceModel parentInvoiceModel = new InvoiceModel();

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
                + " ProformChildDB2.kontragent, ProformChildDB2.invoiceByKontragent " +
                "from ProformParentDB , ProformChildDB2 where ProformParentDB.id = '"
                + id + "' and ProformChildDB2.id = '" + id + "'";

        List<T> childInvoiceModels = new ArrayList<>();

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

                  childInvoiceModels.add((T) model);
                }
            }
        });

        InvoiceModels<T> models = new InvoiceModels<>();
        models.setParentInvoiceModel( parentInvoiceModel);
        models.setChildInvoiceModels((List<InvoiceModel>) childInvoiceModels);
        return (T) models;
    }

}
