package com.koolearn.cloud.composition.enums;

/**
 * 订单状态
 * Created by haozipu on 2016/7/15.
 */
public enum CompositionOrderStatus {

    NO_PAY("未支付",1),PAYED("已支付",2),CORRECTED("已批改",3);

    CompositionOrderStatus(String name, int value) {
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
