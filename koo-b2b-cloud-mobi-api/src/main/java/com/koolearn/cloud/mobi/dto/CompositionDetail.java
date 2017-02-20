package com.koolearn.cloud.mobi.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 作文详情返回的数据结构
 * Created by haozipu on 2016/7/19.
 */
public class CompositionDetail implements Serializable {

    private Integer compositionId;

    private String compositionTitle;

    private String teacherName;

    private Date createTime;

    private Date correctTime;

    private Integer status;

    private CompositionReprotResult compositionReport;

    private List<CompositionImageResult> images;

    private List<CompositionCorrectionRecordResult> compositionCorrectionRecords;


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

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
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

    public CompositionReprotResult getCompositionReport() {
        return compositionReport;
    }

    public void setCompositionReport(CompositionReprotResult compositionReport) {
        this.compositionReport = compositionReport;
    }

    public List<CompositionImageResult> getImages() {
        return images;
    }

    public void setImages(List<CompositionImageResult> images) {
        this.images = images;
    }

    public List<CompositionCorrectionRecordResult> getCompositionCorrectionRecords() {
        return compositionCorrectionRecords;
    }

    public void setCompositionCorrectionRecords(List<CompositionCorrectionRecordResult> compositionCorrectionRecords) {
        this.compositionCorrectionRecords = compositionCorrectionRecords;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
