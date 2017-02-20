package com.koolearn.cloud.exam.examcore.question.dto;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class BaseDto implements Serializable
{
	protected QuestionDto questionDto;

	private static final long serialVersionUID = -803905429670781192L;

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	/**
	 * 获取题目ID
	 * @return
	 * @author DuHongLin
	 */
	protected int takeQid()
	{
		return this.questionDto.getQuestion().getId();
	}
	
	/**
	 * 获取 【类型ID_题目ID_】 前缀
	 * @return
	 * @author DuHongLin
	 */
	protected String takePre()
	{
		return this.questionDto.getQuestion().getQuestionTypeId() + "_" + this.takeQid() + "_";
	}
	
	/**
	 * 用户答案转换
	 */
	public QuestionViewDto convertUserAnswer(IExamQuestionDto dto,String userAnswer){
		QuestionViewDto questionViewDto = new QuestionViewDto();
		
		return questionViewDto;
	}
}
