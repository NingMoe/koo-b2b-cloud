package com.koolearn.cloud.task.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Transient;

public class TaskDto implements Serializable{
	private static final long serialVersionUID = 5109755137979464439L;
	
	private int id;
	/** 考试/作业 名称 (默认取试卷名称)  |  type=2为课堂名称 */
	private String examName;
	/** 班级或小组id（班级type=3） */
	private String classesId;
	/** 默认0：试卷id */
	private Integer paperId;
	/** 默认0：试卷id */
	private Integer paperTagId;
	/** 开始时间 */
	private Date startTime;
	private String startTimeStr;
	/** 结束时间 */
	private Date endTime;
	private String endTimeStr;
	/** 默认0： 0作业、1考试   2课堂   20课堂作业 */
	private int type;
	/**  默认1：1.新建(有效) 2.撤回 3.删除 */
	private Integer status;
	/** 创建老师id */
	private Integer teacherId;
	/** 创建时间 */
	private Date createTime;
	/** 更新时间 */
	private Date updateTime;
	/** 学生浏览状态  1.学生未浏览  2.学生已浏览  */
	private Integer studentView;
	/** 老师浏览状态  1.老师未浏览  2.老师已浏览*/
	private Integer teacherView;
	public Integer pageNo;
	public Integer pageSize;
	
	/** 课堂教学进度id */
	private Integer teachingPointId;
	/** 课堂教学进度点名 */
	private String teachingPoitName;
	/** 已完成学生数 */
	private Integer compStudentNum;
	/** 所有学生数 */
	private Integer allStudentNum;
	/** 作业对应的班级字符串 */
	private String classesName;
	/** 学科id */
	private Integer subjectId;
	/** 年级id */
	private Integer rangeId;
	
	/** te_exam_result表字段 考试结果字段 */
	/** 考试结果状态  0,未开始 1.考试中 2.已交卷 */
	private String resultStatus;
	/** 答卷完成时间= 答卷开始时间+答卷所用时长 */
	private Date completeTime;
	/** 答卷开始时间 */
	private Date beginTime;
	/** 答卷所用时长 */
	private Integer timeOff;
	/** 学生id */
	private Integer studentId;
	/** 结果id */
	private Integer resultId;
	/** 作业完成率 */
	private long completeRate;
	
	private String lastNewStatus;
	/** te_exam_result表字段 考试结果字段 */
	
	//前台显示操作按钮文字
	private String buttonWords;
	//前台显示操作按钮样式
	private String buttonCss;
	//前台显示操作链接
	private String buttonUrl;
	/**跟课堂保持一致  课堂结束状态 0结束1正常 **/
	public static int FINISH_STATUS_END=0;
	public static int FINISH_STATUS_NOMAL=1;
	private Integer finishStatus;
	/* 以大题计算题数 */
	private Integer questionCount;
	/* 以小题计算题数 */
	private Integer questionMinCount;
	
	public Integer getQuestionCount() {
		return questionCount;
	}
	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}
	public Integer getQuestionMinCount() {
		return questionMinCount;
	}
	public void setQuestionMinCount(Integer questionMinCount) {
		this.questionMinCount = questionMinCount;
	}
	public Integer getFinishStatus() {
		return finishStatus;
	}
	public void setFinishStatus(Integer finishStatus) {
		this.finishStatus = finishStatus;
	}
	public Integer getStudentView() {
		return studentView;
	}
	public void setStudentView(Integer studentView) {
		this.studentView = studentView;
	}
	public Integer getTeacherView() {
		return teacherView;
	}
	public void setTeacherView(Integer teacherView) {
		this.teacherView = teacherView;
	}
	public long getCompleteRate() {
		return completeRate;
	}
	public void setCompleteRate(long completeRate) {
		this.completeRate = completeRate;
	}
	public String getButtonCss() {
		return buttonCss;
	}
	public void setButtonCss(String buttonCss) {
		this.buttonCss = buttonCss;
	}
	public String getButtonWords() {
		return buttonWords;
	}
	public void setButtonWords(String buttonWords) {
		this.buttonWords = buttonWords;
	}
	public String getButtonUrl() {
		return buttonUrl;
	}
	public void setButtonUrl(String buttonUrl) {
		this.buttonUrl = buttonUrl;
	}
	public Integer getSubjectId() {
		return subjectId;
	}
	public Integer getResultId() {
		return resultId;
	}
	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}
	public void setPaperId(Integer paperId) {
		this.paperId = paperId;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getLastNewStatus() {
		return lastNewStatus;
	}
	public void setLastNewStatus(String lastNewStatus) {
		this.lastNewStatus = lastNewStatus;
	}
	public Integer getStudentId() {
		return studentId;
	}
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	public Date getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Integer getTimeOff() {
		return timeOff;
	}
	public void setTimeOff(Integer timeOff) {
		this.timeOff = timeOff;
	}
	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}
	public Integer getRangeId() {
		return rangeId;
	}
	public void setRangeId(Integer rangeId) {
		this.rangeId = rangeId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public String getClassesId() {
		return classesId;
	}
	public void setClassesId(String classesId) {
		this.classesId = classesId;
	}
	public int getPaperId() {
		return paperId;
	}
	public void setPaperId(int paperId) {
		this.paperId = paperId;
	}
	public Integer getPaperTagId() {
		return paperTagId;
	}
	public void setPaperTagId(Integer paperTagId) {
		this.paperTagId = paperTagId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getStartTimeStr() {
		return startTimeStr;
	}
	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getEndTimeStr() {
		return endTimeStr;
	}
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getTeachingPointId() {
		return teachingPointId;
	}
	public void setTeachingPointId(Integer teachingPointId) {
		this.teachingPointId = teachingPointId;
	}
	public String getTeachingPoitName() {
		return teachingPoitName;
	}
	public void setTeachingPoitName(String teachingPoitName) {
		this.teachingPoitName = teachingPoitName;
	}
	public String getResultStatus() {
		return resultStatus;
	}
	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}
	public Integer getCompStudentNum() {
		return compStudentNum;
	}
	public void setCompStudentNum(Integer compStudentNum) {
		this.compStudentNum = compStudentNum;
	}
	public Integer getAllStudentNum() {
		return allStudentNum;
	}
	public void setAllStudentNum(Integer allStudentNum) {
		this.allStudentNum = allStudentNum;
	}
	public String getClassesName() {
		return classesName;
	}
	public void setClassesName(String classesName) {
		this.classesName = classesName;
	}
}
