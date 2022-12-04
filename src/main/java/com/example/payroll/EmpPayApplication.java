package com.example.payroll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@RestController
public class EmpPayApplication {

	//TODO these two apis for google sso login test; need to save google user details from google to db
	@GetMapping
	public String welcome() {
		return "Welcome to Google !! Login Successful";
	}
	/*
	* By hitting this endpoint we can get the user information from google
	* */
	@GetMapping("/user")
	public Principal user(Principal principal) {
		System.out.println("username : " + principal.getName());
		return principal;
	}

	public static void main(String[] args) {
		SpringApplication.run(EmpPayApplication.class, args);
	}

}
