package com.koolearn.cloud.school.dto;

import com.koolearn.cloud.school.entity.SchoolPage;

import java.io.Serializable;

/**
 * Created by fn on 2016/11/4.
 */
public class ClassTeacherPageDto extends SchoolPage implements Serializable {
    /**
     * SSO用户id标识
     */
    private Integer ssoUserId;
    /**
     * 用户主键
     */
    private Integer userId;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 学科
     */
    private String subjectStr;
    /**
     * 邮箱
     */
    private String email;

    private String mobile;
    /**
     * 用户账号
     */
    private String userName;
    /**
     * 学生学号
     */
    private String studentCode;

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public Integer getSsoUserId() {
        return ssoUserId;
    }

    public void setSsoUserId(Integer ssoUserId) {
        this.ssoUserId = ssoUserId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSubjectStr() {
        return subjectStr;
    }

    public void setSubjectStr(String subjectStr) {
        this.subjectStr = subjectStr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
