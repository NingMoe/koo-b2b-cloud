package com.koolearn.cloud.school.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import java.util.Date;


/**
学校作业课堂总数统计表
*/
@Entity
@Table(name = "tb_bi_task_course_total")
public class TbBiTaskCourseTotal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 学校id */
	@Column(name = "school_id")
	private Integer schoolId;
	/** 班级主键 */
	@Column(name = "classes_id")
	private Integer classesId;
	/** 学科id */
	@Column(name = "subject_id")
	private Integer subjectId;
	/** 课堂或者作业布置的数量 */
	@Column(name = "exam_num")
	private Integer examNum;
	/** 1: 课堂,2:作业 */
	@Column(name = "type")
	private Integer type;
	/** 创建日期 */
	@Column(name = "create_day")
	private Date createDay;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	
	public Integer getClassesId() {
		return classesId;
	}

	public void setClassesId(Integer classesId) {
		this.classesId = classesId;
	}
	
	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}
	
	public Integer getExamNum() {
		return examNum;
	}

	public void setExamNum(Integer examNum) {
		this.examNum = examNum;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public Date getCreateDay() {
		return createDay;
	}

	public void setCreateDay(Date createDay) {
		this.createDay = createDay;
	}
}
