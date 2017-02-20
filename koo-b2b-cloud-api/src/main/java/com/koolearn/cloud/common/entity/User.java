package com.koolearn.cloud.common.entity ;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id(strategy = GenerationType.AUTO_INCREMENT)
    private Integer id;
    /**真实姓名*/
    @Column(name="real_name")
    private String realName;
    @Column(name="user_id")
    private int userId;
    /** 姓名 */
    @Column(name = "user_name")
    private String userName;
    /** 1.l老师  2. 学生 */
    @Column(name = "type")
    private Integer type;
    /** 学生帐号 */
    @Column(name = "student_code")
    private String studentCode;
    /** 手机 */
    @Column(name = "mobile")
    private String mobile;
    /** 邮箱 */
    @Column(name = "email")
    private String email;
    /** 用户密码 */
    @Column(name = "password")
    private String password;
    /** 学校id */
    @Column(name = "school_id")
    private Integer schoolId;
    /** 学校名称 */
    @Column(name = "school_name")
    private String schoolName;
    /** ( 老师 )  省/直辖市id */
    @Column(name = "province_id")
    private Integer provinceId;
    /** ( 老师 )  省/直辖市name */
    @Column(name = "province_name")
    private String provinceName;
    /** ( 老师 )  市/市辖区id */
    @Column(name = "city_id")
    private Integer cityId;
    /** ( 老师 )  市/市辖区name */
    @Column(name = "city_name")
    private String cityName;
    /** ( 老师 )  区县id */
    @Column(name = "county_id")
    private Integer countyId;
    /** ( 老师 )  区县name */
    @Column(name = "county_name")
    private String countyName;
    /** 创建时间 */
    @Column(name = "create_time")
    private Date createTime;
    /** 更新时间 */
    @Column(name = "update_time")
    private Date updateTime;
    /** 实体状态：默认0 正常 */
    @Column(name = "status")
    private Integer status;
    @Column(name = "process")
    private Integer process;
    /**组长标识*/
    @Transient
    private Integer headman;
    /**组名*/
    @Transient
    private String teamName;
    @Transient
    private String uuid;//合心uuid
    /**小学说课用户使用系统截止日期*/
    @Column(name="end_day")
    private String endDay;
    /**小学说课用户所属学段名称集合*/
    @Column(name="range_name")
    private String rangeName;
    /** 修改人 */
    @Column(name = "updater")
    private String updater;
    /** 用户来源：10: 小学说课用户批量导入，20:教师端批量导入教师   */
    @Column(name = "source")
    private Integer source;
    /** 创建人 */
    @Column(name = "creator")
    private String creator;

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getHeadman() {
        return headman;
    }

    public void setHeadman(Integer headman) {
        this.headman = headman;
    }

    public Integer getProcess() {
        return process;
    }

    public void setProcess(Integer process) {
        this.process = process;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getCountyId() {
        return countyId;
    }

    public void setCountyId(Integer countyId) {
        this.countyId = countyId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        User s=(User)obj;
        return id.equals(s.id);
    }
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getUuid() {
        //UserEntity同时修改
        return this.getUserName();
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
