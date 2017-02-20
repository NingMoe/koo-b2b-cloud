package com.koolearn.cloud.teacher.entity;

import java.io.Serializable;

/**
 * Created by dongfangnan on 2016/3/30.
 */
public class TeacherInfo implements Serializable {

    private int id;
    private int provinceId ;
    private String provinceName;
    private int cityId;
    private String cityName;
    private int schoolId;
    private String schoolName;
    private int rangeId ;//学段ID
    private String rangeName ;//学段名字
    private int subjectId ;//学科ID
    private String subjectName;//学科名字
    private int teacherBookVersionId;//teacher_book_version主键

    public int getTeacherBookVersionId() {
        return teacherBookVersionId;
    }

    public void setTeacherBookVersionId(int teacherBookVersionId) {
        this.teacherBookVersionId = teacherBookVersionId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getRangeId() {
        return rangeId;
    }

    public void setRangeId(int rangeId) {
        this.rangeId = rangeId;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
