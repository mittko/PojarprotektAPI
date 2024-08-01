package com.example.demo.controllers.client;

import com.example.demo.callbacks.PreparedStatementCallback;
import com.example.demo.callbacks.ResultSetCallback;
import com.example.demo.models.Firm;
import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class GetClient<T> {

    @Autowired
    public RepoService<T> service;

    @PostMapping(path = "/insert_client")
    public int insertClient(@RequestBody Firm body) throws SQLException {
         String command = "insert into PersonsTable values (" + "'" + body.getFirm() + "'"
                 + "," + "'" + body.getTelPerson()+ "'" + "," + "'" + body.getDiscount() + "'" + ","
                 + "'" + body.getIncorrect_person() + "')";
        return service.execute(command);
    }

    @PostMapping(path = "/insert_firm")
    public int insertFirm(@RequestBody Firm<T> firm) throws SQLException {
        String command = "insert into FirmsTable values (" + "'" + firm.getFirm() + "','"
                + firm.getCity() + "','" + firm.getAddress() + "','" + firm.getEik() + "','" + firm.getMol()
                + "','" + firm.getEmail() + "','" + firm.getPerson() + "','" + firm.getTelPerson()
                + "','" + firm.getBank() + "','" + firm.getBic() + "','" + firm.getIban() + "','"
                + firm.getDiscount() + "','" + firm.getIncorrect_person() + "','" + firm.getVat_registration() + "')";
       return service.execute(command);
    }

    @PutMapping(path = "/edit_client/{old_name}")
    public int editClient(@RequestBody Firm<T> firm, @PathVariable("old_name") String oldName) throws SQLException {
        String command = "update PersonsTable set name = '" + firm.getFirm()
                + "',tel = '" + firm.getTelPerson() + "',discount = '" + firm.getDiscount()
                + "',incorrectPerson = '" + firm.getIncorrect_person()
                + "' where name = '" + oldName + "'";

        int edit = service.execute(command);

        updateClientInAnotherTables(firm.getFirm(),oldName);

        return edit;
    }


    @PutMapping(path = "/edit_firm/{old_name}")
    public int editFirm(@RequestBody Firm<T> firm, @PathVariable("old_name") String oldName) throws SQLException {
        String command = "update FirmsTable set firm = '" + firm.getFirm()
                + "' ,city = '" + firm.getCity() + "',address = '" + firm.getAddress()
                + "',eik = '" + firm.getEik() + "',mol = '" + firm.getMol() + "',email = '"
                + firm.getEmail() + "',person = '" + firm.getPerson() + "',telPerson = '"
                + firm.getTelPerson() + "',bank = '" + firm.getBank() + "',bic = '" + firm.getBic()
                + "',iban = '" + firm.getIban() + "',discount = '" + firm.getDiscount()
                + "',incorrectPerson = '" + firm.getIncorrect_person() +
                "',vat_registration = '" + firm.getVat_registration() +
                "'  where firm = '" + oldName + "'";


        int edit = service.execute(command);

        updateClientInAnotherTables(firm.getFirm(),oldName);

        return edit;
    }

    private void updateClientInAnotherTables(String newName, String oldName) throws SQLException {
        String command = String.format("update %s set client = '%s' where client = '%s'","ServiceTableDB",newName,oldName);
        service.execute(command);
        command = String.format("update %s set client = '%s' where client = '%s'","ProtokolTableDB5",newName,oldName);
        service.execute(command);
        command = String.format("update %s set client = '%s' where client = '%s'","BrackTableDB2",newName,oldName);
        service.execute(command);
        command = String.format("update %s set client = '%s' where client = '%s'","InvoiceParentDB5",newName,oldName);
        service.execute(command);
        command = String.format("update %s set client = '%s' where client = '%s'","InvoiceChildDB7",newName,oldName);
        service.execute(command);
        command = String.format("update %s set client = '%s' where client = '%s'","ProformParentDB",newName,oldName);
        service.execute(command);
        command = String.format("update %s set client = '%s' where client = '%s'","ProformChildDB2",newName,oldName);
        service.execute(command);
        command = String.format("update %s set client = '%s' where client = '%s'","AcquittanceParentDB",newName,oldName);
        service.execute(command);
        command = String.format("update %s set client = '%s' where client = '%s'","AcquittanceChildDB",newName,oldName);
        service.execute(command);
    }

    @DeleteMapping(path = "/delete_client/{name}")
    public Integer deleteClient(@PathVariable("name") String name) throws SQLException {
        final int[] delete = new int[1];
         String command = "delete from FirmsTable where firm = ?";
         service.execute(command, new PreparedStatementCallback<T>() {
             @Override
             public void callback(PreparedStatement ps) throws SQLException {
                 ps.setString(1,name);
                 delete[0] = ps.executeUpdate();
             }
         });

         if(delete[0] == 0) {
             service.execute("delete from PersonsTable where name = ?", new PreparedStatementCallback<T>() {
                 @Override
                 public void callback(PreparedStatement ps) throws SQLException {
                     ps.setString(1, name);
                     delete[0] = ps.executeUpdate();
                 }
             });
         }
        return delete[0];
    }




    @GetMapping(path = "/client_data")
    public @ResponseBody T getClientData(@RequestParam("client") String client) throws SQLException {
        Firm<T> firm = new Firm<T>();
        service.getResult(String.format("select * from FirmsTable where firm = '%s'",client), new ResultSetCallback() {
            @Override
            public void result(ResultSet resultSet) throws SQLException {
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
                    firm.setDiscount(Integer.parseInt(resultSet.getString(12)));
                    firm.setIncorrect_person(resultSet.getString(13));
                    firm.setVat_registration(resultSet.getString(14));

                    break;
                }
            }
        });

        if(firm.getFirm() == null) {
            service.getResult(String.format("select * from PersonsTable where name = '%s'", client), new ResultSetCallback() {
                @Override
                public void result(ResultSet resultSet) throws SQLException {
                    while (resultSet.next()) {
                        firm.setFirm(resultSet.getString(1));
                        firm.setTelPerson(resultSet.getString(2));
                        firm.setDiscount(Integer.parseInt(resultSet.getString(3)));
                        firm.setIncorrect_person(resultSet.getString(4));
                    }
                }
            });
        }
        return (T) firm;
    }

}
