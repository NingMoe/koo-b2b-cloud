package com.koolearn.cloud.mobi.dto;

import java.io.Serializable;

/**
 * 返回学生用户的信息
 * Created by haozipu on 2016/7/19.
 */
public class UserInfo implements Serializable {

    private Integer userId;

    private String headUrl;

    private String userName;

    private String schoolName;

    private String className;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
