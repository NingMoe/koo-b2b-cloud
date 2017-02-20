<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ page language="java" import="com.koolearn.exam.question.entity.Question" %>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>手动录入试题_排序题_新东方在线</title>
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
<script type="text/javascript" src="<%=basePath%>/js/examcore/question/question-ext.js"></script>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css-exam/main.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css-exam/main1026.css" />

<script type="text/javascript">
var TOTAL_CONTEXT_PATH="<%=request.getContextPath()%>";
function clearAll(){
	$('form')[0].reset(); 
}

function findIdCode(){ 
	var str="["+$.toJSON($('input[name="questionDto_question_id"]').serializeObject())+",";
	str+=$.toJSON($('input[name="questionDto.question.code"]').serializeObject())+"]";
	return str;
}

	//长度大于某值
function lengthLarger(value, n) {
	var s = trim(value);
	return s.length>=n;
} 
function save(){
	fillHeadValue();
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
			if(!isReturn){
				setRadioValue();
				setSortOrderby();
				setAnswerInputValue();
				document.forms[0].submit();
			}	
	}
}
	
function setSortOrderby(){
	var arr=[];
	$('#sortOrder ul li').each(function(i){
		v=$(this).find('.xh').html();
		var a=document.createElement('input');
		a.type='hidden';
		a.name='orderby_'+String.fromCharCode(97+i);
		a.value=v;
		arr[arr.length]=a;
	});
	$('form').append(arr);
}


function setAnswerInputValue(){
	$('#sortOrder input').each(function(i){
		var v=$(this).val();
		var n=$(this).attr('name');
		$(this).attr('id',n);
		$(this)[0].value=v;
		$(this)[0].setAttribute("value",v);
	});
}
/**
*预览排序
*/
function detail(){
	var context_path=TOTAL_CONTEXT_PATH||"";
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
			if(!isReturn){
				setRadioValue();
				setSortOrderby();
				setAnswerInputValue();
				var composeType = $("select[name='choiceQuestion.composeType']").val();
				if(composeType==1){
				   windowOpen(context_path+'/question/base/choice/detail/0?questionType=<%=Question.QUESTION_TYPE_SORT%>'+"&1="+Math.random(),780,400);
				}else if(composeType==2){
					windowOpen(context_path+'/question/base/choice/detail2/0?questionType=<%=Question.QUESTION_TYPE_SORT%>'+"&1="+Math.random(),780,400);
				}
				//popUrl('预览',context_path+'/maintain/judge/detail/0'+"?questionType=<%=Question.QUESTION_TYPE_SORT%>&1="+Math.random());
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
<c:if test="${not empty(choiceQuestionDto.questionDto.question.teId)}">
		fectchParentCode();
</c:if>
});	
</script>
</head>
<body>
<form action="/question/base/choice/sortSave" name="form1" method="post">
<jsp:include page="hiddensort.jsp" flush="true"/> 
<div class="wp1">
	<c:if test="${choiceQuestionDto.questionDto.responseType==1}">
	<div class="fml">
		<span class="tit"><em class="rd">*</em>子题题型：</span>
		<select name='' disabled="disabled">
			<option value="13">拖拽排序</option>
		</select>
	</div>
	</c:if>
	<div class="fml" style="display:none;">
		<span class="tit"><em class="rd">*</em><c:choose><c:when test="${choiceQuestionDto.questionDto.responseType==1}">子</c:when><c:otherwise>试</c:otherwise></c:choose>题编码：</span>
		<c:choose>
		<c:when test="${empty(choiceQuestionDto.questionDto.question.code)}">
			<input type="text" id="questionDto.question.code" name='questionDto.question.code' value=""/>
		</c:when>
		<c:otherwise>
			<input type="text" id="questionDto.question.code" name='questionDto.question.code'  value="${choiceQuestionDto.questionDto.question.code}" />
		</c:otherwise>
		</c:choose>
		<span class="error1">编码不能为空</span>
		<span class="error1">编码长度不能超过150个字符</span>
		<span class="error1">编码已经存在</span>
		<span class="error1">编码只能由数字,字母,下划线和斜线('/')组成</span>
	</div>
	<div class="fml" style="display:none;">
		<span class="tit">答题提示：</span><input type="text" name='questionDto.question.questionTip' value='${choiceQuestionDto.questionDto.question.questionTip}' class="ipt"/><a href="javascript:;" class="a1 bjk">编辑框</a>
		<span class="error1">提示不能超过255字符</span>
	</div>
	<div class="fml"  style="display:none;">
		<span class="tit">展现形式：</span>
		<select name="choiceQuestion.composeType" v="${choiceQuestionDto.choiceQuestion.composeType}">
			<option value="1">拖拽1</option>
			<option value="2">拖拽2</option>
		</select>
		<span class="tip">托福模考题目请选择拖拽1</span>
	</div>
	<c:if test="${choiceQuestionDto.questionDto.responseType==1}">
