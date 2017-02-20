package com.koolearn.cloud.testpaper.entity;

import java.io.Serializable;
 

public class HandTestPagerDto implements Serializable {

	private static final long serialVersionUID = -2434728846894283235L;
	/**分类模版*/
	private String templateType;//模版
	private Integer  paperType;//试卷分类
	private Integer paperId;// 试卷id ，新建试卷默认为0
	private Integer examId;//考试id
	/**搜索题库*/
	private Integer  questionType;//题型分类id
	private Integer  questionName;//题目名称id
	private Integer  questionSource;//试题来源（新东方1、 我的上传2、本校共享3）QUESTION_SOURCE_TYPE_XDF
	private Integer  filterType;  //过滤类型  1过滤使用过的试题  2只选收藏的试题    FILTER_TYPE_USEED
	private Integer  questionId;  //题目
	private String  questionCode;  //题目code
	private Integer  isfrom;  //收藏来源 1.学生 2.老师
	private Integer  userId;  //学生或老师的id
	private Integer  schoolId;  //学校id
	private String  klbId;  //知识库标签id
	
	private boolean autoPaper=false;//智能组卷获取模版时，获取数据库题型题目数量
	
	private Integer examType;//1考试(在线统考) 2随堂测评   3在线练习
	
	public String getQuestionCode() {
		return questionCode;
	}
	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}
 
	public Integer getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}
	public Integer getIsfrom() {
		return isfrom;
	}
	public void setIsfrom(Integer isfrom) {
		this.isfrom = isfrom;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	public String getKlbId() {
		return klbId;
	}
	public void setKlbId(String klbId) {
		this.klbId = klbId;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	public Integer getPaperType() {
		return paperType;
	}
	public void setPaperType(Integer paperType) {
		this.paperType = paperType;
	}
	 
	public Integer getQuestionType() {
		return questionType;
	}
	public void setQuestionType(Integer questionType) {
		this.questionType = questionType;
	}
	public Integer getQuestionName() {
		return questionName;
	}
	public void setQuestionName(Integer questionName) {
		this.questionName = questionName;
	}
	public Integer getQuestionSource() {
		return questionSource;
	}
	public void setQuestionSource(Integer questionSource) {
		this.questionSource = questionSource;
	}
	public Integer getFilterType() {
		return filterType;
	}
	public void setFilterType(Integer filterType) {
		this.filterType = filterType;
	}
	public Integer getPaperId() {
		return paperId;
	}
	public void setPaperId(Integer paperId) {
		this.paperId = paperId;
	}
	public Integer getExamType() {
		return examType;
	}
	public void setExamType(Integer examType) {
		this.examType = examType;
	}
	public Integer getExamId() {
		return examId;
	}
	public void setExamId(Integer examId) {
		this.examId = examId;
	}
	public boolean isAutoPaper() {
		return autoPaper;
	}
	public void setAutoPaper(boolean autoPaper) {
		this.autoPaper = autoPaper;
	}
	
}
