package com.koolearn.cloud.exam.examcore.question.dto;

import java.util.List;

import com.koolearn.cloud.task.dto.QuestionErrUser;


/**
 * 考试获取试题
 * @author wangpeng
 * @date Oct 26, 2012
 * 技术教辅社区组@koolearn.com
 */
public interface IExamQuestionDto {
	/**
	 * 获取试题DTO
	 * @return
	 */
	QuestionDto getQuestionDto();
	/**
	 * 获取试题类型
	 * @return
	 */
	int getQuestionType();
	/**
	 * 是否为主观题 
	 * @return 
	 */
	boolean isSubjectived();
	/**
	 * 获取题干30字以内
	 * @return
	 */
	String getShortTopic();
	/**
	 * 判断是否含有子题
	 * @return
	 */
	boolean haveSubQuestion();
	/**
	 * 返回子题
	 * @return
	 */
	List<IExamQuestionDto> getSubQuestions();
	/**
	 * 清空子题
	 * @return
	 */
	void setSubQuestions(List<IExamQuestionDto> list);
	/**
	 * 返回答案
	 * @return
	 */
	String getRightAnswer();
	
	void setQuestionErrUser(QuestionErrUser questionErrUser);
	
}
