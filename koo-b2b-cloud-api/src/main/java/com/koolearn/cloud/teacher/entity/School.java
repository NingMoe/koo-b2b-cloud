package com.koolearn.cloud.teacher.entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;


@Entity
@Table(name = "school")
public class School implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id(strategy = GenerationType.AUTO_INCREMENT)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "`code`")
    private String code;//学校编码
    @Column(name = "location_id")
    private Integer locationId;//地区id
    @Column(name = "grade_id")
    private String gradeId;//（eg:1,2,3）1 : 大学， 2：高中， 6:初中 ，8 ： 小学 , 9: 九年一贯制，7：职校'
    @Column(name = "logo")
    private String logo;//学校logo图片
    @Column(name = "read_system_status")
    private String readSystemStatus;//扶绥标识阅卷系统是否开通：1：开通，0：未开通
    @Column(name = "status")
    private Integer status;//业务审核结果状态：0可用、1不可用 （业务流程状态，通过延期表定时任务修改此状态）
    @Column(name="entity_status")
    private  Integer entityStatus;//字典表状态：1正常 2删除 3屏蔽
    @Column(name = "begin_time")
    private Date beginTime;//开始生效时间
    @Column(name = "end_time")
    private Date endTime; //帐号终止时间
    @Column(name = "update_time")
    private Date updateTime;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "access_key")
    private String accessKey; //合心认证key
    @Column(name = "access_secret")
    private String accessSecret;
    @Column(name = "`shortname`")
    private String shortname;//学校简称
    @Column(name = "`update_user`")
    private String adder;//学校编码
    @Transient
    private  Integer userCount;//学校用户数量（老师和学生）
    @Transient
    private List<String> range=new ArrayList<String>();//"1_小学","2_中学","3_高中"
    @Transient
    private Integer schoolStatus;//取自entityStatus字段值  1正常 2 删除 3已屏蔽
    @Transient
    private  List<String> area=new ArrayList<String>();///地区省市id
    @Transient
    private  String  areaName;//所在省市名： 北京市海淀区
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getReadSystemStatus() {
        return readSystemStatus;
    }

    public void setReadSystemStatus(String readSystemStatus) {
        this.readSystemStatus = readSystemStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public Integer getEntityStatus() {
        return entityStatus;
    }

    public void setEntityStatus(Integer entityStatus) {
        this.entityStatus = entityStatus;
    }

    public List<String> getRange() {
        return range;
    }

    public void setRange(List<String> range) {
        this.range = range;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public Integer getSchoolStatus() {
        return this.getEntityStatus();
    }

    public void setSchoolStatus(Integer schoolStatus) {
        this.schoolStatus = schoolStatus;
    }

    public List<String> getArea() {
        return area;
    }

    public void setArea(List<String> area) {
        this.area = area;
    }

    public String getAdder() {
        return adder;
    }

    public void setAdder(String adder) {
        this.adder = adder;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
