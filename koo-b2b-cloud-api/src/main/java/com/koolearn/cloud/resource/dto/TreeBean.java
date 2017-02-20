package com.koolearn.cloud.resource.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xin on 16/4/1.
 */
public class TreeBean implements Serializable {

    private int pId;
    private int id;
    private String name;
    private boolean isParent = true;
    private List<TreeBean> child;

    public boolean getisParent(){
        return isParent;
    }


    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

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

    public List<TreeBean> getChild() {
        return child;
    }

    public void setChild(List<TreeBean> child) {
        this.child = child;
    }
}
