<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.koolearn.kootest.examcore.question.entity.*"%>
<%@page import="com.koolearn.kootest.examcore.question.dto.*"%>
<%@page import="java.util.*"%>
<%@page import="com.koolearn.kootest.examcore.util.TestUtil"%>
<%
			  ChoiceQuestionDto dto=(ChoiceQuestionDto)obj;
			  Integer questionType=Integer.parseInt(String.valueOf(questionType_));
			  List<ChoiceAnswer> choiceAnswer=dto.getChoiceAnswers();
			  List<QuestionAttach> attaches=dto.getQuestionDto().getQuestionAttachs();
	%>
	
		<dl class="st" ty="0">
			<dt class="nobold"><span class="num">
			 <% if(request.getAttribute("question_index")!=null){
			 	out.print(request.getAttribute("question_index"));
			 	request.setAttribute("question_index",((Integer)request.getAttribute("question_index"))+1);
			 }else{
			 	out.print(1);
			 } %>
			</span>
			<% if(attaches!=null&&attaches.size()>0){
				for(int i=0;i<attaches.size();i++){
					out.println(attaches.get(i).getContent());
				}
			} %>
			
			<p class="mb10"><%=dto.getChoiceQuestion().getTopic() %>
			<%
			if(dto.getQuestionDto().getQuestion().getTopicExt()!=null){ 
			out.println(dto.getQuestionDto().getQuestion().getTopicExt());
			} %>
			</p>
			<p class="ft3">
				<%
				if(dto.getQuestionDto().getQuestion().getQuestionTip()!=null&&dto.getQuestionDto().getQuestion().getQuestionTip().trim().length()>0){
					out.println(dto.getQuestionDto().getQuestion().getQuestionTip());
				}
			%>
			</p>
			
			<p class="ft3">提示：请点击选择这段话的主旨句。</p>
			</dt>
		</dl>
	
		  