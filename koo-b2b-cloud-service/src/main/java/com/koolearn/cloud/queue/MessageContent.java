package com.koolearn.cloud.queue;

import com.koolearn.cloud.common.entity.User;
import com.koolearn.cloud.login.entity.UserEntity;

import java.io.Serializable;

/**
 * Created by fn on 2016/7/26.
 */
public class MessageContent implements Serializable {

    private int classesId ;
    private UserEntity userEntity;
    private User user;
    private String studentStr;
    private String classNo;

    public String getStudentStr() {
        return studentStr;
    }

    public void setStudentStr(String studentStr) {
        this.studentStr = studentStr;
    }

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    public int getClassesId() {
        return classesId;
    }

    public void setClassesId(int classesId) {
        this.classesId = classesId;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
