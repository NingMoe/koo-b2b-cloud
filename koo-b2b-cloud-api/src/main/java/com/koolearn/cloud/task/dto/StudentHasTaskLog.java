package com.koolearn.cloud.task.dto ;
import java.io.Serializable;
import java.util.Date;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;


@Entity
@Table(name = "student_has_task_log")
public class StudentHasTaskLog implements Serializable {
	private static final long serialVersionUID = 1L;
	//1.有新作业未查看  2.已查看所有新作业
	public static final int STATUS_SEE_NO = 1;
	public static final int STATUS_SEE_YES = 2;
	public static final int STATUS_VALID = 1;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	@Column(name = "subject_id")
	private Integer subjectId;
	@Column(name = "student_id")
	private Integer studentId;
	/** 1.有新作业未查看  2.已查看所有新作业 */
	@Column(name = "status")
	private Integer status;
	/** 作业id */
	@Column(name = "exam_id")
	private Integer examId;
	@Column(name = "create_time")
	private Date createTime;
	
	public Integer getExamId() {
		return examId;
	}

	public void setExamId(Integer examId) {
		this.examId = examId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
