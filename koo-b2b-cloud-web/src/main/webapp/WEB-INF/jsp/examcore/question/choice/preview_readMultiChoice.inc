<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.koolearn.exam.question.entity.Question"%>
<%@page import="com.koolearn.exam.question.entity.ChoiceAnswer"%>
<%@page import="com.koolearn.exam.question.entity.QuestionAttach"%>
<%@page import="com.koolearn.exam.util.TestUtil"%>
<%@page import="com.koolearn.exam.question.template.MultiChoiceTemplate"%>
<%@page import="com.koolearn.exam.question.dto.*"%>
<%@page import="java.util.*"%>
	<c:if test="${sessionScope.userTemplateId==1||sessionScope.userTemplateId==2}">
		<%--
		< %@ include file="preview_readMultiChoice1.inc" %>
		--%>
		<%
		Integer index=(Integer)request.getAttribute("index");
		if(index==null){
			index=0;
		}
		out.println(MultiChoiceTemplate.instance(request, response).outTemplate(process, index));
		%>
	</c:if>
	<c:if test="${sessionScope.userTemplateId==4}">
		<%@ include file="preview_readMultiChoice3.inc" %>
	</c:if>	
	
