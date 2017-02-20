package com.koolearn.cloud.common.entity;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fn on 2016/4/5.
 */
public class HomePageClassesExam implements Serializable{

    private Integer classId;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 学科班，行政班类型名称
     */
    private String classTypeName;//
    /**
     * 学科班，行政班类型
     */
    private int classType;//
    /**
     * 人数
     */
    private int pepoleNum;//
    private int rangeId;//
    private int subjectId;
    /**
     * 是否有最新动态（ 0:没有，1:有）
     */
    private int dynamicStatus;
    /**
     * 学科名称
     */
    private String subjectName;
    /**
     * 学段名称
     */
    private String rangeName;
    /**
     * 该班级下的所有课程和作业
     */
    private List<ClassesExam> examClassesList;

    public int getDynamicStatus() {
        return dynamicStatus;
    }

    public void setDynamicStatus(int dynamicStatus) {
        this.dynamicStatus = dynamicStatus;
    }

    public int getRangeId() {
        return rangeId;
    }

    public void setRangeId(int rangeId) {
        this.rangeId = rangeId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getClassTypeName() {
        return classTypeName;
    }

    public void setClassTypeName(String classTypeName) {
        this.classTypeName = classTypeName;
    }

    public Integer getClassId() {
        return classId;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getClassType() {
        return classType;
    }

    public void setClassType(int classType) {
        this.classType = classType;
    }

    public int getPepoleNum() {
        return pepoleNum;
    }

    public void setPepoleNum(int pepoleNum) {
        this.pepoleNum = pepoleNum;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<ClassesExam> getExamClassesList() {
        return examClassesList;
    }

    public void setExamClassesList(List<ClassesExam> examClassesList) {
        this.examClassesList = examClassesList;
    }
}
