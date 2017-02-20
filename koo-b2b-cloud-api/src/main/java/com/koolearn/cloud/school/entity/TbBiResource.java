package com.koolearn.cloud.school.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import java.util.Date;


/**
统计题，资源，视频，学校，用户总量表--按天统计
*/
@Entity
@Table(name = "tb_bi_resource")
public class TbBiResource implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 题库总量 */
	@Column(name = "subject_count")
	private Integer subjectCount;
	/** 资源统计量 */
	@Column(name = "resource_count")
	private Integer resourceCount;
	/** 视频统计量 */
	@Column(name = "view_count")
	private Integer viewCount;
	/** 试验视频统计量 */
	@Column(name = "view_test_count")
	private Integer viewTestCount;
	/** 学校统计量 */
	@Column(name = "school_count")
	private Integer schoolCount;
	/** 用户统计量 */
	@Column(name = "user_count")
	private Integer userCount;
	/** 创建日期 */
	@Column(name = "create_day")
	private Date createDay;
	/** 备注 */
	@Column(name = "remark")
	private String remark;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getSubjectCount() {
		return subjectCount;
	}

	public void setSubjectCount(Integer subjectCount) {
		this.subjectCount = subjectCount;
	}
	
	public Integer getResourceCount() {
		return resourceCount;
	}

	public void setResourceCount(Integer resourceCount) {
		this.resourceCount = resourceCount;
	}
	
	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}
	
	public Integer getViewTestCount() {
		return viewTestCount;
	}

	public void setViewTestCount(Integer viewTestCount) {
		this.viewTestCount = viewTestCount;
	}
	
	public Integer getSchoolCount() {
		return schoolCount;
	}

	public void setSchoolCount(Integer schoolCount) {
		this.schoolCount = schoolCount;
	}
	
	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}
	
	public Date getCreateDay() {
		return createDay;
	}

	public void setCreateDay(Date createDay) {
		this.createDay = createDay;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
