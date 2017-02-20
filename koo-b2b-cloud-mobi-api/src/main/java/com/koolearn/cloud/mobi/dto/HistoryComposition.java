package com.koolearn.cloud.mobi.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 历史作文返回的数据结构
 * Created by haozipu on 2016/7/19.
 */
public class HistoryComposition implements Serializable {

    private Integer compositionId;

    private Date dateTime;

    private String teacherName;

    private String title;

    public Integer getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(Integer compositionId) {
        this.compositionId = compositionId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
