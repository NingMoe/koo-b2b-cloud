package com.koolearn.cloud.mobi.dto;

import java.io.Serializable;

/**
 * 提交订单返回的数据结构
 * Created by haozipu on 2016/7/19.
 */
public class CompositionOrderResult implements Serializable {

    /**
     * 订单ID
     */
    private Integer orderId;

    /**
     * 本地订单编号
     */
    private String orderNo;

    /**
     * 支付中心订单号
     */
    private String payCenterOrderNo;

    /**
     * 错误标识
     */
    private Integer error;

    /**
     * 信息
     */
    private String msg;

    /**
     * 价格
     */
    private String price;

    /**
     * 支付宝更新
     */
    private String ali_partner;
    private String ali_seller_id;
    private String ali_out_trade_no;
    private String ali_subject;
    private String ali_service;
    private String ali_input_charset;
    private String ali_sign_type;
    private String ali_sign;
    private String ali_notify_url;
    private String ali_payment_type;
    private String ali_total_fee;
    private String ali_body;

    /**
     * 微信支付需要的预支付ID
     */
    private String weixinPrepayId;

    private String payType;

    private String appId;

    private String partnerId;

    private String packageStr;

    private String noncestr;

    private String timestamp;

    private String sign;

    //支付宝需要的

    private String notifyUrl;

    private String productName;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

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

    public String getPayCenterOrderNo() {
        return payCenterOrderNo;
    }

    public void setPayCenterOrderNo(String payCenterOrderNo) {
        this.payCenterOrderNo = payCenterOrderNo;
    }

    public String getWeixinPrepayId() {
        return weixinPrepayId;
    }

    public void setWeixinPrepayId(String weixinPrepayId) {
        this.weixinPrepayId = weixinPrepayId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPackageStr() {
        return packageStr;
    }

    public void setPackageStr(String packageStr) {
        this.packageStr = packageStr;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAli_partner() {
        return ali_partner;
    }

    public void setAli_partner(String ali_partner) {
        this.ali_partner = ali_partner;
    }

    public String getAli_seller_id() {
        return ali_seller_id;
    }

    public void setAli_seller_id(String ali_seller_id) {
        this.ali_seller_id = ali_seller_id;
    }

    public String getAli_out_trade_no() {
        return ali_out_trade_no;
    }

    public void setAli_out_trade_no(String ali_out_trade_no) {
        this.ali_out_trade_no = ali_out_trade_no;
    }

    public String getAli_subject() {
        return ali_subject;
    }

    public void setAli_subject(String ali_subject) {
        this.ali_subject = ali_subject;
    }

    public String getAli_service() {
        return ali_service;
    }

    public void setAli_service(String ali_service) {
        this.ali_service = ali_service;
    }

    public String getAli_input_charset() {
        return ali_input_charset;
    }

    public void setAli_input_charset(String ali_input_charset) {
        this.ali_input_charset = ali_input_charset;
    }

    public String getAli_sign_type() {
        return ali_sign_type;
    }

    public void setAli_sign_type(String ali_sign_type) {
        this.ali_sign_type = ali_sign_type;
    }

    public String getAli_sign() {
        return ali_sign;
    }

    public void setAli_sign(String ali_sign) {
        this.ali_sign = ali_sign;
    }

    public String getAli_notify_url() {
        return ali_notify_url;
    }

    public void setAli_notify_url(String ali_notify_url) {
        this.ali_notify_url = ali_notify_url;
    }

    public String getAli_payment_type() {
        return ali_payment_type;
    }

    public void setAli_payment_type(String ali_payment_type) {
        this.ali_payment_type = ali_payment_type;
    }

    public String getAli_total_fee() {
        return ali_total_fee;
    }

    public void setAli_total_fee(String ali_total_fee) {
        this.ali_total_fee = ali_total_fee;
    }

    public String getAli_body() {
        return ali_body;
    }

    public void setAli_body(String ali_body) {
        this.ali_body = ali_body;
    }
}
