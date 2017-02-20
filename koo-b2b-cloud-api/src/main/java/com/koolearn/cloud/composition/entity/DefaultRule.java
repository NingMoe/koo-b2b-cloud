package com.koolearn.cloud.composition.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;


@Entity
@Table(name = "default_rule")
public class DefaultRule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 教师的ID */
	@Column(name = "user_id")
	private Integer userId;
	/** 议论文 1 叙述文 2 */
	@Column(name = "type")
	private Integer type;
	/** 学段 高中1 初中 2 */
	@Column(name = "school_lev")
	private Integer schoolLev;
	/** 适用地区 */
	@Column(name = "area")
	private String area;
	/** 默认评分规则ID */
	@Column(name = "rule_id")
	private Integer ruleId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getSchoolLev() {
		return schoolLev;
	}

	public void setSchoolLev(Integer schoolLev) {
		this.schoolLev = schoolLev;
	}
	
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
}
