package com.koolearn.cloud.exam.examcore.question.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.koolearn.framework.aries.provider.sqlbuilder.annotation.Id;

public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	protected int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	
}
