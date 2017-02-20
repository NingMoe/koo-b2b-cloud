package com.koolearn.cloud.composition.entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "composition_reprot")
public class CompositionReprot implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 作文ID */
	@Column(name = "composition_id")
	private Integer compositionId;
	/** 订单ID */
	@Column(name = "order_id")
	private Integer orderId;
	/** 批改总得分 */
	@Column(name = "score")
	private String score;
	/** 本批改使用的规则集ID */
	@Column(name = "rule_id")
	private Integer ruleId;
	/** 文本报告 json格式 {'审题':'很好','思路':'顺畅'} */
	@Column(name = "report")
	private String report;
	/** 报告生成时间 */
	@Column(name = "create_time")
	private Date createTime;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getCompositionId() {
		return compositionId;
	}

	public void setCompositionId(Integer compositionId) {
		this.compositionId = compositionId;
	}
	
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
	
	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
	
	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
