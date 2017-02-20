package com.koolearn.cloud.composition.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by haozipu on 2016/7/19.
 */
public class CompositionOrderDto implements Serializable {


    private Integer id;
    /** 订单创建时间 */
    private Date createTime;
    /** 订单归属教师ID */
    private Integer teacherId;
    /** 作文ID */
    private Integer compositionId;
    /** 作文ID */
    private Integer userId;
    /** 批改订单金额 */
    private BigDecimal price;
    /** 订单状态 */
    private Integer status;
    /** 作文类型 */
    private Integer type;
    /** 学段 */
    private Integer schoolLev;
    /** 适用地区 */
    private String area;
    /** 订单编号 业务规则 */
    private String orderCode;
    /** 支付中心订单号 */
    private String payCenterOrderId;
    /** 支付方式 */
    private String payType;
    private Integer viewFlag;
    private String compositionTitle;

    private Date correctTime;

    private String teacherName;

    private String studentName;

    private String className;

    private String pic;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCompositionTitle() {
        return compositionTitle;
    }

    public void setCompositionTitle(String compositionTitle) {
        this.compositionTitle = compositionTitle;
    }

    public Date getCorrectTime() {
        return correctTime;
    }

    public void setCorrectTime(Date correctTime) {
        this.correctTime = correctTime;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(Integer compositionId) {
        this.compositionId = compositionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getSchoolLev() {
        return schoolLev;
    }

    public void setSchoolLev(Integer schoolLev) {
        this.schoolLev = schoolLev;
    }



    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getPayCenterOrderId() {
        return payCenterOrderId;
    }

    public void setPayCenterOrderId(String payCenterOrderId) {
        this.payCenterOrderId = payCenterOrderId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Integer getViewFlag() {
        return viewFlag;
    }

    public void setViewFlag(Integer viewFlag) {
        this.viewFlag = viewFlag;
    }
}
