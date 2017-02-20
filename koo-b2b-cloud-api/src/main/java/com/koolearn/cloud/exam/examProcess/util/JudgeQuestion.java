package com.koolearn.cloud.exam.examProcess.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail;
import com.koolearn.cloud.exam.examcore.question.dto.EssayQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto;
import com.koolearn.cloud.exam.examcore.question.entity.Question;
import org.apache.log4j.Logger;

/**
 * @author DuHongLin 判题工具
 */
public class JudgeQuestion implements Serializable
{
    private static Logger logger=Logger.getLogger(JudgeQuestion.class);
	private static final long serialVersionUID = 6801823649479436223L;

	/**
	 * 判题操作
	 * @param detail
	 * @param qdto
	 * @return
	 * @author DuHongLin
	 */
	public static TpExamResultDetail judge(TpExamResultDetail detail, IExamQuestionDto qdto){
        try{
            if (detail.getSubjective() == 0) // 如果是客观题，执行判题，主观题不判题
            {
                // 判断题目类型
                switch (detail.getQuestionTypeId())
                {
                    case Question.QUESTION_TYPE_DANXUAN: // 单选判题
                        detail = JudgeQuestion.dx(detail);
                        break;
                    case Question.QUESTION_TYPE_DUOXUAN: // 多选题
                        detail = JudgeQuestion.ddx(detail);
                        break;
                    case Question.QUESTION_TYPE_SUB_CORRECTION: // 改错子题
                        detail = JudgeQuestion.gcz(detail);
                        break;
                    case Question.QUESTION_TYPE_SORT: // 拖拽排序题
                        detail = JudgeQuestion.px(detail);
                        break;
                    case Question.QUESTION_TYPE_DANXUAN_BOX: // 方框题
                        detail = JudgeQuestion.fk(detail);
                        break;
                    case Question.QUESTION_TYPE_FILL_BLANK: // 填空题
                        detail = JudgeQuestion.fillNormal(detail, qdto);
                        break;
                    case Question.QUESTION_TYPE_FILL_CALCULATION: // 计算填空
                        detail = JudgeQuestion.fillCal(detail);
                        break;
                    case Question.QUESTION_TYPE_SHORT: // 简答题
                    case Question.QUESTION_TYPE_SPOKEN: // 口语题
                    case Question.QUESTION_TYPE_WHRITE: // 写作题
                        detail = JudgeQuestion.subjective(detail);
                        break;
                    case Question.QUESTION_TYPE_CHOICE_BLANK: // 选择填空题
                    case Question.QUESTION_TYPE_CORRECTION: // 改错题
                    case Question.QUESTION_TYPE_CHOICE_FILL_BLANK: // 选择型完型填空题
                    case Question.QUESTION_TYPE_CLOZE_FILL_BLANK: // 填空型完型填空题
                    case Question.QUESTION_TYPE_READ: // 阅读理解题
                    case Question.QUESTION_TYPE_LISTEN: // 听力题
                    case Question.QUESTION_TYPE_TABLE: // 矩阵题
                        detail = JudgeQuestion.fh(detail);
                        break;
                    default:
                        break;
                }
            }
            if (detail.getScore()!=null&&detail.getScore().doubleValue()>0&&detail.getScore().doubleValue() == detail.getPoints().doubleValue()){
                //得分和总分相等结果判对
                detail.setResultAnswer(1);
            }
            return detail;
        }catch (Exception e){
            logger.error(">>>>>>>>>提交试卷判题错误[未判题-1],结果id"+detail.getResultId()+"题目id"+qdto.getQuestionDto().getQuestion().getId()+">>>>>>>>>>>>>>>>>>");
            logger.error(e.getMessage());
            return detail;
        }

	}
	
	/**
	 * 改错（子）题判题
	 * @param detail
	 * @return
	 * @author DuHongLin
	 */
	public static TpExamResultDetail gcz(TpExamResultDetail detail)
	{
		return JudgeQuestion.judgeEquals(detail);
	}


	/**
	 * 复合题判题【选择型完型填空题，填空型完型填空题，选择填空，改错题，阅读理解题，听力题，矩阵题】
	 * @param detail
	 * @return
	 * @author DuHongLin
	 */
	public static TpExamResultDetail fh(TpExamResultDetail detail)
	{
		return detail;
	}

	/**
	 * 拖拽排序题判题
	 * 
	 * @param detail
	 * @return
	 * @author DuHongLin
	 */
	public static TpExamResultDetail px(TpExamResultDetail detail)
	{
		return JudgeQuestion.judgeEquals(detail);
	}

	/**
	 * 方框题判题
	 * 
	 * @param detail
	 * @return
	 * @author DuHongLin
	 */
	public static TpExamResultDetail fk(TpExamResultDetail detail)
	{
		return JudgeQuestion.judgeEquals(detail);
	}
	
	/**
	 * 主观题判题【简答题，口语题，写作题】
	 * @param detail
	 * @return
	 * @author DuHongLin
	 */
	public static TpExamResultDetail subjective(TpExamResultDetail detail)
	{
		detail.setResultAnswer(1);
		return detail;
	}
	
