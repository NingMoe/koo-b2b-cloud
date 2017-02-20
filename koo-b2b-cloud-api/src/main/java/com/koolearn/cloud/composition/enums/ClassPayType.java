package com.koolearn.cloud.composition.enums;

/**
 * Created by haozipu on 2016/8/25.
 */
public enum ClassPayType {

    ALIPAY("支付宝","80"),WEIXIN("微信","194");

    ClassPayType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    private String name;

    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
