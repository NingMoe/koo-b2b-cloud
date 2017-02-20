package com.koolearn.cloud.common.entity ;
import java.io.Serializable;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.util.Date;


@Entity
@Table(name = "classes_dynamic")
public class ClassesDynamic implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int STATUS_ZERO = 0;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 班级id */
	@Column(name = "classes_id")
	private Integer classesId;
	/** 动态信息 */
	@Column(name = "dynamic_info")
	private String dynamicInfo;
    /**0：有动态未被老师查看，1:全部被班级下老师查看过 2：被删除的无效动态*/
    @Column(name = "status")
    private int status;
	/**  创建时间 */
	@Column(name = "create_time")
	private Date createTime;
    /**  发生动态的用户id */
    @Column(name = "user_id")
    private Integer userId;
    /**教师id*/
    @Transient
    private Integer teacherId;
    @Transient
    private String createTimeStr;



    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /** 标识是否已读*/


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getClassesId() {
		return classesId;
	}

	public void setClassesId(Integer classesId) {
		this.classesId = classesId;
	}
	
	public String getDynamicInfo() {
		return dynamicInfo;
	}

	public void setDynamicInfo(String dynamicInfo) {
		this.dynamicInfo = dynamicInfo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