	/**
	 * 计算填空判题
	 * @param detail
	 * @return
	 * @author DuHongLin
	 */
	public static TpExamResultDetail fillCal(TpExamResultDetail detail)
	{
		String ra = detail.getRightAnswer();
		String ua = detail.getUserAnswer();
		String[] raArr = ra.split("@@");
		Map<String, String> raMap = new HashMap<String, String>(0);
		for (String temp : raArr)
		{
			String[] ta = temp.split("_");
			raMap.put(ta[2].trim(), ta[3].trim());
		}
		String[] uaArr = ua.split("@@");
		Map<String, String> uaMap = new HashMap<String, String>(0);
		for (String temp : uaArr)
		{
			String[] tu = temp.split("_");
			try
			{
				uaMap.put(tu[2].trim(), tu[3].trim());
			}
			catch (Exception e)
			{
				detail.setResultAnswer(0);
				detail.setScore(Double.valueOf(0.0));
				return detail;
			}
		}
		Set<String> ks = raMap.keySet();
		for (String temp : ks)
		{
			String[] ras = raMap.get(temp).split("或");
			if (!uaMap.containsKey(temp) || (uaMap.get(temp) == null))
			{
				detail.setResultAnswer(0);
				detail.setScore(Double.valueOf(0.0));
				return detail;
			}
			else
			{
				boolean flag = false;
				for (String str : ras)
				{
					if (str.equals(uaMap.get(temp)))
					{
						flag = true;
					}
				}
				if (!flag)
				{
					detail.setResultAnswer(0);
					detail.setScore(Double.valueOf(0.0));
					return detail;
				}
			}
		}
		detail.setResultAnswer(1);
		detail.setScore(detail.getPoints());
		return detail;
	}

	/**
	 * 多选判题
	 * @param detail
	 * @return
	 * @author DuHongLin
	 */
	public static TpExamResultDetail ddx(TpExamResultDetail detail)
	{
		String ra = detail.getRightAnswer().substring(detail.getRightAnswer().indexOf("@@") + 2);
		if (detail.getUserAnswer()!=null&&"".equals(detail.getUserAnswer().trim()))
		{
			detail.setResultAnswer(0);
			detail.setScore(Double.valueOf(0.0));
			return detail;
		}
		boolean flag = false;
		if(detail.getUserAnswer()!=null){
			String ua = detail.getUserAnswer().substring(detail.getUserAnswer().indexOf("@@") + 2);
			String[] raArr = ra.split("@@");
			String[] uaArr = ua.split("@@");
			if (raArr.length != uaArr.length)
			{
				detail.setResultAnswer(0);
				detail.setScore(Double.valueOf(0.0));
				return detail;
			}
			for (String r : raArr)
			{
				flag = false;
				for (int i = 0; i < uaArr.length; i++)
				{
					String u = uaArr[i];
					if ((r.trim()).equals(u.trim()))
					{
						flag = true;
						continue;
					}
				}
				if (!flag)
				{
					break;
				}
			}
		}
		if (flag)
		{
			detail.setResultAnswer(1);
			detail.setScore(detail.getPoints());
		}
		else
		{
			detail.setResultAnswer(0);
			detail.setScore(Double.valueOf(0.0));
		}
		return detail;
	}

	/**
	 * 普通单选判题
	 * 
	 * @param detail
	 * @return
	 * @author DuHongLin
	 */
	public static TpExamResultDetail dx(TpExamResultDetail detail)
	{
		return JudgeQuestion.judgeEquals(detail);
	}
	
	/**
	 * 绝对相等判断
	 * @param detail
	 * @return
	 * @author DuHongLin
	 */
	public static TpExamResultDetail judgeEquals(TpExamResultDetail detail)
	{
		if (detail.getUserAnswer()!=null&&(detail.getRightAnswer().trim()).equals(detail.getUserAnswer().trim()))
		{
			detail.setResultAnswer(1);
			detail.setScore(detail.getPoints());
		}
		else
		{
			detail.setResultAnswer(0);
			detail.setScore(Double.valueOf(0.0));
		}
		return detail;
	}
	
