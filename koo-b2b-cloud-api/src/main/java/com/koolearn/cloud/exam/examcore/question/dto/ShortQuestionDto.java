package com.koolearn.cloud.exam.examcore.question.dto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.koolearn.cloud.exam.examcore.question.entity.ShortQuestion;
import com.koolearn.cloud.exam.examcore.util.HtmlUtil;
import com.koolearn.cloud.exam.examcore.util.QuestionUtil;
import com.koolearn.cloud.task.dto.QuestionErrUser;

public class ShortQuestionDto  extends BaseDto  implements IExamQuestionDto{
	
	private static final long serialVersionUID = 1L;
	private ShortQuestion shortQuestion;
	private QuestionErrUser questionErrUser;
	
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
		String s=shortQuestion.getTopic();
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

	public ShortQuestion getShortQuestion() {
		return shortQuestion;
	}

	public void setShortQuestion(ShortQuestion shortQuestion) {
		this.shortQuestion = shortQuestion;
	}

	public void setQuestionDto(QuestionDto questionDto) {
		this.questionDto = questionDto;
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
		return this.takePre() + this.shortQuestion.getAnswerreference();
	}
	
}
