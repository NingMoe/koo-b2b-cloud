package com.koolearn.cloud.composition.entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;


@Entity
@Table(name = "rule_item")
public class RuleItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 评分指标名称 */
	@Column(name = "item_name")
	private String itemName;
	/** 评分指标描述 */
	@Column(name = "describ")
	private String describ;
	/** 评分指标级别 （甲1  乙 2丙3丁4）从最好的到最坏的 一次升序 */
	@Column(name = "lev")
	private Integer lev;
	/** 评分指标等级对应的文字描述 甲乙丙丁 */
	@Column(name = "lev_desc")
	private String levDesc;
	/** 该指标等级对应的得分 */
	@Column(name = "score")
	private Integer score;
	/** 普通指标 1 加分项 2 加分项 3 */
	@Column(name = "item_type")
	private Integer itemType;
	/** 指标归属的规则集 */
	@Column(name = "rule_id")
	private Integer ruleId;
	/** 标识指标的CODE */
	@Column(name = "item_code")
	private String itemCode;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public String getDescrib() {
		return describ;
	}

	public void setDescrib(String describ) {
		this.describ = describ;
	}
	
	public Integer getLev() {
		return lev;
	}

	public void setLev(Integer lev) {
		this.lev = lev;
	}
	
	public String getLevDesc() {
		return levDesc;
	}

	public void setLevDesc(String levDesc) {
		this.levDesc = levDesc;
	}
	
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	
	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
	
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
}
