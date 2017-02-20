<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.koolearn.cloud.exam.examcore.util.QuestionUtil"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.*"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.template.TemplateFtl"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
  <head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>预览</title>
    <link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/qt.css" />
    <link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/kpt_qt.css" />
    <link href="<%=basePath%>jPlayer/skin/blue.monday/jplayer.blue.monday.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="http://images.koolearn.com/www/subject/script/jquery-1.8.2.min.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/jPlayer/js/jquery.jplayer.min.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/1.9.1-jquery-ui.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/jquery.json-2.2.min.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/fckeditor/fckeditor.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/popup.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/jquery.form.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/question/public.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/question/question.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/question/qt.js"></script>
	<script type="text/javascript" src="${examctx}/examcore/question/kpt_qt.js"></script>
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
    <script>
    $(function(){
	var step=${step};
	if(step==0){
		var pform = $(window.parent.document).find("#complexForm");
		$('#form').html(pform.html());
		setValue($("#form"),pform,":text");
		setValue($("#form"),pform,":radio:checked");
		setValue($("#form"),pform,":checkbox");
		setValue($("#form"),pform,"select");
		setValue($("#form"),pform,"textarea");
		$('#form').submit();
	}else{
		$('#form').remove();
		$('body').show();
	}
	});
	
	function setValue(form,pform,element){
		form.find(element).each(function(i){
			$(this).val(pform.find(element).eq(i).val());
		});
	}
    </script>
  </head>
  
  <body>
  <form id="form" action="<%=basePath%>question/base/complex/preview/1" method="post"></form>
<c:if test="${step==1}">
<br/>
	<% 
		String questionType_ =""+(Integer)request.getAttribute("questionType");
		IExamQuestionDto obj=(IExamQuestionDto)request.getAttribute("dto");
		QuestionViewDto questionViewDto = (QuestionViewDto)request.getAttribute("questionViewDto");
		questionViewDto = QuestionUtil.getSubQuestionViewDto(obj,questionViewDto);
		out.println(TemplateFtl.outHtml((IExamQuestionDto)obj, questionViewDto));	
	%>
	<%-- Du HongLin 2014-06-13 11:28 --%>
	<%-- <%
		/* Du HongLin 2014-06-12 18:25 */
		out.println(ChoiceBlank.instance(request, response).outTemplate(process, 0)); 
<%@ include file="preview.inc"%>
	%> --%>
</c:if>
  </body>
</html>