<%--	<c:if test="${not empty(choiceQuestionDto.questionDto.question.teId)}">--%>
		<div class="fml ontab">
			<span class="tit wh_at">试题材料：</span>
			<div class="ipta" style="height:220px;">
	           <textarea id="questionDto.question.topicExt" name='questionDto.question.topicExt' style="display: none;">
	           	${choiceQuestionDto.questionDto.question.topicExt}
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
				<textarea class="ipta fck">${name.content}</textarea>
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
				<textarea class="ipta fck">${choiceQuestionDto.choiceQuestion.topic}</textarea>
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
		<a href="javascript:;" class="a1" onclick="pladdfm(1,1)">批量添加排序项目</a>
	</div>
	<div class="fmbx fmbx1 fmone fc pxfm" id="sortOrder" ic="num" px="no">
		<div class="fl fmfn">
			<div class="num">1</div>
			<a href="javascript:;" class="fmclose"></a>
		</div>
		<ul>
			<li class="hdtit"><span class="tit">正确顺序</span><span class="tit ml3">默认顺序</span></li>
			<c:choose>
			<c:when test="${fn:length(choiceQuestionDto.choiceAnswers)>0}">
				<% int k=0; %>
				<c:forEach var="name" items="${choiceQuestionDto.choiceAnswers}">
				<li>
					<span class="xh">${name.orderby+1 }</span><input type="text" name="fmbx1_<%=(k++)%>" class="ipt wh1" value="${name.description}"/><span class="fn"><a href="javascript:;" class="add"></a><a href="javascript:;" class="remove"></a><a href="javascript:;" class="up"></a><a href="javascript:;" class="down"></a></span><a href="javascript:;" class="a1 bjk">编辑框</a>
					<span class="error1">答案不能为空</span>
					<span class="error1">答案长度不能超过900个字符</span>
				</li>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<li>
					<span class="xh">1</span><input type="text" name="fmbx1_1" class="ipt wh1"/><span class="fn"><a href="javascript:;" class="add"></a><a href="javascript:;" class="remove"></a><a href="javascript:;" class="up"></a><a href="javascript:;" class="down"></a></span><a href="javascript:;" class="a1 bjk">编辑框</a>
					<span class="error1">答案不能为空</span>
					<span class="error1">答案长度不能超过900个字符</span>
					
				</li>
				<li>
					<span class="xh">2</span><input type="text" name="fmbx1_2" class="ipt wh1"/><span class="fn"><a href="javascript:;" class="add"></a><a href="javascript:;" class="remove"></a><a href="javascript:;" class="up"></a><a href="javascript:;" class="down"></a></span><a href="javascript:;" class="a1 bjk">编辑框</a>
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
           oFCKeditor.BasePath = "<%=request.getContextPath()%>/js/fckeditor/";
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
		<a href="#" class="btn1" onclick="javascript:save();return false;">完&nbsp;&nbsp;成</a>
		<a href="#" class="btn2 ml1" onclick="javascript:detail();return false;">预&nbsp;&nbsp;览</a>
	</div>
</div>
</form>
</body>
</html>