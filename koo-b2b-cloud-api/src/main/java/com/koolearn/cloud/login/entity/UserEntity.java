	package com.koolearn.cloud.login.entity ;
import java.io.Serializable;

import com.koolearn.cloud.common.entity.TeacherBookVersion;
import com.koolearn.cloud.teacher.entity.School;
import com.koolearn.cloud.util.GlobalConstant;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Transient;

import java.util.ArrayList;
import java.util.Date;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String USER_REDIS_KEY = "cloudUser";
	/*USER_PROCESS 老师：1.新注册用户  2.完善资料完成 3.教材版本完成  学生：1.新注册用户  2.学生加入班级完成*/
	public static final int USER_PROCESS_ONE = 1;
	public static final int USER_PROCESS_TWO = 2;
	public static final int USER_PROCESS_THREE = 3;
	public static final int USER_TYPE_TEACHER = 1;//老师角色 
	public static final int USER_TYPE_STUDENT = 2;//学生角色
	public static final int USER_STATUS_VALID = 0;//0 有效
    @Transient
    public boolean fusui=false;//是否是扶绥地区用户,默认不是

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 姓名 */
	@Column(name = "real_name")
	private String realName;
	/** 1.老师  2. 学生 */
	@Column(name = "type")
	private Integer type;
	/** 学生帐号(sso用户名) */
	@Column(name = "user_name")
	private String userName;
	/**sso用户id */
	@Column(name = "user_id")
	private Integer userId;
	/** 学生学号 */
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
	/** 老师角色专用(学生可以加入多个学校故不可用) 学校id */
	@Column(name = "school_id")
	private Integer schoolId;
	/** 学生角色专用   学校id,学校id…… */
	@Transient
	private String schoolIdStr;
	/** 学校名称 */
	@Column(name = "school_name")
	private String schoolName;
	/** ( 老师 )  省/直辖市id */
	@Column(name = "province_id")
	private Integer provinceId;
	/** ( 老师 )  省/直辖市name */
	@Column(name = "province_name")
	private String provinceName;
	/** ( 老师 )  市/市辖区id 数据是第三级区域id*/
	@Column(name = "city_id")
	private Integer cityId;
	/** ( 老师 )  市/市辖区name */
	@Column(name = "city_name")
	private String cityName;
	/** ( 老师 )  区县id 弃用 */
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
	/** 老师：1.新注册用户  2.完善资料完成 3.教材版本完成  学生：1.新注册用户  2.学生加入班级页面 */
	@Column(name = "process")
	private Integer process;
	/** 头像 */
	@Column(name = "ico")
	private String ico;
    /** 是否进行过密码重置*/
    @Column(name = "update_password_status")
    private String updatePasswordStatus;

	@Transient
	private Integer isreply;
	@Transient
	private Integer classesId;
	@Transient
	private Integer resultId;
	@Transient
	private String reply;
	/** 班级名  */
	@Transient
	private String classesName;
    @Transient
    private String uuid;//合心uuid

    public String getUpdatePasswordStatus() {
        return updatePasswordStatus;
    }

    public void setUpdatePasswordStatus(String updatePasswordStatus) {
        this.updatePasswordStatus = updatePasswordStatus;
    }

    public String getClassesName() {
		return classesName;
	}

	public void setClassesName(String classesName) {
		this.classesName = classesName;
	}

    @Transient
    private List<School> schoolList=new ArrayList<School>();
   @Transient
    private List<School> fusuiSchoolList=new ArrayList<School>();
   @Transient
    private  List<TeacherBookVersion>  teacherBookVersionList=new ArrayList<TeacherBookVersion>();
	public String getSchoolIdStr() {
		return schoolIdStr;
	}

	public void setSchoolIdStr(String schoolIdStr) {
		this.schoolIdStr = schoolIdStr;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public Integer getResultId() {
		return resultId;
	}

	public void setResultId(Integer resultId) {
		this.resultId = resultId;
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

	public Integer getIsreply() {
		return isreply;
	}

	public void setIsreply(Integer isreply) {
		this.isreply = isreply;
	}

	public Integer getProcess() {
		return process;
	}

	public void setProcess(Integer process) {
		this.process = process;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

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

    public boolean isFusui() {
        return fusui;
    }

    public void setFusui(boolean fusui) {
        this.fusui = fusui;
    }

    public List<School> getSchoolList() {
        return schoolList;
    }

    public void setSchoolList(List<School> schoolList) {
        this.schoolList = schoolList;
    }

    public List<School> getFusuiSchoolList() {
        return fusuiSchoolList;
    }

    public void setFusuiSchoolList(List<School> fusuiSchoolList) {
        this.fusuiSchoolList = fusuiSchoolList;
    }

    public List<TeacherBookVersion> getTeacherBookVersionList() {
        return teacherBookVersionList;
    }

    public void setTeacherBookVersionList(List<TeacherBookVersion> teacherBookVersionList) {
        this.teacherBookVersionList = teacherBookVersionList;
    }

    public String getUuid() {
        //User同时修改
        return this.getUserName();
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
