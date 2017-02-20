package com.koolearn.cloud.exam.examProcess.util;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author DuHongLin
 * 时间处理工具类
 */
public class DateUtil implements Serializable
{


	private static final long serialVersionUID = 8452650322849580759L;

	/**
	 * 计算开始时间与结束时间相距的分钟数.<br/>
	 * @param begin
	 * @param end
	 * @return
	 * @author DuHongLin
	 */
	public static Map<String, Object> CalDuration(Date begin, Date end, Map<String, Object> resultMap)
	{
		Date now = new Date(); // 当前时间
		Date start = begin; // 开考时间
		if (now.after(begin)) // 如果当前时间晚于开考时间则以当前时间为开始时间计算总时长
		{
			start = now;
		}
		long el = end.getTime();
		long sl = start.getTime();
		int time = Double.valueOf((el - sl) / 60000).intValue();
		if (0 == time)
		{
			time = 1;
		}
		resultMap.put("time", time);
		long sec = (el - sl) / 1000; // 秒值时长
		long mm = sec / 60; // 分钟时长
		long ss = sec % 60; // 秒值时长不足一分钟的余秒
		resultMap.put("mm", mm);
		resultMap.put("ss", ss);
		return resultMap;
	}
}
