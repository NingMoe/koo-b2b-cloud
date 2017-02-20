package com.koolearn.cloud.exam.examcore.util;

import com.koolearn.cloud.exam.examcore.question.dto.EssayQuestionDto;
import com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto;
import com.koolearn.cloud.exam.examcore.question.entity.FillblankAnswer;
import org.apache.commons.lang3.StringUtils;
import java.text.DecimalFormat;
import java.util.List;

public class QuestionHelper {
	/**
	 * 保证程序允许,稍后再删除
	 * @param dto
	 * @return
	 */
	@Deprecated
	public static String  findUserAnswer(com.koolearn.exam.structure.dto.PaperItemDto dto){
		StringBuilder sb = new StringBuilder(100);
		sb.append("");
		System.out.println("dto.getUserAnswer()="+dto.getUserAnswer());
		if(dto.getUserAnswer()!=null){
			String[] str=null;
			str=dto.getUserAnswer().split("&");
			for(String string:str){
				sb.append(TestUtil.transToLetter(Integer.parseInt(string))+" ");
			}
		}
		return sb.toString();
		
	}
	public static String  findUserAnswer(QuestionViewDto dto){
		StringBuilder sb = new StringBuilder(100);
		sb.append("");
		if(dto.getUserAnswer()!=null){
			String[] str=null;
			str=dto.getUserAnswer().split("&");
			for(String string:str){
				if (null != string && !"".equals(string.trim()))
				{
					sb.append(TestUtil.transToLetter(Integer.parseInt(string))+" ");
				}
			}
		}
		return sb.toString();
		
	}
	public static String essayQuestionRightAnswer(EssayQuestionDto dto) {
		String rightAnswer="";
		List<FillblankAnswer> answers= dto.getFillblankAnswers();
		for(int ansIndex=0;ansIndex<answers.size();ansIndex++)
	  	{	
		  	if(rightAnswer.equals("")){
		  		rightAnswer=answers.get(ansIndex).getAnswer2();
		  	}else{
				rightAnswer=rightAnswer+", "+answers.get(ansIndex).getAnswer2();
			}
	  	}
		return  rightAnswer;
	}
	public static String essayQuestionSeg(String userAnswer,EssayQuestionDto dto) {
		if(StringUtils.isEmpty(userAnswer)){
			userAnswer="&";
		}
		List<FillblankAnswer> answers= dto.getFillblankAnswers();
		String[] userAnswers=userAnswer.split("&");
		String s=dto.getEssayQuestion().getTopic();
		String rightAnswer="";
		/**
		 * %s (1)...(n)
		 * 2 %s qid
		 * 3 %s cur_userAnswer
		 */
		String pattern="<span class='tktb'>%s<input type='text' name='%s' class='tkt dhl_fill' id='%s_%s_%s' value='%s'/></span>";
		StringBuilder result=new StringBuilder(100);
		for(int ansIndex=0;ansIndex<answers.size();ansIndex++)
	  	{	
		  	if(rightAnswer.equals("")){
		  		rightAnswer=answers.get(ansIndex).getAnswer();
		  	}else{
				rightAnswer=rightAnswer+", "+answers.get(ansIndex).getAnswer();
			}
			String qid=dto.getEssayQuestion().getQuesttionId()+"";
			String qtid = String.valueOf(dto.getQuestionType());
			String aid = String.valueOf(answers.get(ansIndex).getId());
			String preIndex="",cur_userAnswer="";
			if(StringUtils.isNotEmpty(s)&&(!s.equalsIgnoreCase("&nbsp;"))){
				preIndex="("+(ansIndex+1)+")";
			}
			//用户答案不是初始值
			if(!"&".equals(userAnswer) && userAnswers.length>ansIndex){
				cur_userAnswer=userAnswers[ansIndex].trim();
			}
			result.append(String.format(pattern, preIndex,qid,qtid,qid,aid,cur_userAnswer));
	  	}
		return result.toString();
	}
	public static String formatNumber(String pattern,float ff){
//		pattern="#.#";
		DecimalFormat format=new DecimalFormat(pattern);
		String fmt =format.format(ff);  
		return fmt;
	}
	public static void main(String[] args) {
		String format="<span class='tktb'>%s<input type='text' name='%s' class='tkt' value='%s'/></span>";
		String format2="<span class='tktb'>%s<input type='text' name='%i' class='tkt'/></span>";
		String aa[]={"avc","789","vvvv"};
		String rs=String.format(format, aa);
		System.out.println(rs);
		
	}
}
