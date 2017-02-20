package com.koolearn.cloud.exam.examcore.exam.dto;

import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.exam.examcore.question.entity.QuestionTag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ExamResultDetailDto implements Serializable{
	private TpExamResultDetail examResultDetail;
	private List<QuestionTag> tags=new ArrayList<QuestionTag>();
	public TpExamResultDetail  getExamResultDetail() {
		return examResultDetail;
	}
	public void setExamResultDetail(TpExamResultDetail examResultDetail) {
		this.examResultDetail = examResultDetail;
	}
	public List<QuestionTag> getTags() {
		return tags;
	}
	public void setTags(List<QuestionTag> tags) {
		this.tags = tags;
	}
	public QuestionTag getLevelTag(int level){
		if(tags==null||tags.size()==0){
			return null;
		}
		for(QuestionTag tag:tags){
			if(tag.getTagLevel()!=null&&tag.getTagLevel()==level){
				return tag;
			}
		}
		return null;
	}
	
}
