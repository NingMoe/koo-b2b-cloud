package com.koolearn.cloud.exam.examcore.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.koolearn.cloud.exam.examcore.question.entity.FillblankAnswer;

public class FillBlankAnswerUtil {
	public static void main(String[] args) {
		String s="essayQuestionDTOs[2].fillblankAnswers[15].exts[0]";
		 Pattern p = Pattern.compile(".*?\\[(\\d+)\\]\\..*?\\[(\\d+)\\]\\..*?\\[(\\d+)\\]");
		 Matcher m = p.matcher(s);
		 if(m.find()){
			 int cnt=m.groupCount();
			 for(int i=1;i<=cnt;i++){
				 String str=m.group(i);
				 System.out.println(str);
			 }
		 }

	}
	/**
	 * essayQuestionDTOs[2].fillblankAnswers[15].exts[0]
	 * @param dNmu essayQuestionDTOs 位置序号
	 * @param aNum fillblankAnswers 位置序号
	 * @param request
	 * @return
	 */
	public static List<String> fitAnswerExt(int dNmu,int aNum,HttpServletRequest request){
		List<String> list=new ArrayList<String>();
		String key="essayQuestionDTOs[%s].fillblankAnswers[%s].exts[%s]";
		int index=0;
		String temp=null;
		while(true){
			temp=String.format(key, dNmu,aNum,index++);
			temp=request.getParameter(temp);
			
			if(temp!=null&&temp.trim().length()>0){
				list.add(temp.trim());
			}else{
				break;
			}
		}
		return list;
	}
	public static void wrap(List<FillblankAnswer> answers){
		if(answers==null||answers.size()==0){
			return;
		}
		for(FillblankAnswer answer:answers){
			wrap(answer);
		}
	}
	public static void unWrap(List<FillblankAnswer> answers){
		for(FillblankAnswer answer:answers){
			unWrap(answer);
		}
	}
	public static void wrap(FillblankAnswer answer){
		String ext=answer.getExt();
		if(StringUtils.isEmpty(ext)){
			if(answer.getAnswer()!=null){
				List<String> list=answer.getExts();
				list.clear();
				list.add(answer.getAnswer());
			}
		}else{
			List<String> list=JSON.parseArray(ext, String.class);
			answer.setExts(list);
		}
	}
	public static void unWrap(FillblankAnswer answer){
		List<String> list=answer.getExts();
		if(list!=null&&list.size()>0){
			answer.setExt(JSON.toJSONString(list));
		}
	}
}
