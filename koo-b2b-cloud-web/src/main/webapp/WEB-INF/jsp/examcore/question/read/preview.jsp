<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.*"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.entity.*"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.template.TemplateFtl"%>
<%@page import="com.koolearn.cloud.exam.examcore.util.QuestionUtil"%>
<%@page import="java.util.*"%>
<%@page import="com.koolearn.cloud.exam.examcore.util.TestUtil"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/qt.css" />
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/preview.css" />
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/kpt_qt.css" />
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/zw.css" />
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/jPlayer/skin/blue.monday/jplayer.blue.monday.css" />
<script type="text/javascript" src="${examctx}/examcore/popup.js"></script>
<script type="text/javascript" src="${examctx}/examcore/jquery-1.8.2.min.js"></script>
<script type="text/javascript">
	if($.ui){

	}else{
		document.write('<script type="text/javascript" src="${examctx}/examcore/1.9.1-jquery-ui.min.js"><'+'/script>');
	}
</script>
<script type="text/javascript" src="${examctx}/examcore/jquery.cookie.js"></script>
<script type="text/javascript" src="${examctx}/examcore/jPlayer/js/jquery.jplayer.min.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/jquery.json-2.2.min.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/qt.js"></script>
<script type="text/javascript" src="${examctx}/examcore/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/public.js"></script>
<!--
<script type="text/javascript" src="${examctx}/examcore/js/examcore/question/kpt_qt.js"></script>
-->
<script type="text/javascript" src="${examctx}/examcore/question/jquery.md5.js"></script>
<script type="text/javascript" src="${examctx}/examcore/js-exam/rangy-core.js"></script>
<script type="text/javascript" src="${examctx}/examcore/js-exam/zw.js"></script>

<title>预览</title>
<style type="text/css">
	body{
		height: 500px;
	}
	.st DT{
		font-weight: normal !important;
		font-family: Arial, Verdana, sans-serif;
		font-size: 12px;
	}
	.dx_dluo{
		position: relative !important;
	}
</style>
<script type="text/javascript">
$(function(){
	var step=${step};
	if(step==0){
<%--		$('body').hide();--%>
		$('form').html(window.opener.findPreViewData2());
		document.forms[0].submit();
	}else{
		$('body').show();
		//preview_qt();
	}
});
function ajaxPost(){
		$.ajax({
			url: "<%=request.getContextPath()%>/question/base/read/preview/1",
			type: "post",
			cache: false,
			async:false,
			//datatype: "application/json",
			data: parent.findPreViewData(),
			success: function(data){
				alert(data);
				var d =eval("("+data+")");//转换为json对象 
				if(d && d.result == "true"){
					//alert(d.message);
					//rc_pop.close();
					alertMsg(d.message);
				}else{
					alertMsg(d.message)
				}
				
			},
			error: function(data){
				//alert("请求失败，请重试！");
				popAlert("请求失败，请重试！")
			}
		});
}
</script>
</head>
<body style="display: none;"><!-- style="display: none;" -->
<form action="<%=request.getContextPath()%>/question/base/read/preview/1" method="post">
</form>
<c:if test="${step==1}">
		<% Object obj2=request.getAttribute("dto");
			ComplexQuestionDto readQuestionDto=(ComplexQuestionDto)obj2;
			QuestionViewDto questionViewDto = (QuestionViewDto)request.getAttribute("questionViewDto");
			questionViewDto = QuestionUtil.getSubQuestionViewDto(readQuestionDto,questionViewDto);
			
		   	out.println(TemplateFtl.outHtml(readQuestionDto, questionViewDto));
		%>
		<!-- %@ include file="preview.inc"% -->
</c:if>
</body>
</html>