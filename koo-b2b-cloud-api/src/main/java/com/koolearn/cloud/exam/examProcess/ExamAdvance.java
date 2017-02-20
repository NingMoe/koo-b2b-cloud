package com.koolearn.cloud.exam.examProcess;

import java.io.Serializable;

/**
 * @author DuHongLin
 * 是否允许提前交卷
 */
public enum ExamAdvance implements Serializable
{

	/**
	 * 允许提前20分钟交卷
	 */
	ALLOW20(1),
	/**
	 * 不允许提前交卷
	 */
	NOTALLOW(2);

	private int advance;

	private ExamAdvance(int advance)
	{
		this.advance = advance;
	}

	public int getValue()
	{
		return this.advance;
	}
}
