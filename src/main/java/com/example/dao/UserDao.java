package com.example.dao;

import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.entity.User;
import com.example.dao.TokenDao;
import com.example.util.HibernateUtil;

@Repository
@Transactional
public class UserDao {
	
	private TokenDao tokenDAO = new TokenDao();
	
    public int saveUser(User user) {
    	
    	// Check if user exists
    	int result = -1;
        //Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.save(user);
            result = user.getId();
        } catch (Exception e) {
    		System.out.println("Error: "+e.hashCode());
        	e.printStackTrace();
            result = -1;
        }
        return result;
    }
    
	public User getUserById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    		User user = (User) session.get(User.class, id);
			return user;
        }
        catch(Exception e) {
        	return null;
        }
	}

	public User getUserByLogin(String email, String pass) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    		String hql = "FROM User as user WHERE user.userEmail = '"+email+"' and user.userPass = '"+pass+"'";
    		System.out.println("Inside getUserByLogin");
    		@SuppressWarnings("unchecked")
			User user = (User) session.createQuery(hql).getSingleResult();
    		System.out.println("Ran query");
			return user;
        }
        catch(Exception e) {
    		System.out.println("Login Error ");
    		e.printStackTrace();
        	return null;
        }
	}

	public User getUserByToken(String email, String token) {
		System.out.println("Inside getUserByToken with email "+email+ ", token "+token);
		
    	if(!tokenDAO.getTokenIsValid(token)) {
    		System.out.println("Token is not valid");
    		return null;
    	}
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    		String hql = "FROM User as user WHERE user.userEmail = '"+email+"'";
    		User user = (User) session.createQuery(hql).getSingleResult();
    		System.out.println("Ran query");
    		session.close();
			return user;
        }
        catch(Exception e) {
    		System.out.println("Non-existant user!");
    		e.printStackTrace();
        	return null;
        }
	}	

	public Boolean updateUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    		User userFromDb = (User)session.get(User.class, user.getId());
    		if(userFromDb == null){
        		System.out.println("User with idx "+user.getId()+" wasn't found");
    			return false;
    		}
    		System.out.println("Updating User");
    		
    		userFromDb.setUserEmail(user.getUserEmail());
    		userFromDb.setUserName(user.getUserName());
    		userFromDb.setUserPass(user.getUserPass());
    		session.beginTransaction();
            session.saveOrUpdate(userFromDb);
            session.getTransaction().commit();
            session.close();
			return true;
        } catch (Exception e) {
    		System.out.println("Something went wrong");
    		System.out.println(e.getStackTrace());
        }
		return false;
	}


	public boolean deleteUser(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {    
    		User userFromDb = (User)session.get(User.class, id);		
        	if(userFromDb == null){
    		System.out.println("User with idx "+id+" wasn't found");
			return false;
		}
		System.out.println("Deleting User");
		session.beginTransaction();
        session.remove(userFromDb);
        session.getTransaction().commit();
        session.close();
		return true;
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
		return false;
	}
	
	// Deprecated Fns
	
    public List <User> getUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }
	 
}