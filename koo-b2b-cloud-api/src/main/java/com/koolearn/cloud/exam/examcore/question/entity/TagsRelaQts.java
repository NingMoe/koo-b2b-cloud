package com.koolearn.cloud.exam.examcore.question.entity;

import java.io.Serializable;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Column;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Entity;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.GenerationType;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;
import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Table;

@Entity
@Table(name = "te_tags_rela_qts")
public class TagsRelaQts implements Serializable
{

	private static final long serialVersionUID = -8679658953208381000L;

	/** 主键 */
	@Id(strategy = GenerationType.AUTO_INCREMENT)
	private Integer id;
	/** 标签 */
	@Column(name = "tag")
	private Integer tag;
	/** 题目类型 */
	@Column(name = "qts")
	private String qts;

	public Integer getId()
	{
		return id;
	}

	public String getQts()
	{
		return qts;
	}

	public Integer getTag()
	{
		return tag;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public void setQts(String qts)
	{
		this.qts = qts;
	}

	public void setTag(Integer tag)
	{
		this.tag = tag;
	}

}
