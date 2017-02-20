package com.koolearn.cloud.composition.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;


@Entity
@Table(name = "composition_item_score")
public class CompositionItemScore implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 订单ID */
	@Column(name = "order_id")
	private Integer orderId;
	/** 指标ID */
	@Column(name = "item_id")
	private Integer itemId;
	/** 指标得分 */
	@Column(name = "item_score")
	private Integer itemScore;
	/** 指标描述 */
	@Column(name = "item_desc")
	private String itemDesc;
	/** 指标类型 */
	@Column(name = "type")
	private Integer type;
	/** 作文ID */
	@Column(name = "composition_id")
	private Integer compositionId;
	/** 评分规则集ID */
	@Column(name = "rule_id")
	private Integer ruleId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	public Integer getItemScore() {
		return itemScore;
	}

	public void setItemScore(Integer itemScore) {
		this.itemScore = itemScore;
	}
	
	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getCompositionId() {
		return compositionId;
	}

	public void setCompositionId(Integer compositionId) {
		this.compositionId = compositionId;
	}
	
	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
}
