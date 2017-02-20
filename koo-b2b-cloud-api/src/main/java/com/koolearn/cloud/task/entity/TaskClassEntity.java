//package com.koolearn.cloud.task.entity ;
//import java.io.Serializable;
//import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
//import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
//import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
//import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
//import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
//import java.util.Date;
//import java.util.Date;
//
//
//@Entity
//@Table(name = "tp_exam_classes")
//public class TaskClassEntity implements Serializable {
//	private static final long serialVersionUID = 1L;
//	public static final int STATUS_VALID = 0;
//
//	@Id(strategy = GenerationType.AUTO_INCREMENT)
//	private Integer id;
//	/** 考试id  （考试表type=2  表示课堂id） */
//	@Column(name = "exam_id")
//	private Integer examId;
//	/** 默认0：试卷id(冗余字段) */
//	@Column(name = "paper_id")
//	private Integer paperId;
//	/** 班级或小组id（班级type=3） */
//	@Column(name = "classes_id")
//	private Integer classesId;
//	/** 创建老师id */
//	@Column(name = "teacher_id")
//	private Integer teacherId;
//	/** 实体状态：默认0 正常 */
//	@Column(name = "status")
//	private Integer status;
//	/** 创建时间 */
//	@Column(name = "create_time")
//	private Date createTime;
//	/** 更新时间 */
//	@Column(name = "update_time")
//	private Date updateTime;
//	
//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}
//	
//	public Integer getExamId() {
//		return examId;
//	}
//
//	public void setExamId(Integer examId) {
//		this.examId = examId;
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
//	public Integer getClassesId() {
//		return classesId;
//	}
//
//	public void setClassesId(Integer classesId) {
//		this.classesId = classesId;
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
//	public Integer getStatus() {
//		return status;
//	}
//
//	public void setStatus(Integer status) {
//		this.status = status;
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
//}
