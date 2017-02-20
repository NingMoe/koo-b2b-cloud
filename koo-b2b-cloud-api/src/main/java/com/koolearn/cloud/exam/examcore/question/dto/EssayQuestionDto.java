package com.koolearn.cloud.exam.examcore.question.dto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.mrbean.BeanUtil;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Transient;
import com.koolearn.cloud.exam.examcore.question.entity.EssayQuestion;
import com.koolearn.cloud.exam.examcore.question.entity.FillblankAnswer;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.util.HtmlUtil;
import com.koolearn.cloud.exam.examcore.util.QuestionUtil;
import com.koolearn.cloud.task.dto.QuestionErrUser;

public class EssayQuestionDto extends BaseDto implements IExamQuestionDto {
	
	private EssayQuestion essayQuestion=null;
	private List<FillblankAnswer> fillblankAnswers=null;
	private QuestionErrUser questionErrUser;
	
	//	0 全对得分
	@Transient
	public static final int QUESTION_RIGHT_ALL_SCORE = 0;

	//0 按空得分
	@Transient
	public static final int QUESTION_RIGHT_ONE_SCORE = 1;
	
	
	public EssayQuestionDto() {
	}
	public EssayQuestionDto(EssayQuestion essayQuestion) {
		this.essayQuestion = essayQuestion;
	}
	public EssayQuestion getEssayQuestion() {
		return essayQuestion;
	}
	public void setEssayQuestion(EssayQuestion essayQuestion) {
		this.essayQuestion = essayQuestion;
	}
	public List<FillblankAnswer> getFillblankAnswers() {
		return fillblankAnswers;
	}
		
	public void setFillblankAnswers(List<FillblankAnswer> fillblankAnswers) {
		this.fillblankAnswers = fillblankAnswers;
	}
	public void setQuestionDto(QuestionDto questionDto) {
		this.questionDto = questionDto;
	}
	
	public QuestionDto getQuestionDto() {
		return questionDto;
	}
	public int getQuestionType() {
		return questionDto.getQuestion().getQuestionTypeId();
	}
	public boolean isSubjectived() {
		int t=questionDto.getQuestion().getIssubjectived();
		if(t==1){
			return true;
		}
		return false;
	}
	@Override
	public String getShortTopic() {
		String s=essayQuestion.getTopic();
		if(s!=null){
			s=HtmlUtil.Html2Text(s);
			s=HtmlUtil.delEscapeString(s);
			if(s.length()>30){
				s=s.substring(0, 30);
			}
		}
		if(StringUtils.isBlank(s)||StringUtils.isBlank(s.trim())){
			s = QuestionUtil.getQuestionTypeName(getQuestionType());
		}
		return s;
	}
	@Override
	public boolean haveSubQuestion()
	{
		boolean result = false;
		switch (this.getQuestionType())
		{
			case Question.QUESTION_TYPE_FILL_CALCULATION:
			case Question.QUESTION_TYPE_FILL_BLANK:
				result = false;
				break;
			default:
				break;
		}
		return result;
	}
	@Override
	public List<IExamQuestionDto> getSubQuestions()
	{
		List<IExamQuestionDto> result = null;
		switch (this.getQuestionType())
		{
			case Question.QUESTION_TYPE_FILL_CALCULATION:
			case Question.QUESTION_TYPE_FILL_BLANK:
				result = null;
				break;
			default:
				break;
		}
		return result;
	}
	
	@Override
	public void setSubQuestions(List<IExamQuestionDto> list) {
	}
	
	public QuestionErrUser getQuestionErrUser() {
		return questionErrUser;
	}
	public void setQuestionErrUser(QuestionErrUser questionErrUser) {
		this.questionErrUser = questionErrUser;
	}
	@Override
	public String getRightAnswer()
	{
		String result = null;
		switch (this.getQuestionType())
		{
			case Question.QUESTION_TYPE_FILL_CALCULATION:
			case Question.QUESTION_TYPE_FILL_BLANK: // 类型ID_题目ID_答案ID_答案内容@@类型ID_题目ID_答案ID_答案内容
				for (FillblankAnswer answer : this.fillblankAnswers)
				{
					if (null == result)
					{
						result = this.takePre() + answer.getId() + "_" + answer.getAnswer2();
					}
					else
					{
						result = result + "@@" + this.takePre() + answer.getId() + "_" + answer.getAnswer2();
					}
				}
				break;
			default:
				break;
		}
		return result;
	} 
}
