package com.example.demo.controllers.edit;

import com.example.demo.callbacks.PreparedStatementCallback;
import com.example.demo.models.ArtikulSimpleModel;
import com.example.demo.models.ArtikulSimpleModelBodyList;
import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@RestController
public class EditDataController<T> {

    @Autowired
    RepoService<T> service;

    @PutMapping("/add_column/{table}/{column}/{column_length}")
    public @ResponseBody Integer addTableColumn(@PathVariable("table") String table, @PathVariable("column") String columnName,
                                                @PathVariable("column_length") Integer columnLength) throws SQLException {
        String command = "ALTER TABLE " + table + " ADD COLUMN " + columnName
                + " VARCHAR(" + columnLength + ")";
        int result = service.execute(command);

        // set default value
        command = "update " + table + " set sklad = 'Office'";

        service.execute(command);

        return result;
    }


    @PutMapping("/update_artikuls_column")
    public @ResponseBody Integer updateArtikulsColumnData(@RequestBody ArtikulSimpleModelBodyList body) throws SQLException {
        int result = 0;
        for(ArtikulSimpleModel artikul : body.getSimpleModels()) {
            final String command = String.format("update ArtikulsDB set sklad = '%s' where artikul = '%s'",
                    artikul.getSklad(), artikul.getArtikul());
            result += service.execute(command);
        }
        return result;
    }

    @PutMapping("/update_data/{table}/{column}/{value}/{clauseColumn}/{clauseValue}")
    public @ResponseBody Integer updateValue(@PathVariable("table") String table,
                                             @PathVariable("column") String column,
                                             @PathVariable("value") String value,
                                             @PathVariable("clauseColumn") String clauseColumn,
                                             @PathVariable("clauseValue") String clauseValue) throws SQLException {
        String command = String.format("update %s set %s = '%s' where %s = '%s'",
                table,column,value,clauseColumn,clauseValue);
        return service.execute(command);
    }
    @PutMapping("/update_column_width/{table}/{column}/{width}")
    public @ResponseBody Integer updateColumnWidth(@PathVariable("table") String table,
                                                   @PathVariable("column") String column,
                                                   @PathVariable("width") Integer width) throws SQLException {
        String command = String.format(
                "alter table %s alter column %s set data type varchar(%d)",
                table, column, width);
        return service.execute(command);
    }


    @DeleteMapping("/delete_item/{table}/{column_clause}/{column_value}")
    public @ResponseBody Integer deletedItem(@PathVariable("table") String table,
                                             @PathVariable("column_clause") String columnClause,
                                             @PathVariable("column_value") String columnValue) throws SQLException {
        final int[] result = new int[1];
        String command = "delete from " + table
                + " where "+columnClause+" = ?";
        service.execute(command, new PreparedStatementCallback<T>() {
            @Override
            public void callback(PreparedStatement ps) throws SQLException {
                ps.setString(1, columnValue);
                result[0] = ps.executeUpdate();
            }
        });
        String fak = command;
        return result[0];
    }

}
