package com.koolearn.cloud.exam.examcore.exam.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
@Entity
@Table(name = "tp_error_note")
public class TpErrorNote implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int STATUS_VALID = 1;//'状态 1.正常 0移除（删除）'
	
	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 学生id */
	@Column(name = "student_id")
	private Integer studentId;
	/** 题目编码 */
	@Column(name = "question_code")
	private String questionCode;
	/** 题目编码 */
	@Column(name = "question_id")
	private Integer questionId;
//	/** 考试结果id */
//	@Column(name = "result_id")
//	private Integer resultId;
//	/** 考试id */
//	@Column(name = "exam_id")
//	private Integer examId;
//	/** 0 作业    1 课堂作业 */
//	@Column(name = "type")
//	private Integer type;
	
	/** 标签集合 从题上取得标签 */
	@Column(name = "tag_full_path")
	private String tagFullPath;
	
	/** 状态 1.正常 0移除（删除） */
	@Column(name = "status")
	private Integer status;
	
	/** 错题次数 */
	@Column(name = "times")
	private Integer times;
	
	public String getTagFullPath() {
		return tagFullPath;
	}

	public void setTagFullPath(String tagFullPath) {
		this.tagFullPath = tagFullPath;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
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
	
	public String getQuestionCode() {
		return questionCode;
	}

	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}
	
//	public Integer getResultId() {
//		return resultId;
//	}
//
//	public void setResultId(Integer resultId) {
//		this.resultId = resultId;
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
//	public Integer getType() {
//		return type;
//	}
//
//	public void setType(Integer type) {
//		this.type = type;
//	}
}
