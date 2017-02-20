package com.koolearn.cloud.exam.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import java.util.Date;


@Entity
@Table(name = "tp_exam_attachment_complete")
public class TpExamAttachmentComplete implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 课堂附件关联id */
	@Column(name = "exam_attachment_id")
	private Integer examAttachmentId;
	/** 查看附件的学生id */
	@Column(name = "student_id")
	private Integer studentId;
	/**  查看时间 */
	@Column(name = "create_time")
	private Date createTime;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getExamAttachmentId() {
		return examAttachmentId;
	}

	public void setExamAttachmentId(Integer examAttachmentId) {
		this.examAttachmentId = examAttachmentId;
	}
	
	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
