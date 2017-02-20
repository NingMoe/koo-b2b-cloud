package com.koolearn.cloud.util;

import java.io.Serializable;

/**
 * Created by xin on 16/4/19.
 */
public class SelectDTO implements Serializable {

    private Integer id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
