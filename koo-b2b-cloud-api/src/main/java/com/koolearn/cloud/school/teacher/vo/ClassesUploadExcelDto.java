package com.koolearn.cloud.school.teacher.vo;

import java.io.Serializable;

/**
 * 班级导入excel 返回的实体提示信息类
 * Created by fn on 2016/11/16.
 */
public class ClassesUploadExcelDto implements Serializable  {
    /**
     * 班级名称
     */
    private String classesName;
    /**
     * 入学年份
     */
    private String year;
    /**
     * 学段名称
     */
    private String rangeName;
    /**
     * 班级类型
     */
    private String classesType;
    /**
     * 学科名称
     */
    private String subjectName;
    /**
     * 异常信息
     */
    private String errorInfo;
    private int line;

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getClassesName() {
        return classesName;
    }

    public void setClassesName(String classesName) {
        this.classesName = classesName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public String getClassesType() {
        return classesType;
    }

    public void setClassesType(String classesType) {
        this.classesType = classesType;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
}
