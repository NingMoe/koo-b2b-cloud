package com.koolearn.cloud.composition.enums;

/**
 * 作文图片类型
 * Created by haozipu on 2016/7/15.
 */
public enum CompositionImageType {

    ORIGIN_IMG("作文图片",1),CORRECT_IMG("批改图片",2);

    CompositionImageType(String name, int value) {
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
