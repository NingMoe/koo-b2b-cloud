package com.koolearn.cloud.exam.examcore.question.entity;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;

@Entity
@Table(name = "te_whritequestion")
public class WhriteQuestion  extends BaseEntity{
	 /**
	 * TODO
	 */
	private static final long serialVersionUID = 1L;
//	private int  id;
	 // 题目Id
	 @Column(name = "question_id")
	 private int  questionId;
	 
	 //答题提示
	 private String  answer;
	 
	 //题干
	 private String  topic;
	 
	 //参考答案
	 private String  answerreference;
	 
	 //评分标准
	 private String  scorestandad;
	 
	 //是否含有工具栏(''1:有.0：无'')
	 private int  istoolbar;
	 
	 //答题框高度
	 private int  boxheight;
	 
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getIstoolbar() {
		return istoolbar;
	}
	public void setIstoolbar(int istoolbar) {
		this.istoolbar = istoolbar;
	}
	public int getBoxheight() {
		return boxheight;
	}
	public void setBoxheight(int boxheight) {
		this.boxheight = boxheight;
	}

}
