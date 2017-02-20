//package com.koolearn.cloud.task.entity;
//
//import java.io.Serializable;
//import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
//import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
//import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
//import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
//import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
//import java.util.Date;
//
///**
// * tp_exam 实体类
// * Mon Mar 21 16:49:08 CST 2016
// * @lry
// */ 
//@Entity
//@Table(name = "tp_exam")
//public class TaskEntity implements Serializable {
//	private static final long serialVersionUID = 1L;
//	/*te_tag_object 对象类型（1：试卷，2：试题、3：考试/练习）*/
//	public static final int TAG_OBJECT_PAPER = 1;
//	/* 0作业、1考试   2课堂  */
//	public static final int EXAM_TYPE_TASK = 0;
//	/* 1.新建(有效) 2.撤回 3.删除  */
//	public static final int EXAM_STATUS_REVOKE = 2;
//	/* 1.新建(有效) 2.撤回 3.删除  */
//	public static final int EXAM_STATUS_DELETE = 3;
//	
//	
//
//	@Id(strategy = GenerationType.AUTO_INCREMENT)
//	private Integer id;
//	/** 考试/作业 名称 (默认取试卷名称)  |  type=2为课堂名称 */
//	@Column(name = "exam_name")
//	private String examName;
//	/** 默认0：试卷id */
//	@Column(name = "paper_id")
//	private Integer paperId;
//	/** 开始时间 */
//	@Column(name = "start_time")
//	private Date startTime;
//	/** 结束时间 */
//	@Column(name = "end_time")
//	private Date endTime;
//	/** 默认0： 0作业、1考试   2课堂   20课堂作业 */
//	@Column(name = "type")
//	private Integer type;
//	/** 默认1：1.新建(有效) 2.撤回 3.删除 */
//	@Column(name = "status")
//	private Integer status;
//	/** 创建老师id */
//	@Column(name = "teacher_id")
//	private Integer teacherId;
//	/** 创建时间 */
//	@Column(name = "create_time")
//	private Date createTime;
//	/** 更新时间 */
//	@Column(name = "update_time")
//	private Date updateTime;
//	/** 课堂教学进度id */
//	@Column(name = "teaching_point_id")
//	private Integer teachingPointId;
//	/** 课堂教学进度点名 */
//	@Column(name = "teaching_poit_name")
//	private String teachingPoitName;
//	
//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}
//	
//	public String getExamName() {
//		return examName;
//	}
//
//	public void setExamName(String examName) {
//		this.examName = examName;
//	}
//	
//	public Integer getPaperId() {
//		return paperId;
//	}
//
//	public void setPaperId(Integer paperId) {
//		this.paperId = paperId;
//	}
//	
//	public Date getStartTime() {
//		return startTime;
//	}
//
//	public void setStartTime(Date startTime) {
//		this.startTime = startTime;
//	}
//	
//	public Date getEndTime() {
//		return endTime;
//	}
//
//	public void setEndTime(Date endTime) {
//		this.endTime = endTime;
//	}
//	
//	public Integer getType() {
//		return type;
//	}
//
//	public void setType(Integer type) {
//		this.type = type;
//	}
//	
//	public Integer getStatus() {
//		return status;
//	}
//
//	public void setStatus(Integer status) {
//		this.status = status;
//	}
//	
//	public Integer getTeacherId() {
//		return teacherId;
//	}
//
//	public void setTeacherId(Integer teacherId) {
//		this.teacherId = teacherId;
//	}
//	
//	public Date getCreateTime() {
//		return createTime;
//	}
//
//	public void setCreateTime(Date createTime) {
//		this.createTime = createTime;
//	}
//	
//	public Date getUpdateTime() {
//		return updateTime;
//	}
//
//	public void setUpdateTime(Date updateTime) {
//		this.updateTime = updateTime;
//	}
//	
//	public Integer getTeachingPointId() {
//		return teachingPointId;
//	}
//
//	public void setTeachingPointId(Integer teachingPointId) {
//		this.teachingPointId = teachingPointId;
//	}
//	
//	public String getTeachingPoitName() {
//		return teachingPoitName;
//	}
//
//	public void setTeachingPoitName(String teachingPoitName) {
//		this.teachingPoitName = teachingPoitName;
//	}
//}
//
