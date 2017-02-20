package com.koolearn.cloud.school.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import java.util.Date;
import java.util.Date;
import java.util.Date;


/**
 学校延期历史信息记录表(记录学校模块开通信息)
 定时任务要把当前生效到纪录此表信息更新到  school和shcool_extend表中
 */
@Entity
@Table(name = "tb_school_normal_history")
public class TbSchoolNormalHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 学校主键 */
	@Column(name = "school_id")
	private Integer schoolId;
	/** 生效开始时间 */
	@Column(name = "begin_time")
	private Date beginTime;
	/** 学校开通账号结束时间 */
	@Column(name = "end_time")
	private Date endTime;
	/** 财务审核状态：1审核中(添加学校提交财务审核) 2审核通过 3驳回（财务审核不通过） */
	@Column(name = "status")
	private Integer status;
	/** 账号类型（1:试用，2：付费） */
	@Column(name = "type")
	private Integer type;
	/** 允许同时在线人数 */
	@Column(name = "online_num")
	private Integer onlineNum;
	/** 教师端版本类型 */
	@Column(name = "teacher_version_type")
	private String teacherVersionType;
	/** 生端版本 */
	@Column(name = "student_version_type")
	private String studentVersionType;
	/** 学校版本 */
	@Column(name = "school_version_type")
	private String schoolVersionType;
	/** 在线资源版 */
	@Column(name = "online_version_type")
	private String onlineVersionType;
	/** 创建人 */
	@Column(name = "creator")
	private String creator;
	@Column(name = "create_time")
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

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

	public Integer getOnlineNum() {
		return onlineNum;
	}

	public void setOnlineNum(Integer onlineNum) {
		this.onlineNum = onlineNum;
	}

	public String getTeacherVersionType() {
		return teacherVersionType;
	}

	public void setTeacherVersionType(String teacherVersionType) {
		this.teacherVersionType = teacherVersionType;
	}

	public String getStudentVersionType() {
		return studentVersionType;
	}

	public void setStudentVersionType(String studentVersionType) {
		this.studentVersionType = studentVersionType;
	}

	public String getSchoolVersionType() {
		return schoolVersionType;
	}

	public void setSchoolVersionType(String schoolVersionType) {
		this.schoolVersionType = schoolVersionType;
	}

	public String getOnlineVersionType() {
		return onlineVersionType;
	}

	public void setOnlineVersionType(String onlineVersionType) {
		this.onlineVersionType = onlineVersionType;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
