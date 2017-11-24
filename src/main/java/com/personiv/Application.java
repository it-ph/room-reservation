package com.personiv;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer{
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
      return builder.sources(Application.class);
	}
	
	@PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.out.println(TimeZone.getDefault());
        
	}
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
