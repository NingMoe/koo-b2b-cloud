package com.koolearn.cloud.school.schoolclasses.controller.vo;

import java.io.Serializable;

/**
 * Created by fn on 2016/10/31.
 */
public class ClassesPageVo implements Serializable {

    private Integer classesId;
    private String classesName;
    private String rangeName;
    /**
     * 年级名称
     */
    private String levelName;
    /**
     * 班级编码
     */
    private String classesCode;
    /**
     * 老师人数
     */
    private int teacherNum;

    private int studentNum;
    /**
     * 班级类型名称（学科，行政）
     */
    private String classesTypeName;
    /**
     * 学科名称
     */
    private String subjectName;
    /**
     * 任课老师集合
     */
    private String teacherName;
    /**
     * 状态名称
     */
    private String statusName;

    private int status;

    public Integer getClassesId() {
        return classesId;
    }

    public void setClassesId(Integer classesId) {
        this.classesId = classesId;
    }

    public String getClassesName() {
        return classesName;
    }

    public void setClassesName(String classesName) {
        this.classesName = classesName;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getClassesCode() {
        return classesCode;
    }

    public void setClassesCode(String classesCode) {
        this.classesCode = classesCode;
    }

    public int getTeacherNum() {
        return teacherNum;
    }

    public void setTeacherNum(int teacherNum) {
        this.teacherNum = teacherNum;
    }

    public int getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(int studentNum) {
        this.studentNum = studentNum;
    }

    public String getClassesTypeName() {
        return classesTypeName;
    }

    public void setClassesTypeName(String classesTypeName) {
        this.classesTypeName = classesTypeName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
