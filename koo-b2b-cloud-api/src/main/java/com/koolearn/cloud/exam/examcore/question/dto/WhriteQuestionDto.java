package com.koolearn.cloud.exam.examcore.question.dto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.koolearn.cloud.exam.examcore.question.entity.WhriteQuestion;
import com.koolearn.cloud.exam.examcore.util.HtmlUtil;
import com.koolearn.cloud.exam.examcore.util.QuestionUtil;
import com.koolearn.cloud.task.dto.QuestionErrUser;


public class WhriteQuestionDto  extends BaseDto  implements IExamQuestionDto{
	
	/**
	 * TODO
	 */
	private static final long serialVersionUID = 1L;
	private WhriteQuestion whriteQuestion;
	private QuestionErrUser questionErrUser;

	public WhriteQuestion getWhriteQuestion() {
		return whriteQuestion;
	}

	public void setWhriteQuestion(WhriteQuestion whriteQuestion) {
		this.whriteQuestion = whriteQuestion;
	}

	public void setQuestionDto(QuestionDto questionDto) {
		this.questionDto = questionDto;
	}

	@Override
	public QuestionDto getQuestionDto() {
		return questionDto;
	}

	@Override
	public int getQuestionType() {
		return questionDto.getQuestion().getQuestionTypeId();
	}

	@Override
	public boolean isSubjectived() {
		int t = questionDto.getQuestion().getIssubjectived();
		if(t==1){
			return true;
		}
		return false;
	}
	@Override
	public String getShortTopic() {
		if(whriteQuestion==null)return "";
		String s=whriteQuestion.getTopic();
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
		return false;
	}

	@Override
	public List<IExamQuestionDto> getSubQuestions()
	{
		return null;
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
		return this.takePre() + this.whriteQuestion.getAnswerreference();
	}
}
