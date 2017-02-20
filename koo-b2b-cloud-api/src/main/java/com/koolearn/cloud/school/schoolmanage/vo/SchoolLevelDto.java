package com.koolearn.cloud.school.schoolmanage.vo;

import java.io.Serializable;

/**
 * Created by fn on 2016/12/2.
 */
public class SchoolLevelDto implements Serializable {
    /**
     * 是否选中（ 1：选中 ）
     */
    private Integer isCheck;
    /**
     * 年级id值
     */
    private Integer levelId;
    /**
     * 年级名称
     */
    private String levelName;

    public Integer getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Integer isCheck) {
        this.isCheck = isCheck;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