	/**
	 * 普通填空判题
	 * @param detail
	 * @param qdto
	 * @return
	 * @author DuHongLin
	 */
	public static TpExamResultDetail fillNormal(TpExamResultDetail detail, IExamQuestionDto qdto)
	{
		EssayQuestionDto edto = (EssayQuestionDto) qdto;
		String ra = detail.getRightAnswer();
		String ua = detail.getUserAnswer();
		String[] raArr = ra.split("@@");
		Map<String, String> raMap = new HashMap<String, String>(0);
		List<String> ruaList = new ArrayList<String>(0);
		for (String temp : raArr)
		{
			String[] ta = temp.split("_");
			raMap.put(ta[2].trim(), ta[3].trim());
			ruaList.add(ta[3].trim());
		}
		String[] uaArr = ua.split("@@");
		Map<String, String> uaMap = new HashMap<String, String>(0);
		List<String> uaList = new ArrayList<String>(0);
		for (String temp : uaArr)
		{
			String[] tu = temp.split("_");
			try
			{
				uaMap.put(tu[2].trim(), tu[3].trim());
				uaList.add(tu[3].trim());
			}
			catch (Exception e)
			{
				detail.setResultAnswer(0);
				detail.setScore(Double.valueOf(0.0));
				return detail;
			}
		}
		Set<String> ks = raMap.keySet();
		boolean sensing = true; // 是否区分大小写,默认为true
		if (0 == edto.getEssayQuestion().getSensing())
		{
			sensing = false;
		}
		boolean one2one = false; // 是否需要按照顺序壹壹对应
		if (1 == edto.getEssayQuestion().getOne2one())
		{
			one2one = true;
		}
		boolean metewand = true; // 是否全对得分，默认为true
		if (1 == edto.getEssayQuestion().getMetewand())
		{
			metewand = false;
		}
		if (one2one && metewand) // 【按照顺序】且【全对给分】
		{
			for (String temp : ks)
			{
				String[] ras = raMap.get(temp).split("或");
				if (!uaMap.containsKey(temp) || (uaMap.get(temp) == null))
				{
					detail.setResultAnswer(0);
					detail.setScore(Double.valueOf(0.0));
					return detail;
				}
				else
				{
					boolean flag = false;
					for (String str : ras)
					{
						if (sensing)
						{
							if (str.equals(uaMap.get(temp)))
							{
								flag = true;
							}
						}
						else
						{
							if (str.equalsIgnoreCase(uaMap.get(temp)))
							{
								flag = true;
							}
						}
					}
					if (!flag)
					{
						detail.setResultAnswer(0);
						detail.setScore(Double.valueOf(0.0));
						return detail;
					}
				}
			}
		}
		else if (one2one && !metewand) // 【按照顺序】且【按空给分】
		{
			double fillPoint = detail.getPoints() / raMap.size(); // 每空的平均分
			double tempScore = 0; // 临时总分
			for (String temp : ks)
			{
				String[] ras = raMap.get(temp).split("或");
				if (!uaMap.containsKey(temp) || (uaMap.get(temp) == null))
				{
					detail.setResultAnswer(0);
					continue;
				}
				else
				{
					boolean flag = false;
					for (String str : ras)
					{
						if (sensing)
						{
							if (str.equals(uaMap.get(temp)))
							{
								flag = true;
								tempScore = tempScore + fillPoint;
							}
						}
						else
						{
							if (str.equalsIgnoreCase(uaMap.get(temp)))
							{
								flag = true;
								tempScore = tempScore + fillPoint;
							}
						}
					}
					if (!flag)
					{
						detail.setResultAnswer(0);
						continue;
					}
				}
			}
			detail.setScore(tempScore);
			if (detail.getPoints() == detail.getScore())
			{
				detail.setResultAnswer(1);
			}
			else
			{
				detail.setResultAnswer(0);
			}
			return detail;
		}
		else if (!one2one && metewand) // 【乱序】且【全对给分】
		{
			if (ruaList.size() != uaList.size())
			{
				detail.setResultAnswer(0);
				detail.setScore(Double.valueOf(0.0));
				return detail;
			}
			else
			{
				boolean flag = false;
				for (String uat : uaList)
				{
					flag = false;
					for (int i = 0 ; i < ruaList.size() ; i ++)
					{
						if (sensing)
						{
							if (uat.equals(ruaList.get(i)))
							{
								flag = true;
								ruaList.remove(i);
							}
						}
						else
						{
							if (uat.equalsIgnoreCase(ruaList.get(i)))
							{
								flag = true;
								ruaList.remove(i);
							}
						}
					}
					if (!flag)
					{
						detail.setResultAnswer(0);
						detail.setScore(Double.valueOf(0.0));
						return detail;
					}
				}
			}
		}
		else if (!one2one && !metewand) // 【乱序】且【按空给分】
		{
			double fillPoint = detail.getPoints() / raMap.size();
			double tempScore = 0; // 临时总分
			if (ruaList.size() != uaList.size())
			{
				detail.setResultAnswer(0);
			}
			else
			{
				boolean flag = false;
				for (String uat : uaList)
				{
					for (int i = 0 ; i < ruaList.size() ; i ++)
					{
						if (sensing)
						{
							if (uat.equals(ruaList.get(i)))
							{
								flag = true;
								tempScore = tempScore + fillPoint;
								ruaList.remove(i);
							}
						}
						else
						{
							if (uat.equalsIgnoreCase(ruaList.get(i)))
							{
								flag = true;
								tempScore = tempScore + fillPoint;
								ruaList.remove(i);
							}
						}
					}
					if (!flag)
					{
						detail.setResultAnswer(0);
						continue;
					}
				}
			}
			detail.setScore(tempScore);
			if (detail.getPoints() == detail.getScore())
			{
				detail.setResultAnswer(1);
			}
			else
			{
				detail.setResultAnswer(0);
			}
			return detail;
		}
		detail.setResultAnswer(1);
		detail.setScore(detail.getPoints());
		return detail;
	}
}
