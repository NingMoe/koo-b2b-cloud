package com.koolearn.cloud.exam.examcore.paper.entity;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.NumberFormat;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Transient;


@Entity
@Table(name = "te_paper_structure")
public class TestPaperStructure implements Serializable {
	public static final  int  structure_type_structure=0;
	public static final  int  structure_type_question=1;
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private int id;
	/** 试卷ID */
	@Column(name = "paper_id")
	private int paperId;
	/** 上层结构 */
	@Column(name = "parent")
	private int parent;
	/** 名称 */
	@Column(name = "name")
	private String name;
	/** 分值 */
	@Column(name = "points")
	private Double points;
	/** 限时 */
	@Column(name = "timeout")
	private int timeout;
	/** 描述 */
	@Column(name = "descript")
	private String descript;
	/** 排序 */
	@Column(name = "odr")
	private int odr;
    /**
     * 结构类型 @see {@link TestPaperStructure#structure_type_structure}
     */
    @Column(name = "structure_type")
    private int structureType;
    /* 题目序号 */
    @Transient
    private int questionNo;
    /* 题目id */
    @Transient
    private int questionId;
	
	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public int getQuestionNo() {
		return questionNo;
	}

	public void setQuestionNo(int questionNo) {
		this.questionNo = questionNo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getPaperId() {
		return paperId;
	}

	public void setPaperId(int paperId) {
		this.paperId = paperId;
	}
	
	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Double getPoints() {
		if (null != this.points)
		{
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(1);
			nf.setRoundingMode(RoundingMode.HALF_UP);
			return Double.valueOf(nf.format(this.points));
		}
		else
		{
			return this.points;
		}
	}

	public void setPoints(Double points) {
		if (null != points)
		{
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(1);
			nf.setRoundingMode(RoundingMode.HALF_UP);
			this.points = Double.valueOf(nf.format(points));
		}
		else
		{
			this.points = points;
		}
	}
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public int getOdr() {
		return odr;
	}

	public void setOdr(int odr) {
		this.odr = odr;
	}

    public int getStructureType() {
        return structureType;
    }

    public void setStructureType(int structureType) {
        this.structureType = structureType;
    }
}
