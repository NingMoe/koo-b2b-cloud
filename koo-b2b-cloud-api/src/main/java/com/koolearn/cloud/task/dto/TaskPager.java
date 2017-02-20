package com.koolearn.cloud.task.dto;

import com.koolearn.cloud.common.entity.Pager;

/**
 * Created by gehaisong on 2016/4/5.
 */
public class TaskPager extends Pager {
	private static final long serialVersionUID = -8862694060265802716L;
	private String keyWord;//搜索关键词
	private Integer subjectId;//科目
	private String	endTime;//页面查询截止时间
	private Integer userId;//用户id
	private Integer examId;//作业id
	private Integer resultAnswer;//是否正确,-1为 未判题，0为不正确，1为正确
	private Integer classId;//班级id
	private String studentIds;//学生id集合
	private Integer studentId;//学生id
    private Integer  rangeId;//学段id
	private String errRate;//错误率
	private Integer resultId;//考试结果id
	private String ptype;//个人作业详情 只看错题ptype=e	 查看全部 ptype=p
	private String resultIds;
	private String radioType;
	
	public String getRadioType() {
		return radioType;
	}
	public void setRadioType(String radioType) {
		this.radioType = radioType;
	}
	public String getResultIds() {
		return resultIds;
	}
	public void setResultIds(String resultIds) {
		this.resultIds = resultIds;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getPtype() {
		return ptype;
	}
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}
	public Integer getResultId() {
		return resultId;
	}
	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}
	public String getErrRate() {
		return errRate;
	}
	public void setErrRate(String errRate) {
		this.errRate = errRate;
	}
	public Integer getClassId() {
		return classId;
	}
	public void setClassId(Integer classId) {
		this.classId = classId;
	}
	public String getStudentIds() {
		return studentIds;
	}
	public void setStudentIds(String studentIds) {
		this.studentIds = studentIds;
	}
	public Integer getExamId() {
		return examId;
	}
	public void setExamId(Integer examId) {
		this.examId = examId;
	}
	public Integer getResultAnswer() {
		return resultAnswer;
	}
	public void setResultAnswer(Integer resultAnswer) {
		this.resultAnswer = resultAnswer;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public Integer getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getRangeId() {
        return rangeId;
    }

    public void setRangeId(Integer rangeId) {
        this.rangeId = rangeId;
    }
}
