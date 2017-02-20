<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%><jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>手动录入试题_填空型完形填空_新东方在线</title>
<meta name="keywords" content="" />
<meta name="description" content=""/>
<meta name="robots" content="all" />
<script type="text/javascript" src="${examctx}/examcore/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/jquery.json-2.2.min.js"></script>
<script type="text/javascript" src="${examctx}/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="${examctx}/examcore/popup.js"></script>
<script type="text/javascript" src="${examctx}/jquery.form.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/public.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/validate.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/complex.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/question.js"></script>
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css-exam/main.css" />
<script>
var TOTAL_CONTEXT_PATH="<%=request.getContextPath()%>";
function fck_InsertBlank_fun(html,bd,name,span){
	var ipta = $(".fmbx2").find(".ipta").last();
	if(ipta.attr("id")==name){
		addtk3(false,bd);
	}else{
		$(span).remove();
		alert("只能在题干中插入填空！");
	}
}	
function fck_DeleteBlank_fun(html,bd,index,name){
	var ipta = $(".fmbx2").find(".ipta").last();
	if(ipta.attr("id")==name){
		deletetk(false,bd,index);
	}	
}	
function FCKeditor_OnComplete(fck){
		$(fck.EditorWindow).blur(function(){
			var ipta = $(".fmbx2").find(".ipta").last();
			var name = ipta.attr("id");
			if(fck.Name==name){
				var body = $(fck.EditorDocument.body);
				resetFcktk(fck.EditorDocument.body);
				$(".ipta").val(body.html());
				var size = $(".tkbox",body).size();
				if(size == 0){
					$("#tklist1 li").each(function(i){
						setDeleteId(this);	
					});
					$("#tklist1").attr("index",0);
					$("#tklist1").empty();
					return false;
				}
				$("#tklist1 li").each(function(i){
					var tkbox = body.find(".tkbox:contains('("+(i+1)+")')");
					if(tkbox.size() == 0){
						var ul = $("#tklist1"),
						index = (parseInt(ul.attr("index")) || 1)-1;
						ul.attr("index",index);
						setDeleteId(this);	
						$(this).remove();
						ordtk($(".ipta"),ul,false);
						body.html($(".ipta").val());
						resettk();
					}
				});
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
	$(".cbx1").click(function(){
		if(this.checked){
			this.value = 1;
		}else{
			this.value = 0;
		}
	});
	$("#bhxx1").click(function(){
		$("#complexForm").attr("target","_self");
		$("#complexForm").attr("action","<%=basePath%>question/base/complex/toInsert");
		$("#complexForm").submit();
	});
	var ct = $("#composeType").attr("ct") || "0";
	$("#composeType").val(ct);
});

</script>
</head>
<body>
<form id="complexForm" action="<%=request.getContextPath()%>/question/base/complex/insert" method="post">
<div class="wp1">
	<div class="fml" style="display:none;">
		<input type="hidden" id="responseType" name="questionDto.responseType" value="${empty dto.questionDto.responseType ? 0 : dto.questionDto.responseType}"/>
		<input type="hidden" id="saveType" name="questionDto.saveType" value="${empty dto.questionDto.saveType ? 0 : dto.questionDto.saveType}"/>
		<input type="hidden" id="sta" name="questionDto.question.status" value="${empty dto.questionDto.question.status ? 0 : dto.questionDto.question.status}"/>
		<input type="hidden" id="version" name="questionDto.question.version" value="${empty dto.questionDto.question.version ? 0 : dto.questionDto.question.version}"/>
		<input type="hidden" id="cid" name="complexQuestion.id" value="${empty dto.complexQuestion ? 0 : dto.complexQuestion.id}"/>
		<input type="hidden" id="qid" name="questionDto.question.id" value="${empty dto.questionDto.question ? 0 : dto.questionDto.question.id}"/>
		<input type="hidden" id="delqid" name="delQuestionId"/>
		<input type="hidden" id="delsid" name="delSubId"/>
		<input type="hidden" id="delaid" name="delAnswerId"/>
		<input type="hidden" name="questionDto.question.questionTypeId" value="8"/>
		<span class="tit"><em class="rd">*</em>试题编码：</span><input type="text" id="code" name="questionDto.question.code" df="产品简写+日期+自定义编码" class="ipt maxlength required" value="${dto.questionDto.question.code}" ml="140"/><span class="error1"></span>
	</div>
	<div class="fml" style="display:none;">
		<span class="tit">答题提示：</span><input type="text" name="questionDto.question.questionTip" class="ipt maxlength" ml="255" value="${dto.questionDto.question.questionTip}"/><a style="cursor: pointer;" class="a1 bjk">编辑框</a><span class="error1">提示不能超过255个字符</span>
	</div>
	<!-- 
	<div class="fml ontab" forid="ontab1">
		<span class="tit wh_at">是否包含选项：</span>
		<label class="lab1" for="bhxx1"><input type="radio" class="ontabfn" name="questionType" id="bhxx1" value="15">选择型完形</label>
		<label class="lab1" for="bhxx2"><input type="radio" class="ontabfn" checked="checked" name="questionType" id="bhxx2" value="8">填空型完形</label>
	</div>
	 -->
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
				<a href="javascript:;" id="addTk" style="display:none" onclick="addtk(this,false);return false">插入填空</a>
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
				<a href="javascript:;" id="addTk" style="display:none" onclick="addtk(this,false);return false">插入填空</a>
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
				<a href="javascript:;" id="addTk" style="display:none" onclick="addtk(this,false);return false">插入填空</a>
				<a style="cursor: pointer;" class="btn1 addtg">增加题干</a>
			</div>
		</div>
	</c:otherwise>
	</c:choose>	
	</div>
	</div>
	<div class="wp1-lr bdb1"></div>
	<div class="wp1">
	<div class="ontabtxt" for="ontab1">
	<div class="fml">
		<span class="tit"><em class="rd">*</em>参考答案：</span><span class="error1"></span>
	</div>
	<c:choose>
	<c:when test="${empty (dto.essayQuestionDTOs)}">
		<ul class="tklist fc mb10" id="tklist1" index="${fn:length(dto.essayQuestionDTOs)}">
		</ul>
	</c:when>
	<c:otherwise>
	<ul class="tklist fc mb10" id="tklist1" index="${fn:length(dto.essayQuestionDTOs)}">
	<c:forEach var="essay" items="${dto.essayQuestionDTOs}" varStatus="s">
		<li index="${s.index+1}"><span class="tktit">(${s.index+1})</span>
		<input type="hidden" name="essayQuestionDTOs[${s.index}].essayQuestion.id" class="hidcid" value="${empty essay.essayQuestion ? 0 : essay.essayQuestion.id}">
		<input type="hidden" name="essayQuestionDTOs[${s.index}].essayQuestion.questtionId" class="hidqid" value="${empty essay.essayQuestion ? 0 : essay.essayQuestion.questtionId}">
		<input type="hidden" name="essayQuestionDTOs[${s.index}].fillblankAnswers[0].id" class="hid" value="${empty essay.fillblankAnswers[0] ? 0 : essay.fillblankAnswers[0].id}">
		<input type="text" name="essayQuestionDTOs[${s.index}].fillblankAnswers[0].answer" value="${essay.fillblankAnswers[0].answer }" class="ipt2">
		</li>
	</c:forEach>
	</ul>
	</c:otherwise>
	</c:choose>
	<div class="fml">
		<span class="tit"><em class="rd">*</em>批改方式：</span>
		<c:choose>
			<c:when test="${dto.questionDto.question==null}">
				<label class="lab1" for="pgfs1"><input type="radio" name="questionDto.question.issubjectived" value="1" checked="checked" id="pgfs1">系统批改（客观题）</label>
				<label class="lab1" for="pgfs2"><input type="radio" name="questionDto.question.issubjectived" value="0"  id="pgfs2">人工/自评（主观题）</label>
			</c:when>
			<c:otherwise>
				<label class="lab1" for="pgfs1"><input type="radio" name="questionDto.question.issubjectived" value="1" <c:if test="${dto.questionDto.question.issubjectived==1}">checked="checked"</c:if> id="pgfs1">系统批改（客观题）</label>
				<label class="lab1" for="pgfs2"><input type="radio" name="questionDto.question.issubjectived" value="0"  <c:if test="${dto.questionDto.question.issubjectived==0}">checked="checked"</c:if> id="pgfs2">人工/自评（主观题）</label>
			</c:otherwise>
		</c:choose>
	</div>
	</div>
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
		<a style="cursor: pointer;" class="btn1" id="submitTk">完&nbsp;&nbsp;成</a>
		<a style="cursor: pointer;" class="btn2 ml1" id="previewTk">预&nbsp;&nbsp;览</a>
	</div>
</div>
	<input type="hidden" id="questionDto.questionBankExt.tag1" name="questionDto.questionBankExt.tag1" value="${dto.questionDto.questionBankExt.tag1 }"/>
	<input type="hidden" id="questionDto.questionBankExt.tag2" name="questionDto.questionBankExt.tag2" value="${dto.questionDto.questionBankExt.tag2 }"/>
	<input type="hidden" id="questionDto.questionBankExt.tag3" name="questionDto.questionBankExt.tag3" value="${dto.questionDto.questionBankExt.tag3 }"/>
	<input type="hidden" id="questionDto.questionBankExt.status" name="questionDto.questionBankExt.status" value="${dto.questionDto.questionBankExt.status }"/>
	<input type="hidden" id="questionDto.questionBankExt.schoolFrom" name="questionDto.questionBankExt.schoolFrom" value="${dto.questionDto.questionBankExt.schoolFrom }"/>

</form>
</body>
</html>