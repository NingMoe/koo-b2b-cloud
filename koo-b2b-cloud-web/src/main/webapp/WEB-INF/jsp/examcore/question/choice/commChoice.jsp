<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>手动录入试题_选择题_新东方在线</title>
<meta name="keywords" content="" />
<meta name="description" content=""/>
<meta name="robots" content="all" />
<base target="_self" />
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/main.css" />
<script type="text/javascript" src="${examctx}/examcore/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="${examctx}/examcore/popup.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/jquery.json-2.2.min.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/public.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/question.js"></script>
<script type="text/javascript" src="${examctx}/examcore/jquery.form.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/page.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/validate.js"></script>
<script type="text/javascript" src="${examctx}/examcore/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/question-ext.js"></script>

<script type="text/javascript">

TOTAL_CONTEXT_PATH="<%=request.getContextPath()%>";

function clearAll(){
	$('form')[0].reset(); 
}
function findIdCode(){
	var str="["+$.toJSON($('input[name="questionDto_question_id"]').serializeObject())+",";
	str+=$.toJSON($('input[name="questionDto.question.code"]').serializeObject())+"]";
	return str;
}
function postME(){
	var isReturn = false;
	if(checkForm()){
		$("input[name^='fmbx1_']").each(function(){
			var answer=$(this);	
			var err = $(this).parent().children(".error1:eq(0)");
			var err1 = $(this).parent().children(".error1:eq(1)");
			if(isBlank(answer.val())){
				err.show();
				isReturn = true;
				$(this).focus();
				alertMsg("答案不能为空!");	
				return;
			}
			
			if(lengthLarger(answer.val(), 900)){
				err1.show();
				isReturn = true;
				$(this).focus();
				alertMsg("答案长度不能超过900字符!");	
				return;
			}
		});
		fecthTopicExt();
		//标签赋值
		fillHeadValue();
		if(!isReturn){
			setRadioValue();
			if(!chkIsright()){
				alertMsg("至少要选中一个选项!");	
				return;
			}
			document.forms[0].submit();
		}
	}
}

function beforeDetail(){
	var isReturn = false;
	if(checkForm()){
		$("input[name^='fmbx1_']").each(function(){
			var answer=$(this);	
			var err = $(this).parent().children(".error1:eq(0)");
			var err1 = $(this).parent().children(".error1:eq(1)");
			if(isBlank(answer.val())){
				err.show();
				isReturn = true;
				$(this).focus();
				alertMsg("答案不能为空!");	
				return;
			}
			
			if(lengthLarger(answer.val(), 900)){
				err1.show();
				isReturn = true;
				$(this).focus();
				alertMsg("答案长度不能超过900字符!");	
				return;
			}
		});
		fecthTopicExt();
		//标签赋值
		fillHeadValue();
		if(!isReturn){
			setRadioValue();
			if(!chkIsright()){
				alertMsg("至少要选中一个选项!");	
				return;
			}
			previewPop();
		}	
	}	
}

