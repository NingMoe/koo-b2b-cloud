package com.koolearn.cloud.school.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import java.util.Date;
import java.util.Date;


/**
学校年级升级表
*/
@Entity
@Table(name = "tb_school_up")
public class TbSchoolUp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	@Column(name = "school_id")
	private Integer schoolId;
	/** 年级升级时间 */
	@Column(name = "auto_up_time")
	private Date autoUpTime;
	/** 升级年份，如果当前年等于该字段则表示本年已经升级过 */
	@Column(name = "up_year")
	private Integer upYear;
	/** 毕业时间 */
	@Column(name = "auto_graduate_time")
	private Date autoGraduateTime;
	/** 所有毕业的年级，level下划线隔开：11_12_13 */
	@Column(name = "class_graduate_level")
	private String classGraduateLevel;
	/** 毕业年份，如果当前年等于该字段则表示本年已经毕业过 */
	@Column(name = "graduate_year")
	private Integer graduateYear;
    /**版本*/
    @Column(name = "version")
    private Integer version;
    @Column(name = "creator")
    private String creator;
    @Column(name = "create_time")
    private Date createTime;

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
	
	public Date getAutoUpTime() {
		return autoUpTime;
	}

	public void setAutoUpTime(Date autoUpTime) {
		this.autoUpTime = autoUpTime;
	}
	
	public Integer getUpYear() {
		return upYear;
	}

	public void setUpYear(Integer upYear) {
		this.upYear = upYear;
	}
	
	public Date getAutoGraduateTime() {
		return autoGraduateTime;
	}

	public void setAutoGraduateTime(Date autoGraduateTime) {
		this.autoGraduateTime = autoGraduateTime;
	}
	
	public String getClassGraduateLevel() {
		return classGraduateLevel;
	}

	public void setClassGraduateLevel(String classGraduateLevel) {
		this.classGraduateLevel = classGraduateLevel;
	}
	
	public Integer getGraduateYear() {
		return graduateYear;
	}

	public void setGraduateYear(Integer graduateYear) {
		this.graduateYear = graduateYear;
	}
}
