package com.koolearn.cloud.composition.enums;

/**
 * 评分指标类型 普通指标 加分项 减分项
 * Created by haozipu on 2016/7/15.
 */
public enum RuleItemType {

    NORMAL_ITEM("普通指标",1),SUBTRACT_ITEM("减分项",2),ADD_ITEM("加分项",3);

    RuleItemType(String name, int value) {
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
