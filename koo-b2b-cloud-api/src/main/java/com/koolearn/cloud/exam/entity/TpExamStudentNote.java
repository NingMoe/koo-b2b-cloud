package com.koolearn.cloud.exam.entity;
import java.io.Serializable;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.util.Date;


@Entity
@Table(name = "tp_exam_student_note")
public class TpExamStudentNote implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 学生id */
	@Column(name = "student_id")
	private Integer studentId;
	/**
	 * 课堂附件关联id
	 */
	@Column(name = "exam_attachment_id")
	private Integer examAttachmentId;
	/** 笔记内容 */
	@Column(name = "comment")
	private String comment;
	/** 创建时间 */
	@Column(name = "create_time")
	private Date createTime;

	@Transient
	private String createTimeStr;

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public Integer getExamAttachmentId() {
		return examAttachmentId;
	}

	public void setExamAttachmentId(Integer examAttachmentId) {
		this.examAttachmentId = examAttachmentId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
