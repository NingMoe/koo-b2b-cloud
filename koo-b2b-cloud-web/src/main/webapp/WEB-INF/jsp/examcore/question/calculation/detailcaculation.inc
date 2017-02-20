<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.koolearn.exam.essayquestion.dto.EssayQuestionDTO"%>
<%@page import="com.koolearn.exam.essayquestion.entity.*"%>
<%@page import="com.koolearn.exam.question.entity.*"%>
<%@page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%
			  EssayQuestionDTO dto=(EssayQuestionDTO)obj;
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
			 		<%for(int attacheIndex=0;attacheIndex<dto.getQuestionDto().getQuestionAttachs().size();attacheIndex++)
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
					 <span class="tktb">
					 <% if(answers.get(ansIndex).getPlace()==0){
					 %>
					 	<%=answers.get(ansIndex).getCompany()%>
					 <%
					 } %>
					 <input type="text" name="answer" class="tkt" />
					 <% if(answers.get(ansIndex).getPlace()==1){
					 %>
					 	<%=answers.get(ansIndex).getCompany()%>
					 <%
					 } %>
					 </span>
				<%	  
				  	}
                 }else{
				%>
					<span class="tktb">录题错误，无对应答案内容！
				<%} %>
                </dd>
        </dl>