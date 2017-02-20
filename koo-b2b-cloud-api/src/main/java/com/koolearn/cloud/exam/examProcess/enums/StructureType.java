package com.koolearn.cloud.exam.examProcess.enums;

import java.io.Serializable;

/**
 * @author DuHongLin
 * 
 */
public enum StructureType implements Serializable
{
	/** 节点 */
	STRUCTURE(0),
	/** 题目 */
	QUESTION(1);

	private int type;

	private StructureType(int type)
	{
		this.type = type;
	}

	public int getValue()
	{
		return this.type;
	}
}
