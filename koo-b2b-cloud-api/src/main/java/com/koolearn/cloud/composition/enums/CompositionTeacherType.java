package com.koolearn.cloud.composition.enums;

/**
 * 定义教师的类型
 * 如果是学校的老师 则免费 如果是新东方的老师 则收费
 * 收费金额需要来确定
 * Created by haozipu on 2016/8/1.
 */
public enum CompositionTeacherType {

    FREE("免费老师",1),UNFREE("新东方收费老师",2);

    CompositionTeacherType(String name, int value) {
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
