package com.koolearn.cloud.task.entity ;
import java.io.Serializable;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.util.Date;
import java.util.Date;


@Entity
@Table(name = "tp_exam_student")
public class TpExamStudent implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int STATUS_VALID = 1;//默认1 正常 0 删除
	public static final int STATUS_INVALID = 0;//默认1 正常 0 删除
	//默认1： 1作业   2课堂   20课堂作业
	public static final int TYPE_TASK = 1;
	public static final int TYPE_COURSE = 2;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 考试id  （考试表type=2  表示课堂id） */
	@Column(name = "exam_id")
	private Integer examId;
	/** 默认0：试卷id(冗余字段) */
	@Column(name = "paper_id")
	private Integer paperId;
	/** 学生id */
	@Column(name = "student_id")
	private Integer studentId;
	/** 当前学生所在班级ID */
	@Column(name = "classes_id")
	private Integer classesId;
	/** 创建老师id */
	@Column(name = "teacher_id")
	private Integer teacherId;
	/** 实体状态：默认1 正常 0 删除 */
	@Column(name = "status")
	private Integer status;
	/** 创建时间 */
	@Column(name = "create_time")
	private Date createTime;
	/** 更新时间 */
	@Column(name = "update_time")
	private Date updateTime;
	/** '默认1： 1作业   2课堂   20课堂作业' */
	@Column(name = "type")
	private Integer type;
	/** 学科id */
	@Column(name = "subject_id")
	private Integer subjectId;
	/** 0：查看过，1：未查看 */
	@Column(name = "view")
	private Integer view;
	/** 课堂完成时间 */
	@Column(name = "finish_time")
	private Date finishTime;
    /**班级人数*/
    @Transient
    private int studentNum;
    /**作业完成人数*/
    @Transient
    private int hadDoneNum;
    @Transient
    private String startTimeStr;
    @Transient
    private String endTimeStr;
    @Transient
    private Date startTime;
    /**课堂结束时间*/
    @Transient
    private Date endTime;
    /**课堂显示班级**/
    @Transient
    private String classesStr;
    /**课堂结束状态 0结束1正常 **/
    @Transient
    private Integer finishStatus;
    /**课堂名称*/
    @Transient
    private String examName;
    @Transient
    private String typeName;

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getView() {
		return view;
	}

	public void setView(Integer view) {
		this.view = view;
	}

	public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public int getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(int studentNum) {
        this.studentNum = studentNum;
    }

    public int getHadDoneNum() {
        return hadDoneNum;
    }

    public void setHadDoneNum(int hadDoneNum) {
        this.hadDoneNum = hadDoneNum;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getClassesStr() {
        return classesStr;
    }

    public void setClassesStr(String classesStr) {
        this.classesStr = classesStr;
    }

    public Integer getFinishStatus() {
        return finishStatus;
    }

    public void setFinishStatus(Integer finishStatus) {
        this.finishStatus = finishStatus;
    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getExamId() {
		return examId;
	}

	public void setExamId(Integer examId) {
		this.examId = examId;
	}
	
	public Integer getPaperId() {
		return paperId;
	}

	public void setPaperId(Integer paperId) {
		this.paperId = paperId;
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
	
	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
