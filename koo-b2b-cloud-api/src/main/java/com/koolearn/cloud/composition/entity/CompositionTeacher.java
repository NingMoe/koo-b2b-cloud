package com.koolearn.cloud.composition.entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "composition_teacher")
public class CompositionTeacher implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 新东方批阅教师名 */
	@Column(name = "teacher_name")
	private String teacherName;
	/** 新东方批阅教师介绍 */
	@Column(name = "teacher_desc")
	private String teacherDesc;
	/** 批阅一次价格 */
	@Column(name = "price")
	private BigDecimal price;
	/** 教师状态 */
	@Column(name = "status")
	private Integer status;
	/** 教师批阅的学段 */
	@Column(name = "school_lev")
	private Integer schoolLev;
	/** 教师头像 */
	@Column(name = "head_url")
	private String headUrl;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	
	public String getTeacherDesc() {
		return teacherDesc;
	}

	public void setTeacherDesc(String teacherDesc) {
		this.teacherDesc = teacherDesc;
	}
	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getSchoolLev() {
		return schoolLev;
	}

	public void setSchoolLev(Integer schoolLev) {
		this.schoolLev = schoolLev;
	}
	
	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
}
