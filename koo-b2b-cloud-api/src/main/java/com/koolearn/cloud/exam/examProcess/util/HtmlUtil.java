package com.koolearn.cloud.exam.examProcess.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.koolearn.cloud.exam.entity.TpExam;
import com.koolearn.cloud.exam.examProcess.Process;
import com.koolearn.cloud.exam.examProcess.enums.StructureType;
import com.koolearn.cloud.exam.examProcess.vo.ResultDetailVO;
import com.koolearn.cloud.exam.examProcess.vo.ResultStructureVO;
import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;
import com.koolearn.cloud.exam.examcore.question.template.TemplateFtl;

import freemarker.template.TemplateException;

/**
 * @author DuHongLin
 * 关于HTML的工具类
 */
public final class HtmlUtil implements Serializable
{

	/** 自动生成的序列化ID */
	private static final long serialVersionUID = 69546775636039463L;
	
	/**
	 * 处理考试过程的显示
	 * @param resultHtml
	 * @param resultVO
	 * @return
	 * @author DuHongLin
	 */
	public static final StringBuffer handlerProcess(List<ResultStructureVO> structures, StringBuffer resultHtml, Map<String, String> qsHtml)
	{
		if (null != structures && structures.size() > 0)
		{
			for (ResultStructureVO structure : structures)
			{
				switch (structure.takeType())
				{
					case STRUCTURE:
						resultHtml = HtmlUtil.handlerStructure(resultHtml, qsHtml, structure);
						break;
						
					case QUESTION:
						resultHtml = HtmlUtil.handlerQuestion(resultHtml, qsHtml, structure);
						break;
						
					default:
						break;
				}
			}
		}
		else
		{
			System.out.println("com.koolearn.cloud.exam.examProcess.util.HtmlUtil==>处理页面展现时没有结构内容！");
		}
		return resultHtml;
	}
	
	/**
	 * 处理结构类型
	 * @param resultHtml
	 * @param qsHtml
	 * @param structure
	 * @return
	 * @author DuHongLin
	 */
	public static final StringBuffer handlerStructure(StringBuffer resultHtml, Map<String, String> qsHtml, ResultStructureVO structure)
	{
		int qc = structure.takeQuestionCount(0);
		if (qc > 0)
		{
			/*
			if (0 == structure.getResultStructure().getParent())
			{
				resultHtml.append("<h6 class=\"error_h6\">");
				resultHtml.append(structure.getResultStructure().getName());
				resultHtml.append("<i>（共 "); 
				resultHtml.append(qc); 
				resultHtml.append(" 题，共");
				resultHtml.append(structure.getResultStructure().getPoint());
				resultHtml.append("分）</i></h6>");
			}
			else
			{
				resultHtml	.append("<h6 class=\"error_h6\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"); 
				resultHtml.append(structure.getResultStructure().getName());
				resultHtml.append("</h6>");
			}*/
			if (structure.takeHaveSubs())
			{
				HtmlUtil.handlerProcess(structure.getSubs(), resultHtml, qsHtml);
			}
		}
		return resultHtml;
	}
	
	public static final StringBuffer handlerQuestion(StringBuffer resultHtml, Map<String, String> qsHtml, ResultStructureVO structure)
	{
		List<ResultDetailVO> details = structure.getDetails();
		if (null != details && details.size() > 0)
		{
			for (ResultDetailVO detail : details)
			{
				String qhtml = qsHtml.get(String.valueOf(detail.getDetail().getQuestionId()));
				if (null != qhtml)
				{
					resultHtml.append(qhtml);
				}
			}
		}
		return resultHtml;
	}

