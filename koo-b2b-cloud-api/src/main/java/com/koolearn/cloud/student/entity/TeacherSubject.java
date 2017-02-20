package com.koolearn.cloud.student.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by fn on 2016/5/24.
 */
public class TeacherSubject implements Serializable {

    private Integer subjectId;
    private String subjectName;
    private Date createDate;
    private Integer ClassesId;
    private Integer teacherId;
    private String teacherName;

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Integer getClassesId() {
        return ClassesId;
    }

    public void setClassesId(Integer classesId) {
        ClassesId = classesId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeacherSubject)) return false;

        TeacherSubject that = (TeacherSubject) o;

        if (!subjectId.equals(that.subjectId)) return false;
        if (!subjectName.equals(that.subjectName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subjectId.hashCode();
        result = 31 * result + subjectName.hashCode();
        return result;
    }
}
