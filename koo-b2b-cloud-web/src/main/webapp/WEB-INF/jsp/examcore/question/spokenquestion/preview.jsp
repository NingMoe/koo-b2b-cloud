<%@page import="com.koolearn.cloud.exam.examcore.question.dto.SpokenQuestionDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.template.TemplateFtl"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>口语题前台预览_新东方在线</title>
<meta name="keywords" content="" />
<meta name="description" content=""/>
<meta name="robots" content="all" />
<link rel="stylesheet" type="text/css" href="${ctx}/old/reset.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/old/comm.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/old/style.css"/>
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/qt.css" />
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/preview.css" />
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/kpt_qt.css" />
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/jPlayer/skin/blue.monday/jplayer.blue.monday.css" />
<script type="text/javascript" src="${examctx}/examcore/popup.js"></script>
<script type="text/javascript" src="${examctx}/examcore/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="${examctx}/examcore/1.9.1-jquery-ui.min.js"></script>
<script type="text/javascript" src="${examctx}/examcore/jPlayer/js/jquery.jplayer.min.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/jquery.json-2.2.min.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/qt.js"></script>
<script type="text/javascript" src="${examctx}/examcore/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/public.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/kpt_qt.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/jquery.md5.js"></script>
<script type="text/javascript" src="${examctx}/examcore/old/public.js"></script>

<script type="text/javascript">
	function soundUploadCallback(id,val){ // 口语题回调函数
		// 回调
		var valArr = val.split(",");
		if(valArr[0] == "0"){
			// 提交失败
			pop_alert("温馨提示","录音提交失败，请重新提交！");
		}else{
			// 提交成功
			pop_alert("温馨提示","录音提交成功！");
		}
	};
	function showMessage(message){
		pop_alert("温馨提示", "" + message);
	}
</script>
</head>
<body>
<%-- <c:if test="${spokenquestionDto.question.teId == 0 }">
<div class="wp"> 
<div class="wp2">
<div class="wp3">
<div class="wp4">
</c:if> --%>
		<!--口语题--><a name="jdt" id="tkt"></a>
		<%
		Object obj=request.getAttribute("dto");
		QuestionViewDto questionViewDto=(QuestionViewDto)request.getAttribute("questionViewDto");
			out.println(TemplateFtl.outHtml((SpokenQuestionDto)obj, questionViewDto));
		 %>
	<%-- <div class="tit1 pd1">
	<!-- 
	<span class="ft6">Part I. Speaking（15min）</span>
		<div class="ft5 mb20"><strong>Directions:</strong> <br />
	In this part,you will have 15 minutes to go over the passage quickly and answer the questions ．For questions 1-3,complete the questions with the information given in the below.
		</div>
	 -->
	</div>
	<dl class="st" ty="3">
		<dt class="nobold"><span class="num">1</span>	
		<c:if test="${spokenquestionDto.question.teId != 0 }">
			${spokenquestionDto.question.topicExt}
		</c:if>	
		<c:forEach var="questionAttach" items="${spokenquestionDto.questionAttachs}">
			${questionAttach.content }
		</c:forEach>
		${spokenquestionDto.topic  }
		<c:if test="${not empty(spokenquestionDto.answer)}">
		<p class="ft3">${spokenquestionDto.answer}</p>
		</c:if>
		</dt>
		<dd>
			<object type="application/x-shockwave-flash" width="218" height="137" id="play" data="<%=basePath%>swf/record.swf?read_time=${spokenquestionDto.recordtimeSecond}">
		        <param name="allowScriptAccess" value="sameDomain" />
		        <param name="wmode" value="transparent" />
		        <param name="allowFullScreen" value="true" />
		        <param name="FlashVars" value="read_time=${spokenquestionDto.recordtimeSecond}">
		        <param name="movie" value="<%=basePath%>swf/record.swf" />
		        <param name="quality" value="high" />
		        <embed id="myembed" src='<%=basePath%>swf/record.swf' quality="high" width="218" FlashVars="read_time=${spokenquestionDto.recordtimeSecond}" height="137" name="play" allowScriptAccess="sameDomain" allowFullScreen="true" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
	    	</object>
			
		</dd>
	</dl>
	
	<!--end 口语题-->
<c:if test="${spokenquestionDto.question.teId == 0 }">
	</div>
	</div> 
	</div>  
	 </div> 
</c:if> --%>
</body>
</html>