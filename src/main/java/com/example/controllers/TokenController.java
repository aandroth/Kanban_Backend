package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.dao.TokenDao;
@ComponentScan(basePackages = { "com.example.dao"} )
@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("kb")
public class TokenController {
	
	public TokenController() {
		System.out.println("TokenController created.");
	}

	@Autowired
	private TokenDao tokenDAO;

	@CrossOrigin(origins = "*")
	@GetMapping(value="token/{token}", produces = {"application/json"})
	public ResponseEntity<Boolean> validateToken(@PathVariable("token") String token) {
		Boolean bool = tokenDAO.getTokenIsValid(token);
		System.out.println("Inside validateToken, reporting that the token validity is "+bool + " for token " + token);
		return new ResponseEntity<Boolean>(bool, HttpStatus.OK);
	}
	
	@CrossOrigin(origins="*")
	@PostMapping(value="token/{token}", produces = {"application/json"})
	public ResponseEntity<Integer> addToken(@PathVariable ("token") String token) {
		System.out.println("Attempting to add token "+token);
		System.out.println("Inside addToken");
		int result = tokenDAO.saveToken(token);
        return new ResponseEntity<Integer>(result, HttpStatus.CREATED);
	}

	@CrossOrigin(origins="*")
	@DeleteMapping("token/{token}")
	public ResponseEntity<Boolean> deleteToken(@PathVariable("token") String token) {
		System.out.println("Attempting to delete token "+token);
		boolean result = tokenDAO.deleteToken(token);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
}  
