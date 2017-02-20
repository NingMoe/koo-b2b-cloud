package com.koolearn.cloud.exam.examcore.paper.entity;
import java.io.Serializable;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;


@Entity
@Table(name = "te_paper_question_detail")
public class PaperDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private int id;
	/** 分值 */
	@Column(name = "points")
	private Double points;
	/** 题目编码 */
	@Column(name = "question_code")
	private String questionCode;
	/** 是否包含子题 */
	@Column(name = "subs")
	private int subs;
	/** 结构ID */
	@Column(name = "paper_structure_id")
	private int paperStructureId;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}
	
	public String getQuestionCode() {
		return questionCode;
	}

	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}
	
	public int getSubs() {
		return subs;
	}

	public void setSubs(int subs) {
		this.subs = subs;
	}
	
	public int getPaperStructureId() {
		return paperStructureId;
	}

	public void setPaperStructureId(int paperStructureId) {
		this.paperStructureId = paperStructureId;
	}
}
