package com.koolearn.cloud.school.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import java.util.Date;


/**
学校运营人员基本表
*/
@Entity
@Table(name = "tb_school_maintain_user")
public class TbSchoolMaintainUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 学校id主键 */
	@Column(name = "shcool_id")
	private Integer shcoolId;
	@Column(name = "name")
	private String name;
	/** 性别：1：男，2：女 */
	@Column(name = "sex")
	private Integer sex;
	/** 登录账户 */
	@Column(name = "account_number")
	private String accountNumber;
	/** 密码 */
	@Column(name = "pass_word")
	private String passWord;
	/** 邮箱 */
	@Column(name = "email")
	private String email;
	/** 移动电话 */
	@Column(name = "mobile")
	private String mobile;
	/** 创建时间 */
	@Column(name = "create_time")
	private Date createTime;
	@Column(name = "creator")
	private String creator;
	@Column(name = "version")
	private Integer version;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getShcoolId() {
		return shcoolId;
	}

	public void setShcoolId(Integer shcoolId) {
		this.shcoolId = shcoolId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
