package com.koolearn.cloud.exam.examcore.question.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang3.StringUtils;

import com.koolearn.cloud.exam.examcore.question.entity.ChoiceAnswer;
import com.koolearn.cloud.exam.examcore.question.entity.ChoiceQuestion;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;
import com.koolearn.cloud.exam.examcore.util.HtmlUtil;
import com.koolearn.cloud.exam.examcore.util.QuestionUtil;
import com.koolearn.cloud.task.dto.QuestionErrUser;

/**
 * 选择题DTO 包括选择题答案
 * 普通多选
 * QUESTION_TYPE_DUOXUAN = 1;
 * 普通单选
 * QUESTION_TYPE_DANXUAN = 6;
 * 排序题(子题)
 * QUESTION_TYPE_SORT = 13;
 * @author wangpeng
 * @date Oct 25, 2012
 * 技术教辅社区组@koolearn.com
 */
public class ChoiceQuestionDto extends BaseDto implements IExamQuestionDto {
	private static final long serialVersionUID = -712882979223901162L;
	private ChoiceQuestion choiceQuestion=null;
	private List<ChoiceAnswer> choiceAnswers=null; 
	private QuestionErrUser questionErrUser;
	
	public String getRightSortAnswer(){
		String str ="";
		if(choiceAnswers!=null&&choiceAnswers.size()>0){
			List<ChoiceAnswer> choiceAnswers1 = new ArrayList<ChoiceAnswer>(); 
			choiceAnswers1.addAll(choiceAnswers);
			Collections.sort(choiceAnswers1);
			for (ChoiceAnswer choiceAnswer : choiceAnswers1) {
				if("".equals(str))
					str+=choiceAnswer.getSequenceId();
				else
					str+=ConstantTe.SEPERATOR_ANSWER_AND+choiceAnswer.getSequenceId();
			}
			return str;
		}	
		return str;
	}

	public ChoiceQuestionDto(){
		
	}
	public ChoiceQuestionDto(ChoiceQuestion choiceQuestion){
		this.choiceQuestion=choiceQuestion;
	}
	public ChoiceQuestion getChoiceQuestion() {
		return choiceQuestion;
	}
	public void setChoiceQuestion(ChoiceQuestion choiceQuestion) {
		this.choiceQuestion = choiceQuestion;
	}
	/** 此方法已被污染，涉及到了排序的业务逻辑，因涉及原有业务，故不能删除，如果要获取原生的选项集合请用getCas()，后续的开发中应该注意：在PO的公共方法中不应该掺杂涉及业务的操作 */
	public List<ChoiceAnswer> getChoiceAnswers() {
		if(choiceAnswers!=null&&choiceAnswers.size()>0){
			
			try {
				Comparator mycmp=ComparableComparator.getInstance();
				mycmp=ComparatorUtils.nullLowComparator(mycmp);//允许null
				ArrayList<Object>sortFields=new ArrayList<Object>();
				sortFields.add(new BeanComparator("sequenceId"));
				ComparatorChain multiSort=new ComparatorChain(sortFields);
				Collections.sort(choiceAnswers, multiSort);
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
		return choiceAnswers;
	}
	/** 获取原始的选项集合 */
	public List<ChoiceAnswer> getCas()
	{
		return choiceAnswers;
	}
	
	public void setChoiceAnswers(List<ChoiceAnswer> choiceAnswers) {
		this.choiceAnswers = choiceAnswers;
	}
	public QuestionDto getQuestionDto() {
		return questionDto;
	}
	public void setQuestionDto(QuestionDto questionDto) {
		this.questionDto = questionDto;
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
		String s=choiceQuestion.getTopic();
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
			case Question.QUESTION_TYPE_DANXUAN: // 单选题不包含子题
			case Question.QUESTION_TYPE_DUOXUAN: // 多选题不包含子题
			case Question.QUESTION_TYPE_SORT: // 拖拽排序题不包含子题
			case Question.QUESTION_TYPE_DANXUAN_BOX: // 方框题不包含子题
				result = false;
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
			case Question.QUESTION_TYPE_DANXUAN: // 单选题不包含子题
			case Question.QUESTION_TYPE_DUOXUAN: // 多选题不包含子题
			case Question.QUESTION_TYPE_SORT: // 拖拽排序题不包含子题
			case Question.QUESTION_TYPE_DANXUAN_BOX: // 方框题不包含子题
				result = null;
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

	@Override
	public void setQuestionErrUser(QuestionErrUser questionErrUser) {
		this.questionErrUser=questionErrUser;
	}

	@Override
	public String getRightAnswer()
	{
		String result = null;
		switch (this.getQuestionType())
		{
			case Question.QUESTION_TYPE_DANXUAN: // 题目类型ID_题目ID_选项SequenceId
				for (ChoiceAnswer choiceAnswer : this.choiceAnswers)
				{
					if (null != choiceAnswer.getIsright() && choiceAnswer.getIsright() == 1)
					{
						result = this.takePre() + choiceAnswer.getSequenceId();
						break;
					}
				}
				break;
			case Question.QUESTION_TYPE_DUOXUAN: // 题目类型ID_题目ID_@@选项SequenceId@@选项SequenceId@@选项SequenceId
				result = this.takePre();
				for (ChoiceAnswer choiceAnswer : this.choiceAnswers)
				{
					if (choiceAnswer.getIsright() == 1)
					{
						result = result + "@@" + choiceAnswer.getSequenceId();
					}
				}
				break;
			case Question.QUESTION_TYPE_SORT: // 题目类型ID_题目ID_@@选项orderby@@选项orderby
				result = this.takePre();
				for (ChoiceAnswer choiceAnswer : this.orderAsc())
				{
					result = result + "@@" + choiceAnswer.getSequenceId().toString();
				}
				break;
			case Question.QUESTION_TYPE_DANXUAN_BOX: // 只作为子题,,// 题目类型ID_题目ID_序列ID
				for (ChoiceAnswer choiceAnswer : this.choiceAnswers)
				{
					if (choiceAnswer.getIsright() == 1)
					{
						result = this.takePre() + choiceAnswer.getSequenceId().toString();
					}
				}
				break;
			default:
				break;
		}
		return result;
	}
	
	/** 对结果按照orderby字段进行升序排列 */
	private List<ChoiceAnswer> orderAsc()
	{
		for (int i = 0; i < this.choiceAnswers.size(); i++)
		{
			for (int j = i; j < this.choiceAnswers.size(); j++)
			{
				if (this.choiceAnswers.get(i).getOrderby().intValue() > this.choiceAnswers.get(j).getOrderby().intValue())
				{
					ChoiceAnswer temp = this.choiceAnswers.get(i);
					this.choiceAnswers.set(i, this.choiceAnswers.get(j));
					this.choiceAnswers.set(j, temp);
				}
			}
		}
		return this.choiceAnswers;
	}
	
	/**
	 * 将选项乱序
	 * 
	 * @author DuHongLin
	 */
	public List<ChoiceAnswer> unSortAnswers()
	{
		int size = this.choiceAnswers.size();
		List<ChoiceAnswer> result = new ArrayList<ChoiceAnswer>(size);
		Random random = new Random();
		int sizeTemp = size;
		for (int i = 0; i < size; i++)
		{
			int temp = random.nextInt(sizeTemp --);
			result.add(this.choiceAnswers.get(temp));
			this.choiceAnswers.remove(temp);
		}
		this.choiceAnswers = result;
		return result;
	}
}
