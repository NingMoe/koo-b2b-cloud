package com.koolearn.cloud.classRoom.dto;

import com.koolearn.cloud.exam.entity.TpExam;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xin on 16/4/20.
 */
public class ClassRoomBean implements Serializable {

    private Integer classRoomId;//课堂id
    private String classRoomName;//课堂名称
    private Integer subjectId;//学科
    private Integer rangeId;//学段
    private Integer bookVersion;//教材id
    private Integer obligatoryId;// 必修id
    private Integer tagId;// 进度点id
    private Integer[] classesIds;// 班级id
    private String endTime;// 截止日期
    private String startTime; //开放日期
    List<ClassRoomAttachment> attachments; //附件
    private Integer submitType;//提交类型

    public String getClassRoomName() {
        return classRoomName;
    }

    public void setClassRoomName(String classRoomName) {
        this.classRoomName = classRoomName;
    }

    public Integer getBookVersion() {
        return bookVersion;
    }

    public void setBookVersion(Integer bookVersion) {
        this.bookVersion = bookVersion;
    }

    public Integer getSubjectId() {
        return subjectId;
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

    public List<ClassRoomAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ClassRoomAttachment> attachments) {
        this.attachments = attachments;
    }

    public Integer getSubmitType() {
        return submitType;
    }

    public void setSubmitType(Integer submitType) {
        this.submitType = submitType;
    }

    public Integer getClassRoomId() {
        return classRoomId;
    }

    public void setClassRoomId(Integer classRoomId) {
        this.classRoomId = classRoomId;
    }

    public Integer getObligatoryId() {
        return obligatoryId;
    }

    public void setObligatoryId(Integer obligatoryId) {
        this.obligatoryId = obligatoryId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public Integer[] getClassesIds() {
        return classesIds;
    }

    public void setClassesIds(Integer[] classesIds) {
        this.classesIds = classesIds;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

}
