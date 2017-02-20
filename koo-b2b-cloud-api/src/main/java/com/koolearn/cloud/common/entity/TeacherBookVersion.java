package com.koolearn.cloud.common.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Transient;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;
import java.util.Date;
import java.util.Date;


@Entity
@Table(name = "teacher_book_version")
public class TeacherBookVersion implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int STATUS_VALID = 1;//'0:删除，1：有效'
	public static final int STATUS_INVALID = 0;//'0:删除，1：有效'

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	@Column(name = "teacher_id")
	private Integer teacherId;
	/** ( 老师 )  学段id */
	@Column(name = "range_id")
	private Integer rangeId;
	/** ( 老师 )  学段name */
	@Column(name = "range_name")
	private String rangeName;
	/** ( 老师 )  学科id */
	@Column(name = "subject_id")
	private Integer subjectId;
	/** ( 老师 )  学科name */
	@Column(name = "subject_name")
	private String subjectName;
	/** 教材版本id */
	@Column(name = "book_version_id")
	private Integer bookVersionId;
	/** 教材版本name */
	@Column(name = "book_version_name")
	private String bookVersionName;
	/** 创建时间 */
	@Column(name = "create_time")
	private Date createTime;
	/** 更新时间 */
	@Column(name = "update_time")
	private Date updateTime;
	/** 排序字段 */
	@Column(name = "reorder")
	private Integer reorder;
	/**  字段 */
	@Column(name = "status")
	private Integer status;
	/** 1.有新作业未查看  2.已查看所有新作业 */
	@Transient
	private Integer tagStatus;
	 /**进度*/
    @Transient
    private int process;
	
	public Integer getTagStatus() {
		return tagStatus;
	}

	public void setTagStatus(Integer tagStatus) {
		this.tagStatus = tagStatus;
	}

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

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
	
	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	
	public Integer getRangeId() {
		return rangeId;
	}

	public void setRangeId(Integer rangeId) {
		this.rangeId = rangeId;
	}
	
	public String getRangeName() {
		return rangeName;
	}

	public void setRangeName(String rangeName) {
		this.rangeName = rangeName;
	}
	
	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}
	
	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	
	public Integer getBookVersionId() {
		return bookVersionId;
	}

	public void setBookVersionId(Integer bookVersionId) {
		this.bookVersionId = bookVersionId;
	}
	
	public String getBookVersionName() {
		return bookVersionName;
	}

	public void setBookVersionName(String bookVersionName) {
		this.bookVersionName = bookVersionName;
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
	
	public Integer getReorder() {
		return reorder;
	}

	public void setReorder(Integer reorder) {
		this.reorder = reorder;
	}
}
