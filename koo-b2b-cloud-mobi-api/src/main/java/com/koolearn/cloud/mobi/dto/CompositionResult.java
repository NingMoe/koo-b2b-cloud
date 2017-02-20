package com.koolearn.cloud.mobi.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 手机端作文列表数据结构
 * Created by haozipu on 2016/7/19.
 */
public class CompositionResult implements Serializable {

    private Integer compositionId;

    private String compositionTitle;

    private String imgUrl;

    private Date createTime;

    private Date correctTime;

    private Integer teacherId;

    private String teacherName;

    public Integer getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(Integer compositionId) {
        this.compositionId = compositionId;
    }

    public String getCompositionTitle() {
        return compositionTitle;
    }

    public void setCompositionTitle(String compositionTitle) {
        this.compositionTitle = compositionTitle;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCorrectTime() {
        return correctTime;
    }

    public void setCorrectTime(Date correctTime) {
        this.correctTime = correctTime;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
