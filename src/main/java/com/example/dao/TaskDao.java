package com.example.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.entity.Task;
import com.example.entity.User;
import com.example.entity.Proj;
import com.example.dao.UserDao;
import com.example.dao.ProjDao;
import com.example.util.HibernateUtil;

@Repository
@Transactional
public class TaskDao {

	private UserDao userDao = new UserDao();
	private ProjDao projDao = new ProjDao();
	
    public int saveTaskToProjWithUserIdAndProjId(int userId, int projId, Task task) {

		System.out.println("task.saveTaskToUserId(): "+userId);
	int result;
	//Transaction transaction = null;
	try (Session session = HibernateUtil.getSessionFactory().openSession()) {
		System.out.println("task.saveTaskToUserId(): getting the user");
		User user = userDao.getUserById(userId);
		Proj proj = projDao.getProjByUserIdAndId(userId, projId);
		System.out.println("task.saveTaskToProjId(): adding user to task");
		task.setProj(proj);
		//System.out.println("task.saveTaskToUserId(): about to set the userIdx of the task");
		//task.setUserIdx(userId);
		System.out.println("task.saveTaskToProjId(): adding task to chidren");
		proj.addToChildren(task);
		System.out.println("task.saveTaskToProjId(): saving the proj");
	    session.update(proj);
	    projDao.updateProjByUserIdAndId(userId, projId, proj);
	    //session.update(user);
		//userDao.updateUser(user); //Do we need this?
		//System.out.println("task.saveTaskToProjId(): about to save the task");
		//session.save(task);
	    result = task.getId();
		System.out.println("task.saveTaskToUserId(): result is " + result);
	    } catch (Exception e) {
	    	e.printStackTrace();
	        result = -1;
	    }
	    return result;
	}
	/*
		public String getAllTasksWithUserIdAsString(int userId) {
	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        	String taskList = userDao.getUserById(userId).getAllChildrenAsJson().toString();
				return taskList;
	        }
	        catch(Exception e) {
	        	return null;
	        }
		}	
        	String hqlString = "SELECT c FROM User u JOIN u.projList AS c WHERE u.id = "+userId+" AND c.id = "+id;
*/
	public Task getTaskByUserIdAndProjIdAndId(int userId, int projId, int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    		System.out.println("task.getTaskByUserIdAndProjIdAndId(): making the query");
        	String hqlString = "SELECT c FROM Proj p JOIN p.taskList AS c " +
        																		" WHERE p.user.id = "+userId+
																				" AND p.id = "+projId+
																				" AND c.id = "+id;
			Query query = session.createQuery(hqlString);
    		System.out.println("task.getTaskByUserIdAndProjIdAndId(): exec query:" + (Task) query.getSingleResult());
	        return (Task) query.getSingleResult();
        }
        catch(Exception e) {
        	return null;
        }
	}	
	public List<Task> getTasksOfUserIdAndProjIdAndCategoryIdx(int userId, int projId, int catIdx) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    		System.out.println("task.getTaskByUserIdAndProjIdAndId(): making the query");
        	String hqlString = "SELECT c FROM Proj p JOIN p.taskList AS c " +
        																		" WHERE p.user.id = "+userId+
																				" AND p.id = "+projId+
																				" AND c.categoryIdx = "+catIdx;
			Query query = session.createQuery(hqlString);
	        return (List<Task>) query.getResultList();
        }
        catch(Exception e) {
        	return null;
        }
	}	

	public Boolean updateTaskByUserIdAndProjIdAndId(int userId, int projId, int id, Task task) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    		Task taskFromDb = getTaskByUserIdAndProjIdAndId(userId, projId, id);
    		if(taskFromDb == null){
        		System.out.println("Task with idx "+task.getId()+" wasn't found");
    			return false;
    		}
    		System.out.println("Updating Task");
    		System.out.println("Catagory is "+task.getCategoryIdx());
    		System.out.println("Due Date is "+task.getTaskDueDate());
    		taskFromDb.setCategoryIdx(task.getCategoryIdx());
    		taskFromDb.setTaskTitle(task.getTaskTitle());
    		taskFromDb.setTaskSummary(task.getTaskSummary());
    		taskFromDb.setTaskCriteria(task.getTaskCriteria());
    		taskFromDb.setTaskDueDate(task.getTaskDueDate());
    		taskFromDb.setTaskPriority(task.getTaskPriority());
    		session.beginTransaction();
            session.saveOrUpdate(taskFromDb);
            session.getTransaction().commit();
            session.close();
			return true;
        } catch (Exception e) {
    		System.out.println("Something went wrong");
    		System.out.println(e.getStackTrace());
        }
		return false;
	}

	public boolean deleteTask(int userId, int projId, int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {    
    		Task taskFromDb = getTaskByUserIdAndProjIdAndId(userId, projId, id);
    		if(taskFromDb == null){
        		System.out.println("Task with userId "+userId+" projId "+projId+" and taskId "+ id +" was not found");
    			return false;
    		}
		System.out.println("Deleting Task");
		session.beginTransaction();
        session.remove(taskFromDb);
        session.getTransaction().commit();
        session.close();
		return true;
        }
	}
}