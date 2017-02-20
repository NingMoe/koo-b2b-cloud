package com.koolearn.cloud.login.dto ;
import java.io.Serializable;
import java.util.Date;

public class UserMobi implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int USER_STATUS_VALID = 0;//0 有效

	private Integer id;
	/** 姓名 */
	private String realName;
	/** 1.老师  2. 学生 */
	private Integer type;
	/** 学生帐号(sso用户名) */
	private String userName;
	/**sso用户id */
	private Integer userId;
	/** 学生学号 */
	private String studentCode;
	/** 手机 */
	private String mobile;
	/** 邮箱 */
	private String email;
	/** 老师角色专用(学生可以加入多个学校故不可用) 学校id */
	private Integer schoolId;
	/** 学校名称 */
	private String schoolName;
	/** ( 老师 )  省/直辖市id */
	private Integer provinceId;
	/** ( 老师 )  省/直辖市name */
	private String provinceName;
	/** ( 老师 )  市/市辖区id */
	private Integer cityId;
	/** ( 老师 )  市/市辖区name */
	private String cityName;
	/** ( 老师 )  区县id */
	private Integer countyId;
	/** ( 老师 )  区县name */
	private String countyName;
	/** 创建时间 */
	private Date createTime;
	/** 更新时间 */
	private Date updateTime;
	/** 实体状态：默认0 正常 */
	private Integer status;
	/** 老师：1.新注册用户  2.完善资料完成 3.教材版本完成  学生：1.新注册用户  2.学生加入班级页面 */
	private Integer process;
	/** 头像 */
	private String ico;
	/** 班级id */
	private Integer classesId;
	/** 班级名  */
	private String classesName;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getStudentCode() {
		return studentCode;
	}
	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public Integer getProcess() {
		return process;
	}
	public void setProcess(Integer process) {
		this.process = process;
	}
	public String getIco() {
		return ico;
	}
	public void setIco(String ico) {
		this.ico = ico;
	}
	public Integer getClassesId() {
		return classesId;
	}
	public void setClassesId(Integer classesId) {
		this.classesId = classesId;
	}
	public String getClassesName() {
		return classesName;
	}
	public void setClassesName(String classesName) {
		this.classesName = classesName;
	}
}