	/**
	 * 处理题目
	 * @param qdtos
	 * @param exam
	 * @param viewType
	 * @param detailsMap
	 * @return
	 * @throws java.io.IOException
	 * @throws freemarker.template.TemplateException
	 * @author DuHongLin
	 */
	public static final Map<String, String> handlerQuestions(List<IExamQuestionDto> qdtos, TpExam exam, int viewType, Map<String, TpExamResultDetail> detailsMap) throws IOException, TemplateException
	{
		Map<String, String> result = null;
		if (null != qdtos && qdtos.size() > 0)
		{
			result = new HashMap<String, String>(0);
			QuestionViewDto qvd = null;
			for (int i = 0; i < qdtos.size(); i++)
			{
				IExamQuestionDto dto = qdtos.get(i);
				qvd = new QuestionViewDto();
				qvd.setViewType(viewType);
				if (viewType == QuestionViewDto.view_type_question) {
					qvd.setExaming(true);
				}
				qvd.setQuestionNo(String.valueOf(i + 1));
				qvd.setExamType(exam.getType());
				if (dto.haveSubQuestion())
				{
					qvd.setSubDtos(HtmlUtil.handlerSubQvd(qvd, dto.getSubQuestions(), detailsMap));
				}
				if (null != detailsMap && detailsMap.size() > 0)
				{
					TpExamResultDetail dtemp = detailsMap.get(String.valueOf(qdtos.get(i).getQuestionDto().getQuestion().getId()));
                    if(dtemp!=null){
                        qvd.setIsCorrect(dtemp.getResultAnswer());
                        qvd.setScore(dtemp.getPoints());
                        qvd.handlerUserResult(dtemp, qdtos.get(i));
                        qvd.setUserScore(dtemp.getScore());
                        qvd.setUserAnswer(dtemp.getUserAnswer());
                    }
				}
				result.put(String.valueOf(dto.getQuestionDto().getQuestion().getId()), TemplateFtl.outHtml(dto, qvd));
			}
		}
		return result;
	}
	
	/**
	 * 处理题目   将题目对应的QuestionViewDto和IExamQuestionDto存到questionDtoAndView里
	 * 并将此questionDtoAndView在将放到map(questionId,questionDtoAndView)
	 * @param qdtos
	 * @param exam
	 * @param viewType
	 * @param detailsMap
	 * @return
	 * @throws java.io.IOException
	 * @throws freemarker.template.TemplateException
	 * @author DuHongLin
	 */
	public static final Map<String, Map> handlerQuestionsData(List<IExamQuestionDto> qdtos, TpExam exam, int viewType, Map<String, TpExamResultDetail> detailsMap) throws IOException, TemplateException
	{
		Map<String, Map> result = null;
		if (null != qdtos && qdtos.size() > 0)
		{
			result = new HashMap<String, Map>(0);
			QuestionViewDto qvd = null;
			for (int i = 0; i < qdtos.size(); i++)
			{
				IExamQuestionDto dto = qdtos.get(i);
				qvd = new QuestionViewDto();
				qvd.setViewType(viewType);
				if (viewType == QuestionViewDto.view_type_question) {
					qvd.setExaming(true);
				}
				qvd.setQuestionNo(String.valueOf(i + 1));
				qvd.setExamType(exam.getType());
				if (dto.haveSubQuestion())
				{
					qvd.setSubDtos(HtmlUtil.handlerSubQvd(qvd, dto.getSubQuestions(), detailsMap));
				}
				if (null != detailsMap && detailsMap.size() > 0)
				{
					TpExamResultDetail dtemp = detailsMap.get(String.valueOf(qdtos.get(i).getQuestionDto().getQuestion().getId()));
					if(dtemp!=null){
						qvd.setIsCorrect(dtemp.getResultAnswer());
						qvd.setScore(dtemp.getPoints());
						qvd.handlerUserResult(dtemp, qdtos.get(i));
						qvd.setUserScore(dtemp.getScore());
					}
				}
				Map questionDtoAndView = new HashMap();
				questionDtoAndView.put("QuestionDto", dto);
				questionDtoAndView.put("QuestionViewDto", qvd);
				result.put(String.valueOf(dto.getQuestionDto().getQuestion().getId()), questionDtoAndView);
			}
		}
		return result;
	}
	
