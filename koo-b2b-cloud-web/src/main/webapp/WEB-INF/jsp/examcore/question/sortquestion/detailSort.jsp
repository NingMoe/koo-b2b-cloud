<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.entity.Question"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.*"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.template.TemplateFtl"%>
<%@page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/preview.css" />
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/qt.css" />
<link href="${examctx}/examcore/jPlayer/skin/blue.monday/jplayer.blue.monday.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${examctx}/examcore/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="${examctx}/examcore/1.9.1-jquery-ui.js"></script>
<script type="text/javascript" src="${examctx}/examcore/jPlayer/js/jquery.jplayer.min.js"></script>
<script type="text/javascript" src="${examctx}/examcore/jquery.json-2.2.min.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/public.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/qt.js"></script>
<script type="text/javascript" src="${examctx}/examcore/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/question.js"></script>

<title>预览</title>
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
<form action="<%=request.getContextPath()%>/question/base/choice/detail/1?questionType=<%=Question.QUESTION_TYPE_SORT%>" method="post">
</form>
<c:if test="${step==1}">
		<% IExamQuestionDto obj=(IExamQuestionDto)request.getAttribute("dto");
		   out.println(TemplateFtl.outHtml(obj, null)); %>
</c:if>
</body>
</html>