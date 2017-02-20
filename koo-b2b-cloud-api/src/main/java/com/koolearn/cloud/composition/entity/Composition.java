package com.koolearn.cloud.composition.entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "composition")
public class Composition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 作文提交人ID */
	@Column(name = "user_id")
	private Integer userId;
	/** 议论文 记叙文 */
	@Column(name = "type")
	private Integer type;
	/** 规则适配区域 */
	@Column(name = "area")
	private String area;
	/** 学段 */
	@Column(name = "school_lev")
	private Integer schoolLev;
	/** 作文的首图 缩略图 */
	@Column(name = "pic")
	private String pic;
	@Column(name = "create_time")
	private Date createTime;
	@Column(name = "title")
	private String title;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	public Integer getSchoolLev() {
		return schoolLev;
	}

	public void setSchoolLev(Integer schoolLev) {
		this.schoolLev = schoolLev;
	}
	
	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
