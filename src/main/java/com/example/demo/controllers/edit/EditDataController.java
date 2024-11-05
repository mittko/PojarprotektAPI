package com.example.demo.controllers.edit;

import com.example.demo.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class EditDataController {

    @Autowired
    RepoService service;


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
}
