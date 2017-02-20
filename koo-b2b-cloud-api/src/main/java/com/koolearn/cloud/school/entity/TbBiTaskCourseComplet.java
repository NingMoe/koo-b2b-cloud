package com.koolearn.cloud.school.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import java.util.Date;


/**
作业课堂完成率统计表
*/
@Entity
@Table(name = "tb_bi_task_course_complet")
public class TbBiTaskCourseComplet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 学校id */
	@Column(name = "schoo_id")
	private Integer schooId;
	/** 班级id */
	@Column(name = "classes_id")
	private Integer classesId;
	/** 学科id */
	@Column(name = "subject_id")
	private Integer subjectId;
	/** 课堂作业完成率 */
	@Column(name = "complete_rate")
	private Double completeRate;
	/** 1: 作业，2：课堂 */
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
	
	public Integer getSchooId() {
		return schooId;
	}

	public void setSchooId(Integer schooId) {
		this.schooId = schooId;
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
	
	public Double getCompleteRate() {
		return completeRate;
	}

	public void setCompleteRate(Double completeRate) {
		this.completeRate = completeRate;
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
