package com.koolearn.cloud.exam.entity ;

import java.io.Serializable;
import java.util.Date;

import com.koolearn.cloud.util.ParseDate;

public class ExamQueryDto implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	/** 考试状态 0.新建（未审核） 1.有效（审核通过） 2.删除 3.作废 */
	private String status;
	private String statusComplete;
	/** 老师id */
	private int teacherId;
	
	private Integer userId;// 学生id
	private Integer classId;//班级id
	private Integer completeType;//COMPLETE_TYPE_LAST_NEW   最新(未完成)or已完成  1 最新（未完成） 2.已完成
	private Integer examType;     //EXAMTYPE_MYEXAM         1考试(在线统考) 2随堂测评   3在线练习    
	private Integer schoolId;//学校id
	private Integer paperType;//试卷分类标签id
	private String examIds;//考试id集合
	private Integer examId;
	private Integer isFrom;//1新东方 2我的上传 3本校共享  4院校（本校所有试卷或题）
	
	private Integer paperId;//试卷id
	
	public String getExamIds() {
		return examIds;
	}
	public void setExamIds(String examIds) {
		this.examIds = examIds;
	}
	public Integer getIsFrom() {
		return isFrom;
	}
	public void setIsFrom(Integer isFrom) {
		this.isFrom = isFrom;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getClassId() {
		return classId;
	}
	public void setClassId(Integer classId) {
		this.classId = classId;
	}
	public Integer getCompleteType() {
		return completeType;
	}
	public void setCompleteType(Integer completeType) {
		this.completeType = completeType;
	}
	public Integer getExamType() {
		return examType;
	}
	public void setExamType(Integer examType) {
		this.examType = examType;
	}
	public Integer getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	public Integer getPaperType() {
		return paperType;
	}
	public void setPaperType(Integer paperType) {
		this.paperType = paperType;
	}
	public Integer getPaperId() {
		return paperId;
	}
	public void setPaperId(Integer paperId) {
		this.paperId = paperId;
	}
	public Integer getExamId() {
		return examId;
	}
	public void setExamId(Integer examId) {
		this.examId = examId;
	}
    public String nowDate;

    public String getNowDate() {
        return ParseDate.formatByDate(new Date(), ParseDate.DATE_FORMAT_YYYYMMDD_HHMMSS);
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }
}
