<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.ShortQuestionDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.*"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.template.TemplateFtl"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>简答题前台预览_新东方在线</title>
<meta name="keywords" content="" />
<meta name="description" content=""/>
<meta name="robots" content="all" />
<link type="text/css" rel="stylesheet" href=${examctx}/examcore/css-exam/qt.css" />
<link href=${examctx}/examcore/jPlayer/skin/blue.monday/jplayer.blue.monday.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${examctx}/examcore/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src=${examctx}/examcore/1.9.1-jquery-ui.js"></script>
<script type="text/javascript" src=${examctx}/examcore/jPlayer/js/jquery.jplayer.min.js"></script>
<script type="text/javascript" src=${examctx}/examcore/question/qt.js"></script>
<script type="text/javascript">
function changePH(){
	if(window == top) return;
	var treeHeight = $("body").height();
	parent.document.getElementById('frameContent').height=treeHeight+'px';
}
</script>
</head>
<body>
<%-- <c:if test="${shortQuestionDto.question.teId == 0 }">
	<div class="wp">
	<div class="wp2">
	<div class="wp3">
	<div class="wp4">
</c:if>--%>
	<!--简答题--><a name="jdt" id="jdt"></a>
	<div class="tit1 pd1">简答题</div>
	<%-- <dl class="st" ty="1">
		<dt class="nobold"><span class="num">1</span>	
		<c:if test="${shortQuestionDto.question.teId != 0 }">
			${shortQuestionDto.question.topicExt}
		</c:if>	
		<c:forEach var="questionAttach" items="${shortQuestionDto.questionAttachs}">
			${questionAttach.content }
		</c:forEach>
		${shortQuestionDto.topic }
		<c:if test="${not empty(shortQuestionDto.answer)}">
		<p class="ft3">${shortQuestionDto.answer}</p>
		</c:if>
		</dt>
		<dd>
			<div class="ft4">填写答案</div>
			<textarea class="jdt"></textarea>
			<div class="jdtdiv" style="display:none"></div>
			<div class="ta_r"><a href="javascript:;" class="upload_0902">上传图片</a><a href="javascript:;" class="jdtzhk zhk2">完成</a></div>
		</dd>
	</dl>
	<!--end 简答题-->
<c:if test="${shortQuestionDto.question.teId == 0 }">
	</div>
	</div> 
	</div>  
	</div> 
</c:if> --%>
	<%
	ShortQuestionDto shortQuestionDto=(ShortQuestionDto)request.getAttribute("shortQuestionDto");
	QuestionViewDto questionViewDto=(QuestionViewDto)request.getAttribute("questionViewDto");
	out.println(TemplateFtl.outHtml(shortQuestionDto, questionViewDto));
	%>
</body>
</html>