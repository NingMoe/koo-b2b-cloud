package com.koolearn.cloud.teacher.entity;

import java.io.Serializable;

/**
 * Created by fn on 2016/6/24.
 */
public class ExamNum implements Serializable {

    private int num ;
    private int classesId;
    private int studentId;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getClassesId() {
        return classesId;
    }

    public void setClassesId(int classesId) {
        this.classesId = classesId;
    }
}
