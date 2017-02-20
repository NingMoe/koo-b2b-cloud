package com.koolearn.cloud.school.schoolmanage.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 学校管理展示信息
 * Created by fn on 2016/11/22.
 */
public class SchoolPowerDto implements Serializable {

    /**
     * 标识是否已经升级（ 1：标识已经升级）
     */
    private Integer hadUpClasses;
    /**
     * 标识是否已经毕业（ 1：标识已经升级）
     */
    private Integer hadGrade;
    /**
     * 最大在线人数
     */
    private Integer maxOnLine;
    /**
     * 包含的学段信息
     */
    private String rangeInfo;
    /**
     * 开始生效时间
     */
    private String beginTime;
    /**
     * 账号终止时间
     */
    private String endTime;
    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 管理员姓名列表
     */
    private List< String > managerNameList;
    /**
     * 自动毕业年级level
     */
    private List< Integer > aotoGradeLevelValueList;
    /**
     * 联系人姓名
     */
    private String contacter;
    /**
     * 联系人手机
     */
    private String contacterMobile;
    /**
     * 联系人邮件
     */
    private String contacterMail;
    /**
     * 年级自动升级日期
     */
    private String autoUpTime;
    /**
     * 年级自动毕业日期
     */
    private String autoGraduateTime;
    /**
     * 自动毕业年级
     */
    private String classGraduateLevel;
    /**
     * 学校主键
     */
    private Integer schoolId;
    /**
     * 操作人
     */
    private String updater;
    /**
     * 所有学段下包含的所有年级
     */
    private Map< String , Map<String,SchoolLevelDto >> rangedLevelMap;

    public Integer getHadUpClasses() {
        return hadUpClasses;
    }

    public void setHadUpClasses(Integer hadUpClasses) {
        this.hadUpClasses = hadUpClasses;
    }

    public Integer getHadGrade() {
        return hadGrade;
    }

    public void setHadGrade(Integer hadGrade) {
        this.hadGrade = hadGrade;
    }

    public List<Integer> getAotoGradeLevelValueList() {
        return aotoGradeLevelValueList;
    }

    public void setAotoGradeLevelValueList(List<Integer> aotoGradeLevelValueList) {
        this.aotoGradeLevelValueList = aotoGradeLevelValueList;
    }

    public Map<String, Map<String, SchoolLevelDto>> getRangedLevelMap() {
        return rangedLevelMap;
    }

    public void setRangedLevelMap(Map<String, Map<String, SchoolLevelDto>> rangedLevelMap) {
        this.rangedLevelMap = rangedLevelMap;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getAutoUpTime() {
        return autoUpTime;
    }

    public void setAutoUpTime(String autoUpTime) {
        this.autoUpTime = autoUpTime;
    }

    public String getAutoGraduateTime() {
        return autoGraduateTime;
    }

    public void setAutoGraduateTime(String autoGraduateTime) {
        this.autoGraduateTime = autoGraduateTime;
    }

    public String getClassGraduateLevel() {
        return classGraduateLevel;
    }

    public void setClassGraduateLevel(String classGraduateLevel) {
        this.classGraduateLevel = classGraduateLevel;
    }

    public Integer getMaxOnLine() {
        return maxOnLine;
    }

    public void setMaxOnLine(Integer maxOnLine) {
        this.maxOnLine = maxOnLine;
    }

    public String getRangeInfo() {
        return rangeInfo;
    }

    public void setRangeInfo(String rangeInfo) {
        this.rangeInfo = rangeInfo;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public List<String> getManagerNameList() {
        return managerNameList;
    }

    public void setManagerNameList(List<String> managerNameList) {
        this.managerNameList = managerNameList;
    }

    public String getContacter() {
        return contacter;
    }

    public void setContacter(String contacter) {
        this.contacter = contacter;
    }

    public String getContacterMobile() {
        return contacterMobile;
    }

    public void setContacterMobile(String contacterMobile) {
        this.contacterMobile = contacterMobile;
    }

    public String getContacterMail() {
        return contacterMail;
    }

    public void setContacterMail(String contacterMail) {
        this.contacterMail = contacterMail;
    }
}
