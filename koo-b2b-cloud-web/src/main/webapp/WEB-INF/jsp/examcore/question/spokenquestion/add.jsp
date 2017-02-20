<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.entity.Question"%>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>手动录入试题_口语题_新东方在线</title>
<meta name="keywords" content="" />
<meta name="description" content=""/>
<meta name="robots" content="all" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css-exam/main.css" />
<script type="text/javascript" src="http://images.koolearn.com/www/subject/script/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="http://www.koolearn.com/js/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/jquery.json-2.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/public.js"></script>
<script type="text/javascript" src="http://www.koolearn.com/js/popup.js"></script>
<script type="text/javascript" src="<%=basePath%>js/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=basePath%>js/examcore/question/page.js"></script>
<script type="text/javascript" src="<%=basePath%>js/examcore/question/validate.js"></script>
<script type="text/javascript" src="<%=basePath%>js/examcore/question/question.js"></script>
<script type="text/javascript" src="<%=basePath%>js/examcore/question/question-ext.js"></script>
<script type="text/javascript" src="<%=basePath%>js/examcore/question/short.js"></script>


</head>
<body>
<form action="<%=basePath%>/question/base/spokenQuestion/insert" method="post" name="spokenForm" id="spokenForm">
<c:if test="${not empty(questionDto) && not empty(questionDto.responseType )}">
	<input type="hidden" name="questionDto.responseType" value="${spokenQuestionDto.questionDto.responseType }"/>
</c:if>
<c:if test="${not empty(questionDto) && not empty(spokenQuestionDto.questionDto.question.teId)}">
	<input type="hidden" name="questionDto.question.teId" value="${spokenQuestionDto.questionDto.question.teId }"/>
</c:if>
<c:if test="${empty(choiceQuestionDto.questionDto.question.code)}">
	<input type="hidden" name="questionDto.question.code" value="0"/>
</c:if>
<c:if test="${not empty(choiceQuestionDto.questionDto.question.code)}">
	<input type="hidden" name="questionDto.question.code" value="${spokenQuestionDto.questionDto.question.code}"/>
</c:if>
<input type="hidden" name="questionDto.question.questionTypeId" value="<%=Question.QUESTION_TYPE_SPOKEN %>"/>
<input type="hidden" name="questionDto.questionBankExt.tag1" value="0"/> 
<input type="hidden" name="questionDto.questionBankExt.tag2" value="0"/>
<input type="hidden" name="questionDto.questionBankExt.tag3" value="0"/> 
<input type="hidden" name="questionDto.questionBankExt.status" value="0"/>
<input type="hidden" id="questionDto.questionBankExt.schoolFrom" name="questionDto.questionBankExt.schoolFrom" value="0"/>
<input type="hidden" name="questionDto.textarea_param" id="questionDto.textarea_param" value=""/>
<div class="wp1">
	<c:if test="${spokenQuestionDto.questionDto.responseType==1}">
		<div class="fml">
			<span class="tit"><em class="rd">*</em>子题题型：</span>
			<select name='' disabled="disabled">
				<option value="6">口语题</option>
			</select>
		</div>
	</c:if>
	<div class="fml" style="display:none;">
		<span class="tit">答题提示：</span>
		<input type="text" name="spokenQuestion.answer" value=""  maxLength="255" class="ipt"/><a href="javascript:;" class="a1 bjk">编辑框</a>
		<span class="error1">提示不能超过255个字符</span>
	</div>
	<c:if test="${not empty(spokenQuestionDto.questionDto.question.teId)}">
	<div class="fml ontab">
		<span class="tit wh_at">试题材料：</span>
		<div class="ipta" style="height:220px;">
           <textarea id="question.topicExt" name='question.topicExt' style="display: none;">
      
           </textarea>
			<script type="text/javascript">
			var oFCKeditor = new FCKeditor('question.topicExt');
			           oFCKeditor.BasePath = "<%=request.getContextPath()%>/js/fckeditor/";
			           oFCKeditor.ToolbarSet = "Basic";
			           oFCKeditor.Width="100%";
			           oFCKeditor.Height="217";
			           oFCKeditor.ReplaceTextarea() ;
			       </script>
			</div>
	</div>
	<span class="tit wh_at">子试题题干：</span>
	</c:if>
	
	<div class="fmt">
		<div class="fmbx fmbx2 fmone fc">
			<div class="fl fmfn">
				<div class="num">1</div>
				<a href="javascript:;" class="fmclose"></a>
			</div>
			<textarea class="ipta fck"></textarea>
			<span class="error1">题干不能为空</span>
			<span class="error1">题干不能超过65535个字符</span>
			<div class="ta_r">
				<a href="javascript:;" class="btn1 addtg">增加题干</a>
			</div>
		</div>
	</div>
	</div>
	<div class="wp1-lr bdb1"></div>
	<div class="wp1">
	<div class="ontab">

		<div class="ontabtxt">
			<div class="fmt">
				<span class="tit" id="spokenQuestion.scorestandadId">评分标准：</span> <span class="error1">请填写评分标准</span><br>
			</div>		
			<script type="text/javascript">
				var oFCKeditor = new FCKeditor('spokenQuestion.scorestandad');
			        oFCKeditor.BasePath = "<%=basePath%>js/fckeditor/";
			        oFCKeditor.ToolbarSet = "Basic";
			        oFCKeditor.Height="217";
			        oFCKeditor.Create();
			 </script>
			
		</div>
	</div>
	<div class="fmt">
		<span class="tit" id="answerreferenceId">参考答案：</span> <span class="error1">请填写参考答案</span><br>
	</div>
	<script type="text/javascript">
		var oFCKeditor = new FCKeditor('spokenQuestion.answerreference');
		    oFCKeditor.BasePath = "<%=basePath%>js/fckeditor/";
		    oFCKeditor.ToolbarSet = "Basic";
		    oFCKeditor.Height="217";
		    oFCKeditor.Create();
	</script>
	
	<div class="fmt">
		<span class="tit">答题解析：</span><br>
	</div>
	<div class="mb10">
	<script type="text/javascript">
		var oFCKeditor = new FCKeditor('questionDto.question.explan');
		    oFCKeditor.BasePath = "<%=basePath%>js/fckeditor/";
		    oFCKeditor.ToolbarSet = "Basic";
		    oFCKeditor.Height="217";
		    oFCKeditor.Create();
	</script>
	</div>
	<div class="fml">
		<span class="tit">录音时间：</span>
		<input type="text" name="spokenQuestion.recordtime" id="spokenQuestion.recordtime" value="大于0的正数" df="大于0的正数" class="ipt"/>分钟（为空表示不限时）<span class="error1">时间必须为大于0的正数</span>
	</div>
	</div>
	<div class="wp1-lr bdb1"></div>
	<div class="wp1">
	<div class="ta_c pd1">
		<a href="javascript:;" class="btn1" onclick="saveSpoken(0);return false;">完&nbsp;&nbsp;成</a>
		<a href="javascript:;" class="btn2 ml1" onclick="previewSpoken();return false;">预&nbsp;&nbsp;览</a>
	</div>
</div>
<input type="hidden" name="questionDto_question_id" value=""/>
</form>
<script type="text/javascript">
var TOTAL_CONTEXT_PATH="<%=request.getContextPath()%>";

</script>
<c:if test="${not empty(questionDto) && not empty(questionDto.question.teId)}">
	<script>
		fectchParentCode1();
	</script>
</c:if>
</body>
</html>