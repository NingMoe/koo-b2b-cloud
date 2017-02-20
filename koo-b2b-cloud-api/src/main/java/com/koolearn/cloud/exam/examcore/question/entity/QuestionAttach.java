package com.koolearn.cloud.exam.examcore.question.entity;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;

@Table(name="te_questionattach")
@Entity
public class QuestionAttach extends BaseEntity{

	@Column(name="question_id")
	private int questionId;
	
	/**
	 * 材料顺序
	 */
	@Column(name="sequence_id")
	private int sequenceId;
	
	/**
	 * 材料类型 默认是试题的材料
	 */
	@Column(name="attach_type")
	private int attachType=0;

	private String content;

	

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public int getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getAttachType() {
		return attachType;
	}

	public void setAttachType(int attachType) {
		this.attachType = attachType;
	}
	
	
}
