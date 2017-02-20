package com.koolearn.cloud.exam.examcore.paper.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;


@Entity
@Table(name = "")
public class TestPaperExt implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 试卷ID */
	@Column(name = "paper_id")
	private int paperId;
	/** 试卷编码 */
	@Column(name = "paper_code")
	private String paperCode;
	/** 试卷状态 1.未审核 2.已审核 3.已作废 */
	@Column(name = "paper_status")
	private Integer paperStatus;
	/** 1.已共享 */
	@Column(name = "paper_share")
	private int paperShare;
	/** 老师ID */
	@Column(name = "teacher_id")
	private Integer teacherId;
	/** 学校ID */
	@Column(name = "school_id")
	private Integer schoolId;
	/** 浏览量 */
	@Column(name = "hot")
	private Integer hot;
	/**
	 * 是否是新东方试卷
	 */
	private int xdf;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public int getPaperId() {
		return paperId;
	}

	public void setPaperId(int paperId) {
		this.paperId = paperId;
	}
	
	public String getPaperCode() {
		return paperCode;
	}

	public void setPaperCode(String paperCode) {
		this.paperCode = paperCode;
	}
	
	public Integer getPaperStatus() {
		return paperStatus;
	}

	public void setPaperStatus(Integer paperStatus) {
		this.paperStatus = paperStatus;
	}
	
	public int getPaperShare() {
		return paperShare;
	}

	public void setPaperShare(int paperShare) {
		this.paperShare = paperShare;
	}
	
	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	
	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	
	public Integer getHot() {
		return hot;
	}

	public void setHot(Integer hot) {
		this.hot = hot;
	}

	public int getXdf() {
		return xdf;
	}

	public void setXdf(int xdf) {
		this.xdf = xdf;
	}
	
}
