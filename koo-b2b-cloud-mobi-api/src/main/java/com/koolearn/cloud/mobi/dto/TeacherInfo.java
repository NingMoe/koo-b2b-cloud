package com.koolearn.cloud.mobi.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 可选教师信息
 * Created by haozipu on 2016/7/19.
 */
public class TeacherInfo implements Serializable {

    private Integer teacherId;

    private String teacherName;

    private String headUrl;

    private BigDecimal price;

    private Integer teacherType;

    private Integer schoolLev;

    public Integer getTeacherType() {
        return teacherType;
    }

    public void setTeacherType(Integer teacherType) {
        this.teacherType = teacherType;
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

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getSchoolLev() {
        return schoolLev;
    }

    public void setSchoolLev(Integer schoolLev) {
        this.schoolLev = schoolLev;
    }
}
