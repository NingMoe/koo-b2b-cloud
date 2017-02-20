package com.koolearn.cloud.exam.examProcess.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;

/**
 * @author DuHongLin
 * 题目操作工具类
 */
public class QuestionUtil implements Serializable
{

	private static final long serialVersionUID = 6217956042473105601L;

	public static List<IExamQuestionDto> takeSubQuestions(String qcode, List<IExamQuestionDto> dtos)
	{
		List<IExamQuestionDto> result = new ArrayList<IExamQuestionDto>(0);
		int qid = -1;
		for (IExamQuestionDto dto : dtos)
		{
			if ((dto.getQuestionDto().getQuestion().getCode()).equals(qcode))
			{
				qid = dto.getQuestionDto().getQuestion().getId();
				break;
			}
		}
		for (IExamQuestionDto dto : dtos)
		{
			if (dto.getQuestionDto().getQuestion().getTeId() == qid)
			{
				result.add(dto);
			}
		}
		return result;
	}
	/**
	 * 组织明细结构
	 * @param details
	 * @return
	 * @author DuHongLin
	 */
	public static List<TpExamResultDetail> groupDetail(List<TpExamResultDetail> details)
	{
		System.out.println("###################################################");
		List<TpExamResultDetail> result = new ArrayList<TpExamResultDetail>(0);
		for (TpExamResultDetail detail : details)
		{
			if (detail.getTeId().intValue() == 0)
			{
				detail.getSubDetails().add(QuestionUtil.handlerGroupDetail(detail, details));
				result.add(detail);
			}
		}
		System.out.println("###################################################");
		return result;
	}
	/**
	 * 向父级中添加子集
	 * @param parent
	 * @param details
	 * @return
	 * @author DuHongLin
	 */
	public static TpExamResultDetail handlerGroupDetail(TpExamResultDetail parent, List<TpExamResultDetail> details)
	{
		parent.setSubDetails(new ArrayList<TpExamResultDetail>(0));
		boolean first = true; // 标识是否第一次获取到子题
		for (TpExamResultDetail detail : details)
		{
			if (parent.getQuestionId().intValue() == detail.getTeId().intValue())
			{
				if (first)
				{
					parent.setScore(Double.valueOf(0.0));
					first = false;
				}
				detail = QuestionUtil.handlerGroupDetail(detail, details);
				/*此处理为有子题的大题的正确性*/
//				if (detail.getResultAnswer() == 1 || detail.getResultAnswer() == -1)
				if (detail.getResultAnswer() == 1)
				{
					if (parent.getResultAnswer() == -1)
					{
						parent.setResultAnswer(1);
					}
				}
				else if (detail.getResultAnswer() == 0)
				{
					parent.setResultAnswer(0);
				}
				parent.setScore(BigDecimal.valueOf(parent.getScore()).add(BigDecimal.valueOf(detail.getScore())).doubleValue());
				System.out.println("parent.getScore()="+parent.getScore()+"  detail.getScore()="+detail.getScore()+"  parent.getQuestionId()="
				+parent.getQuestionId()+"  detail.getTeId()="+detail.getTeId()+"   detail.getQuestionId()="+detail.getQuestionId());
				if (detail.getUserAnswer() != null && !"".equals(detail.getUserAnswer().trim()))
				{
					parent.setUserAnswer(parent.getUserAnswer() + "@#@" + detail.getUserAnswer());
				}
				else
				{
					// 不处理用户答案为空的情况
				}
				parent.getSubDetails().add(detail);
			}
		}
		return parent;
	}
	
	/**
	 * 处理用户答案
	 * @param resultDetails
	 * @return
	 * @author DuHongLin
	 */
	public static final Map<String, String> handlerUserResult(List<TpExamResultDetail> resultDetails)
	{
		Map<String, String> result = new HashMap<String, String>(0);
		for (TpExamResultDetail resultDetail : resultDetails)
		{
			int qid = resultDetail.getQuestionId();
			int qt = resultDetail.getQuestionTypeId();
			String ur = resultDetail.getUserAnswer();
			result.put(qt + "_" + qid, ur);
		}
		return result;
	}
}
