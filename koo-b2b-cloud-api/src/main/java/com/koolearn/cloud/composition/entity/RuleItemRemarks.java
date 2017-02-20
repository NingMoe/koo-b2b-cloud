package com.koolearn.cloud.composition.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;


@Entity
@Table(name = "rule_item_remarks")
public class RuleItemRemarks implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 得分指标项ID */
	@Column(name = "item_id")
	private Integer itemId;
	/** 评语 */
	@Column(name = "remark")
	private String remark;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
