package com.koolearn.cloud.mobi.dto;

import java.io.Serializable;

/**
 * Created by haozipu on 2016/8/16.
 */
public class OrderStatusResult implements Serializable {

    private Integer orderNo;

    private Integer orderStatus;

    private Integer error;

    private String msg;

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
