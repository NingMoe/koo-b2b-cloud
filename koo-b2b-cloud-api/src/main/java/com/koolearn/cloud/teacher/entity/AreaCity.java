package com.koolearn.cloud.teacher.entity;

import java.io.Serializable;

/**
 * Created by fn on 2016/4/26.
 */

public class AreaCity implements Serializable {
    private int id;
    private int parentId;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
