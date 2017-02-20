package com.koolearn.cloud.mobi.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by haozipu on 2016/8/3.
 */
public class CompositionReprotResult implements Serializable {


    private Integer id;
    /** 作文ID */

    private Integer compositionId;
    /** 订单ID */

    private Integer orderId;
    /** 批改总得分 */

    private String score;
    /** 本批改使用的规则集ID */

    private Integer ruleId;
    /** 文本报告 json格式 {'审题':'很好','思路':'顺畅'} */

    private String report;
    /** 报告生成时间 */

    private Date createTime;

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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
