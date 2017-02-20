package com.koolearn.cloud.exam.examcore.question.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer;
import com.koolearn.cloud.exam.examcore.question.entity.ComplexQuestion;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.util.HtmlUtil;
import com.koolearn.cloud.exam.examcore.util.QuestionUtil;
import com.koolearn.cloud.task.dto.QuestionErrUser;

/**
 * 选择型完型填空题DTO
 * @author tangshuren
 * @date 2012-10-29
 *
 */
public class ComplexQuestionDto extends BaseDto implements IExamQuestionDto{

	private static final long serialVersionUID = 6400630342701750085L;

	private ComplexQuestion complexQuestion;
	
	private List<ChoiceQuestionDto> choiceQuestionDtos;
	
	private List<EssayQuestionDto> essayQuestionDTOs;
	
	private List<ChoiceAnswer> choiceAnswers;
	
	private int questionType;
	
	
	private List<IExamQuestionDto> subItems;
	private QuestionErrUser questionErrUser;
	/**
	 * 子题目顺序
	 */
	private String subItemOrderStr;
	
	public List<IExamQuestionDto> getSubQuestions(){
		if(subItems!=null){
			return subItems;
		}
		if(choiceQuestionDtos!=null){
			List<IExamQuestionDto> subExamList = new ArrayList<IExamQuestionDto>();
			for(ChoiceQuestionDto cdto:choiceQuestionDtos){
				subExamList.add(cdto);
			}
			return subExamList;
		}
		if(essayQuestionDTOs!=null){
			List<IExamQuestionDto> subExamList = new ArrayList<IExamQuestionDto>();
			for(EssayQuestionDto edto:essayQuestionDTOs){
				subExamList.add(edto);
			}
			return subExamList;
		}
		return null;
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

	public void setComplexQuestion(ComplexQuestion complexQuestion) {
		this.complexQuestion = complexQuestion;
	}

	public ComplexQuestion getComplexQuestion() {
		return complexQuestion;
	}

	public void setChoiceQuestionDtos(List<ChoiceQuestionDto> choiceQuestionDtos) {
		this.choiceQuestionDtos = choiceQuestionDtos;
	}

	public List<ChoiceQuestionDto> getChoiceQuestionDtos() {
		return choiceQuestionDtos;
	}

	@Override
	public QuestionDto getQuestionDto() {
		return questionDto;
	}

	public void setQuestionDto(QuestionDto questionDto) {
		this.questionDto = questionDto;
	}
	
	@Override
	public int getQuestionType() {
		if( null != questionDto){
			questionType = questionDto.getQuestion().getQuestionTypeId();
		}
		return questionType;
	}
	@Override
	public boolean isSubjectived() {
		int t=questionDto.getQuestion().getIssubjectived();
		if(t==1){
			return true;
		}
		return false;
	}

	public List<IExamQuestionDto> getSubItems() {
		return subItems;
	}

	public void setSubItems(List<IExamQuestionDto> subItems) {
		this.subItems = subItems;
	}

	@Override
	public String getShortTopic() {
		if(complexQuestion==null)return "";
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

	public String getSubItemOrderStr() {
		return subItemOrderStr;
	}

	public void setSubItemOrderStr(String subItemOrderStr) {
		this.subItemOrderStr = subItemOrderStr;
	}

	public void setEssayQuestionDTOs(List<EssayQuestionDto> essayQuestionDTOs) {
		this.essayQuestionDTOs = essayQuestionDTOs;
	}

	public List<EssayQuestionDto> getEssayQuestionDTOs() {
		return essayQuestionDTOs;
	}

	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}

	public void setChoiceAnswers(List<ChoiceAnswer> choiceAnswers) {
		this.choiceAnswers = choiceAnswers;
	}

	public List<ChoiceAnswer> getChoiceAnswers() {
		return choiceAnswers;
	}

	@Override
	public boolean haveSubQuestion()
	{
		return true;
	}

	@Override
	public String getRightAnswer() // 类型ID_题目ID_@#@子题答案@#@子题答案@#@子题答案@#@子题答案
	{
		String result = this.takePre();
		switch (this.getQuestionType())
		{
			case Question.QUESTION_TYPE_CHOICE_FILL_BLANK:
			case Question.QUESTION_TYPE_CLOZE_FILL_BLANK:
			case Question.QUESTION_TYPE_CHOICE_BLANK:
			case Question.QUESTION_TYPE_LISTEN:
			case Question.QUESTION_TYPE_READ:
				List<IExamQuestionDto> dtos = this.getSubQuestions();
				for (IExamQuestionDto dto : dtos)
				{
					result = result + "@#@" + dto.getRightAnswer();
				}
				break;
			default:
				break;
		}
		return result;
	}

	
}
