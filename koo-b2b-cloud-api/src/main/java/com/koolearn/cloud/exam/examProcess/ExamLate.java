package com.koolearn.cloud.exam.examProcess;

import java.io.Serializable;

/**
 * @author DuHongLin 考试是否允许迟到
 */
public enum ExamLate implements Serializable
{
	/**
	 * 允许迟到20分钟
	 */
	ALLOW20(1),
	/**
	 * 不允许迟到
	 */
	NOTALLOW(2);

	private int late;

	private ExamLate(int late)
	{
		this.late = late;
	}

	public int getValue()
	{
		return this.late;
	}
}
