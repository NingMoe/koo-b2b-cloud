package com.koolearn.cloud.teacher.entity;

import com.koolearn.cloud.common.entity.Classes;

import java.io.Serializable;

/**
 * Created by fn on 2016/4/27.
 */
public class ClassesEvent implements Serializable {

    public ClassesEvent( Classes classes ){
        this.classes = classes;
    }
    private Classes classes;

    public Classes getClasses() {
        return classes;
    }

    public void setClasses(Classes classes) {
        this.classes = classes;
    }
}
