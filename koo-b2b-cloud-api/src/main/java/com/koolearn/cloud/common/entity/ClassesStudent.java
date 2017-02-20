package com.koolearn.cloud.common.entity ;
import java.io.Serializable;
import java.util.Date;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;


@Entity
@Table(name = "classes_student")
public class ClassesStudent implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int STATUS_NOMAL = 0;//实体状态：默认0 正常 ,1:删除

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 学生id */
	@Column(name = "student_id")
	private Integer studentId;
    @Transient
    private String studentName;//学生姓名
	/** 班级或小组id（班级type=3） */
	@Column(name = "classes_id")
	private Integer classesId;
	/** 职位（待定） */
	@Column(name = "job")
	private Integer job;
	/** 是否小组长默认0 不是    1：是 */
	@Column(name = "headman")
	private Integer headman;
	/** 实体状态：默认0 正常 */
	@Column(name = "status")
	private Integer status;
    /**创建时间*/
    @Column(name="create_time")
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	
	public Integer getClassesId() {
		return classesId;
	}

	public void setClassesId(Integer classesId) {
		this.classesId = classesId;
	}
	
	public Integer getJob() {
		return job;
	}

	public void setJob(Integer job) {
		this.job = job;
	}
	
	public Integer getHeadman() {
		return headman;
	}

	public void setHeadman(Integer headman) {
		this.headman = headman;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
