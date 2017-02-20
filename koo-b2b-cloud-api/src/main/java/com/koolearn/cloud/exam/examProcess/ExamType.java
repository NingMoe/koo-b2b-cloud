package com.koolearn.cloud.exam.examProcess;

import java.io.Serializable;

/**
 * @author DuHongLin 考试类型枚举
 */
public enum ExamType implements Serializable
{
	/** 在线作业 */
	EXAM(1), 
	/** 组题自测 */
	LESSON(2);

	private int examType;

	private ExamType(int examType)
	{
		this.examType = examType;
	}

	public int getValue()
	{
		return this.examType;
	}
}
