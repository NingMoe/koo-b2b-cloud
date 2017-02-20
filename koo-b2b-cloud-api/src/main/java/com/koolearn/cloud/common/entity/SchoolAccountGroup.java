package com.koolearn.cloud.common.entity ;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;


@Entity
@Table(name = "school_account_group")
public class SchoolAccountGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 学校id */
	@Column(name = "school_id")
	private Integer schoolId;
	/** 数图账户组 */
	@Column(name = "account_group_id")
	private Integer accountGroupId;
	
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
	
	public Integer getAccountGroupId() {
		return accountGroupId;
	}

	public void setAccountGroupId(Integer accountGroupId) {
		this.accountGroupId = accountGroupId;
	}
}
