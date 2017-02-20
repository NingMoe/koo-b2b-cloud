<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>手动录入试题_改错题_新东方在线</title>
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
<script type="text/javascript" src="<%=basePath%>js/jquery.form.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/examcore/question/correction.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/examcore/question/choiceWord.js"></script>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css-exam/main.css" />


<script>
var TOTAL_CONTEXT_PATH="<%=request.getContextPath()%>";
var FWB_TOOLBAR_NAME='SentenceFormat';
	function fck_insertbox_fun(html,name,fckName){
		var j=findSuitName(), v;
		if(j!=fckName){
				return;
		}
		v=$('<div/>');
		v.html(html);
		addfenju(v,$('#zstfm'),name);
	}
	function findSuitName(){
		var j=$('textarea[name="questionDto.question.topicExt"]');
		if(j.size()>0){
			return "questionDto.question.topicExt";
		}
		j=$('textarea[name^="content"]').last();
		return j.attr('name');
	}
	function clearAll(){
		$('form')[0].reset(); 
	}	
function FCKeditor_OnComplete(fck){
		$(fck.EditorWindow).blur(function(){
		var ipta = $(".fmbx2").find(".ipta").last();
		var name = ipta.attr("id");
			if(fck.Name==name){
				var body = $(fck.EditorDocument.body);
				resetFcktk(fck.EditorDocument.body);
				ipta.val(body.html());
				rsetfms();
			}
	    });
			
}	
$(function(){
	var message = "${message}";
	if(message){
		popAlert(message);
	}
	$(".xh").each(function(){
		var seq = $(this).text();
		if(!isNaN(seq)){
			$(this).text(String.fromCharCode(seq));
		}
	});
	
	$(".cbx1").each(function(){
		if(this.checked){
			this.value = 1;
		}else{
			this.value = 0;
		}
	});
	
	var ct = $("#composeType").attr("ct") || "0";
	$("#composeType").val(ct);
	<c:if test="${not empty(dto.questionDto.question.teId) and dto.questionDto.question.teId!=0}">
	if($('#qid').val()==0){
		if(parent.window.dialogArguments){
			var v=parent.window.dialogArguments.document.getElementById("questionDto.question.code").value+"_1";
			document.getElementById("code").value=v;
		}
	}
	</c:if>
});
</script>
</head>
<body>
<form id="saveForm" action="" method="post">
<div class="wp1">
	<c:if test="${dto.questionDto.responseType==1}">
	<div class="fml">
		<span class="tit"><em class="rd">*</em>子题题型：</span>
		<select name='' disabled="disabled">
			<option value="1">改错题</option>
		</select>
	</div>
	</c:if>
	<div class="fml" style="display:none;">
		<input type="hidden" id="saveType" name="questionDto.saveType" value="${empty dto.questionDto.saveType ? 0 : dto.questionDto.saveType}"/>
		<input type="hidden" id="responseType" name="questionDto.responseType" value="${empty dto.questionDto.responseType ? 0 : dto.questionDto.responseType}"/>
		<input type="hidden" id="sta" name="questionDto.question.status" value="${empty dto.questionDto.question.status ? 0 : dto.questionDto.question.status}"/>
		<input type="hidden" id="version" name="questionDto.question.version" value="${empty dto.questionDto.question.version ? 0 : dto.questionDto.question.version}"/>
		<input type="hidden" id="cid" name="complexQuestion.id" value="${empty dto.complexQuestion ? 0 : dto.complexQuestion.id}"/>
		<input type="hidden" id="qid" name="questionDto.question.id" value="${empty dto.questionDto.question ? 0 : dto.questionDto.question.id}"/>
		
		
		<input type="hidden" id="questionDto.questionBankExt.tag1" name="questionDto.questionBankExt.tag1" value="${dto.questionDto.questionBankExt.tag1}"/>
		<input type="hidden" id="questionDto.questionBankExt.tag2" name="questionDto.questionBankExt.tag2" value="${dto.questionDto.questionBankExt.tag2}"/>
		<input type="hidden" id="questionDto.questionBankExt.tag3" name="questionDto.questionBankExt.tag3" value="${dto.questionDto.questionBankExt.tag3}"/>
		<input type="hidden" id="questionDto.questionBankExt.status" name="questionDto.questionBankExt.status" value="${dto.questionDto.questionBankExt.status}"/>
		<input type="hidden" id="questionDto.questionBankExt.schoolFrom" name="questionDto.questionBankExt.schoolFrom" value="${dto.questionDto.questionBankExt.schoolFrom}"/>
		<input type="hidden" id="questionDto.question.code" name="questionDto.question.code" value="${dto.questionDto.question.code}"/>
		
		<input type="hidden" name="questionDto.question.questionTypeId" value="106"/>
	</div>
	<div class="fml" style="display:none;">
		<span class="tit">答题提示：</span><input type="text" name="questionDto.question.questionTip" class="ipt maxlength" ml="255" value="${empty dto.questionDto.question.questionTip ? '' : dto.questionDto.question.questionTip}"/><a href="javascript:;" class="a1 bjk">编辑框</a><span class="error1">提示不能超过255个字符</span>
	</div>
	<c:if test="${not empty(dto.questionDto.question.teId) and dto.questionDto.question.teId!=0}">
	<div class="fml ontab">
		<span class="tit wh_at">试题材料 / 听力原文：</span>
		<div class="ipta" style="height:220px;">
           <textarea id="questionDto.question.topicExt" name='questionDto.question.topicExt' style="display: none;">
           	${dto.questionDto.question.topicExt}
           </textarea>
			<script type="text/javascript">
			var oFCKeditor = new FCKeditor('questionDto.question.topicExt');
			           oFCKeditor.BasePath = "<%=request.getContextPath()%>/js/fckeditor/";
			           oFCKeditor.ToolbarSet = "SentenceFormat";
			           oFCKeditor.Width="100%";
			           oFCKeditor.Height="217";
			           oFCKeditor.ReplaceTextarea() ;
			       </script>
			</div>
	</div>
	<span class="tit wh_at">子试题题干：</span>
	</c:if>
	<div class="fmt BD_tkt" ul="#tklist1"><span class="error1"></span>
	<c:choose>
	<c:when test="${empty (dto.questionDto.questionAttachs) || fn:length(dto.questionDto.questionAttachs)==0}">
		<div class="fmbx fmbx2 fmone fc">
			<div class="fl fmfn">
				<div class="num">1</div>
				<a style="cursor: pointer;" class="fmclose"></a>
			</div>
			<span class="error1"></span>
			<textarea class="ipta iptatk fck" name="content">${dto.complexQuestion.topic}</textarea>
			<div class="ta_r">
				<a style="cursor: pointer;" class="btn1 addtg">增加题干</a>
			</div>
		</div>
		
	</c:when>
	<c:otherwise>
		<c:forEach var="attach" items="${dto.questionDto.questionAttachs}" varStatus="s">
			<div class="fmbx fmbx2 fc">
			<div class="fl fmfn">
				<div class="num">${s.index+1}</div>
				<a style="cursor: pointer;" class="fmclose"></a>
			</div>
			<span class="error1"></span>
			<textarea class="ipta iptatk fck">${attach.content}</textarea>
			<div class="ta_r">
				<a style="cursor: pointer;" class="btn1 addtg">增加题干</a>
			</div>
		</div>
		</c:forEach>
		<div class="fmbx fmbx2 fc">
			<div class="fl fmfn">
				<div class="num">${fn:length(dto.questionDto.questionAttachs)+1 }</div>
				<a style="cursor: pointer;" class="fmclose"></a>
			</div>
			<span class="error1"></span>
			<textarea class="ipta iptatk fck" name="content">${dto.complexQuestion.topic}</textarea>
			<div class="ta_r">
				<a style="cursor: pointer;" class="btn1 addtg">增加题干</a>
			</div>
		</div>
		
	</c:otherwise>
	</c:choose>	
	<div class="fml" style="color:red">
				*请在题干处选择句子并使用<img src="/js/fckeditor/editor/plugins/sentenceformat/placeholder.png">分句<br>
			</div>
	</div>
	</div>
	<div class=" fmone zhaocuo fc" id="zstfm">
	<c:choose>
		<c:when test="${empty (dto.correctionQuestionDtos) || fn:length(dto.correctionQuestionDtos)==0}">
		<ul class="hide2">
			<li class="fmbx1">
				<div class="mb10">
					分句<span class="num">1</span>：<span class="fjct"></span>
					<input type="hidden" class="ipt fenju" value=""/>
				</div>
				<div class="mb10">
					分句<span class="num">1</span>答案：
					<input type="text" class="ipt fenju_a" value=""/>
				</div>
				<span class="error">答案不能为空</span><input type="hidden" class="add"/>
			</li>
		</ul>
		</c:when>
	<c:otherwise>
	<ul>
		<c:forEach items="${dto.correctionQuestionDtos}" var="cqdto" varStatus="sta">
		<li class="fmbx1">
			<div class="mb10">
				分句<span class="num">${sta.count}</span>：<span class="fjct">${cqdto.correctionQuestion.clause}</span>
				<input type="hidden" name="correctionQuestionDtos[${sta.index}].correctionQuestion.clause" class="ipt fenju" value="${cqdto.correctionQuestion.clause}"/>
				<input type="hidden" name="correctionQuestionDtos[${sta.index}].questionDto.question.id" class="fenju_id" value="${cqdto.questionDto.question.id}"/>
				<input type="hidden" name="correctionQuestionDtos[${sta.index}].questionDto.question.questionTypeId" class="fenju_t_id" value="${cqdto.questionDto.question.questionTypeId}"/>
			</div>
			<div class="mb10">
				分句<span class="num">${sta.count}</span>答案：
				<input type="text" name="correctionQuestionDtos[${sta.index}].correctionQuestion.clauseAnswer" class="ipt fenju_a" value="${cqdto.correctionQuestion.clauseAnswer}"/>
			</div>
			<span class="error">答案不能为空</span><input type="hidden" class="add"/>
		</li>
		</c:forEach>
	</ul>
	</c:otherwise>
	</c:choose>
	<span class="error">请至少选择一个分句</span>
	</div>

	<div class="wp1-lr bdb1">
		<div class="fmt">
			<span class="tit">答题解析：</span><br>
		</div>
		<div class="ipta">
		<textarea name="questionDto.question.explan" style="display:none" >${dto.questionDto.question.explan}</textarea>
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
		<a style="cursor: pointer;" class="btn1" onclick="savest()">完&nbsp;&nbsp;成</a>
		<a style="cursor: pointer;" class="btn2 ml1" onclick="preview()">预&nbsp;&nbsp;览</a>
	</div>
</div>
</form>
</body>
</html>