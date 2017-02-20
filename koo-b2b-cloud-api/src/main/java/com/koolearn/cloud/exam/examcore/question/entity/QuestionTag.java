package com.koolearn.cloud.exam.examcore.question.entity;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;

/**
 * 试题标签关系
 * @author yangzhenye
 */
@Entity
@Table(name = "te_question_tag")
public class QuestionTag extends BaseEntity {

	@Column(name="question_id")
	private Integer questionId;
	@Column(name="tag_id")
	private Integer tagId;
	@Column(name="tag_level")
	private Integer tagLevel;
	@Column(name="tag_name")
	private String tagName;
	@Column(name="tag_full_path")
	private String tagFullPath;
	
	public QuestionTag(){
		
	}
	public QuestionTag(Integer questionId, Integer tagId, Integer tagLevel, String tagName, String tagFullPath){
		this.questionId = questionId;
		this.tagId = tagId;
		this.tagLevel = tagLevel;
		this.tagName = tagName;
		this.tagFullPath = tagFullPath;
	}
	public Integer getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}
	public Integer getTagId() {
		return tagId;
	}
	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}
	public Integer getTagLevel() {
		return tagLevel;
	}
	public void setTagLevel(Integer tagLevel) {
		this.tagLevel = tagLevel;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getTagFullPath() {
		return tagFullPath;
	}
	public void setTagFullPath(String tagFullPath) {
		this.tagFullPath = tagFullPath;
	}

}