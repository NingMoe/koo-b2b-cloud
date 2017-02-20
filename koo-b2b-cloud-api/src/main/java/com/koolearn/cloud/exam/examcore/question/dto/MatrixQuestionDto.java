package com.koolearn.cloud.exam.examcore.question.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer;
import com.koolearn.cloud.exam.examcore.question.entity.MatrixQuestion;
import com.koolearn.cloud.exam.examcore.util.HtmlUtil;
import com.koolearn.cloud.exam.examcore.util.QuestionUtil;
import com.koolearn.cloud.task.dto.QuestionErrUser;


/**
 * 矩阵题DTO
 * @author tangshuren
 * @date 2012-10-29
 *
 */
public class MatrixQuestionDto  extends BaseDto implements IExamQuestionDto{
	
	private static final long serialVersionUID = 6400630342701750096L;
	private MatrixQuestion matrixQuestion;
	
	private List<ChoiceQuestionDto> choiceQuestionDtos;
	
	private List<ChoiceAnswer> choiceAnswers;
	
	private List<IExamQuestionDto> subItems;
	
	private QuestionErrUser questionErrUser;

	public void setMatrixQuestion(MatrixQuestion matrixQuestion) {
		this.matrixQuestion = matrixQuestion;
	}

	public MatrixQuestion getMatrixQuestion() {
		return matrixQuestion;
	}


	public QuestionDto getQuestionDto() {
		return questionDto;
	}

	public void setQuestionDto(QuestionDto questionDto) {
		this.questionDto = questionDto;
	}

	public List<ChoiceQuestionDto> getChoiceQuestionDtos() {
		return choiceQuestionDtos;
	}

	public void setChoiceQuestionDtos(List<ChoiceQuestionDto> choiceQuestionDtos) {
		this.choiceQuestionDtos = choiceQuestionDtos;
	}

	@Override
	public int getQuestionType() {
		return questionDto.getQuestion().getQuestionTypeId();
	}

	public void setChoiceAnswers(List<ChoiceAnswer> choiceAnswers) {
		this.choiceAnswers = choiceAnswers;
	}

	public List<ChoiceAnswer> getChoiceAnswers() {
		return choiceAnswers;
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
		String s=matrixQuestion.getTopic();
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
	
	public static void main(String[] rags){
		System.out.println(System.currentTimeMillis());
	}

	@Override
	public boolean haveSubQuestion()
	{
		return true;
	}

	@Override
	public List<IExamQuestionDto> getSubQuestions()
	{
		if(subItems!=null){
			return subItems;
		}
		List<IExamQuestionDto> result = new ArrayList<IExamQuestionDto>(0);
		for (ChoiceQuestionDto dto : this.choiceQuestionDtos)
		{
			result.add(dto);
		}
		return result;
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

	public List<IExamQuestionDto> getSubItems() {
		return subItems;
	}

	public void setSubItems(List<IExamQuestionDto> subItems) {
		this.subItems = subItems;
	}

	@Override
	public String getRightAnswer() // 类型ID_题目ID_@#@子题答案@#@子题答案@#@子题答案@#@子题答案
	{
		String result = this.takePre();
		for (IExamQuestionDto dto : this.getSubQuestions())
		{
			result = result + "@#@" + dto.getRightAnswer();
		}
		return result;
	}
}
