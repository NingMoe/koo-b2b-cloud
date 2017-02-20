package com.koolearn.cloud.exam.examcore.exam.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;


@Entity
@Table(name = "")
public class ExamResultExt implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private int id;
	/** 结果ID */
	@Column(name = "result_id")
	private int resultId;
	/** 考试ID */
	@Column(name = "exam_id")
	private int examId;
	/** 学生ID */
	@Column(name = "student_id")
	private int studentId;
	/** 班级ID */
	@Column(name = "class_id")
	private int classId;
	/** 老师ID */
	@Column(name = "teacher_id")
	private int teacherId;
	/** 学校ID */
	@Column(name = "school_id")
	private int schoolId;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getResultId() {
		return resultId;
	}

	public void setResultId(int resultId) {
		this.resultId = resultId;
	}
	
	public int getExamId() {
		return examId;
	}

	public void setExamId(int examId) {
		this.examId = examId;
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
}
