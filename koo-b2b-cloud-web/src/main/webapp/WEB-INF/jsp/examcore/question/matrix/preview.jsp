<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.*"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.template.TemplateFtl"%>
<%@page import="com.koolearn.cloud.exam.examcore.util.QuestionUtil"%>

<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>预览</title>
    <link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/qt.css" />
    <link href="${examctx}/examcore/jPlayer/skin/blue.monday/jplayer.blue.monday.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${examctx}/examcore/jquery-1.8.2.min.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/jPlayer/js/jquery.jplayer.min.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/1.9.1-jquery-ui.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/question/jquery.json-2.2.min.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/fckeditor/fckeditor.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/popup.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/jquery.form.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/question/public.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/question/question.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/question/qt.js"></script>
	<style type="text/css">
	body{
		height: 600px;
	}
	.st DT{
		font-weight: normal !important;
		font-family: Arial, Verdana, sans-serif;
		font-size: 12px;
		}
	</style>
  </head>
  
  <body>
  <br/>
  <% 
		MatrixQuestionDto obj=(MatrixQuestionDto)request.getAttribute("dto");
   		QuestionViewDto questionViewDto = (QuestionViewDto)request.getAttribute("questionViewDto");
		questionViewDto = QuestionUtil.getSubQuestionViewDto(obj,questionViewDto);  	
		out.println(TemplateFtl.outHtml(obj, questionViewDto));	
   %>
  </body>
</html>
