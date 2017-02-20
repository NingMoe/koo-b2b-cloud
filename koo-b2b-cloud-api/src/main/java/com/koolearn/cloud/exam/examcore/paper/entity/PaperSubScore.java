package com.koolearn.cloud.exam.examcore.paper.entity;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.NumberFormat;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;
/**
 * 试卷子试题分数
 * @author wangpeng
 */
@Entity
@Table(name = "te_paper_sub_score")
public class PaperSubScore implements Serializable {
	/** 主键 */
	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private int id;
	@Column(name = "paper_id")
	private int paperId;
	/**
	 * 分数
	 */
    @Column(name="`points`")
	private Double points;
    @Column(name="`code`")
	private String code;
	@Column(name = "parent_code")
	private String parentCode;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	
}
