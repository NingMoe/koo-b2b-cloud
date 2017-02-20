package com.koolearn.cloud.exam.examcore.question.entity;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;

@Entity
@Table(name = "te_choicequestion")
public class ChoiceQuestion extends BaseEntity {
	// Fields

	@Column(name="question_id")
	private Integer questionId;
	
	private String answer;

	private String topic;

	@Column(name = "china_english")
	private Integer chinaEnglish;

	@Column(name = "compose_type")
	private Integer composeType;
	//目前作为方框题 要插入的句子,以后其他类型选择题也可以作为特征
	private String feature;


	// Constructors

	/** default constructor */
	public ChoiceQuestion() {
	}




	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getTopic() {
		return this.topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Integer getChinaEnglish() {
		return this.chinaEnglish;
	}

	public void setChinaEnglish(Integer chinaEnglish) {
		this.chinaEnglish = chinaEnglish;
	}

	public Integer getComposeType() {
		return this.composeType;
	}

	public void setComposeType(Integer composeType) {
		this.composeType = composeType;
	}




	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}




	public Integer getQuestionId() {
		return questionId;
	}




	public String getFeature() {
		return feature;
	}




	public void setFeature(String feature) {
		this.feature = feature;
	}


}