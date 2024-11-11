package com.example.demo.controllers.init;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloPojarprotektController {

    @GetMapping("/hello")
    public @ResponseBody String sayHello()  {
        return "Hello Pojarprotekt !";
    }
}
