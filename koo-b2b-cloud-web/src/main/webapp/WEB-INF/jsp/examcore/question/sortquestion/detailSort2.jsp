<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.koolearn.exam.question.entity.*"%>
<%@ page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto" %>
<%@ page import="com.koolearn.cloud.exam.examcore.question.template.TemplateFtl" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/preview.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/qt.css" />
<link href="<%=request.getContextPath()%>/jPlayer/skin/blue.monday/jplayer.blue.monday.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="http://images.koolearn.com/www/subject/script/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/1.9.1-jquery-ui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jPlayer/js/jquery.jplayer.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.json-2.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/public.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/qt.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/question.js"></script>
<title>预览</title>
<style type="text/css">
	.st DT{
		font-weight: normal !important;
		font-family: Arial, Verdana, sans-serif;
		font-size: 12px;
	}
</style>
<script type="text/javascript">
$(function(){
	var step=${step};
	if(step==0){
	
		$('form').html(window.opener.findPreViewData());
		document.forms[0].submit();
	}else{
		$('body').show();
	}
});

</script>
</head>
<body ><!-- style="display: none;" -->
<form action="<%=request.getContextPath()%>/question/base/choice/detail2/1?questionType=<%=Question.QUESTION_TYPE_SORT%>" method="post">
</form>
<c:if test="${step==1}">
		<% IExamQuestionDto obj=(IExamQuestionDto)request.getAttribute("dto");
		   out.println(TemplateFtl.outHtml(obj, null);
		   %>
</c:if>
</body>
</html>