	/**
	 * 组织模板附件数据对象
	 * @param parent
	 * @param subQes
	 * @param detailsMap
	 * @return
	 * @author DuHongLin
	 */
	public static final List<QuestionViewDto> handlerSubQvd(QuestionViewDto parent, List<IExamQuestionDto> subQes, Map<String, TpExamResultDetail> detailsMap)
	{
		List<QuestionViewDto> result = new ArrayList<QuestionViewDto>(0);
		QuestionViewDto qvd = null;
		for (int i = 0; i < subQes.size(); i++)
		{
			String questionNo = parent.getQuestionNo() + "." + (i + 1);
			qvd = new QuestionViewDto();
			qvd.setExamType(parent.getExamType());
			qvd.setQuestionNo(questionNo);
			qvd.setViewType(parent.getViewType());
			qvd.setExaming(parent.isExaming());
			IExamQuestionDto qtemp = subQes.get(i);
			if (qtemp.haveSubQuestion())
			{
				qvd.setSubDtos(HtmlUtil.handlerSubQvd(qvd, qtemp.getSubQuestions(), detailsMap));
			}
			if (null != detailsMap && detailsMap.size() > 0)
			{
				TpExamResultDetail dtemp = detailsMap.get(String.valueOf(subQes.get(i).getQuestionDto().getQuestion().getId()));
                if(dtemp!=null){
                    qvd.setIsCorrect(dtemp.getResultAnswer());
                    qvd.setScore(dtemp.getPoints());
                    qvd.handlerUserResult(dtemp, subQes.get(i));
                    qvd.setUserScore(dtemp.getScore());
                }
			}
			result.add(qvd);
		}
		return result;
	}
	
	/**
	 * 处理答题卡
	 * @param structures
	 * @param cardHtml
	 * @param process
	 * @return
	 * @author DuHongLin
	 */
	public static final StringBuffer handlerCard(List<ResultStructureVO> structures, StringBuffer cardHtml, Process process)
	{
		if (null != structures && structures.size() > 0)
		{
			for (ResultStructureVO structure : structures)
			{
				switch (structure.takeType())
				{
					case STRUCTURE:
						if (structure.takeHaveSubs())
						{
							cardHtml = HtmlUtil.handlerCStructure(structure.getSubs(), cardHtml, process);
						}
						break;
						
					case QUESTION:
						cardHtml = HtmlUtil.handlerCDetail(structure.getDetails(), cardHtml, process);
						break;

					default:
						break;
				}
			}
		}
		return cardHtml;
	}

	/** 处理答题卡题目节点 */
	private static StringBuffer handlerCDetail(List<ResultDetailVO> details, StringBuffer cardHtml, Process process)
	{
		if (null != details && details.size() > 0)
		{
			for (ResultDetailVO detail : details)
			{
				if (null != detail)
				{
					int seq = process.getNum()[0];
					int err = process.getNum()[1];
					int nop = process.getNum()[2];
					if (null == detail.getDetail().getUserAnswer() || "".equals(detail.getDetail().getUserAnswer().trim()))
					{
						if (detail.getDetail().getSubjective() == 0)
						{
							cardHtml.append("<a href='#card");
							cardHtml.append(seq);
							cardHtml.append("'>");
							cardHtml.append(seq);
							cardHtml.append("</a>");
							nop++;
						}
						else
						{
							cardHtml.append("<a class='current green' href='#card");
							cardHtml.append(seq);
							cardHtml.append("'>");
							cardHtml.append(seq);
							cardHtml.append("</a>");
						}
					}
					else
					{
						int resultAnswer = detail.getDetail().getResultAnswer();
						if (resultAnswer == -1)
						{
						}
						else if (resultAnswer == 0)
						{
							cardHtml.append("<a class='orange' href='#card");
							cardHtml.append(seq);
							cardHtml.append("'>");
							cardHtml.append(seq);
							cardHtml.append("</a>");
							err ++;
						}
						else if (resultAnswer == 1)
						{
							cardHtml.append("<a class='current green' href='#card");
							cardHtml.append(seq);
							cardHtml.append("'>");
							cardHtml.append(seq);
							cardHtml.append("</a>");
						}
					}
					seq ++;
					process.getNum()[0] = seq;
					process.getNum()[1] = err;
					process.getNum()[2] = nop;
				}
			}
		}
		return cardHtml;
	}

	/** 处理答题卡结构节点 */
	private static StringBuffer handlerCStructure(List<ResultStructureVO> subs, StringBuffer cardHtml, Process process)
	{
		if (subs != null && subs.size() > 0)
		{
			for (ResultStructureVO sub : subs)
			{
				if (sub.takeHaveSubs())
				{
					cardHtml = HtmlUtil.handlerCard(sub.getSubs(), cardHtml, process);
				}
				if (sub.takeType() == StructureType.QUESTION)
				{
					cardHtml = HtmlUtil.handlerCDetail(sub.getDetails(), cardHtml, process);
				}
			}
		}
		return cardHtml;
	}
	
}
