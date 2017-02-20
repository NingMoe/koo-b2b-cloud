package com.koolearn.cloud.school.student.vo;

import com.koolearn.cloud.school.entity.SchoolPage;

import java.io.Serializable;

/**
 * Created by fn on 2016/11/10.
 */
public class StudentPageVo extends SchoolPage implements Serializable {
    /**
     * 学段名称
     */
    private String rangeName;
    /**
     * 年级
     */
    private Integer level;

    private String studentName;
    /**
     * 学校id
     */
    private Integer schoolId;

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
