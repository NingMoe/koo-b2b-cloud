package com.koolearn.cloud.composition.enums;

/**
 * 作文类型 议论1 和 记叙2
 * Created by haozipu on 2016/7/15.
 */
public enum CompositionType {

    DESCRIPTIVE("议论文",1),NARRATIVE("记叙文",2);

    CompositionType(String name, int value) {
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
