package com.koolearn.cloud.mobi.dto;

import java.io.Serializable;

/**
 * Created by haozipu on 2016/8/3.
 */
public class CompositionImageResult implements Serializable {


    private Integer id;
    /** 作文ID */

    private Integer compositionId;
    /** 图片地址 */

    private String imgUrl;
    /** 图片的状态 */

    private Integer status;
    /** 图片顺序 */

    private Integer imgIndex;
    /** 是批改结果图片 2 还是作文图片 1 */

    private Integer type;
    /** 如果是批改图片 则需要记录是那个订单批改的结果图片 */

    private Integer orderId;
    /** 批次ID */

    private Integer batchId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(Integer compositionId) {
        this.compositionId = compositionId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getImgIndex() {
        return imgIndex;
    }

    public void setImgIndex(Integer imgIndex) {
        this.imgIndex = imgIndex;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }
}
