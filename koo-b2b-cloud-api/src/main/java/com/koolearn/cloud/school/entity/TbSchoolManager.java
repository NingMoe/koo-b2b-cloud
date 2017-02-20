package com.koolearn.cloud.school.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import java.util.Date;


@Entity
@Table(name = "tb_school_manager")
public class TbSchoolManager implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 学校主键 */
	@Column(name = "school_id")
	private Integer schoolId;
	/** 管理员名称 */
	@Column(name = "manager_name")
	private String managerName;
	/** 管理员手机 */
	@Column(name = "manager_mobile")
	private String managerMobile;
	/** 管理员邮箱 */
	@Column(name = "manager_email")
	private String managerEmail;
	/** 密码 */
	@Column(name = "pass_word")
	private String passWord;
	/** 角色id：（用户管理列表排除管理员用户） */
	@Column(name = "role_id")
	private Integer roleId;
	/** 1激活  2 冻结 */
	@Column(name = "status")
	private Integer status;
	/** 角色拥有的所有学科集合（23108,24781,39201） */
	@Column(name = "subject_ids")
	private String subjectIds;
	/** 角色拥有的年级集合( 282921,342345,3928489) */
	@Column(name = "classes_ids")
	private String classesIds;
	/** 创建人 */
	@Column(name = "creator")
	private String creator;
	@Column(name = "create_time")
	private Date createTime;
	@Column(name = "version")
	private Integer version;
	
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
	
	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
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
	
	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getSubjectIds() {
		return subjectIds;
	}

	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}
	
	public String getClassesIds() {
		return classesIds;
	}

	public void setClassesIds(String classesIds) {
		this.classesIds = classesIds;
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
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
