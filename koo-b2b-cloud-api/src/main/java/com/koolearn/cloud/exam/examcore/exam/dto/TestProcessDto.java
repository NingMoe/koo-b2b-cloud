package com.koolearn.cloud.exam.examcore.exam.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.paper.dto.PaperItemDto;
@Deprecated
public class TestProcessDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3592227098727778254L;
	
	public static int previewType_TEST = 0; //正常考试
	
	public static int previewType_EXAM = 1; //考试预览
	
	public static int previewType_PAPER = 2; //考卷预览
	
	public static int previewType_Playback = 3; //考试回放

	private int userId; //用户id
	
	private Date startTime; //考试开始时间
	
	private int questionAmount; //题目数量
	
	private List<PaperItemDto> paperItemDtoList; //试卷元素id列表
	
	private int testResultId; //用户考试答案总id
	
	private int testCount; //考试次数
	
	private List<Object> paperObjectList; //试卷元素对象列表
	
	private float examScore; //考试总分
	
	private int testTimeConsumed; //考试用时
	
	private int status; //答题状态
	
	private List<PaperItemDto> paperItemDtoComparisonList; //用户答题比对列表
	
	private List<Integer[]> fromTo; //分页数组
	
	private int pageNo; //当前分页
	
	private int autoTime;//自动保存时间
	
	private int previewType; //预览类型
	
	private float userTotalScore; //用户总得分
	
	private int entExamId=-1; //企培考试Id
	
	private String subaccountService;//考试服务
	
	private Map<String, String> urlMap; //批改报告地址
	
	private Date reStartTime; //考试重新开始时间
	
	private double maxScore; //本考试最高得分
	
	private int moduleIndex = -1; //当前模块
	
	private List<PaperItemDto> paperModuleItemDtoList; //试卷模块节点列表 
	
	private List<PaperItemDto> paperStandbyModuleItemDtoList; //试卷备用模块节点列表 
	
	private Map<Integer, String> moduleReportMap; //模块统计
	
	private int limitTime; //模块限时
	
	private String moduleStart;
	
	private float objectiveScores; //客观题得分
	
	private float subjectiveScores; //主观题得分
	
	public float getExamScore() {
		return examScore;
	}

	public void setExamScore(float examScore) {
		this.examScore = examScore;
	}

	public List<Object> getPaperObjectList() {
		return paperObjectList;
	}

	public void setPaperObjectList(List<Object> paperObjectList) {
		this.paperObjectList = paperObjectList;
	}

	public int getTestCount() {
		return testCount;
	}

	public void setTestCount(int testCount) {
		this.testCount = testCount;
	}

	public int getTestResultId() {
		return testResultId;
	}

	public void setTestResultId(int testResultId) {
		this.testResultId = testResultId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getQuestionAmount() {
		return questionAmount;
	}

	public void setQuestionAmount(int questionAmount) {
		this.questionAmount = questionAmount;
	}

	public List<PaperItemDto> getPaperItemDtoList() {
		return paperItemDtoList;
	}

	public void setPaperItemDtoList(List<PaperItemDto> paperItemDtoList) {
		this.paperItemDtoList = paperItemDtoList;
	}

	public int getTestTimeConsumed() {
		return testTimeConsumed;
	}

	public void setTestTimeConsumed(int testTimeConsumed) {
		this.testTimeConsumed = testTimeConsumed;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<PaperItemDto> getPaperItemDtoComparisonList() {
		return paperItemDtoComparisonList;
	}

	public void setPaperItemDtoComparisonList(
			List<PaperItemDto> paperItemDtoComparisonList) {
		this.paperItemDtoComparisonList = paperItemDtoComparisonList;
	}

	public List<Integer[]> getFromTo() {
		return fromTo;
	}

	public void setFromTo(List<Integer[]> fromTo) {
		this.fromTo = fromTo;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getAutoTime() {
		return autoTime;
	}

	public void setAutoTime(int autoTime) {
		this.autoTime = autoTime;
	}

	public int getPreviewType() {
		return previewType;
	}

	public void setPreviewType(int previewType) {
		this.previewType = previewType;
	}

	public float getUserTotalScore() {
		return userTotalScore;
	}

	public void setUserTotalScore(float userTotalScore) {
		this.userTotalScore = userTotalScore;
	}

	public int getEntExamId() {
		return entExamId;
	}

	public void setEntExamId(int entExamId) {
		this.entExamId = entExamId;
	}

	public String getSubaccountService() {
		return subaccountService;
	}

	public void setSubaccountService(String subaccountService) {
		this.subaccountService = subaccountService;
	}

	public Map<String, String> getUrlMap() {
		return urlMap;
	}

	public void setUrlMap(Map<String, String> urlMap) {
		this.urlMap = urlMap;
	}

	public Date getReStartTime() {
		return reStartTime;
	}

	public void setReStartTime(Date reStartTime) {
		this.reStartTime = reStartTime;
	}

	public double getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(double maxScore) {
		this.maxScore = maxScore;
	}

	public int getModuleIndex() {
		return moduleIndex;
	}

	public void setModuleIndex(int moduleIndex) {
		this.moduleIndex = moduleIndex;
	}

	public List<PaperItemDto> getPaperModuleItemDtoList() {
		return paperModuleItemDtoList;
	}

	public void setPaperModuleItemDtoList(List<PaperItemDto> paperModuleItemDtoList) {
		this.paperModuleItemDtoList = paperModuleItemDtoList;
	}

	public List<PaperItemDto> getPaperStandbyModuleItemDtoList() {
		return paperStandbyModuleItemDtoList;
	}

	public void setPaperStandbyModuleItemDtoList(
			List<PaperItemDto> paperStandbyModuleItemDtoList) {
		this.paperStandbyModuleItemDtoList = paperStandbyModuleItemDtoList;
	}

	public Map<Integer, String> getModuleReportMap() {
		return moduleReportMap;
	}

	public void setModuleReportMap(Map<Integer, String> moduleReportMap) {
		this.moduleReportMap = moduleReportMap;
	}

	public int getLimitTime() {
		return limitTime;
	}

	public void setLimitTime(int limitTime) {
		this.limitTime = limitTime;
	}

	public String getModuleStart() {
		return moduleStart;
	}

	public void setModuleStart(String moduleStart) {
		this.moduleStart = moduleStart;
	}

	public float getObjectiveScores() {
		return objectiveScores;
	}

	public void setObjectiveScores(float objectiveScores) {
		this.objectiveScores = objectiveScores;
	}

	public float getSubjectiveScores() {
		return subjectiveScores;
	}

	public void setSubjectiveScores(float subjectiveScores) {
		this.subjectiveScores = subjectiveScores;
	}
	
}
