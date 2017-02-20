package com.koolearn.cloud.exam.examcore.question.entity;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;

@Entity
@Table(name = "te_spokenequestion")
public class SpokenQuestion extends BaseEntity {
	/**
	 * TODO
	 */
	private static final long serialVersionUID = 1L;
	// 题目Id
	@Column(name = "question_id")
	private int questionId;

	// 答题提示
	private String answer;

	// 题干
	private String topic;

	// 参考答案
	private String answerreference;

	// 评分标准
	private String scorestandad;

	// 录音时间
	private float recordtime;

	// 中文
	// DuHongLin 2014-06-20 10:20
	private String chineseContent;
	
	// 音频解析播放顺序
	// DuHongLin 2014-06-20 10:21
	private String radioSort;

	// 录音时间（以秒计算）
	public float getRecordtimeSecond() {
		return recordtime * 60;
	}
	public String getChineseContent() {
		return chineseContent;
	}

	public void setChineseContent(String chineseContent) {
		this.chineseContent = chineseContent;
	}

	public String getRadioSort() {
		return radioSort;
	}

	public void setRadioSort(String radioSort) {
		this.radioSort = radioSort;
	}

	public float getRecordtime() {
		return recordtime;
	}

	public void setRecordtime(float recordtime) {
		this.recordtime = recordtime;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getAnswerreference() {
		return answerreference;
	}

	public void setAnswerreference(String answerreference) {
		this.answerreference = answerreference;
	}

	public String getScorestandad() {
		return scorestandad;
	}

	public void setScorestandad(String scorestandad) {
		this.scorestandad = scorestandad;
	}

}
