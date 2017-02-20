package com.koolearn.cloud.school.student.vo;

import scala.Int;

import java.io.Serializable;

/**
 * Created by fn on 2016/11/10.
 */
public class StudentPageDto implements Serializable {

    private Integer userId;
    /**
     * SSO 端用户id
     */
    private Integer ssoUserId;//

    private String userName;
    /**
     * 学生学号
     */
    private String studentCode;
    /**
     * 班级名称
     */
    private String classesName;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 学生真实姓名
     */
    private String realName;
    /**
     * 0 正常 ,1:删除, 10: 冻结
     */
    private Integer status;
    /**
     * 班级全称
     */
    private String fullName;

    public Integer getSsoUserId() {
        return ssoUserId;
    }

    public void setSsoUserId(Integer ssoUserId) {
        this.ssoUserId = ssoUserId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getClassesName() {
        return classesName;
    }

    public void setClassesName(String classesName) {
        this.classesName = classesName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
