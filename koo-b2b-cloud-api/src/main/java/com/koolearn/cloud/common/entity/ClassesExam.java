package com.koolearn.cloud.common.entity;

import java.io.Serializable;

/**
 * Created by fn on 2016/4/5.
 */
public class ClassesExam implements Serializable {

    private int id;//班级ID
    private int classesId;//班级ID
    private int examId;//作业id
    private int examType;//作业类型（ 课堂，作业 ） 0作业、1考试   2课堂   20课堂作业'
    private String examName;//作业名称

    private int readNum;//作业未浏览过的数量
    private int teacherView;// 1.老师未浏览  2.老师已浏览

    public int getTeacherView() {
        return teacherView;
    }

    public void setTeacherView(int teacherView) {
        this.teacherView = teacherView;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassesId() {
        return classesId;
    }

    public void setClassesId(int classesId) {
        this.classesId = classesId;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public int getExamType() {
        return examType;
    }

    public void setExamType(int examType) {
        this.examType = examType;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassesExam)) return false;

        ClassesExam that = (ClassesExam) o;

        if (classesId != that.classesId) return false;
        if (examId != that.examId) return false;
        if (examType != that.examType) return false;
        if (id != that.id) return false;
        if (examName != null ? !examName.equals(that.examName) : that.examName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + classesId;
        result = 31 * result + examId;
        result = 31 * result + examType;
        result = 31 * result + (examName != null ? examName.hashCode() : 0);
        return result;
    }
}
