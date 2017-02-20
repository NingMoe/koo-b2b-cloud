package com.koolearn.cloud.common.entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "yy_collection")
public class Collection implements Serializable {
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 收藏人id */
	@Column(name = "user_id")
	private Integer userId;
	/** 资源id或 题目编码 */
	@Column(name = "object_id")
	private Integer objectId;
    /**类型：1.资源  2.题目*/
    @Column(name = "object_type")
    private Integer objectType;
	/** 收藏来源 1.学生  2.老师 */
	@Column(name = "isfrom")
	private Integer isfrom;
	/** 学校id */
	@Column(name = "school_id")
	private Integer schoolId;
	/** 收藏时间 */
	@Column(name = "create_time")
	private Date createTime;
    /**
     * 0,取消，1，收藏
     */
    @Column(name = "status")
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

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
	
	public Integer getIsfrom() {
		return isfrom;
	}

	public void setIsfrom(Integer isfrom) {
		this.isfrom = isfrom;
	}
	
	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }
}
