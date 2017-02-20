package com.koolearn.cloud.mobi.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 消费记录返回的数据结构
 * Created by haozipu on 2016/7/19.
 */
public class PurchaseHistory implements Serializable {

    private Integer orderId;

    private String orderNo;

    private Date createTime;

    private String userName;

    private String teacherName;

    private String compositionTitle;

    private BigDecimal price;

    private String payType;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCompositionTitle() {
        return compositionTitle;
    }

    public void setCompositionTitle(String compositionTitle) {
        this.compositionTitle = compositionTitle;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
