package com.koolearn.cloud.common.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import java.util.Date;


@Entity
@Table(name = "yy_use_record")
public class UseRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 使用人id */
	@Column(name = "user_id")
	private Integer userId;
    /** 资源id或 题目编码 */
    @Column(name = "object_id")
    private Integer objectId;
    /**类型：4.资源  2.题目*/
    @Column(name = "object_type")
    private Integer objectType;
	/** 首次使用时间 */
	@Column(name = "use_time")
	private Date useTime;
    @Column(name = "status")
	private Integer status;
    @Column(name = "use_times")
    private Integer useTimes;
	
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
	
	public Date getUseTime() {
		return useTime;
	}

	public void setUseTime(Date useTime) {
		this.useTime = useTime;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(Integer useTimes) {
        this.useTimes = useTimes;
    }
}
