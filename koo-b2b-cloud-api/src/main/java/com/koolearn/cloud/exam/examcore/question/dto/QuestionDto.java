package com.koolearn.cloud.exam.examcore.question.dto;

import java.util.ArrayList;
import java.util.List;

import com.koolearn.cloud.exam.examcore.question.entity.Question;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach;
import com.koolearn.cloud.exam.examcore.question.entity.TagObject;
import com.koolearn.cloud.exam.examcore.util.ConstantTe;



/**
 * 题目DTO 涉及其他相关属性
 * @author wangpeng
 * @date Oct 24, 2012
 * 技术教辅社区组@koolearn.com
 */
public class QuestionDto extends BaseDto{
	private static final long serialVersionUID = 1L;
	public QuestionDto(){
		
	}
	public QuestionDto(Question question){
		this.question=question;
	}
	private Question question;
	/**
	 * 题库业务信息扩展:学校，老师，来源
	 */
//	private QuestionBankExt questionBankExt=new QuestionBankExt();
	/**
	 * 保存类型
	 */
	private int saveType=ConstantTe.QUESTION_SAVETYPE_SAVE;
	
	/**
	 * 试题响应类型 默认为正常响应,用于web控制层
	 */
	private int responseType=ConstantTe.QUESTION_RESPONSETYPE_NORMAL;
	
	//材料
	private List<QuestionAttach> questionAttachs=new ArrayList<QuestionAttach>();
	//标签
	private List<TagObject> tagObjectList=new ArrayList<TagObject>();
	//题目三级标签
//	private List<QuestionTag> questionTagList=new ArrayList<QuestionTag>();

	//保存content开头富文本的出现顺序
	private String textarea_param;
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public List<QuestionAttach> getQuestionAttachs() {
		return questionAttachs;
	}
	public void setQuestionAttachs(List<QuestionAttach> questionAttachs) {
		this.questionAttachs = questionAttachs;
	}
	public int getSaveType() {
		return saveType;
	}
	public void setSaveType(int saveType) {
		this.saveType = saveType;
	}
	public int getResponseType() {
		return responseType;
	}
	public void setResponseType(int responseType) {
		this.responseType = responseType;
	}
	public String getTextarea_param() {
		return textarea_param;
	}
	public void setTextarea_param(String textarea_param) {
		this.textarea_param = textarea_param;
	}
    public List<TagObject> getTagObjectList() {
        return tagObjectList;
    }
    public void setTagObjectList(List<TagObject> tagObjectList) {
        this.tagObjectList = tagObjectList;
    }

}
