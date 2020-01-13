package com.example.entity;


import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.JSONObject;

@Entity
@Table(name = "kbtokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "token")
    private String token;
    @Column(name = "expireDate")
    private LocalDate expireDate;
    public Token() {
    }
    public Token(String token, LocalDate localDate) {
    	this.token = token;
        this.expireDate = localDate;
    }
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public LocalDate getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(LocalDate expireDate) {
		this.expireDate = expireDate;
	}
	@Override
	public String toString() {
		return "Token [id=" + id + ", token=" + token + ", expireDate=" + expireDate + "]";
	}
	
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();

		jo.put("id", id);
		jo.put("key", id);
		jo.put("token", token);
		jo.put("expireDate", expireDate);
		
		return jo;
	}
}