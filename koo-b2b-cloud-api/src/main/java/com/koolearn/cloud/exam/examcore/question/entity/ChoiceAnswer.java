package com.koolearn.cloud.exam.examcore.question.entity;

import org.springframework.web.util.HtmlUtils;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;


/**
 * 选择题答案
 * @author yangzhenye
 */
@Entity
@Table(name = "te_choiceanswer")
public class ChoiceAnswer extends BaseEntity implements Comparable {

	@Column(name = "sequence_id")
	private Integer sequenceId;

	private String description;

	private Integer isright;
	
	@Column(name = "choice_id")
	private Integer choiceId;
	
	private Integer orderby;
	
	//add for linshi 导入for wujianjun
	private String vin;


	// Constructors

	/** default constructor */
	public ChoiceAnswer() {
	}

	public Integer getSequenceId() {
		return this.sequenceId;
	}

	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

	public String getDescription() {
		if(this.description!=null){
		return HtmlUtils.htmlUnescape(this.description);
		}else{
		return this.description;
		}
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIsright() {
		return this.isright;
	}

	public void setIsright(Integer isright) {
		this.isright = isright;
	}

	public void setChoiceId(Integer choiceId) {
		this.choiceId = choiceId;
	}

	public Integer getChoiceId() {
		return choiceId;
	}

	public Integer getOrderby() {
		return orderby;
	}

	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}

	public int compareTo(Object o) {
		ChoiceAnswer anObj = (ChoiceAnswer) o;
		return (this.orderby == null ? 0 : this.orderby)-(anObj.getOrderby() == null ? 0 : anObj.getOrderby());
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	

}