$(function(){
	initRadioEvent();
	initSelectAndCheckedValue();
<% 
	String msg=(String)request.getAttribute("errMsg");
	if(msg!=null){
%>
	popAlert('<%=msg%>');
<%} 
%>
<c:if test="${not empty (choiceQuestionDto.questionDto.question.teId) and choiceQuestionDto.questionDto.question.teId!=0}">
	fectchParentCode();
</c:if>
});	
</script>
<style type="text/css">
a:link { text-decoration: none;}
　　 a:active { text-decoration:none;}
　　 a:hover { text-decoration:none;} 
　　 a:visited { text-decoration: none;}
</style>
</head>
<body >
<form action="<%=request.getContextPath()%>/question/base/choice/save2" method="post">

	
<div class="wp1">
	<c:if test="${choiceQuestionDto.questionDto.responseType==1}">
	<div class="fml">
		<span class="tit"><em class="rd">*</em>子题题型：</span>
		<select name='' disabled="disabled">
			<option value="6">普通单选</option>
		</select>
	</div>
	</c:if>
	<div class="fml" style="display:none;">
		题编码： 
		<c:choose>
		<c:when test="${empty (choiceQuestionDto.questionDto.question.code)}">
			<input type="text" id='questionDto.question.code'  name='questionDto.question.code' value=""  df="产品简写+日期+自定义编码" value="" class="ipt"/>
		</c:when>
		<c:otherwise>
		<span class="tit"><em class="rd">*</em><jsp:include page="codeNamePart.jsp" flush="true" />题编码：</span>
		<input type="text" id='questionDto.question.code'  name='questionDto.question.code'  value="${choiceQuestionDto.questionDto.question.code}" class="ipt"/>
		</c:otherwise>
		</c:choose>
	</div>
	<div class="fml" style="display:none;">
		<span class="tit">答题提示：</span><input type="text" name='questionDto.question.questionTip' value='${choiceQuestionDto.questionDto.question.questionTip}' class="ipt"/><a href="javascript:;" class="a1 bjk">编辑框</a>
		<span class="error1">提示不能超过255字符</span>
	</div>
	<c:if test="${not empty (choiceQuestionDto.questionDto.question.teId) and choiceQuestionDto.questionDto.question.teId!=0}">
	<div class="fml ontab">
		<span class="tit wh_at">试题材料：</span>
		<div class="ipta"  style="height:220px;">
           <textarea id="questionDto.question.topicExt" name='questionDto.question.topicExt' style="display: none;">
           	${choiceQuestionDto.questionDto.question.topicExt}
           </textarea>
			<script type="text/javascript">
			var oFCKeditor = new FCKeditor('questionDto.question.topicExt');
			           oFCKeditor.BasePath = "${examctx}/examcore/fckeditor/";
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
	<c:choose>
		<c:when test="${choiceQuestionDto.questionDto.question.id>0}">
			<% int i=1; %>
			<c:forEach var="name" items="${choiceQuestionDto.questionDto.questionAttachs}">
			<div class="fmbx fmbx2 fc">
				<div class="fl fmfn">
					<div class="num"><%=i++%></div>
					<a href="javascript:;" class="fmclose">
					</a>
				</div>
				<textarea class="ipta fck" >${name.content}</textarea>
				<span class="error1">题干不能为空</span>
				<span class="error1">题干不能超过65535个字符</span>
				<div class="ta_r">
					<a href="javascript:;" class="btn1 addtg">增加题干</a>
				</div>
			</div>
			</c:forEach>
			<div class="fmbx fmbx2 fc">
				<div class="fl fmfn">
					<div class="num"><%=i++%></div>
					<a href="javascript:;" class="fmclose">
					</a>
				</div>
				<textarea class="ipta fck" >${choiceQuestionDto.choiceQuestion.topic}</textarea>
				<span class="error1">题干不能为空</span>
				<span class="error1">题干不能超过65535个字符</span>
				<div class="ta_r">
					<a href="javascript:;" class="btn1 addtg">增加题干</a>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<div class="fmbx fmbx2 fmone fc">
			<div class="fl fmfn">
				<div class="num">1</div>
				<a href="javascript:;" class="fmclose">
				</a>
				</div>
				<textarea class="ipta fck"></textarea>
				<span class="error1">题干不能为空</span>
				<span class="error1">题干不能超过65535个字符</span>
				<div class="ta_r">
					<a href="javascript:;" class="btn1 addtg">增加题干</a>
				</div>
			</div>
		</c:otherwise>
		</c:choose>
	</div>
	</div>
	<div class="wp1-lr bdb1"></div>
	<div class="wp1">
	<div class="fml">
		<select name='choiceQuestion.composeType' v='${choiceQuestionDto.choiceQuestion.composeType}'>
			<option value="1">竖向排列</option>
			<option value="2">横向2列</option>
			<option value="3">横向3列</option>
			<option value="4">横向4列</option>
			<option value="5">横向5列</option>
			<option value="6">横向6列</option>
			<option value="7">横向7列</option>
		</select>
		<a href="javascript:;" class="a1" onclick="pladdfm(1,1)">批量添加描述</a>
	</div>
	<div class="fmbx fmbx1 fmone fc">
		<div class="fl fmfn">
			<div class="num">1</div>
			<a href="javascript:;" class="fmclose"></a>
		</div>
		<ul>
			<c:choose>
			<c:when test="${fn:length(choiceQuestionDto.choiceAnswers)>0}">
				<% int i=0; %>
				<c:forEach var="name" items="${choiceQuestionDto.choiceAnswers}">
				<li>
					<input type="radio" class="fl cbx1" name="isright" value='${name.isright}' <c:if test="${name.isright==1}">checked="checked"</c:if> />&nbsp; <span class="xh"><%=(char)(65+i)%></span><input type="text" name="fmbx1_<%=(char)(97+i++)%>" value="${name.description}" class="ipt wh1"/><span class="fn"><a href="javascript:;" class="add"></a><a href="javascript:;" class="remove"></a><a href="javascript:;" class="up"></a><a href="javascript:;" class="down"></a></span><a href="javascript:;" class="a1 bjk">编辑框</a>
					<span class="error1">答案不能为空</span>
					<span class="error1">答案长度不能超过900个字符</span>
				</li>
				</c:forEach>
			</c:when>
			<c:otherwise>
			<li>
				<input type="radio" class="fl cbx1" name="isright" value="" />&nbsp;<span class="xh">A</span><input type="text" name="fmbx1_a" class="ipt wh1" value=""/><span class="fn"><a href="javascript:;" class="add"></a><a href="javascript:;" class="remove"></a><a href="javascript:;" class="up"></a><a href="javascript:;" class="down"></a></span><a href="javascript:;" class="a1 bjk">编辑框</a>
				<span class="error1">答案不能为空</span>
				<span class="error1">答案长度不能超过900个字符</span>
			</li>
			<li>
				<input type="radio" class="fl cbx1" name="isright"  value="" />&nbsp;<span class="xh">B</span><input type="text" name="fmbx1_b" class="ipt wh1" value=""/><span class="fn"><a href="javascript:;" class="add"></a><a href="javascript:;" class="remove"></a><a href="javascript:;" class="up"></a><a href="javascript:;" class="down"></a></span><a href="javascript:;" class="a1 bjk">编辑框</a>
				<span class="error1">答案不能为空</span>
				<span class="error1">答案长度不能超过900个字符</span>
			</li>
			</c:otherwise>
			</c:choose>
		</ul>
	</div>
	</div>
	<div class="wp1-lr bdb1">
		<div class="fmt">
			<span class="tit">答题解析：</span><br>
		</div>
<!--		<textarea class="ipta"></textarea>-->
<div class="ipta">
           <textarea id="questionDto.question.explan" name='questionDto.question.explan' style="display: none;">
           	${choiceQuestionDto.questionDto.question.explan}
           </textarea>
<script type="text/javascript">
var oFCKeditor = new FCKeditor('questionDto.question.explan');
           oFCKeditor.BasePath = "${examctx}/examcore/fckeditor/";
           oFCKeditor.ToolbarSet = "Basic";
           oFCKeditor.Width="100%";
           oFCKeditor.Height="217";
           //oFCKeditor.Create();
           oFCKeditor.ReplaceTextarea() ;
       </script>
</div>
	</div>
	<div class="wp1">
	<div class="ta_c pd1">
<!--		<a href="javascript:;" class="btn1" onclick="savest()">完&nbsp;&nbsp;成</a>-->
		<a href="javascript:;" class="btn1" onclick="postME();return false;">完&nbsp;&nbsp;成</a>
		<a href="javascript:;" class="btn2 ml1" onclick="beforeDetail();return false;">预&nbsp;&nbsp;览</a>
	</div>
</div>
	<jsp:include page="hideField.jsp" flush="true"/> 
</form>
</body>
</html>