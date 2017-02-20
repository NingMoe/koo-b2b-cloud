<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>手动录入试题_完形填空题_新东方在线</title>
<meta name="keywords" content="" />
<meta name="description" content=""/>
<meta name="robots" content="all" />
<script type="text/javascript" src="${examctx}/examcore/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/jquery.json-2.2.min.js"></script>
<script type="text/javascript" src="${examctx}/examcore/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="${examctx}/examcore/popup.js"></script>
<script type="text/javascript" src="${examctx}/examcore/jquery.form.js"></script>
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
		addtk3(true,bd);
	}else{
		$(span).remove();
		alert("只能在题干中插入填空！");
	}
}	
function fck_DeleteBlank_fun(html,bd,index,name){
	var ipta = $(".fmbx2").find(".ipta").last();
	if(ipta.attr("id")==name){
		$(".tkbox",$(bd)).eq(index).remove();
		if($(".fmbx1").size()==1){
			$(".fmbx1").eq(0).find("input").val("");
			$(".fmbx1").eq(0).addClass("hide2").removeAttr("index");
			return;
		}
		var fmclose = $(".fmbx1 .fmclose").eq(index);
		removeChoice(fmclose,bd);
	}	
}	
function FCKeditor_OnComplete(fck){
		$(fck.EditorWindow).blur(function(){
		var ipta = $(".fmbx2").find(".ipta").last();
		var name = ipta.attr("id");
			if(fck.Name==name){
				var body = $(fck.EditorDocument.body);
				resetFcktk(fck.EditorDocument.body);
				ipta.val(body.html());
				var size = $(".tkbox",body).size();
				if(size == 0){
					$(".fmbx1 .num").each(function(){
						setDeleteId(this);
					});
					$(".fmbx1:gt(0)").remove();
					$(".fmbx1:eq(0)").addClass("hide2");
					clearfm($(".fmbx1:eq(0)"));
					return false;
				}
				$(".fmbx1").each(function(i){
					var tkbox = body.find(".tkbox:contains('("+(i+1)+")')");
					if(tkbox.size() == 0){
						var fmclose = $(this).find(".fmclose");
						removeChoice(fmclose,fck.EditorDocument.body);
					}
				});
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
	
	$("#bhxx2").click(function(){
		$("#complexForm").attr("target","_self");
		$("#complexForm").attr("action","<%=basePath%>question/base/complex/clozeInsert");
		$("#complexForm").submit();
	});
	var ct = $("#composeType").attr("ct") || "0";
	$("#composeType").val(ct);
});
function showSubExp(o){
	var obj = $(o), win = fckEditMsg(obj.next(),"<%=request.getContextPath()%>");
	win.okCallBack = function(val){
		obj.nextAll(".subExpShow").html(val);
		obj.nextAll(".subExp").val(val);
	};
}
</script>
</head>
<body>
<form id="complexForm" action="<%=request.getContextPath()%>/question/base/complex/insert" method="post">
<div class="wp1">
	<div class="fml" style="display:none;">
		<input type="hidden" id="saveType" name="questionDto.saveType" value="${empty dto.questionDto.saveType ? 0 : dto.questionDto.saveType}"/>
		<input type="hidden" id="responseType" name="questionDto.responseType" value="${empty dto.questionDto.responseType ? 0 : dto.questionDto.responseType}"/>
		<input type="hidden" id="sta" name="questionDto.question.status" value="${empty dto.questionDto.question.status ? 0 : dto.questionDto.question.status}"/>
		<input type="hidden" id="version" name="questionDto.question.version" value="${empty dto.questionDto.question.version ? 0 : dto.questionDto.question.version}"/>
		<input type="hidden" id="cid" name="complexQuestion.id" value="${empty dto.complexQuestion ? 0 : dto.complexQuestion.id}"/>
		<input type="hidden" id="qid" name="questionDto.question.id" value="${empty dto.questionDto.question ? 0 : dto.questionDto.question.id}"/>
		<input type="hidden" id="delqid" name="delQuestionId"/>
		<input type="hidden" id="delsid" name="delSubId"/>
		<input type="hidden" id="delaid" name="delAnswerId"/>
		<input type="hidden" name="questionDto.question.questionTypeId" value="15"/>
		<span class="tit"><em class="rd">*</em>试题编码：</span><input type="text" id="code" name="questionDto.question.code" df="产品简写+日期+自定义编码" class="ipt maxlength required" value="${dto.questionDto.question.code}" ml="140"/><span class="error1"></span>
	</div>
	<div class="fml" style="display:none;">
		<span class="tit">答题提示：</span><input type="text" name="questionDto.question.questionTip" class="ipt maxlength" ml="255" value="${dto.questionDto.question.questionTip}"/><a style="cursor: pointer;" class="a1 bjk">编辑框</a><span class="error1">提示不能超过255个字符</span>
	</div>
	<!-- 
	<div class="fml ontab" forid="ontab1">
		<span class="tit wh_at">是否包含选项：</span>
		<label class="lab1" for="bhxx1"><input type="radio" class="ontabfn" checked="checked" name="questionType" id="bhxx1" value="15">选择型完形</label>
		<label class="lab1" for="bhxx2"><input type="radio" class="ontabfn" name="questionType" id="bhxx2" value="8">填空型完形</label>
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
				<a href="javascript:;" id="addTk" style="display:none" onclick="addtk(this,true);return false;">插入填空</a>
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
				<a href="javascript:;" style="display:none"  onclick="addtk(this,true);return false;">插入填空</a>
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
				<a href="javascript:;" id="addTk" style="display:none" onclick="addtk(this,true);return false">插入填空</a>
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
		<select name="choiceQuestionDtos[0].choiceQuestion.composeType" ct="${dto.choiceQuestionDtos[0].choiceQuestion.composeType}" id="composeType">
			<option value="1" selected="selected">竖向排列</option>
			<option value="2">横向2列</option>
			<option value="3">横向3列</option>
			<option value="4">横向4列</option>
			<option value="5">横向5列</option>
			<option value="6">横向6列</option>
			<option value="7">横向7列</option>
		</select>
		<a style="cursor: pointer;" class="a1" onclick="pladdfm(tplen = $('.fortkt>.fmbx1:not(.hide2)').size(),tplen)">批量编辑描述</a><span class="error1" id="cberror"></span>
	</div>
	<div class="fmt fortkt">
	<c:choose>
		<c:when test="${empty (dto.choiceQuestionDtos)}">
			<div class="fmbx fmbx1 fmone fc hide2">
				<div class="fl fmfn">
					<div class="num">1</div>
					<a style="cursor: pointer;" class="fmclose"></a>
				</div>
				<ul>
					<li>
						<input type="radio" name="choiceQuestionDtos[0].choiceAnswers[0].isright" class="fl cbx1" value="1"/>&nbsp;<span class="xh">A</span><input type="text" name="choiceQuestionDtos[0].choiceAnswers[0].description" class="ipt wh1 required"/><span class="fn"><a style="cursor: pointer;" class="add"></a><a style="cursor: pointer;" class="remove"></a><a style="cursor: pointer;" class="up"></a><a style="cursor: pointer;" class="down"></a></span><a style="cursor: pointer;" class="a1 bjk">编辑框</a><span class="error1"></span>
					</li>
					<li>
						<input type="radio" name="choiceQuestionDtos[0].choiceAnswers[1].isright" class="fl cbx1" value="1"/>&nbsp;<span class="xh">B</span><input type="text" name="choiceQuestionDtos[0].choiceAnswers[1].description" class="ipt wh1 required"/><span class="fn"><a style="cursor: pointer;" class="add"></a><a style="cursor: pointer;" class="remove"></a><a style="cursor: pointer;" class="up"></a><a style="cursor: pointer;" class="down"></a></span><a style="cursor: pointer;" class="a1 bjk">编辑框</a><span class="error1"></span>
					</li>
				</ul>
				<a style="cursor: pointer;" class="a1" onclick="showSubExp(this);">添加答案解析</a>
				<input type="hidden" class="subExp" name="choiceQuestionDtos[0].questionDto.question.explan">
				<div class="subExpShow"></div>
			</div>	
		</c:when>
		<c:otherwise>
			<c:set var="status" value="${dto.questionDto.question.status}"/>
			<c:forEach var="choice" items="${dto.choiceQuestionDtos}" varStatus="s">
				<div class="fmbx fmbx1 fc" index="${s.index+1}">
					<div class="fl fmfn ">
						<div class="num">${s.index+1}</div>
						<input type="hidden" class="hidcid" name="choiceQuestionDtos[${s.index}].choiceQuestion.id" value="${(empty status || status==1) ? 0 : choice.choiceQuestion.id}"/>
						<input type="hidden" class="hidqid" name="choiceQuestionDtos[${s.index}].choiceQuestion.questionId" value="${(empty status || status==1) ? 0 : choice.choiceQuestion.questionId}"/>
						<a style="cursor: pointer;" class="fmclose"></a>
					</div>
					<ul>
					<c:forEach var="answer" items="${choice.choiceAnswers}" varStatus="as">
						<li>
							<input type="hidden" class="hid" name="choiceQuestionDtos[${s.index}].choiceAnswers[${as.index}].id" value="${(empty status || status==1) ? 0 : answer.id}"/>
							<input type="radio" name="choiceQuestionDtos[${s.index}].choiceAnswers[${as.index}].isright" class="fl cbx1" value="${answer.isright}" <c:if test="${answer.isright==1}">checked="checked"</c:if> />
							&nbsp;<span class="xh">${65+as.index}</span>
							<input type="text" name="choiceQuestionDtos[${s.index}].choiceAnswers[${as.index}].description" class="ipt wh1 required" value="${answer.description}"/><span class="fn"><a style="cursor: pointer;" class="add"></a><a style="cursor: pointer;" class="remove"></a><a style="cursor: pointer;" class="up"></a><a style="cursor: pointer;" class="down"></a></span><a style="cursor: pointer;" class="a1 bjk">编辑框</a><span class="error1"></span>
						</li>
					</c:forEach>
					</ul>
					<a style="cursor: pointer;" class="a1" onclick="showSubExp(this);">添加答案解析</a>
					<input type="hidden" class="subExp" name="choiceQuestionDtos[${s.index}].questionDto.question.explan" value="${choice.questionDto.question.explan }">
					<div class="subExpShow">${choice.questionDto.question.explan }</div>
				</div>	
			</c:forEach>
		</c:otherwise>
	</c:choose>
	
	</div>
	</div>
	<div class="ontabtxt" style="display:none" for="ontab1">
		<div class="fml">
			<span class="tit"><em class="rd">*</em>参考答案：</span>
		</div>
		<ul class="tklist fc mb10" id="tklist1" >
		</ul>
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
		<a style="cursor: pointer;" class="btn1" id="submit">完&nbsp;&nbsp;成</a>
		<a style="cursor: pointer;" class="btn2 ml1" id="preview">预&nbsp;&nbsp;览</a>
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