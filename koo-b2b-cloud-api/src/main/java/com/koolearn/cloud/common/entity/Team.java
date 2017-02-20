package com.koolearn.cloud.common.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fn on 2016/4/11.
 */
public class Team implements Serializable {
    //小组id
    private int id;
    //组长id
    private int teamManId;
    //小组名称
    private String teamName;

    //小组学生列表
    private List< ClassesStudent > classesStudentsList;

    public int getTeamManId() {
        return teamManId;
    }

    public void setTeamManId(int teamManId) {
        this.teamManId = teamManId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<ClassesStudent> getClassesStudentsList() {
        return classesStudentsList;
    }

    public void setClassesStudentsList(List<ClassesStudent> classesStudentsList) {
        this.classesStudentsList = classesStudentsList;
    }
}
