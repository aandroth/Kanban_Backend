package com.example.dao;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
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

import com.example.entity.Token;
import com.example.util.HibernateUtil;

@Repository
@Transactional
public class TokenDao {
	
    public int saveToken(String tokenStr) {

		System.out.println("tokenDoa.saveToken(): "+tokenStr);    	
		int result = -1;
		Token token = new Token(tokenStr, LocalDate.now().plusDays(3));
        //Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.save(token);
            result = token.getId();
        } catch (Exception e) {
    		System.out.println("Error: "+e.hashCode());
        	e.printStackTrace();
            result = -1;
        }
	    return result;
	}
    
	public Boolean getTokenIsValid(String tokenStr) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    		System.out.println("token.getTokenIsValid(): making the query");
        	String hqlString = "FROM Token as t WHERE t.token = '"+tokenStr+"'";
    		System.out.println("token.getTokenIsValid(): hqlString:" + hqlString);
			Query query = session.createQuery(hqlString);
    		System.out.println("token.getTokenIsValid(): exec query:" + (Token) query.getSingleResult());
    		Token token = (Token) query.getSingleResult();

			Boolean result = token.getExpireDate().compareTo(LocalDate.now()) > 0;
			if(!result) {
	    		System.out.println("token is expired. Deleting token:" + token);
				session.beginTransaction();
		        session.remove(token);
		        session.getTransaction().commit();
		        session.close();
			}
	        return result;
        }
        catch(Exception e) {
        	return false;
        }
	}	

	public boolean deleteToken(String tokenStr) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) { 
        	String hqlString = "FROM Token as t WHERE t.token = '"+tokenStr+"'";
    		System.out.println("token.deleteToken query:" + hqlString);
			Query query = session.createQuery(hqlString);
    		System.out.println("token.deleteToken: exec query:" + (Token) query.getSingleResult());
    		Token token = (Token) query.getSingleResult();
    		if(token == null){
        		System.out.println("Token with token "+tokenStr+" was not found");
    			return false;
    		}
		System.out.println("Deleting Token");
		session.beginTransaction();
        session.remove(token);
        session.getTransaction().commit();
        session.close();
		return true;
        }
	}
}