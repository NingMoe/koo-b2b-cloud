package com.koolearn.cloud.teacher.task;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Test {
	public static void main(String[] args) {
		String s1=new String("kvill");
		String s2=s1.intern();
		System.out.println(s1==s1.intern());
		System.out.println(s1+" "+s2);
		System.out.println(s2==s1.intern());
		System.out.println( new String("wo") == new String("wo") );
		System.out.println( new String("wo").intern() == new String("wo").intern() );
	}
	
//	public static void main(String[] args) {
//		DecimalFormat dfo =new DecimalFormat("#");
////		System.out.println(dfo.format(123.879));
//		
//		String str = "abcdec";
//		System.out.println(str.lastIndexOf("c")+"  "+str.length()+"  "+str.substring(0, 2)+"  "+str.substring(1));	
//		
////		String userResultStr = "{\"7_959916\":\"\",\"3_959867\":\"<p><span style=\"color: #24831B; font-family: 微软雅黑; font-size: 14px; font-weight: bold; line-height: 21px; background-color: #F5F5F5;\">“泰山童子”作为中国第十一届运动会的吉祥物，其创作灵感来源于充满文化、自然内涵和动人传说的泰山，胸前是第十一届全运会会徽标志，可以结合其构图笔法和姿态进行联想。</span></p>\"}";
////		String userResultStr = "{\"7_959916\":\"\",\"3_959867\":\"<p>①称谓“××学兄”(没有顶格)　</p>\"}";
//		String userResultStr = "{\"3_959904\":\"<p><span class=\\\"ft11 ftc1\\\">①称谓“××学兄”(没有顶格)　②祝语“夏安”(没有顶格)　③“寄呈”(不得体)　④“雅正”(不得体)</span></p>\"}";
//		userResultStr = userResultStr.substring(1, userResultStr.length() - 1).replace("\\n", "").replace("\\t", "").replace("\\\"","'");
//		String[] urArr = userResultStr.split("\",\"");
//		for (int i = 0; i < urArr.length; i++) {
//			System.out.println(urArr[i]);
//		}
//		System.out.println("--------------");
//		Map<String, String> urMap = new HashMap<String, String>(0);
//		for (String string : urArr)
//		{
//			String[] temp = string.split("\":\"");
//			String key = temp[0].substring(temp[0].trim().lastIndexOf("_") + 1);
////			System.out.println("key="+key);
//			String value = "";
//			try
//			{
//				value = temp[1].trim().replace("\\n", "").replace("\\t", "");
//				System.out.println((value.lastIndexOf("\"")+1)+"   "+value.length()+"  "+value);
//				if((value.lastIndexOf("\"")+1)==value.length()){
//					value = value.substring(0,value.length()-1) + value.substring(value.length()-1).replace("\"", "");
//				}
//			}
//			catch (Exception e)
//			{
//				value = "";
//			}
//			System.out.println("value="+value);
//			urMap.put(key, value);
//		}
//	}
}
