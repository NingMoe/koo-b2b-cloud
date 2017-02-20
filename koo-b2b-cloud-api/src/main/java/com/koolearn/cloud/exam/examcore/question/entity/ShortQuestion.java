package com.koolearn.cloud.exam.examcore.question.entity;

import java.util.List;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Transient;
import com.koolearn.cloud.exam.examcore.question.dto.ShortQuestionDto;

@Entity
@Table(name = "te_shortquestion")
public class ShortQuestion  extends BaseEntity{
	
//	private int  id;
	// 题目Id
	 @Column(name = "question_id")
	private int  questionId;
	//答题提示
	private String  answer;
	//题干
	private String  topic;
	//批改类型( ''1:人工批改/自评分'')
	private int  marktype;
	//参考答案
	private String  answerreference;
	//评分标准/关键字
	private String  scorestandad;
	//是否含有工具栏(''1:有.0：无'')
	private int  istoolbar;
	//答题框高度
	private int  boxheight;
	//关键词list
	@Transient
	private List<String> keyWordList;
	 
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
	public int getMarktype() {
		return marktype;
	}
	public void setMarktype(int marktype) {
		this.marktype = marktype;
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
	public List<String> getKeyWordList() {
		return keyWordList;
	}
	public void setKeyWordList(List<String> keyWordList) {
		this.keyWordList = keyWordList;
	}
}
