package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.example.demo"})
public class DemoApplication {

	@Autowired
	RepoController controller;
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
