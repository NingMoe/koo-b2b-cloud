package com.koolearn.cloud.school.authorityschool.vo;

import java.io.Serializable;

/**
 * Created by fn on 2016/11/18.
 */
public class SchoolManagerDTO implements Serializable {



    private String managerMobile;
    /**
     * 邮箱
     */
    private String managerEmail;
    /**
     * 管理者姓名
     */
    private String managerName;
    /**
     * 角色id
     */
    private Integer roleTypeId;
    /**
     * 年级集合
     */
    private String classesLevelStr;
    /**
     * 学科id集合
     */
    private String subjectIdStr;
    /**
     * 学校主键
     */
    private Integer schoolId;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 更新人
     */
    private String updater;
    /**
     * 版本
     */
    private Integer version;

    private Integer managerId;

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getManagerMobile() {
        return managerMobile;
    }

    public void setManagerMobile(String managerMobile) {
        this.managerMobile = managerMobile;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Integer getRoleTypeId() {
        return roleTypeId;
    }

    public void setRoleTypeId(Integer roleTypeId) {
        this.roleTypeId = roleTypeId;
    }

    public String getClassesLevelStr() {
        return classesLevelStr;
    }

    public void setClassesLevelStr(String classesLevelStr) {
        this.classesLevelStr = classesLevelStr;
    }

    public String getSubjectIdStr() {
        return subjectIdStr;
    }

    public void setSubjectIdStr(String subjectIdStr) {
        this.subjectIdStr = subjectIdStr;
    }
}
