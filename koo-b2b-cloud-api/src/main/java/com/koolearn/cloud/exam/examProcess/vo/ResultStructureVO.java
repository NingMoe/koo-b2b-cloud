package com.koolearn.cloud.exam.examProcess.vo;

import java.io.Serializable;
import java.util.List;

import com.koolearn.cloud.exam.examProcess.enums.StructureType;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultStructure;

/**
 * @author DuHongLin 考试结果结果VO
 */
public class ResultStructureVO implements Serializable
{

	/** 自动生成的序列化ID */
	private static final long serialVersionUID = -1946404885826092981L;

	/** 考试结果结构 */
	private TpExamResultStructure resultStructure;
	/** 子结构 */
	private List<ResultStructureVO> subs;
	/** 明细集合 */
	private List<ResultDetailVO> details;

	public List<ResultDetailVO> getDetails()
	{
		return details;
	}

	public TpExamResultStructure getResultStructure()
	{
		return resultStructure;
	}

	public List<ResultStructureVO> getSubs()
	{
		return subs;
	}

	public void setDetails(List<ResultDetailVO> details)
	{
		this.details = details;
	}

	public void setResultStructure(TpExamResultStructure resultStructure)
	{
		this.resultStructure = resultStructure;
	}

	public void setSubs(List<ResultStructureVO> subs)
	{
		this.subs = subs;
	}
	/** 获取节点类型 */
	public StructureType takeType()
	{
		if (this.resultStructure.getType().intValue() == StructureType.STRUCTURE.getValue())
		{
			return StructureType.STRUCTURE;
		}
		else if (this.resultStructure.getType().intValue() == StructureType.QUESTION.getValue())
		{
			return StructureType.QUESTION;
		}
		else
		{
			return null;
		}
	}
	/** 获取结构下的题目数 */
	public int takeQuestionCount(int result)
	{
		if (null != this.details && this.details.size() > 0)
		{
			result = result + this.details.size();
		}
		if (null != this.subs && this.subs.size() > 0)
		{
			for (ResultStructureVO sub : this.subs)
			{
				result = sub.takeQuestionCount(result);
			}
		}
		return result;
	}
	/** 检查是否含有子结构 */
	public boolean takeHaveSubs()
	{
		if (null != this.subs && this.subs.size() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
