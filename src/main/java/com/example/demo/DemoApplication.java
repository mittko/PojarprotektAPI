package com.example.demo;


import org.apache.tomcat.util.buf.EncodedSolidusHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.SpringVersion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;


@SpringBootApplication
@ComponentScan(basePackages = {"com.example.demo"})
//  go to http://localhost:1526/swagger-ui/index.html to open Swagger UI
public class DemoApplication  implements WebMvcConfigurer {



	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		//System.out.println(SpringVersion.getVersion());
	}

	// allow front slash in path variables
	@Bean
	TomcatConnectorCustomizer connectorCustomizer() {
		return (connector) -> connector.setEncodedSolidusHandling(EncodedSolidusHandling.DECODE.getValue());
	}
	@Bean
	public StrictHttpFirewall httpFirewall() {
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		firewall.setAllowSemicolon(true);
		firewall.setAllowUrlEncodedSlash(true);
		firewall.setAllowBackSlash(true);
		return firewall;
	}
	// allow front slash in path variables


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
