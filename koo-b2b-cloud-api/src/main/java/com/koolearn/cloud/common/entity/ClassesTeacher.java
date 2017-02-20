package com.koolearn.cloud.common.entity ;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;


@Entity
@Table(name = "classes_teacher")
public class ClassesTeacher implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int STATUS_VALID = 0;//0 有效

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 老师id */
	@Column(name = "teacher_id")
	private Integer teacherId;
	/** 班级id */
	@Column(name = "classes_id")
	private Integer classesId;
	/** 所任学科 */
	@Column(name = "subject_id")
	private Integer subjectId;
	/** 实体状态：默认0 正常 */
	@Column(name = "status")
	private Integer status;
    /**创建时间*/
    @Column(name = "create_time")
    private Date createTime;
    @Transient
    private String subjectName;//学科名称
    @Transient
    private String teacherName;//老师名称

    @Transient
    private List< String > list;//老师所有学科

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	
	public Integer getClassesId() {
		return classesId;
	}

	public void setClassesId(Integer classesId) {
		this.classesId = classesId;
	}
	
	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
