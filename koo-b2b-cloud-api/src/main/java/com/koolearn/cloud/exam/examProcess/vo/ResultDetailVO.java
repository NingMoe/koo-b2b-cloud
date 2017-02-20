package com.koolearn.cloud.exam.examProcess.vo;

import java.io.Serializable;
import java.util.List;

import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;

/**
 * @author DuHongLin 考试结果明细VO
 */
public class ResultDetailVO implements Serializable
{

	/** 自动生成的序列化ID */
	private static final long serialVersionUID = -973492041562087159L;

	/** 考试结果明细 */
	private TpExamResultDetail detail;
	/** 考试结果子题 */
	private List<ResultDetailVO> subs;

	/** 考试结果明细 */
	public TpExamResultDetail getDetail()
	{
		return detail;
	}

	/** 考试结果子题 */
	public List<ResultDetailVO> getSubs()
	{
		return subs;
	}

	public void setDetail(TpExamResultDetail detail)
	{
		this.detail = detail;
	}

	public void setSubs(List<ResultDetailVO> subs)
	{
		this.subs = subs;
	}

	/** 是否包含子题 */
	public boolean takeHaveSubs()
	{
		boolean result;
		if (null == this.subs || this.subs.size() <= 0)
		{
			result = false;
		}
		else
		{
			result = true;
		}
		return result;
	}

	/** 是否是顶级大题 */
	public boolean takeIsTop()
	{
		boolean result;
		if (this.detail.getTeId() > 0)
		{
			result = false;
		}
		else
		{
			result = true;
		}
		return result;
	}

}
