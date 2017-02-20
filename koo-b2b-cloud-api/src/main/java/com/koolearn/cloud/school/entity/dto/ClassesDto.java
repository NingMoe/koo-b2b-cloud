package com.koolearn.cloud.school.entity.dto;

import java.io.Serializable;

/**
 * Created by fn on 2016/10/31.
 */
public class ClassesDto implements Serializable {

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
