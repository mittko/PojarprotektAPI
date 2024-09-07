package com.example.demo;

import com.example.demo.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.demo"})
public class DemoApplication {


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

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
