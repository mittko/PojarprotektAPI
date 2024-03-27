package com.example.demo;

import com.example.demo.requestbodies.DemoPostBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
//@RequestMapping(path = "/demo")
public class RepoController {

    @Autowired
    RepoService repoService;

    @GetMapping(path = "/all")
    public @ResponseBody ArrayList<ArrayList<Object>> getAllUsers2() {

        return repoService.getTEST();
    }

    /*In Postman. under Body, select raw and choose JSON from the drop down menu that appears.
    Then write the JSON that is the request body.
    You can't use form-data or x-www-form-urlencoded with
    @RequestBody, they are used when the binding is @ModelAttribute.*/
    @PostMapping(path = "/add")
    public @ResponseBody void add(@RequestBody DemoPostBody body) {
        repoService.insertTEST(body.getId(), body.getName());
      //  return ResponseEntity.ok(HttpStatus.OK);
    }
}
