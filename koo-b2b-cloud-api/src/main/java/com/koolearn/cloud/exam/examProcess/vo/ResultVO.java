package com.koolearn.cloud.exam.examProcess.vo;

import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResult;

import java.io.Serializable;
import java.util.List;


/**
 * @author DuHongLin 考试结果VO
 */
public class ResultVO implements Serializable
{

	/** 自动生成的序列化ID */
	private static final long serialVersionUID = -410283873190344904L;

	/** 考试结果 */
	private TpExamResult result;
	/** 考试结构 */
	private List<ResultStructureVO> structures;

	public TpExamResult getResult()
	{
		return result;
	}

	public List<ResultStructureVO> getStructures()
	{
		return structures;
	}

	public void setResult(TpExamResult result)
	{
		this.result = result;
	}

	public void setStructures(List<ResultStructureVO> structures)
	{
		this.structures = structures;
	}
	
	public int takeQuestionCount()
	{
		int count = 0;
		for (ResultStructureVO structure : this.structures)
		{
			count = count + structure.takeQuestionCount(0);
		}
		return count;
	}

}
