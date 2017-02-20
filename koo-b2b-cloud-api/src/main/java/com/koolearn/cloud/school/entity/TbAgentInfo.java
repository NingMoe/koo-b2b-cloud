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


/**
代理商基本信息表
*/
@Entity
@Table(name = "tb_agent_info")
public class TbAgentInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 代理商名称 */
	@Column(name = "name")
	private String name;
	/** 省份id */
	@Column(name = "provence_id")
	private Integer provenceId;
	/** 市/地区id */
	@Column(name = "city_id")
	private Integer cityId;
	/** 区，县id */
	@Column(name = "area_id")
	private Integer areaId;
	/** 代理类型：1：省，2：市，3：区县，4：其他 */
	@Column(name = "agent_type")
	private Integer agentType;
	/** 代理开始日期 */
	@Column(name = "begin_time")
	private Date beginTime;
	/** 代理终止日期 */
	@Column(name = "end_time")
	private Date endTime;
	/** 代理人姓名 */
	@Column(name = "agent_name")
	private String agentName;
	/** 电话 */
	@Column(name = "mobile")
	private String mobile;
	@Column(name = "email")
	private String email;
	/** 详细地址 */
	@Column(name = "address")
	private String address;
	/** 状态：1：正常，2：删除 */
	@Column(name = "status")
	private Integer status;
	/** 创建人 */
	@Column(name = "creator")
	private String creator;
	/** 更新时间 */
	@Column(name = "update_time")
	private Date updateTime;
	/** 创建时间 */
	@Column(name = "create_time")
	private Date createTime;
	
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
	
	public Integer getProvenceId() {
		return provenceId;
	}

	public void setProvenceId(Integer provenceId) {
		this.provenceId = provenceId;
	}
	
	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	
	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	
	public Integer getAgentType() {
		return agentType;
	}

	public void setAgentType(Integer agentType) {
		this.agentType = agentType;
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
	
	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
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
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
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
}
