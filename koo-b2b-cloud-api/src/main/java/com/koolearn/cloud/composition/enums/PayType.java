package com.koolearn.cloud.composition.enums;

/**
 * Created by haozipu on 2016/7/19.
 */
public enum PayType {

    ALIPAY("支付宝","1000"),WEIXIN("微信","2000");

    PayType(String name, String value) {
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
