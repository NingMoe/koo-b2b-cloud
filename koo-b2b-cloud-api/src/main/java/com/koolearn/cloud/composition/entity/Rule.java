package com.koolearn.cloud.composition.entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;


@Entity
@Table(name = "rule")
public class Rule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 评分名称 */
	@Column(name = "rule_name")
	private String ruleName;
	/** 适用地区 */
	@Column(name = "rule_area")
	private String ruleArea;
	/** 初中 2 高中 1 */
	@Column(name = "school_lev")
	private Integer schoolLev;
	/** 适用文体 1议论 2记叙 */
	@Column(name = "rule_type")
	private Integer ruleType;
	/** 总分 */
	@Column(name = "score_sum")
	private Integer scoreSum;
	/** 是否为显示分数模式 1 是 0 不是 */
	@Column(name = "no_score")
	private Integer noScore;
	/** 如果是不显示分数模式 则匹配一个分数的规则 */
	@Column(name = "rule_id")
	private Integer ruleId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	
	public String getRuleArea() {
		return ruleArea;
	}

	public void setRuleArea(String ruleArea) {
		this.ruleArea = ruleArea;
	}
	
	public Integer getSchoolLev() {
		return schoolLev;
	}

	public void setSchoolLev(Integer schoolLev) {
		this.schoolLev = schoolLev;
	}
	
	public Integer getRuleType() {
		return ruleType;
	}

	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}
	
	public Integer getScoreSum() {
		return scoreSum;
	}

	public void setScoreSum(Integer scoreSum) {
		this.scoreSum = scoreSum;
	}
	
	public Integer getNoScore() {
		return noScore;
	}

	public void setNoScore(Integer noScore) {
		this.noScore = noScore;
	}
	
	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
}
