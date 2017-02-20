package com.koolearn.cloud.exam.examcore.exam.entity;

import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;

@Entity
@Table(name = "")
public class SearchStructureResult implements Serializable{
	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private int id;
	/** 考试ID */
	@Column(name = "exam_id")
	private int examId;
	/** 考试ID */
	@Column(name = "paper_id")
	private int paperId;
	/** 结构ID */
	@Column(name = "structure_id")
	private int structureId;
	/** 学生ID */
	@Column(name = "student_id")
	private int studentId;
	/** 班级ID */
	@Column(name = "class_id")
	private int classId;
	/** 得分 */
	@Column(name = "score")
	private double score;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getExamId() {
		return examId;
	}
	public void setExamId(int examId) {
		this.examId = examId;
	}
	public int getPaperId() {
		return paperId;
	}
	public void setPaperId(int paperId) {
		this.paperId = paperId;
	}
	public int getStructureId() {
		return structureId;
	}
	public void setStructureId(int structureId) {
		this.structureId = structureId;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
}
