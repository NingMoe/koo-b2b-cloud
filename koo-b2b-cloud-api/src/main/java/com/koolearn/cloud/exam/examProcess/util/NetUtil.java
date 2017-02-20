package com.koolearn.cloud.exam.examProcess.util;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author DuHongLin 关于网络的工具类
 */
public class NetUtil implements Serializable
{

	/** 自动生成的序列化ID */
	private static final long serialVersionUID = -8121674727010105206L;

	/**
	 * 获取客户端的真实IP
	 * @param request 请求对象
	 * @return 客户端IP
	 * @author DuHongLin
	 */
	public static String takeIP(HttpServletRequest request)
	{
		try
		{
			String ip = request.getHeader("x-forwarded-for");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			{
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			{
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			{
				ip = request.getRemoteAddr();
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			{
				ip = request.getHeader("http_client_ip");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			{
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			// 如果是多级代理，那么取第一个ip为客户ip
			if (ip != null && ip.indexOf(",") != -1)
			{
				ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
			}
			return ip;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * IPV4验证ip是否在指定的IP闭区间内
	 * @param vaip 待验证的IP
	 * @param begin 起始IP
	 * @param end 结束IP
	 * @return 当且仅当待验证IP在指定IP的闭区间内时返回true
	 * @author DuHongLin
	 */
	public static boolean validateIPV4(String vaip, String begin, String end)
	{
		try
		{
			long vaipLong = NetUtil.parseIP2Long(vaip);
			long beginLong = NetUtil.parseIP2Long(begin);
			long endLong = NetUtil.parseIP2Long(end);
			if (vaipLong < beginLong || vaipLong > endLong)
			{
				return false;
			}
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 将IP转化成长整形
	 * @param ip
	 * @return
	 * @author DuHongLin
	 */
	public static long parseIP2Long(String ip)
	{
		String[] ipArr = ip.trim().split("\\.");
		long iplong = 256 * 256 * 256 * Long.valueOf(ipArr[0].trim()) + 
							  256 * 256 * Long.valueOf(ipArr[1].trim()) + 
							  256 * Long.valueOf(ipArr[2].trim()) + 
							  Long.valueOf(ipArr[3].trim());
		return iplong;
	}
	
}
