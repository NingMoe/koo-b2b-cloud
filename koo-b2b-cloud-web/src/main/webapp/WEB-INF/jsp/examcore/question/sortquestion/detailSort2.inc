<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.koolearn.exam.choice.dto.JudgeQuestionDTO"%>
<%@page import="com.koolearn.exam.question.entity.*"%>
<%@page import="java.util.*"%>
<%@page import="com.koolearn.exam.util.TestUtil"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
			  JudgeQuestionDTO dto=(JudgeQuestionDTO)obj;
			  Integer questionType=Integer.parseInt(String.valueOf(questionType_));
			  List<ChoiceAnswer> answers = dto.getChoiceAnswers();
%>
		<!--单选排序--><a name="dxpx" id="dxpx"></a>
		
		

        	
		
	<dl class="st" ty="0">
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
	
	 	<%if(dto.getChoiceQuestion().getTopic()!=null){%>
           	<%=dto.getChoiceQuestion().getTopic() %>
        <%}%>
        <p class="ft3">
        <%
				if(dto.getQuestionDto().getQuestion().getQuestionTip()!=null&&dto.getQuestionDto().getQuestion().getQuestionTip().trim().length()>0){
					out.println(dto.getQuestionDto().getQuestion().getQuestionTip());
				}
			%>
		</p>
		</dt>
		<dd class="ft4 drag">
		   <div class="fc mb20 bx2">
        	<ul class="ul1 fl dragfrom">
    			<%
				  for(int ansIndex=0;ansIndex<answers.size();ansIndex++)
				  {
				%>
					<li>[<%=TestUtil.transToLetter(answers.get(ansIndex).getSequenceId()) %>] <%=answers.get(ansIndex).getDescription() %></li>
				<%	  
				  }
				%>
    		</ul>
    		<ul class="ul2 fl dragto">
    			<%
				  for(int ansIndex=0;ansIndex<answers.size();ansIndex++)
				  {
				%>
					<li><span class="fl"><%=(ansIndex+1)%>.</span><span class="ft9"></span></li>
				<%	  
				  }
				%>
    		</ul>
    	   </div>
		</dd>
	</dl>
