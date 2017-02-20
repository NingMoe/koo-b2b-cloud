package com.koolearn.cloud.school.dto;

import com.koolearn.cloud.school.entity.SchoolPage;

import java.io.Serializable;

/**
 * 教师分页查询条件
 * Created by fn on 2016/11/8.
 */
public class TeacherPageDto extends SchoolPage implements Serializable {
    /**
     * 学段mc
     */
    private String rangeName;
    /**
     * 学科名称
     */
    private String subjectName;
    /**
     * 学科id
     */
    private Integer subjectId;
    /**
     * 状态（有效，冻结）
     */
    private Integer status;
    /**
     * SSO用户id
     */
    private Integer SsoUserId;

    private Integer schoolId;
    /**
     * 教师名称
     */
    private String teacherName;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Integer getSsoUserId() {
        return SsoUserId;
    }

    public void setSsoUserId(Integer ssoUserId) {
        SsoUserId = ssoUserId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

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

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
