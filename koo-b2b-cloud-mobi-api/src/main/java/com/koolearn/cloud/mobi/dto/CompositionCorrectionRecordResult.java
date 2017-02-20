package com.koolearn.cloud.mobi.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by haozipu on 2016/8/3.
 */
public class CompositionCorrectionRecordResult implements Serializable {

    private Integer id;
    /** 订单ID */

    private Integer orderId;
    /** 作文ID */

    private Integer compositionId;
    /** 批改类型 错字 病句等 */

    private Integer type;
    /** 批注 */

    private String remark;
    /** 对应图片坐标 {x:10,y:10} */

    private String picIndex;
    /** 批改记录所在图片ID */

    private Integer picId;
    /** 顺序 */

    private Integer picOrder;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(Integer compositionId) {
        this.compositionId = compositionId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPicIndex() {
        return picIndex;
    }

    public void setPicIndex(String picIndex) {
        this.picIndex = picIndex;
    }

    public Integer getPicId() {
        return picId;
    }

    public void setPicId(Integer picId) {
        this.picId = picId;
    }

    public Integer getPicOrder() {
        return picOrder;
    }

    public void setPicOrder(Integer picOrder) {
        this.picOrder = picOrder;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
