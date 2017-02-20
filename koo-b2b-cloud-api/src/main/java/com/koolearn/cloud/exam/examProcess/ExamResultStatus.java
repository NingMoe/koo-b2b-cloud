package com.koolearn.cloud.exam.examProcess;

import java.io.Serializable;

/**
 * @author DuHongLin 考试结果类型枚举
 */
public enum ExamResultStatus implements Serializable
{

	/**
	 * 考试过程中
	 */
	PROCESSING(1),
	/**
	 * 已提交
	 */
	SUBMITTED(2);

	private int status;

	private ExamResultStatus(int status)
	{
		this.status = status;
	}

	public int getValue()
	{
		return this.status;
	}
}
