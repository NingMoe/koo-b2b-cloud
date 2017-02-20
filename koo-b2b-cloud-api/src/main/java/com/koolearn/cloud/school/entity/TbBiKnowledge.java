package com.koolearn.cloud.school.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import java.util.Date;


/**
知识点得分率统计表
*/
@Entity
@Table(name = "tb_bi_knowledge")
public class TbBiKnowledge implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 二级节点id */
	@Column(name = "parent_id")
	private Integer parentId;
	/** 叶子节点id */
	@Column(name = "child_id")
	private Integer childId;
	/** 班级id */
	@Column(name = "classes_id")
	private Integer classesId;
	/** 班级名称 */
	@Column(name = "classes_name")
	private String classesName;
	/** 学校id */
	@Column(name = "school_id")
	private Integer schoolId;
	/** 学科id */
	@Column(name = "subject_id")
	private Integer subjectId;
	/** 学科名称 */
	@Column(name = "subject_name")
	private String subjectName;
	/** 创建日期 */
	@Column(name = "create_time")
	private Date createTime;
	/** 得分率 */
	@Column(name = "score_rete")
	private Double scoreRete;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	public Integer getChildId() {
		return childId;
	}

	public void setChildId(Integer childId) {
		this.childId = childId;
	}
	
	public Integer getClassesId() {
		return classesId;
	}

	public void setClassesId(Integer classesId) {
		this.classesId = classesId;
	}
	
	public String getClassesName() {
		return classesName;
	}

	public void setClassesName(String classesName) {
		this.classesName = classesName;
	}
	
	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
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
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Double getScoreRete() {
		return scoreRete;
	}

	public void setScoreRete(Double scoreRete) {
		this.scoreRete = scoreRete;
	}
}
