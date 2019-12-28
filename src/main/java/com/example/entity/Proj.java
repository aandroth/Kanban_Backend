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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.json.JSONArray;
import org.json.JSONObject;
@Entity
@Table(name = "kbprojs")
public class Proj {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projId")
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userIdx", nullable = false)
    private User user;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "projId")
    private List<Task> taskList = new ArrayList<Task>();
    @Column(name = "projTitle")
    private String projTitle;
    @Column(name = "projSubtitle")
    private String projSubtitle;
    @Column(name = "projDescription")
    private String projDescription;
    @Column(name = "projStartDate")
    private String projStartDate;
    @Column(name = "projEndDate")
    private String projEndDate;
    public Proj() {
    }
    public Proj(List<Task> taskList, String projTitle, String projSubtitle, 
    		String projDescription, String projStartDate, String projEndDate) {
    	this.taskList = taskList;
        this.projTitle = projTitle;
        this.projSubtitle = projSubtitle;
        this.projDescription = projDescription;
        this.projStartDate = projStartDate;
        this.projEndDate = projEndDate;
    }
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserIdx() {
		return this.user.getId();
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	public void setUserIdx(int userIdx) {
		this.user.setId(userIdx);
	}
    protected void setChildren(List<Task> taskList) {
        this.taskList = taskList;
    }
    public void addToChildren(Task task) {
        this.taskList.add(task);
    }
    public List<Task> getAllChildren() {
        return this.taskList;
    }
    public JSONArray getAllChildrenAsJson() {
    	JSONArray ja = new JSONArray();
    	for(int ii=0; ii<this.taskList.size(); ++ii)
    		ja.put(this.taskList.get(ii).toJson());
        return ja;
    }
	public String getProjTitle() {
		return projTitle;
	}
	public void setProjTitle(String projTitle) {
		this.projTitle = projTitle;
	}
	public String getProjSubtitle() {
		return projSubtitle;
	}
	public void setProjSubtitle(String projSubtitle) {
		this.projSubtitle = projSubtitle;
	}
	public String getProjDescription() {
		return projDescription;
	}
	public void setProjDescription(String projDescription) {
		this.projDescription = projDescription;
	}
	public String getProjStartDate() {
		return projStartDate;
	}
	public void setProjStartDate(String projStartDate) {
		this.projStartDate = projStartDate;
	}
	public String getProjEndDate() {
		return projEndDate;
	}
	public void setProjEndDate(String projEndDate) {
		this.projEndDate = projEndDate;
	}
	
	@Override
	public String toString() {
		return ("{id:" + id + 
				", userId:" + user.getId() + 
				", projTitle: " + projTitle + 
				", projSubtitle:" + projSubtitle + 
				", projDescription:" + projDescription + 
				", projStartDate:" + projStartDate + 
				", projEndDate:" + projEndDate + "}");
	}
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();

		ja.put(new JSONArray());
		ja.put(new JSONArray());
		ja.put(new JSONArray());

		jo.put("id", id);
		jo.put("key", id);
		jo.put("title", projTitle);
		jo.put("subtitle", projSubtitle);
		jo.put("description", projDescription);
		jo.put("start_date", projStartDate);
		jo.put("end_date", projEndDate);
		jo.put("categories", ja);
		
		return jo;
	}
}