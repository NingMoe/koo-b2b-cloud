<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%><jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>手动录入试题_简答题_新东方在线</title>
<meta name="keywords" content="" />
<meta name="description" content=""/>
<meta name="robots" content="all" />
<script type="text/javascript" src="http://images.koolearn.com/www/subject/script/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/jquery.json-2.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/public.js"></script>
<script type="text/javascript" src="http://www.koolearn.com/js/popup.js"></script>
<script type="text/javascript" src="<%=basePath%>js/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/examcore/question/page.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/examcore/question/validate.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/examcore/question/question.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/examcore/question/short.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/examcore/question/question-ext.js"></script>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css-exam/main.css" />

</head>
<body>
<form action="<%=basePath%>question/base/shortQuestion/insert" method="post" name="shortForm" id="shortForm">
<c:if test="${empty(questionDto.responseType)}">
	<input type="hidden" id="questionDto.responseType" name="questionDto.responseType" value="0"/>
</c:if>
<c:if test="${not empty(questionDto.responseType)}">
	<input type="hidden" id="questionDto.responseType" name="questionDto.responseType" value="${questionDto.responseType}"/>
</c:if>
<c:if test="${not empty(questinDto) && not empty(questinDto.question.teId)}">
	<input type="hidden" name="questionDto.question.teId" value="${questinDto.question.teId }"/>	
</c:if>
<input type="hidden" name="questionDto.textarea_param" id="questionDto.textarea_param" value=""/>

		
	<input type="hidden" id="questionDto.questionBankExt.tag1" name="questionDto.questionBankExt.tag1" value="0"/>
	<input type="hidden" id="questionDto.questionBankExt.tag2" name="questionDto.questionBankExt.tag2" value="0"/>
	<input type="hidden" id="questionDto.questionBankExt.tag3" name="questionDto.questionBankExt.tag3" value="0"/>
	<input type="hidden" id="questionDto.questionBankExt.status" name="questionDto.questionBankExt.status" value="0"/>
	<input type="hidden" id="questionDto.questionBankExt.schoolFrom" name="questionDto.questionBankExt.schoolFrom" value="0"/>
	<input type="hidden" id="questionDto.question.questionTypeId" name="questionDto.question.questionTypeId" value="3"/>
		
<div class="wp1">
	<c:if test="${questinDto.responseType==1}">
		<div class="fml">
			<span class="tit"><em class="rd">*</em>子题题型：</span>
			<select name='' disabled="disabled">
				<option value="6">简答题</option>
			</select>
		</div>
	</c:if>
	<div class="fml" style="display:none;">
		<span class="tit">
			<em class="rd">*</em><c:choose><c:when test="${questinDto.responseType==1}">子题编码：</c:when><c:otherwise>试题编码：</c:otherwise></c:choose>
		</span>	
		<input type="text" name="question.code" id="question.code" df="产品简写+日期+自定义编码" value="" maxlength="50" class="ipt"/>
		<span class="error1">请填写试题编码</span>
		<span class="error1">试题编码重复</span>
		<span class="error1">编码只能由数字,字母,下划线和斜线('/')组成</span>
		<input type="hidden" name="questionDto.question.code" id="questionDto.question.code"  value=""/>
	</div>
	<div class="fml" style="display:none;">
		<span class="tit">答题提示：</span>
		<input type="text" name="shortQuestion.answer" value="" class="ipt"  maxLength="255" /><a href="javascript:;" class="a1 bjk">编辑框</a>
		<span class="error1">提示不能超过255个字符</span>
	</div>
	<c:if test="${not empty(questinDto.question.teId)}">
	<div class="fml ontab">
		<span class="tit wh_at">试题材料：</span>
		<div class="ipta" style="height:220px;">
           <textarea id="questionDto.question.topicExt" name='questionDto.question.topicExt' style="display: none;">
      
           </textarea>
			<script type="text/javascript">
			var oFCKeditor = new FCKeditor('questionDto.question.topicExt');
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
			<textarea class="ipta fck" ></textarea>
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
	<div class="fml" style="display:none;">
		<label class="lab1 ontabfn" for="pgfs1">
			<input type="radio" checked="checked" value="1" name="shortQuestion.marktype" id="pgfs1">人工批改/自评分
		</label>
		<label class="lab1 ontabfn" for="pgfs2">
			<input type="radio" name="shortQuestion.marktype" value="2" id="pgfs2">系统批改（关键词匹配）
		</label>
	</div>
	<div class="ontabtxt">
		<div class="fmt">
			<span class="tit" id="scorestandadId"><em class="rd">*</em>评分标准：</span> <span class="error1">请填写评分标准</span><br>
		</div>		
		<script type="text/javascript">
			var oFCKeditor = new FCKeditor('shortQuestion.scorestandad');
		        oFCKeditor.BasePath = "<%=basePath%>js/fckeditor/";
		        oFCKeditor.ToolbarSet = "Basic";
		        oFCKeditor.Height="217";
		        oFCKeditor.Create();
		 </script>
		
	</div>
	<div class="ontabtxt gjc">
	<em class="rd">*</em>填写关键词：
	<input type="text" id="keyWord" df="每个关键词得分=试题总分/关键词个数" value="每个关键词得分=试题总分/关键词个数" class="ipt"/>
	<input type="hidden" name="keyWords" id="keyWords" value=""/> <span class="error1">请填写关键词</span>
	<span class="tip">关键词之间用分号隔开，回车完成录入</span>
	<ul class="gjclist fc">
	</ul>
	</div>
	</div>
	<div class="fmt">
		<span class="tit" id="answerreferenceId" ><em class="rd">*</em>参考答案：</span> <span class="error1">请填写参考答案</span><br>
	</div>
	<script type="text/javascript">
		var oFCKeditor = new FCKeditor('shortQuestion.answerreference');
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
	<div class="fml" style="display: none;">
		<span class="tit wh_at">前台答题框高度：</span>
		<select class="numSel" name="shortQuestion.boxheight" id="shortQuestion.boxheight">
			<option value="2">2行</option>
			<option value="3">3行</option>
			<option value="4">4行</option>
			<option value="5" selected="selected">5行</option>
			<option value="6">6行</option>
			<option value="7">7行</option>
			<option value="8">8行</option>
			<option value="9">9行</option>
			<option value="10">10行</option>
			<option value="0" class="other">自定义</option>
		</select><input type="number" id="otherHeight"  value="11" min="1" max="20" class="hide ipt3" /><span class="error1">高度必须为大于0的整数</span>
		<span class="ml3">是否包含工具栏：</span>
		<label class="lab2" for="isgjl1">
			<input type="radio" checked="checked" value=0 name="shortQuestion.istoolbar" id="shortQuestion.istoolbar">否
		</label>
		<label class="lab2" for="isgjl2">
			<input type="radio" name="shortQuestion.istoolbar" value=1 id="shortQuestion.istoolbar">是
		</label>
	</div>
	</div>
	<div class="wp1-lr bdb1"></div>
	<div class="wp1">
	<div class="ta_c pd1">
		<a href="javascript:;" class="btn1" onclick="saveShort(0);return false;" >完&nbsp;&nbsp;成</a>
		<a href="javascript:;" class="btn2 ml1"  onclick="previewShort();return false;">预&nbsp;&nbsp;览</a>
	</div>
</div>
<input type="hidden" name="questionDto_question_id" value=""/>
</form>
<c:if test="${not empty(questinDto) && not empty(questinDto.question.teId)}">
	<script>
		fectchParentCode1();
	</script>
</c:if>
</body>
</html>