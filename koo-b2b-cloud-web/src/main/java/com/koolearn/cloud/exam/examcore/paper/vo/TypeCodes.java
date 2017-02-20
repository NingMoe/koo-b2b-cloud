package com.koolearn.cloud.exam.examcore.paper.vo;

import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;

import java.util.ArrayList;
import java.util.List;


/**
 * 前台传递参数对象
 * @author wangpeng
 *
 */
@Deprecated
public class TypeCodes {
	private int tagId;
	private List<String> codes=new ArrayList<String>();
	private List<IExamQuestionDto> questions=new ArrayList<IExamQuestionDto>();
	public int getTagId() {
		return tagId;
	}
	public void setTagId(int tagId) {
		this.tagId = tagId;
	}
	public List<String> getCodes() {
		return codes;
	}
	public void setCodes(List<String> codes) {
		this.codes = codes;
	}
	public List<IExamQuestionDto> getQuestions() {
		return questions;
	}
	public void setQuestions(List<IExamQuestionDto> questions) {
		this.questions = questions;
	}
	
	
}
