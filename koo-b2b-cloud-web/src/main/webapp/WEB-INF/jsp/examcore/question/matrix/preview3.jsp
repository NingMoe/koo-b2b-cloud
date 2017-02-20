<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.*"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.template.TemplateFtl"%>
<%@page import="com.koolearn.cloud.exam.examcore.util.QuestionUtil"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<!DOCTYPE HTML>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>预览</title>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css-exam/qt.css" />
    <link href="<%=basePath%>jPlayer/skin/blue.monday/jplayer.blue.monday.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="http://images.koolearn.com/www/subject/script/jquery-1.8.2.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>jPlayer/js/jquery.jplayer.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/1.9.1-jquery-ui.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/examcore/question/jquery.json-2.2.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/fckeditor/fckeditor.js"></script>
	<script type="text/javascript" src="http://www.koolearn.com/js/popup.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/jquery.form.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/examcore/question/public.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/examcore/question/question.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/examcore/question/qt.js"></script>
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
