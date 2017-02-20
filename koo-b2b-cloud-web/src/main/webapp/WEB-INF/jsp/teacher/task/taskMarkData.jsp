<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="com.koolearn.cloud.exam.examcore.util.QuestionUtil"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.template.TemplateFtl"%>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<!DOCTYPE html> 
<!-- <jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include> -->
<div class="i-main">
	<c:out value="${resultHtml}" escapeXml="false"></c:out>
</div>

