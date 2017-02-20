package com.koolearn.cloud.school.teacher.vo;

import java.io.Serializable;

/**
 * 教师分页查询返回的实体类
 * Created by fn on 2016/11/8.
 */
public class TeacherPageResultDto implements Serializable {
    private Integer userId;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 账号
     */
    private String userName;
    /**
     * 学段集合
     */
    private String rangeNameStr;
    /**
     * 学科集合
     */
    private String subjectNameStr;

    private String email;

    private String mobile;
    /**
     * 状态
     */
    private Integer status;
    /**
     * SSO用户id
     */
    private Integer ssoUserId;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRangeNameStr() {
        return rangeNameStr;
    }

    public void setRangeNameStr(String rangeNameStr) {
        this.rangeNameStr = rangeNameStr;
    }

    public String getSubjectNameStr() {
        return subjectNameStr;
    }

    public void setSubjectNameStr(String subjectNameStr) {
        this.subjectNameStr = subjectNameStr;
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
}
