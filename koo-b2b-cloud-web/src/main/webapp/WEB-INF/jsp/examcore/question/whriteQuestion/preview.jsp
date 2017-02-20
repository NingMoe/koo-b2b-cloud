<%@page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.template.TemplateFtl"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>写作题前台预览_新东方在线</title>
<meta name="keywords" content="" />
<meta name="description" content=""/>
<meta name="robots" content="all" />
<link type="text/css" rel="stylesheet" href="${ctx}/examcore/css-exam/qt.css" />
<link href="${ctx}/examcore/jPlayer/skin/blue.monday/jplayer.blue.monday.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${examctx}/examcore/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="${ctx}/examcore/1.9.1-jquery-ui.js"></script>
<script type="text/javascript" src="${ctx}/examcore/jPlayer/js/jquery.jplayer.min.js"></script>
<script type="text/javascript" src="${ctx}/examcore/question/qt.js"></script>
</head>
<body>
		<!--写作题--><a name="xzt2" id="xzt2"></a>
		<%
		IExamQuestionDto obj=(IExamQuestionDto)request.getAttribute("whriteQuestionDto");
		if(obj==null){
			obj=(IExamQuestionDto)request.getAttribute("dto");
		}
		out.println(TemplateFtl.outHtml((IExamQuestionDto)obj, null));
		%>
</body>
</html>