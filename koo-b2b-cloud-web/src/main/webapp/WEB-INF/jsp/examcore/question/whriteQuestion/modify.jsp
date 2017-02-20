<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>手动修改试题_写作题_新东方在线</title>
<meta name="keywords" content="" />
<meta name="description" content=""/>
<meta name="robots" content="all" />
<script type="text/javascript" src="http://images.koolearn.com/www/subject/script/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="http://www.koolearn.com/js/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/jquery.json-2.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/public.js"></script>
<script type="text/javascript" src="http://www.koolearn.com/js/popup.js"></script>
<script type="text/javascript" src="<%=basePath%>js/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/examcore/question/page.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/examcore/question/validate.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/examcore/question/question.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/examcore/question/question-ext.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/examcore/question/short.js"></script>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css-exam/main.css" />
</head>
<body>
<form action="<%=basePath%>question/base/whriteQuestion/modify" method="post" name="whriteForm" id="whriteForm">
<input type="hidden" name="whriteQuestion.id" value="${dto.whriteQuestion.id}"/>
<input type="hidden" name="whriteQuestion.questionId" value="${dto.questionDto.question.id }"/>
<input type="hidden" id="questionId" name="questionDto.question.id" value="${dto.questionDto.question.id }"/>
<input type="hidden" name="questionDto.question.status" value="${dto.questionDto.question.status }"/>
<!-- input type="hidden" name="questionDto.question.createDate" value='${dto.questionDto.question.createDate}'/ -->
<input type="hidden" name="questionDto.question.createBy" value="${dto.questionDto.question.createBy }"/>
<input type="hidden" name="questionDto.question.questionTypeId" value="${dto.questionDto.question.questionTypeId }"/>
<input type="hidden" name="questionDto.question.issubjectived" value="${dto.questionDto.question.issubjectived }"/>
<c:if test="${not empty(questionDto) && not empty(questionDto.responseType )}">
	<input type="hidden" id="questionDto.responseType" name="questionDto.responseType" value="${questionDto.responseType }"/>
</c:if>
<input type="hidden" id="questionDto.question.teId" name="questionDto.question.teId" value="${dto.questionDto.question.teId }"/>
<input type="hidden" name="pageNo" value="${pageNo}" />
<input type="hidden" name="redictType" value="${redictType }"/>
<input type="hidden" name="questionDto.textarea_param" id="questionDto.textarea_param" value=""/>

<input type="hidden" id="questionDto.questionBankExt.tag1" name="questionDto.questionBankExt.tag1" value="${dto.questionDto.questionBankExt.tag1 }"/>
<input type="hidden" id="questionDto.questionBankExt.tag2" name="questionDto.questionBankExt.tag2" value="${dto.questionDto.questionBankExt.tag2 }"/>
<input type="hidden" id="questionDto.questionBankExt.tag3" name="questionDto.questionBankExt.tag3" value="${dto.questionDto.questionBankExt.tag3 }"/>
<input type="hidden" id="questionDto.questionBankExt.status" name="questionDto.questionBankExt.status" value="${dto.questionDto.questionBankExt.status }"/>

