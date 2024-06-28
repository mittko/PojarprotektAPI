package com.example.demo.controllers.init;

import com.example.demo.models.TechnicalReview;
import com.example.demo.services.RepoService;
import com.example.demo.requestbodies.DemoPostBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


@RestController
public class RepoController {

    @Autowired
    public RepoService repoService;


    /*In Postman. under Body, select raw and choose JSON from the drop down menu that appears.
    Then write the JSON that is the request body.
    You can't use form-data or x-www-form-urlencoded with
    @RequestBody, they are used when the binding is @ModelAttribute.*/
//    @PostMapping(path = "/add")
//    public Map<String,Boolean> add(@RequestBody DemoPostBody body) {
//        repoService.insertTEST(body.getId(), body.getName());
//        return Collections.singletonMap("added",true);
//    }
}
