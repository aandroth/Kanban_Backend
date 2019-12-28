package com.example.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;
import com.example.entity.User;
import com.example.entity.Proj;
import com.example.dao.UserDao;
import com.example.controllers.ProjController;
@ComponentScan(basePackages = { "com.example.dao"} )
@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("kb")
public class UserController {
	
	public UserController() {
		System.out.println("UserController created.");
	}

	@Autowired
	private UserDao userDAO;

	@CrossOrigin(origins="*")
	@PostMapping(value="user", produces = {"application/json"})
	public ResponseEntity<Integer> addUser(@RequestBody User user, UriComponentsBuilder builder) {
		System.out.println("Attempting to add user "+user.getUserName());
                int id = userDAO.saveUser(user);
                if (id <= 0) {
                	return new ResponseEntity<Integer>(-1, HttpStatus.CONFLICT);
                }
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(builder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
        		System.out.println("user.getUserId(): "+user.getId());
                return new ResponseEntity<Integer>(user.getId(), HttpStatus.CREATED);
	}
	
	@CrossOrigin(origins="*")
	@GetMapping(value="login/{email}/{pass}", produces = {"application/json"})
	public ResponseEntity<String> getUserFromLogin(@PathVariable("email") String email, @PathVariable("pass") String pass) {
		System.out.println("Attempting to login user "+email + " with pass "+pass);
		User user = userDAO.getUserByLogin(email, pass);
		System.out.println("Returning user "+user);
		return new ResponseEntity<String>(user.toJson().toString(), HttpStatus.OK);
	}
	//Access-Control-Allow-Origin: http://localhost:3000
	@CrossOrigin(origins="*")
	@GetMapping(value="loginStatus", produces = {"application/json"})
	public ResponseEntity<String> getUserLoginStatus(@PathVariable("email") String email) {
		System.out.println("Checking login status of "+ email);
		User user = userDAO.getUserByLogin(email, email);
		System.out.println("Returning user "+user);
		return new ResponseEntity<String>(user.toJson().toString(), HttpStatus.OK);
	}

	@CrossOrigin(origins="*")
	@PutMapping("user")
	public ResponseEntity<Boolean> updateUser(@RequestBody User user, UriComponentsBuilder builder) {
		System.out.println("updateUser: "+user.getId());
		boolean result = userDAO.updateUser(user);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	@CrossOrigin(origins="*")
	@DeleteMapping("user/{id}")
	public ResponseEntity<Boolean> deleteUser(@PathVariable("id") Integer id) {
		System.out.println("Delete called for id : "+id);
		boolean result = userDAO.deleteUser(id);
		System.out.println("Delete result is : "+result);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
	
	// Deprecated Fns

	@CrossOrigin(origins="*")
	@GetMapping(value="basic/{email}")
	public ResponseEntity<String> getBasic(@PathVariable("email") String email) {
		UserDao uDao = new UserDao();
		System.out.println("Basic has been requested.");
		List<Proj> pList = new ArrayList<Proj>();
		User u = new User(pList, email, "New Name", "New Pass", false);
		System.out.println("Filled user: " + u);
		System.out.println("Basic Adding user");
		int res = uDao.saveUser(u);
		String sRes = "";
		if(res > 0)
			sRes = "Success: "+ res;
		else
			sRes = "Failure";
		System.out.println("Adding user " + sRes);
		return new ResponseEntity<String>(sRes, HttpStatus.OK);
	}

	@CrossOrigin(origins="*")
	@GetMapping(value="user/{id}", produces = {"application/json"})
	public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
		User user = userDAO.getUserById(id);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	@CrossOrigin(origins="*")
	@GetMapping(value="users", produces = {"application/json"})
	public ResponseEntity<List<User>> getAllUsers() {
		System.out.println("Users have been requested.");
		List<User> list = userDAO.getUsers();
		return new ResponseEntity<List<User>>(list, HttpStatus.OK);
	}
	 
	

}  