<div class="wp1">
	<c:if test="${dto.questionDto.responseType==1}">
		<div class="fml">
			<span class="tit"><em class="rd">*</em>子题题型：</span>
			<select name='' disabled="disabled">
				<option value="6">作文题</option>
			</select>
		</div>
	</c:if>
	<div class="fml" style="display:none;">
		<input type="hidden" name="questionDto.question.code" id="questionDto.question.code"  value="${dto.questionDto.question.code}"/>
	</div>
	<div class="fml" style="display:none;">
		<span class="tit">答题提示：</span>
		<input type="text" name="whriteQuestion.answer" value="${dto.whriteQuestion.answer }" class="ipt"/><a href="javascript:;" class="a1 bjk">编辑框</a>
		<span class="error1">提示不能超过255个字符</span>
	</div>
	<c:if test="${not empty(dto.questionDto.question.teId) && dto.questionDto.question.teId != 0}">
	<div class="fml ontab">
		<span class="tit wh_at">试题材料：</span>
		<div class="ipta" style="height:220px;">
           <textarea id="questionDto.question.topicExt" name='questionDto.question.topicExt' style="display: none;">
      			${dto.questionDto.question.topicExt}
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
	<% int i=1; %>
	<c:forEach var="questionAttach" items="${dto.questionDto.questionAttachs}">
			<div class="fmbx fmbx2 fc">
				<div class="fl fmfn">
					<div class="num"><%=i++%></div>
					<a href="javascript:;" class="fmclose">
						<pre>			
						</pre>
					</a>
				</div>
				<textarea class="ipta fck">${questionAttach.content}</textarea>
				<span class="error1">题干不能为空</span>
				<span class="error1">题干不能超过65535个字符</span>
				<div class="ta_r">
					<a href="javascript:;" class="btn1 addtg">增加题干</a>
				</div>
			</div>
	</c:forEach>
	<div class="fmt">
		<div class="fmbx fmbx2  fc">
			<div class="fl fmfn">
				<div class="num"><%=i++%></div>
				<a href="javascript:;" class="fmclose"></a>
			</div>
			<textarea class="ipta fck">${dto.whriteQuestion.topic}</textarea>
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
			<span class="tit" id="scorestandadId">评分标准：</span> <span class="error1">请填写评分标准</span><br>
		</div>		
		<textarea id="whriteQuestion.scorestandad"  name="whriteQuestion.scorestandad"  class="ipta" style="display: none;">${dto.whriteQuestion.scorestandad}</textarea>
		<script type="text/javascript">
			var oFCKeditor = new FCKeditor('whriteQuestion.scorestandad');
		        oFCKeditor.BasePath = "<%=basePath%>js/fckeditor/";
		        oFCKeditor.ToolbarSet = "Basic";	        
		        oFCKeditor.ReplaceTextarea() ;
		 </script>
		
	</div>
	</div>
	<div class="fmt">
		<span class="tit" id="answerreferenceId">参考答案：</span> <span class="error1">请填写参考答案</span><br>
	</div>
	<textarea id="whriteQuestion.answerreference" name="whriteQuestion.answerreference"  class="ipta" style="display: none;">${dto.whriteQuestion.answerreference}</textarea>
	<script type="text/javascript">
		var oFCKeditor = new FCKeditor('whriteQuestion.answerreference');
		    oFCKeditor.BasePath = "<%=basePath%>js/fckeditor/";
		    oFCKeditor.ToolbarSet = "Basic";
		    oFCKeditor.ReplaceTextarea() ;
	</script>
	
	<div class="fmt">
		<span class="tit">答题解析：</span><br>
	</div>
	<div class="mb10">
	<textarea id="questionDto.question.explan"  name="questionDto.question.explan"  class="ipta" style="display: none;">${dto.questionDto.question.explan}</textarea>
	<script type="text/javascript">
		var oFCKeditor = new FCKeditor('questionDto.question.explan');
		    oFCKeditor.BasePath = "<%=basePath%>js/fckeditor/";
		    oFCKeditor.ToolbarSet = "Basic";
		    oFCKeditor.ReplaceTextarea() ;
	</script>
	</div>
	<div class="fml" style="display: none;">
		<span class="tit wh_at">前台答题框高度：</span>
		<select class="numSel" name="whriteQuestion.boxheight" id="whriteQuestion.boxheight">
			<option value="0"   class="other">自定义</option>
			<option value="2" <c:if test="${dto.whriteQuestion.boxheight == 2 }"> selected="selected"</c:if> >2行</option>
			<option value="3" <c:if test="${dto.whriteQuestion.boxheight == 3 }"> selected="selected"</c:if> >3行</option>
			<option value="4" <c:if test="${dto.whriteQuestion.boxheight == 4 }"> selected="selected"</c:if> >4行</option>
			<option value="5" <c:if test="${dto.whriteQuestion.boxheight == 5 }"> selected="selected"</c:if> >5行</option>
			<option value="6" <c:if test="${dto.whriteQuestion.boxheight == 6 }"> selected="selected"</c:if> >6行</option>
			<option value="7" <c:if test="${dto.whriteQuestion.boxheight == 7 }"> selected="selected"</c:if> >7行</option>
			<option value="8" <c:if test="${dto.whriteQuestion.boxheight == 8 }"> selected="selected"</c:if> >8行</option>
			<option value="9" <c:if test="${dto.whriteQuestion.boxheight == 8 }"> selected="selected"</c:if> >8行</option>
			<option value="10" <c:if test="${dto.whriteQuestion.boxheight == 10 }"> selected="selected"</c:if> >10行</option>
		</select><input type="number" id="otherHeight" name="otherHeight" value="11" min="1" max="20" class="hide ipt3" /><span class="error1">高度必须为大于0的整数</span>	
		
		<script type="text/javascript">
		(function(sele){
			var val = sele.val();
			if(val<2 || val>10){
				sele.children(".other").attr("selected","selected");
				$("#otherHeight").val("${dto.whriteQuestion.boxheight}");
				sele.change();
			}
		})($("#boxheight"));
		</script>
				
		<span class="ml3">是否包含工具栏：</span>
		<label class="lab2" for="isgjl1">
			<input type="radio" checked="checked" value=0 name="whriteQuestion.istoolbar" id="whriteQuestion.istoolbar" <c:if test="${dto.whriteQuestion.istoolbar == 0 }">  checked="checked" </c:if>>否
		</label>
		<label class="lab2" for="isgjl2">
			<input type="radio" name="whriteQuestion.istoolbar" value=1 id="whriteQuestion.istoolbar" <c:if test="${dto.whriteQuestion.istoolbar == 1 }">  checked="checked" </c:if>>是
		</label>
	</div>
	</div>
	<div class="wp1-lr bdb1"></div>
	<div class="wp1">
	<div class="ta_c pd1">
		<a href="javascript:;" class="btn1" onclick="saveWhrite(${type});return false;">完&nbsp;&nbsp;成</a>
		<a href="javascript:;" class="btn2 ml1" onclick="previewWhrite();return false;">预&nbsp;&nbsp;览</a>
	</div>
</div>
<input type="hidden" name="questionDto_question_id" value="${ dto.questionDto.question.id}"/>
</form>
</body>
</html>