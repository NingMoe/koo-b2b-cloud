<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.koolearn.cloud.exam.examcore.util.QuestionUtil"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.template.TemplateFtl"%>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://www.koolearn.com/taglib/koo" prefix="koo" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<!-- 题目页头 -->
	<head>
		<meta charset="UTF-8">
		<title>题库-预览</title>
		<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/qt.css" />
		<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/cal.css" />
		<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/zw.css" />
		<link href="${examctx}/examcore/jPlayer/skin/blue.monday/jplayer.blue.monday.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${examctx}/examcore/jquery-1.8.2.min.js"></script>
		<script type="text/javascript" src="${examctx}/examcore/1.9.1-jquery-ui.js"></script>
		<script type="text/javascript" src="${examctx}/examcore/jPlayer/js/jquery.jplayer.min.js"></script>
		<script type="text/javascript" src="${examctx}/examcore/question/public.js"></script>
		<script type="text/javascript" src="${examctx}/examcore/question/qt.js"></script>
		<script type="text/javascript" src="${examctx}/examcore/js-exam/rangy-core.js"></script>
		<script type="text/javascript" src="${examctx}/examcore/js-exam/zw-q.js"></script>
	</head>
	<script type="text/javascript">
	function soundUploadCallback(id,val){ // 口语题回调函数
		// 回调
		var valArr = val.split(",");
		if(valArr[0] == "0"){
			// 提交失败
			//pop_alert("温馨提示", "录音提交失败，请重新提交！");
			alert("录音提交失败，请重新提交！");
		}else{
			// 提交成功
			//pop_alert("温馨提示", "录音提交成功！");
			alert("录音提交成功！");
		}
	};
	function showMessage(message){
		alert(message);
	}
	</script>
	<body>
		<%
			QuestionViewDto questionViewDto=(QuestionViewDto)request.getAttribute("questionViewDto");
			IExamQuestionDto dto=(IExamQuestionDto)request.getAttribute("dto");
			questionViewDto = QuestionUtil.getSubQuestionViewDto(dto,questionViewDto);
			out.println(TemplateFtl.outHtml(dto, questionViewDto));
		 %>
	</body>
