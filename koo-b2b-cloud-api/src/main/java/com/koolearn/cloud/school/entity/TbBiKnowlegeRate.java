package com.koolearn.cloud.school.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import java.util.Date;


/**
知识点得分率统计表
*/
@Entity
@Table(name = "tb_bi_knowlege_rate")
public class TbBiKnowlegeRate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 学校id */
	@Column(name = "school_id")
	private Integer schoolId;
	/** 班级id */
	@Column(name = "classes_id")
	private Integer classesId;
	/** 学科id */
	@Column(name = "subject_id")
	private Integer subjectId;
	/** 知识点得分情况集合（知识点1:12,知识点2:23 , 知识点3:34 ） */
	@Column(name = "points_score")
	private String pointsScore;
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
	
	public String getPointsScore() {
		return pointsScore;
	}

	public void setPointsScore(String pointsScore) {
		this.pointsScore = pointsScore;
	}
	
	public Date getCreateDay() {
		return createDay;
	}

	public void setCreateDay(Date createDay) {
		this.createDay = createDay;
	}
}
