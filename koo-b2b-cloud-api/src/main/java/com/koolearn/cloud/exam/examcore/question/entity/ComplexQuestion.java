package com.koolearn.cloud.exam.examcore.question.entity;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;

/**
 * 完型填空题/阅读理解 实体类
 * TODO
 * @author tangshuren
 * @date 2012-10-29
 *
 */
@Table(name="te_complexquestion")
@Entity
public class ComplexQuestion extends BaseEntity{
	
	@Column(name="question_id")
	private Integer questionId;
	
	@Column(name="sub_question_count")
	private Integer subQuestionCount;

	private String topic;
	
	private Integer metewand;
	
	private Integer timing;	// 1：不计时；2：正计时；3：倒计时
	
	private Integer countdowntime;//倒计时 时间  单位 S秒
	
	private String translate;//原文翻译
	

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Integer getSubQuestionCount() {
		return subQuestionCount;
	}

	public void setSubQuestionCount(Integer subQuestionCount) {
		this.subQuestionCount = subQuestionCount;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Integer getMetewand() {
		return metewand;
	}

	public void setMetewand(Integer metewand) {
		this.metewand = metewand;
	}
	
	public Integer getTiming() {
		if(timing==null){
			timing=0;
		}
		return timing;
	}

	public void setTiming(Integer timing) {
		this.timing = timing;
	}

	public Integer getCountdowntime() {
		if(countdowntime==null){
			countdowntime=0;
		}
		return countdowntime;
	}

	public void setCountdowntime(Integer countdowntime) {
		this.countdowntime = countdowntime;
	}

	public String getTranslate() {
		return translate;
	}

	public void setTranslate(String translate) {
		this.translate = translate;
	}
}