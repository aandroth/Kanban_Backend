package com.example.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.entity.Proj;

@Entity
@Table(name = "kbusers")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userIdx")
    private int id;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "userIdx")
    private List<Proj> projList = new ArrayList<Proj>();
    @Column(name = "userEmail")
    private String userEmail;
    @Column(name = "userName")
    private String userName;
    @Column(name = "userPass")
    private String userPass;
    @Column(name = "loginStatus")
    private Boolean loginStatus;
    public User() {
    }
    public User(List<Proj> list, String email, String name, String pass, Boolean login) {
    	this.projList = list;
        this.userEmail = email;
        this.userName = name;
        this.userPass = pass;
        this.loginStatus = login;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }    
    protected void setChildren(List<Proj> projList) {
        this.projList = projList;
    }
    public void addToChildren(Proj proj) {
        this.projList.add(proj);
    }
    public List<Proj> getAllChildren() {
        return this.projList;
    }
    public JSONArray getAllChildrenAsJson() {
    	JSONArray ja = new JSONArray();
    	for(int ii=0; ii<this.projList.size(); ++ii)
    		ja.put(this.projList.get(ii).toJson());
        return ja;
    }
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public Boolean getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(Boolean loginStatus) {
		this.loginStatus = loginStatus;
	}
	@Override
	public String toString() {
		return "{id:" + id + ", userEmail:'" + userEmail + "', userName:'" + userName + "', userPass:'" + userPass + "'}";
	}
	
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		jo.put("id", id);
		jo.put("userEmail", userEmail);
		jo.put("userName", userName);
		jo.put("userPass", "No");
		jo.put("projList", this.getAllChildrenAsJson());
		return jo;
	}
    
}