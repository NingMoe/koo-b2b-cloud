package com.koolearn.cloud.teacher.entity;

import java.io.Serializable;

/**
 * Created by fn on 2016/5/11.
 */
public class TeacherBookVer implements Serializable {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
