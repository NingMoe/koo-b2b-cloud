package com.koolearn.cloud.exam.examcore.paper.dto;

import java.math.BigDecimal;

import com.koolearn.exam.base.dto.BaseDto;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;

/**
 * 试卷前台显示的节点
 * TODO
 * @author miaoyoumeng
 * @date Oct 24, 2012
 *
 */
public class PaperItemDto extends BaseDto{

	private static final long serialVersionUID = -8771838262295195566L;

	private int id;//id
	
	private String name;//名称
	
	private int type;// 节点类型

	private int outerId;// 对应的外部节点id

	private String orderIndex;// 顺序

	private int questionType;// 题目类型

	private int parentNodeId;// 父节点id

	private String userAnswer; // 用户答案

	private int isCorrect=-1; // 答案是否正确,默认未答

	private float score; // 题目分数

	private int version; // 版本号

	private int orderNo;

	private float userScore = 0; // 答题得分
	
	private String errorTotal; //错题数/总题数（字符串类型）
	
	private int questionNo;
	
	private int moduleSum; //所在模块题目数
	
	private int sequence;
	
	private int usedTime; //答题所用时间
	
	private String clickIndex; //点击过的位置索引（目前仅用于限时选词）
	
	private int mark = 0;  //用户标记 0：未标记 ，1：标记
	
	public int getParentNodeId() {
		return parentNodeId;
	}
	
	public void setParentNodeId(int parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	public int getQuestionType() {
		return questionType;
	}

	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getOuterId() {
		return outerId;
	}

	public void setOuterId(int outerId) {
		this.outerId = outerId;
	}

	public String getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(String orderIndex) {
		this.orderIndex = orderIndex;
	}


	public String getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}

	public int getIsCorrect() {
		return isCorrect;
	}

	public void setIsCorrect(int isCorrect) {
		this.isCorrect = isCorrect;
	}

	public float getScore() {
		return score;
	}
	public String getScoreStr(){
		String s=Float.toString(score);
		if(s.split("\\.")[1].length()>2){
			 BigDecimal a=new BigDecimal(score);
			 s=a.setScale(2, 2).toString();
		}
		return s;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public float getUserScore() {
		return userScore;
	}

	public void setUserScore(float userScore) {
		this.userScore = userScore;
	}

	public String getErrorTotal() {
		return errorTotal;
	}

	public void setErrorTotal(String errorTotal) {
		this.errorTotal = errorTotal;
	}

	public int getQuestionNo() {
		return questionNo;
	}

	public void setQuestionNo(int questionNo) {
		this.questionNo = questionNo;
	}

	public int getModuleSum() {
		return moduleSum;
	}

	public void setModuleSum(int moduleSum) {
		this.moduleSum = moduleSum;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(int usedTime) {
		this.usedTime = usedTime;
	}

	public String getClickIndex() {
		return clickIndex;
	}

	public void setClickIndex(String clickIndex) {
		this.clickIndex = clickIndex;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}
	
}
