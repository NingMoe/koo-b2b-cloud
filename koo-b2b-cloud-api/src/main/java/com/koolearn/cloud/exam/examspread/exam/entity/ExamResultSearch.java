package com.koolearn.cloud.exam.examspread.exam.entity;

import java.io.Serializable;
import java.util.Date;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
@Entity
@Table(name = "te_exam_result_search")
public class ExamResultSearch  implements Serializable {
	/**
	 * 考试结构结构id
	 */
	private int id;
	/**
	 * 学生
	 */
	@Column(name="student_id")
	private int studentId;
	/**
	 * 考试
	 */
	@Column(name="exam_id")
	private int examId;
	/**
	 * 班级
	 */
	@Column(name="class_id")
	private int classId;
	/**
	 * 老师
	 */
	@Column(name="teacher_id")
	private int teacherId;
	/**
	 * 学校
	 */
	@Column(name="school_id")
	private int schoolId;
	/**
	 * 批改状态
	 */
	@Column(name="pg_status")
	private int pgStatus;
	/**
	 * 题目
	 */
	@Column(name="question_id")
	private int questionId;
	/**
	 * 题目内部类型
	 */
	@Column(name="question_type")
	private int questionType;
	/**
	 * 考试结构id
	 */
	@Column(name="result_id")
	private int resultId;
	/**
	 * 题目标签类型
	 */
	@Column(name="question_tag_type")
	private int questionTagType;
	/**
	 * 题目父Id
	 */
	@Column(name="te_id")
	private int teId;
	/**
	 * 老师批复的时间
	 */
	@Column(name="rtime")
	private Date rtime;
	
	public Date getRtime()
	{
		return rtime;
	}
	public void setRtime(Date rtime)
	{
		this.rtime = rtime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public int getExamId() {
		return examId;
	}
	public void setExamId(int examId) {
		this.examId = examId;
	}
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public int getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}
	public int getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(int schoolId) {
		this.schoolId = schoolId;
	}
	public int getPgStatus() {
		return pgStatus;
	}
	public void setPgStatus(int pgStatus) {
		this.pgStatus = pgStatus;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public int getQuestionType() {
		return questionType;
	}
	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}
	public int getResultId() {
		return resultId;
	}
	public void setResultId(int resultId) {
		this.resultId = resultId;
	}
	public int getQuestionTagType() {
		return questionTagType;
	}
	public void setQuestionTagType(int questionTagType) {
		this.questionTagType = questionTagType;
	}
	public int getTeId() {
		return teId;
	}
	public void setTeId(int teId) {
		this.teId = teId;
	}
}
