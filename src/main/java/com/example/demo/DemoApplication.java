package com.example.demo;

import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;

import org.hibernate.Session;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dao.*;
import com.example.entity.*;
import com.example.util.HibernateUtil;

import com.example.util.CorsConfig;

@ComponentScan(basePackages = { "com.example.controllers"} )
@RestController
@SpringBootApplication
//@SqlResultSetMappings(value="selectIdFromUsersById", "SELECT * FROM 'kbusers'");
public class DemoApplication {
    public static void main(String[] args) {   	
        //UserDao userDao = new UserDao();
        //User user = new User("H", "H", "H@H.com");
        //userDao.saveUser(user);
    	
        SpringApplication.run(DemoApplication.class, args); 
    }
    
	@GetMapping(value="/home")
	public ResponseEntity<String> getHome() {
		System.out.println("Home has been requested.");
    	UserDao userDao = new UserDao();
        User user = userDao.getUserById(20);
        userDao.deleteUser(20);
		return new ResponseEntity<String>("Home has been requested.", HttpStatus.OK);
	}
}