package com.koolearn.cloud.task.dto ;
import java.io.Serializable;
import java.util.Date;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;


public class QuestionErrUser implements Serializable {
	private static final long serialVersionUID = 1L;
	public static String QuestionErrUser_mapAvg="mapAvg";
	public static String QuestionErrUser_mapErrUser="mapErrUser";
	public static String QuestionErrUser_mapNoAnswerUser="mapNoAnswerUser";
	/** 题目id */
	private Integer questionId;
	/** 题目code */
	private String questionCode;
	/** 多个用户名 */
	private String userName;
	/** 查询到的用户数 */
	private String userNum;
	/** 未做答人 */
	private String noAnswerUserName;
	/** 未做答人数 */
	private String noAnswerUserNum;
	/** 未做答人 */
	private String errUserName;
	/** 未做答人数 */
	private String errUserNum;
	/** 平均得分率 */
	private String avgRate;
	/** 平均得分 */
	private String avgScore;
	/** 答对题目数 */
	private String answerRightNum;
	/** 总题目数 */
	private String answerAllNum;
	/** 得分率 */
	private String scoreRate;
	/** 父题目id */
	private int teId;
	
	public String getNoAnswerUserName() {
		return noAnswerUserName;
	}
	public void setNoAnswerUserName(String noAnswerUserName) {
		this.noAnswerUserName = noAnswerUserName;
	}
	public String getNoAnswerUserNum() {
		return noAnswerUserNum;
	}
	public void setNoAnswerUserNum(String noAnswerUserNum) {
		this.noAnswerUserNum = noAnswerUserNum;
	}
	public String getErrUserName() {
		return errUserName;
	}
	public void setErrUserName(String errUserName) {
		this.errUserName = errUserName;
	}
	public String getErrUserNum() {
		return errUserNum;
	}
	public void setErrUserNum(String errUserNum) {
		this.errUserNum = errUserNum;
	}
	public int getTeId() {
		return teId;
	}
	public void setTeId(int teId) {
		this.teId = teId;
	}
	public String getScoreRate() {
		return scoreRate;
	}
	public void setScoreRate(String scoreRate) {
		this.scoreRate = scoreRate;
	}
	public String getAnswerRightNum() {
		return answerRightNum;
	}
	public void setAnswerRightNum(String answerRightNum) {
		this.answerRightNum = answerRightNum;
	}
	public String getAnswerAllNum() {
		return answerAllNum;
	}
	public void setAnswerAllNum(String answerAllNum) {
		this.answerAllNum = answerAllNum;
	}
	public String getAvgRate() {
		return avgRate;
	}
	public void setAvgRate(String avgRate) {
		this.avgRate = avgRate;
	}
	public String getAvgScore() {
		return avgScore;
	}
	public void setAvgScore(String avgScore) {
		this.avgScore = avgScore;
	}
	public Integer getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}
	public String getQuestionCode() {
		return questionCode;
	}
	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserNum() {
		return userNum;
	}
	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}
}
