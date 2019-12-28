package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.json.JSONObject;

@Entity
@Table(name = "kbtasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskId")
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projId", nullable = false)
    private Proj proj;
    @Column(name = "categoryIdx", nullable = false)
    private int categoryIdx;
    @Column(name = "taskTitle")
    private String taskTitle;
    @Column(name = "taskSummary")
    private String taskSummary;
    @Column(name = "taskCriteria")
    private String taskCriteria;
    @Column(name = "taskDueDate")
    private String taskDueDate;
    @Column(name = "taskPriority")
    private String taskPriority;
    public Task() {
    }
    public Task(int categoryIdx, String taskTitle, String taskSummary, 
    		String taskCriteria, String taskDueDate, String taskPriority) {
    	this.categoryIdx = categoryIdx;
        this.taskTitle = taskTitle;
        this.taskSummary = taskSummary;
        this.taskCriteria = taskCriteria;
        this.taskDueDate = taskDueDate;
        this.taskPriority = taskPriority;
    }
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProjId() {
		return this.proj.getId();
	}
	public Proj getProj() {
		return proj;
	}
	public void setProj(Proj proj) {
		this.proj = proj;
	}
	public void setProjId(int projId) {
		this.proj.setId(projId);
	}
	public int getCategoryIdx() {
		return categoryIdx;
	}
	public void setCategoryIdx(int categoryIdx) {
		this.categoryIdx = categoryIdx;
	}
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	public String getTaskSummary() {
		return taskSummary;
	}
	public void setTaskSummary(String taskSummary) {
		this.taskSummary = taskSummary;
	}
	public String getTaskCriteria() {
		return taskCriteria;
	}
	public void setTaskCriteria(String taskCriteria) {
		this.taskCriteria = taskCriteria;
	}
	public String getTaskDueDate() {
		return taskDueDate;
	}
	public void setTaskDueDate(String taskDueDate) {
		this.taskDueDate = taskDueDate;
	}
	public String getTaskPriority() {
		return taskPriority;
	}
	public void setTaskPriority(String taskPriority) {
		this.taskPriority = taskPriority;
	}
	@Override
	public String toString() {
		return "Task [id=" + id + ", categoryIdx=" + categoryIdx + ", taskTitle=" + taskTitle
				+ ", taskSummary=" + taskSummary + ", taskCriteria=" + taskCriteria + ", taskDueDate=" + taskDueDate
				+ ", taskPriority=" + taskPriority + "]";
	}
	
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();

		jo.put("id", id);
		jo.put("key", id);
		jo.put("category", categoryIdx);
		jo.put("title", taskTitle);
		jo.put("summary", taskSummary);
		jo.put("acc_Crit", taskCriteria);
		jo.put("due_Date", taskDueDate);
		jo.put("priority", taskPriority);
		
		return jo;
	}
}