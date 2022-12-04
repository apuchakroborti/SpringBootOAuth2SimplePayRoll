package com.example.payroll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@SpringBootApplication
@RestController
@EnableOAuth2Sso
public class EmpPayApplication {

	//TODO these two apis for google sso login test; need to save google user details from google to db
	//for google
	/*@GetMapping
	public String welcome() {
		return "Welcome to Google !! Login Successful";
	}*/
	/*
	* By hitting this endpoint we can get the user information from google
	* */
	@GetMapping("/user")
	public Principal user(Principal principal) {
		System.out.println("username : " + principal.getName());
		return principal;
	}

	//for facebook
	@GetMapping("/")
	public String welcome(Principal principal) {
		Map<String, Object> details = (Map<String, Object>)
				((OAuth2Authentication) principal).getUserAuthentication().getDetails();
		String userName = (String) details.get("name");
		return "Hi " + userName + " Welcome to my application !!";
	}

	public static void main(String[] args) {
		SpringApplication.run(EmpPayApplication.class, args);
	}

}
