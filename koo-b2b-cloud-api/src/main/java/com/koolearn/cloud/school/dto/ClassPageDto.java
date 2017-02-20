package com.koolearn.cloud.school.dto;

import com.alibaba.dubbo.container.page.Page;
import com.koolearn.cloud.common.entity.Pager;
import com.koolearn.cloud.school.entity.SchoolPage;
import com.koolearn.cloud.util.PagerBean;

import java.io.Serializable;

/**
 * 班级查询分页实体
 * Created by fn on 2016/10/31.
 */
public class ClassPageDto extends SchoolPage implements Serializable {
    /**
     * 班级主键
     */
    private Integer classesId;
    /**
     * 学段id
     */
    private Integer rangeId;
    /**
     * 学段名称
     */
    private String rangeName;
    /**
     * 班级id，全部为0
     */
    private Integer classesLevel;
    /**
     * 年级集合（11,12,13,14,15）
     */
    private String classesLevelStr;
    /**
     * 状态（ 有效 已毕业  封闭 ）
     */
    private Integer status;//（ 0:有效，1：已毕业，10：封闭 ）
    /**
     * 班级类型（ 行政班， 学科班 ，全部）
     */
    private Integer type;
    /**
     * 学校id
     */
    private Integer schoolId;

    public String getClassesLevelStr() {
        return classesLevelStr;
    }

    public void setClassesLevelStr(String classesLevelStr) {
        this.classesLevelStr = classesLevelStr;
    }

    public Integer getClassesId() {
        return classesId;
    }

    public void setClassesId(Integer classesId) {
        this.classesId = classesId;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public Integer getRangeId() {
        return rangeId;
    }

    public void setRangeId(Integer rangeId) {
        this.rangeId = rangeId;
    }

    public Integer getClassesLevel() {
        return classesLevel;
    }

    public void setClassesLevel(Integer classesLevel) {
        this.classesLevel = classesLevel;
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
}
