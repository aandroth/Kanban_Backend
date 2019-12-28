package com.example.controllers;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
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
import com.example.entity.Proj;
import com.example.entity.User;
import com.example.dao.ProjDao;
import com.example.dao.UserDao;
@ComponentScan(basePackages = { "com.example.dao"} )
@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("kb")
public class ProjController {
	
	public ProjController() {
		System.out.println("ProjController created.");
	}

	@Autowired
	private ProjDao projDAO;

	@CrossOrigin(origins = "*")
	@GetMapping(value="proj/{userId}/{id}", produces = {"application/json"})
	public ResponseEntity<String> getProjByUserId(@PathVariable("userId") int userId, @PathVariable("id") int id) {
		JSONObject jsonObj = projDAO.getProjByUserIdAndId(userId, id).toJson();
		return new ResponseEntity<String>(jsonObj.toString(), HttpStatus.OK);
	}
	@GetMapping(value="projs/{userId}", produces = {"application/json"})
	public ResponseEntity<String> getAllProjsWithUserId(@PathVariable("userId") int userId) {
		System.out.println("Projs with userId "+userId+" have been requested.");
		return new ResponseEntity<String>(projDAO.getAllProjsWithUserIdAsString(userId), HttpStatus.OK);
	}
	@CrossOrigin(origins="*")
	@PostMapping(value="proj/{userId}", produces = {"application/json"})
	public ResponseEntity<Integer> addProj(@PathVariable ("userId") int userId,
											@RequestBody Proj proj, UriComponentsBuilder builder) {
		System.out.println("Attempting add proj to userId");
		System.out.println("Attempting to add proj to userId "+userId);
        int newId = projDAO.saveProjToUserId(userId, proj);
        if (newId < 0) {
        	return new ResponseEntity<Integer>(-1, HttpStatus.CONFLICT);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/proj/{userId}/"+newId).buildAndExpand(userId).toUri());
		System.out.println("proj.getUserIdx(): "+proj.getUserIdx());
		System.out.println("proj.getProjId(): "+proj.getId());
		System.out.println("headers: "+headers);
        return new ResponseEntity<Integer>(proj.getId(), HttpStatus.CREATED);
	}

	@CrossOrigin(origins="*")
	@PutMapping("proj/{userId}/{id}")
	public ResponseEntity<Boolean> updateProj(@PathVariable("userId") int userId, @PathVariable("id") int id,
																@RequestBody Proj proj, UriComponentsBuilder builder) {
		boolean result = projDAO.updateProjByUserIdAndId(userId, id, proj);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	@CrossOrigin(origins="*")
	@DeleteMapping("proj/{userId}/{id}")
	public ResponseEntity<Boolean> deleteProj(@PathVariable("userId") Integer userId, @PathVariable("id") Integer id) {
		System.out.println("Delete called for userId "+userId+id+" and projId "+ id);
		boolean result = projDAO.deleteProj(userId, id);
		System.out.println("Delete result is : "+result);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
	
	// Deprecated Fns
	/*
	@GetMapping(value="projTest")
	public ResponseEntity<String> getBasic() {
		ProjDao pDao = new ProjDao();
		System.out.println("Basic has been requested.");
		Proj u = new Proj("New Title", "New Sub", "New Description", "New Start", "New End");
		System.out.println("Filled proj: " + u);
		System.out.println("Basic Adding proj");
		int res = pDao.saveProj(u);
		String sRes = "";
		if(res > 0)
			sRes = "Success";
		else
			sRes = "Failure";
		System.out.println("Adding proj " + sRes);
		return new ResponseEntity<String>(sRes, HttpStatus.OK);
	}
	@GetMapping(value="proj/{id}", produces = {"application/json"})
	public ResponseEntity<Proj> getProjById(@PathVariable("id") int id) {
		Proj proj = projDAO.getProjById(id);
		return new ResponseEntity<Proj>(proj, HttpStatus.OK);
	}
	@GetMapping(value="projs", produces = {"application/json"})
	public ResponseEntity<List<Proj>> getAllProjs() {
		System.out.println("Projs have been requested.");
		List<Proj> list = projDAO.getAllProjs();
		return new ResponseEntity<List<Proj>>(list, HttpStatus.OK);
	}
	 */
}  