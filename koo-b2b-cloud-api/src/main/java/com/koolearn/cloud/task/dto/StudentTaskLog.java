//package com.koolearn.cloud.task.dto ;
//import java.io.Serializable;
//import java.util.Date;
//
//import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
//import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
//import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
//import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
//import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
//
//
//@Entity
//@Table(name = "student_task_log")
//public class StudentTaskLog implements Serializable {
//	private static final long serialVersionUID = 1L;
//
//	@Id(strategy = GenerationType.AUTO_INCREMENT)
//	private int id;
//	/** 学科标签id */
//	@Column(name = "tag_id")
//	private int tagId;
//	/** 班级id */
//	@Column(name = "classes_id")
//	private int classesId;
//	/** 作业id */
//	@Column(name = "task_id")
//	private int taskId;
//	@Column(name = "create_time")
//	private Date createTime;
//	
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//	
//	public int getTagId() {
//		return tagId;
//	}
//
//	public void setTagId(int tagId) {
//		this.tagId = tagId;
//	}
//	
//	public int getClassesId() {
//		return classesId;
//	}
//
//	public void setClassesId(int classesId) {
//		this.classesId = classesId;
//	}
//	
//	public int getTaskId() {
//		return taskId;
//	}
//
//	public void setTaskId(int taskId) {
//		this.taskId = taskId;
//	}
//	
//	public Date getCreateTime() {
//		return createTime;
//	}
//
//	public void setCreateTime(Date createTime) {
//		this.createTime = createTime;
//	}
//}
