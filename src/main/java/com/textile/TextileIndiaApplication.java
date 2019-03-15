package com.textile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TextileIndiaApplication {
	
	@RequestMapping(value="/")
	public String show() {
		return "Test Heroku";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(TextileIndiaApplication.class, args);
	}
	
}

