<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.koolearn.kootest.examcore.question.dto.EssayQuestionDto"%>
<%@page import="com.koolearn.kootest.examcore.question.entity.*"%>
<%@page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  

<style>
.tip{ width:98%; padding-left:2%; height:28px; line-height:28px; background:#f1faee; color:#339944;}
.tip span{ margin-right:18px;}
</style>
<%
			  EssayQuestionDto dto=(EssayQuestionDto)obj;
			  Integer questionType=Integer.parseInt(String.valueOf(questionType_));
			  List<FillblankAnswer> answers = dto.getFillblankAnswers();
%>
		 <dl class="st" ty="2">
                <dt class="nobold"><span class="num">
<% if(request.getAttribute("question_index")!=null){
			 	out.print(request.getAttribute("question_index"));
			 	request.setAttribute("question_index",((Integer)request.getAttribute("question_index"))+1);
			 }else{
			 	out.print(1);
			 } %>
</span>
                <%if(dto.getQuestionDto().getQuestionAttachs()!=null&&dto.getQuestionDto().getQuestionAttachs().size()>0){%>
		 		<%  for(int attacheIndex=0;attacheIndex<dto.getQuestionDto().getQuestionAttachs().size();attacheIndex++)
			 		{ %>
			 			<%=dto.getQuestionDto().getQuestionAttachs().get(attacheIndex).getContent() %>
			 			
			 		<%} %>
		        <%}%>
		
			 	<%if(dto.getEssayQuestion().getTopic()!=null){%>
		           	<%=dto.getEssayQuestion().getTopic() %>
		        <%}%>
		        <p class="ft3">
		         <%
				if(dto.getQuestionDto().getQuestion().getQuestionTip()!=null&&dto.getQuestionDto().getQuestion().getQuestionTip().trim().length()>0){
					out.println(dto.getQuestionDto().getQuestion().getQuestionTip());
				}
				%>
                </p>
                </dt>
                <dd>
                <%
                 if(answers!=null&&answers.size()>0){%>
                 <span class="ft4">填写答案：</span>
                 <%
				  	for(int ansIndex=0;ansIndex<answers.size();ansIndex++)
				  	{	
				%>
					 <span class="tktb">(<%=(ansIndex+1)%>)<input type="text" name="answer" class="tkt" /></span>
				<%	  
				  	}
                 }else{
				%>
					<span class="tktb">录题错误，无对应答案内容！
				<%} %>
                </dd>
        </dl>