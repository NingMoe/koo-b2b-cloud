package com.koolearn.cloud.exam.examcore.question.entity;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;

/**
 * 改错题entity
 * @author tangshuren
 * @date 2013-9-5
 *
 */
@Table(name="te_correctionquestion")
@Entity
public class CorrectionQuestion extends BaseEntity{

	private static final long serialVersionUID = 5466798225382704727L;

	@Column(name="question_id")
	private int questionId;

	@Column(name="complex_id")
	private int complexId;
	
	@Column(name="clause")
	private String clause;
	
	@Column(name="clause_answer")
	private String clauseAnswer;
	
	@Column(name="order_num")
	private int orderNum;
	
	@Column(name="topic")
	private String topic;

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getClause() {
		return clause;
	}

	public void setClause(String clause) {
		this.clause = clause;
	}

	public String getClauseAnswer() {
		return clauseAnswer;
	}

	public void setClauseAnswer(String clauseAnswer) {
		this.clauseAnswer = clauseAnswer;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getComplexId() {
		return complexId;
	}

	public void setComplexId(int complexId) {
		this.complexId = complexId;
	}
	
}
