package com.koolearn.cloud.exam.examcore.question.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.koolearn.cloud.exam.examcore.question.entity.ComplexQuestion;
import com.koolearn.cloud.exam.examcore.util.HtmlUtil;
import com.koolearn.cloud.exam.examcore.util.QuestionUtil;
import com.koolearn.cloud.task.dto.QuestionErrUser;

public class ComplexCorrectionQuestionDto extends BaseDto implements IExamQuestionDto{

	private static final long serialVersionUID = 4027569487397828151L;

	private ComplexQuestion complexQuestion;
	
	private List<CorrectionQuestionDto> correctionQuestionDtos;
	
	private List<IExamQuestionDto> subItems;
	
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
		int t=questionDto.getQuestion().getIssubjectived();
		if(t==1){
			return true;
		}
		return false;
	}

	@Override
	public String getShortTopic() {
		String s=complexQuestion.getTopic();
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

	public ComplexQuestion getComplexQuestion() {
		return complexQuestion;
	}

	public void setComplexQuestion(ComplexQuestion complexQuestion) {
		this.complexQuestion = complexQuestion;
	}

	public void setQuestionDto(QuestionDto questionDto) {
		this.questionDto = questionDto;
	}

	public List<CorrectionQuestionDto> getCorrectionQuestionDtos() {
		return correctionQuestionDtos;
	}

	public void setCorrectionQuestionDtos(List<CorrectionQuestionDto> correctionQuestionDtos) {
		this.correctionQuestionDtos = correctionQuestionDtos;
	}

	@Override
	public boolean haveSubQuestion()
	{
		return true;
	}

	public List<IExamQuestionDto> getSubQuestions(){
		if(subItems!=null){
			return subItems;
		}
		if(correctionQuestionDtos==null)return null;
		List<IExamQuestionDto> subs = new ArrayList<IExamQuestionDto>();
		for(CorrectionQuestionDto sub : correctionQuestionDtos){
			subs.add(sub);
		}
		return subs;
	}
	
	@Override
	public void setSubQuestions(List<IExamQuestionDto> list) {
		subItems = list;
	}

	public QuestionErrUser getQuestionErrUser() {
		return questionErrUser;
	}

	public void setQuestionErrUser(QuestionErrUser questionErrUser) {
		this.questionErrUser = questionErrUser;
	}

	@Override
	public String getRightAnswer() // 类型ID_题目ID_@#@子题答案@#@子题答案@#@子题答案@#@子题答案
	{
		String result = this.takePre();
		for (IExamQuestionDto sub : this.getSubQuestions())
		{
			result = result + "@#@" + sub.getRightAnswer();
		}
		return result;
	}

}
