package com.koolearn.cloud.exam.examProcess.validate;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

import com.koolearn.cloud.exam.examProcess.ExamAdvance;
import com.koolearn.cloud.exam.examProcess.ExamLate;

/**
 * @author DuHongLin
 * 验证考试时间相关
 */
public class ValidateExamTime implements Serializable
{


	private static final long serialVersionUID = 1882301413837220866L;

	/**
	 * 考试时间验证
	 * 
	 * @param late
	 * @param now
	 * @param start
	 * @param result
	 * @throws Exception
	 * @author DuHongLin
	 */
	public static boolean validateExamTime(int late, Calendar now, Calendar start, Calendar finish, Map<String, Object> result) throws Exception
	{
		boolean pass = true;
		// 验证是否早于考试开考进入
		pass = ValidateExamTime.validateExamStartTime(now, start, result);
//		if (pass)
//		{
//			// 验证迟到进场
//			pass = ValidateExamTime.validateExamLate(late, now, start, result);
//		}
		if (pass)
		{
			// 验证考试是否结束
			pass = ValidateExamTime.validateExamEndTime(now, finish, result);
		}
		return pass;
	}
	
	/**
	 * 验证是否早于考试开考进入
	 * @param now
	 * @param start
	 * @param result
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 */
	public static boolean validateExamStartTime(Calendar now, Calendar start, Map<String, Object> result) throws Exception
	{
		boolean pass = true;
		if (now.before(start))
		{
			result.put("ErrMsg", "开考时间未到，当前考试不允许进入！");
			result.put("target", "/student/task/index");
			result.put("path", "Error");
			pass = false;
		}
		return pass;
	}
	
	/**
	 * 验证迟到
	 * @param late
	 * @param now
	 * @param start
	 * @param result
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 */
	public static boolean validateExamLate(int late, Calendar now, Calendar start, Map<String, Object> result) throws Exception
	{
		boolean pass = true;
		if (late == ExamLate.ALLOW20.getValue()) // 允许迟到
		{
			now.add(Calendar.MINUTE, -20); // 允许迟到20分钟
		}
		else if (late == ExamLate.NOTALLOW.getValue()) // 不允许迟到
		{
			now.add(Calendar.MINUTE, -1); // 有1分钟的缓冲时间
		}
		else
		{
			throw new Exception("考试是否允许迟到取值范围溢出！");
		}

		if (now.after(start))
		{
			result.put("ErrMsg", "您已错过开考时间，当前考试不允许进入！");
			result.put("target", "/student/task/index");
			result.put("path", "Error");
			pass = false;
		}
		return pass;
	}
	
	/**
	 * 验证考试结束时间
	 * @param now
	 * @param finish
	 * @param result
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 */
	public static boolean validateExamEndTime(Calendar now, Calendar finish, Map<String, Object> result) throws Exception
	{
		boolean pass = true;
		if (now.after(finish))
		{
			result.put("ErrMsg", "考试已经结束，当前考试不允许进入！");
			result.put("target", "/student/pc/index");
			result.put("path", "Error");
		}
		return pass;
	}
	
	/**
	 * 验证是否允许提前交卷
	 * @param advance
	 * @param now
	 * @param finish
	 * @param result
	 * @param mm
	 * @return
	 * @throws Exception
	 * @author DuHongLin
	 */
	public static boolean validateSubmit(int advance, Calendar now, Calendar finish, Map<String, Object> result, int mm) throws Exception
	{
		boolean pass = true;
		boolean flag = false;
		if (advance == ExamAdvance.ALLOW20.getValue())
		{
			now.add(Calendar.MINUTE, 20);
			if (mm < 20)
			{
				flag = true;
			}
		}
		else if (advance == ExamAdvance.NOTALLOW.getValue())
		{
			now.add(Calendar.MINUTE, 5);
			if (mm < 5)
			{
				flag = true;
			}
		}
		else
		{
			throw new Exception("考试是否允许提前交卷取值范围溢出！");
		}
		
		if (now.before(finish) && !flag)
		{
			result.put("ErrMsg", "未到交卷时间，当前考试不允许交卷！");
			result.put("target", "index.jsp");
			result.put("path", "Error");
			pass = false;
		}
		return pass;
	}
}
