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
import java.util.Date;
import java.util.Date;


/**
学校管理表
*/
@Entity
@Table(name = "tb_school_extend")
public class TbSchoolExtend implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 学校id主键 */
	@Column(name = "school_id")
	private Integer schoolId;
	/** 学校地址 */
	@Column(name = "address")
	private String address;
	/** 联系人 */
	@Column(name = "contacter")
	private String contacter;
	/** 联系人电话 */
	@Column(name = "contacter_mobile")
	private String contacterMobile;
	/** 联系人邮箱 */
	@Column(name = "contacter_mail")
	private String contacterMail;
	/** 代理商id */
	@Column(name = "agent_id")
	private Integer agentId;
	/** 学校图片logo地址 */
	@Column(name = "logo_url")
	private String logoUrl;
	/** 账号类型,1：试用账号，2： 正式账号 */
	@Column(name = "type")
	private Integer type;
	/** 开始生效时间 */
	@Column(name = "begin_time")
	private Date beginTime;
	/** 账号终止时间 */
	@Column(name = "end_time")
	private Date endTime;
	/** 最大在线人数 */
	@Column(name = "max_online")
	private Integer maxOnline;

	@Column(name = "remark")
	private String remark;
	@Column(name = "version")
	private Integer version;
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
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
	
	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}
	
	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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
	
	public Integer getMaxOnline() {
		return maxOnline;
	}

	public void setMaxOnline(Integer maxOnline) {
		this.maxOnline = maxOnline;
	}
	

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
