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
import com.example.entity.Task;
import com.example.entity.User;
import com.example.dao.TaskDao;
import com.example.dao.UserDao;
@ComponentScan(basePackages = { "com.example.dao"} )
@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("kb")
public class TaskController {
	
	public TaskController() {
		System.out.println("TaskController created.");
	}

	@Autowired
	private TaskDao taskDAO;

	@CrossOrigin(origins = "*")
	@GetMapping(value="task/{userId}/{projId}/{id}", produces = {"application/json"})
	public ResponseEntity<String> getTaskByUserIdAndProjIdAndId(@PathVariable("userId") int userId, 
												@PathVariable("projId") int projId, 
												@PathVariable("id") int id) {
		JSONObject jsonObj = taskDAO.getTaskByUserIdAndProjIdAndId(userId, projId, id).toJson();
		return new ResponseEntity<String>(jsonObj.toString(), HttpStatus.OK);
	}
/*
	@GetMapping(value="tasks/{userId}", produces = {"application/json"})
	public ResponseEntity<String> getAllTasksWithUserId(@PathVariable("userId") int userId) {
		System.out.println("Tasks with userId "+userId+" have been requested.");
		return new ResponseEntity<String>(taskDAO.getAllTasksWithUserIdAsString(userId), HttpStatus.OK);
	}*/
	@CrossOrigin(origins="*")
	@PostMapping(value="task/{userId}/{projId}", produces = {"application/json"})
	public ResponseEntity<Integer> addTask(@PathVariable ("userId") int userId, @PathVariable ("projId") int projId,
											@RequestBody Task task, UriComponentsBuilder builder) {
		System.out.println("Attempting to add task to Proj with userId "+userId+ " and projId " + projId);
        int newId = taskDAO.saveTaskToProjWithUserIdAndProjId(userId, projId, task);
        if (newId < 0) {
        	return new ResponseEntity<Integer>(-1, HttpStatus.CONFLICT);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/task/{userId}/"+projId+"/"+newId).buildAndExpand(userId).toUri());
		System.out.println("task.getProjIdx(): "+task.getProjId());
		System.out.println("task.getTaskId(): "+task.getId());
		System.out.println("headers: "+headers);
        return new ResponseEntity<Integer>(task.getId(), HttpStatus.CREATED);
	}

	@CrossOrigin(origins="*")
	@PutMapping("task/{userId}/{projId}/{id}")
	public ResponseEntity<Boolean> updateTask(@PathVariable("userId") int userId, @PathVariable("projId") int projId,
										@PathVariable("id") int id,@RequestBody Task task, UriComponentsBuilder builder) {
		System.out.println("Attempting to udate task");
		System.out.println("Title is "+task.getTaskTitle()+", Catagory is "+task.getCategoryIdx());
		boolean result = taskDAO.updateTaskByUserIdAndProjIdAndId(userId, projId, id, task);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	@CrossOrigin(origins="*")
	@DeleteMapping("task/{userId}/{projId}/{id}")
	public ResponseEntity<Boolean> deleteTask(@PathVariable("userId") int userId,
			 								@PathVariable("projId") int projId,
											@PathVariable("id") int id) {
		System.out.println("Delete called for userId "+userId+", projId "+projId+" and taskId "+ id);
		boolean result = taskDAO.deleteTask(userId, projId, id);
		System.out.println("Delete result is : "+result);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
	@CrossOrigin(origins="*")
	@GetMapping(value="tasksByCategory/{userId}/{projId}", produces = {"application/json"})
	public ResponseEntity<String> getTasksOfUserIdAndProjIdOrganizedByCategory(@PathVariable("userId") Integer userId,
																					@PathVariable("projId") int projId) {
		System.out.println("Tasks have been requested.");
		JSONArray jaOfJa = new JSONArray();
		jaOfJa.put(taskListAsJsonArray(taskDAO.getTasksOfUserIdAndProjIdAndCategoryIdx(userId, projId, 0)));
		jaOfJa.put(taskListAsJsonArray(taskDAO.getTasksOfUserIdAndProjIdAndCategoryIdx(userId, projId, 1)));
		jaOfJa.put(taskListAsJsonArray(taskDAO.getTasksOfUserIdAndProjIdAndCategoryIdx(userId, projId, 2)));
		return new ResponseEntity<String>(jaOfJa.toString(), HttpStatus.OK);
	}
	
	public JSONArray taskListAsJsonArray(List<Task> tList) {
		JSONArray ja = new JSONArray();
		for(int ii=0; ii<tList.size(); ++ii)
			ja.put(tList.get(ii).toJson());
		return ja;
	}
	// Deprecated Fns
	/*
	@GetMapping(value="taskTest")
	public ResponseEntity<String> getBasic() {
		TaskDao pDao = new TaskDao();
		System.out.println("Basic has been requested.");
		Task u = new Task("New Title", "New Sub", "New Description", "New Start", "New End");
		System.out.println("Filled task: " + u);
		System.out.println("Basic Adding task");
		int res = pDao.saveTask(u);
		String sRes = "";
		if(res > 0)
			sRes = "Success";
		else
			sRes = "Failure";
		System.out.println("Adding task " + sRes);
		return new ResponseEntity<String>(sRes, HttpStatus.OK);
	}
	@GetMapping(value="task/{id}", produces = {"application/json"})
	public ResponseEntity<Task> getTaskById(@PathVariable("id") int id) {
		Task task = taskDAO.getTaskById(id);
		return new ResponseEntity<Task>(task, HttpStatus.OK);
	}
	 
	@GetMapping(value="/some_url", produces = "application/json")
	public ResponseEntity<Integer> returnNum() {
		return new ResponseEntity<Integer>(7777, HttpStatus.OK);
	}*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

			
}  