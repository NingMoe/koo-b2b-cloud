package com.koolearn.cloud.composition.enums;

/**
 * 高中 1 初中2
 * Created by haozipu on 2016/7/15.
 */
public enum SchoolLev {

    HIGH_SCHOO("高中",1),MIDDLE_SCHOOL("初中",2);

    SchoolLev(String name, int value) {
        this.name = name;
        this.value = value;
    }

    private String name;

    private int value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
