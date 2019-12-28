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

import com.example.entity.Proj;
import com.example.entity.User;
import com.example.dao.UserDao;
import com.example.util.HibernateUtil;

@Repository
@Transactional
public class ProjDao {

	private UserDao userDao = new UserDao();
	
    public int saveProjToUserId(int userId, Proj proj) {

		System.out.println("proj.saveProjToUserId(): "+userId);
    	int result;
        //Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    		System.out.println("proj.saveProjToUserId(): getting the user");
        	User user = userDao.getUserById(userId);
    		System.out.println("proj.saveProjToUserId(): adding user to proj");
        	proj.setUser(user);
    		//System.out.println("proj.saveProjToUserId(): about to set the userIdx of the proj");
    		//proj.setUserIdx(userId);
    		System.out.println("proj.saveProjToUserId(): adding project to chidren");
        	user.addToChildren(proj);
    		System.out.println("proj.saveProjToUserId(): saving the user");
            session.update(user);
    		userDao.updateUser(user);
    		//System.out.println("proj.saveProjToUserId(): about to save the proj");
    		//session.save(proj);
            result = proj.getId();
    		System.out.println("proj.saveProjToUserId(): result is " + result);
        } catch (Exception e) {
        	e.printStackTrace();
            result = -1;
        }
        return result;
    }

	public String getAllProjsWithUserIdAsString(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        	String projList = userDao.getUserById(userId).getAllChildrenAsJson().toString();
			return projList;
        }
        catch(Exception e) {
        	return null;
        }
	}	

	public Proj getProjByUserIdAndId(int userId, int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    		System.out.println("proj.getProjByUserIdAndId(): making the query");
        	String hqlString = "SELECT c FROM User u JOIN u.projList AS c WHERE u.id = "+userId+" AND c.id = "+id;
    		System.out.println("proj.getProjByUserIdAndId(): made the query: "+hqlString);
			Query query = session.createQuery(hqlString);
    		System.out.println("proj.getProjByUserIdAndId(): made the query");
    		System.out.println("proj.getProjByUserIdAndId(): exec query:" + (Proj) query.getSingleResult());
	        return (Proj) query.getSingleResult();
        }
        catch(Exception e) {
        	return null;
        }
	}	

	public Boolean updateProjByUserIdAndId(int userId, int id, Proj proj) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    		Proj projFromDb = getProjByUserIdAndId(userId, id);
    		if(projFromDb == null){
        		System.out.println("Proj with userId "+userId+" and id "+id+" wasn't found");
    			return false;
    		}
    		System.out.println("Updating Proj");
    		projFromDb.setProjTitle(proj.getProjTitle());
    		projFromDb.setProjSubtitle(proj.getProjSubtitle());
    		projFromDb.setProjDescription(proj.getProjDescription());
    		projFromDb.setProjStartDate(proj.getProjStartDate());
    		projFromDb.setProjEndDate(proj.getProjEndDate());
    		session.beginTransaction();
            session.saveOrUpdate(projFromDb);
            session.getTransaction().commit();
            session.close();
			return true;
        } catch (Exception e) {
    		System.out.println("Something went wrong");
    		System.out.println(e.getStackTrace());
    		return false;
        }
	}

	public boolean deleteProj(int userId, int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {    
    		Proj projFromDb = getProjByUserIdAndId(userId, id);
    		if(projFromDb == null){
        		System.out.println("Proj with userId " +userId+ " and id "+id+" wasn't found");
    			return false;
    		}
		System.out.println("Deleting Proj");
		session.beginTransaction();
        session.remove(projFromDb);
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
	/*	
    public int saveProj(Proj proj) {
    	
    	// Check if project exists
    	int result;
        //Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.save(proj);
            result = proj.getId();
        } catch (Exception e) {
        	e.printStackTrace();
            result = -1;
        }
        return result;
    }
    public List < Proj > getAllProjs() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Proj", Proj.class).list();
        }
    }
	public List<Proj> getAllProjsWithUserId(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        	//String sqlString = "SELECT * FROM kbprojs WHERE userIdx = " + userId;
        	//List<Proj> projList = (List<Proj>) session.createNativeQuery(sqlString, Proj.class).list();
        	User user = userDao.getUserById(userId);
        	List<Proj> projList = user.getAllChildren();
			return projList;
        }
        catch(Exception e) {
        	return null;
        }
	}
	public Proj getProjById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        	Proj proj = (Proj) session.get(Proj.class, id);
			return proj;
        }
        catch(Exception e) {
        	return null;
        }
	}
	 */
}