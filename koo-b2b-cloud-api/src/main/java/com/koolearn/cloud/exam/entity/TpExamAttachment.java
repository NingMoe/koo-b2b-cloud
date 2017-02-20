package com.koolearn.cloud.exam.entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.*;

import java.io.Serializable;


@Entity
@Table(name = "tp_exam_attachment")
public class TpExamAttachment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 课堂id */
	@Column(name = "exam_id")
	private Integer examId;
	/** 附件id */
	@Column(name = "attachment_id")
	private Integer attachmentId;
	/** 附件类型：0:本地上传（附件）  1.资源库资源（微课、课件） 2作业 */
	@Column(name = "attachment_type")
	private Integer attachmentType;
	@Column(name = "attachment_name")
	private String attachmentName;
	/** 实体状态：默认0 正常 */
	@Column(name = "status")
	private Integer status;
	/** 排序 */
	@Column(name = "sort")
	private Integer sort;
	/** 班级名称 */
    @Transient
    private String classesFullNames;
	/** 附件类型 */
	@Transient
	private String typeStr;
	/** 未完成名称 */
	@Transient
	private String unFinishStudentName;
	/** 完成率 */
    @Transient
	private Double rate;
	/** 上一个 */
	@Transient
	private Integer prevId;
	/** 下一个 */
	@Transient
	private Integer nextId;

	public Integer getPrevId() {
		return prevId;
	}

	public void setPrevId(Integer prevId) {
		this.prevId = prevId;
	}

	public Integer getNextId() {
		return nextId;
	}

	public void setNextId(Integer nextId) {
		this.nextId = nextId;
	}

	public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getUnFinishStudentName() {
		return unFinishStudentName;
	}

	public void setUnFinishStudentName(String unFinishStudentName) {
		this.unFinishStudentName = unFinishStudentName;
	}

	public String getTypeStr() {
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	public String getClassesFullNames() {
        return classesFullNames;
    }

    public void setClassesFullNames(String classesFullNames) {
        this.classesFullNames = classesFullNames;
    }

    public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

	public Integer getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	public Integer getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(Integer attachmentType) {
		this.attachmentType = attachmentType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
