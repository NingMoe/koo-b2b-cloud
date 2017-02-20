<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>手动录入试题_选择填空题_新东方在线</title>
<meta name="keywords" content="" />
<meta name="description" content=""/>
<meta name="robots" content="all" />

<script type="text/javascript" src="${examctx}/examcore/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="${examctx}/examcore/jquery.json-2.2.min.js"></script>
<script type="text/javascript" src="${examctx}/examcore/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="${examctx}/examcore/popup.js"></script>
<script type="text/javascript" src="${examctx}/examcore/jquery.form.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/public.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/validate.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/complex.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/question.js"></script>
<!-- 
<script type="text/javascript" src="${examctx}/examcore/page.js"></script> -->
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css/main.css" />
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
});

</script>
</head>
<body>
<div class="path"><a href="<%=basePath%>pages/index.jsp">首页</a><c:if test="${not empty sessionScope.userTemplateName}"><span>></span>${sessionScope.userTemplateName}</c:if><span>></span><a href="<%=basePath%>pages/menu.jsp">试题管理</a><span>></span><em>手动录入试题_选择填空</em></div>
<form id="complexForm" action="<%=request.getContextPath()%>/maintain/complex/insertLcb" method="post">
	<c:if test="${dto.questionDto.responseType!=1}">
	<jsp:include page="/pages/menu1.jsp" flush="true" />
	</c:if>
<div class="wp1">
	<c:if test="${dto.questionDto.responseType==1}">
	<div class="fml">
		<span class="tit"><em class="rd">*</em>子题题型：</span>
		<select name='' disabled="disabled">
			<option value="101">选择填空</option>
		</select>
	</div>
	</c:if>
	<div class="fml">
	
	
		<input type="hidden" id="responseType" name="questionDto.responseType" value="${empty dto.questionDto.responseType ? 0 : dto.questionDto.responseType}"/>
		<input type="hidden" id="saveType" name="questionDto.saveType" value="${empty dto.questionDto.saveType ? 0 : dto.questionDto.saveType}"/>
		<input type="hidden" id="sta" name="questionDto.question.status" value="${empty dto.questionDto.question.status ? 0 : dto.questionDto.question.status}"/>
		<input type="hidden" id="version" name="questionDto.question.version" value="${empty dto.questionDto.question.version ? 0 : dto.questionDto.question.version}"/>
		<input type="hidden" id="cid" name="complexQuestion.id" value="${empty dto.complexQuestion ? 0 : dto.complexQuestion.id}"/>
		<input type="hidden" id="qid" name="questionDto.question.id" value="${empty dto.questionDto.question ? 0 : dto.questionDto.question.id}"/>
		<input type="hidden" id="qteId" name="questionDto.question.teId" value="${empty dto.questionDto.question ? 0 : dto.questionDto.question.teId}"/>
		<input type="hidden" id="delqid" name="delQuestionId"/>
		<input type="hidden" id="delsid" name="delSubId"/>
		<input type="hidden" id="delaid" name="delAnswerId"/>
		<input type="hidden" id="type" name="questionType" value="101"/>
		
		<!-- added @ 0322 -->
		<input type="hidden" id="questionDto_question_id" name="questionDto_question_id" value="${empty dto.questionDto.question ? 0 : dto.questionDto.question.id}"/>
	
		<span class="tit"><em class="rd">*</em><jsp:include page="/pages/question/listen/codeNamePart.jsp" flush="true" />题编码：</span>
		<c:choose>
		<c:when test="${empty(dto.questionDto.question.code)}">
			<input type="text" id='questionDto.question.code'  name='questionDto.question.code'  df="产品简写+日期+自定义编码" value="产品简写+日期+自定义编码" class="ipt"/>
		</c:when>
		<c:otherwise>
		<input type="text"  disabled="disabled"  df="产品简写+日期+自定义编码" value="${dto.questionDto.question.code}" class="ipt"/>
		<input type="text" id='questionDto.question.code' name='questionDto.question.code' style="display: none;"  df="产品简写+日期+自定义编码" value="${dto.questionDto.question.code}" class="ipt"/>
		</c:otherwise>
		</c:choose>
		<span class="error1">编码不能为空</span>
		<span class="error1">编码长度不能超过150个字符</span>
		<span class="error1">编码已经存在</span>
		<span class="error1">编码只能由数字,字母,下划线和斜线('/')组成</span>
	</div>
	<div class="fml">
		<span class="tit">答题提示：</span><input type="text" name="questionDto.question.questionTip" class="ipt maxlength" ml="255" value="${dto.questionDto.question.questionTip}"/><a style="cursor: pointer;" class="a1 bjk">编辑框</a><span class="error1">提示不能超过255个字符</span>
	</div>
	
	
	<div class="fml ontab">
		<span class="tit wh_at">听力原文：</span>
		<div class="ipta">
           <textarea id="questionDto.question.topicExt" name='questionDto.question.topicExt' style="display: none;">
           	${dto.questionDto.question.topicExt}
           </textarea>
			<script type="text/javascript">
			var oFCKeditor = new FCKeditor('questionDto.question.topicExt');
			           oFCKeditor.BasePath = "<%=request.getContextPath()%>/js/fckeditor/";
			           oFCKeditor.ToolbarSet = "Basic";
			           oFCKeditor.Width="95.5%";
			           oFCKeditor.Height="217";
			           oFCKeditor.ReplaceTextarea() ;
			       </script>
			</div>
	</div>
	
	
	
	<span class="tit wh_at"><em class="rd">*</em>子试题题干：</span>
	<div class="fmt BD_tkt" ul="#tklist1">
		<span class="error1"></span>
	
	
	
	
	<c:choose>
		<c:when test="${dto.questionDto.question.id>0}">
			<% int i=1; %>
			<c:forEach var="attach" items="${dto.questionDto.questionAttachs}">
				<div class="fmbx fmbx2 fc">
					<div class="fl fmfn">
					<div class="num"><%=i++%></div>
						<a style="cursor: pointer;" class="fmclose"></a>
					</div>
					<span class="error1"></span>
					<textarea class="ipta iptatk fck">${attach.content}</textarea>
					<span class="error1">题干不能为空</span>
					<span class="error1">题干不能超过65535个字符</span>
					<div class="ta_r">
						<a href="javascript:;" id="addTk" style="display:none" onclick="addtk(this,true);return false;">插入填空</a>
						<a style="cursor: pointer;" class="btn1 addtg">增加题干</a>
					</div>
				</div>
			</c:forEach>
			<div class="fmbx fmbx2 fc">
				<div class="fl fmfn">
					<div class="num"><%=i++%></div>
					<a style="cursor: pointer;" class="fmclose"></a>
				</div>
				<textarea class="ipta iptatk fck" name="content">${dto.complexQuestion.topic}</textarea>
				<span class="error1">题干不能为空</span>
				<span class="error1">题干不能超过65535个字符</span>
				<div class="ta_r">
					<a href="javascript:;" id="addTk" style="display:none" onclick="addtk(this,true);return false;">插入填空</a>
					<a style="cursor: pointer;" class="btn1 addtg">增加题干</a>
				</div>
			</div>	
		</c:when>
		<c:otherwise>
			<div class="fmbx fmbx2 fmone fc">
				<div class="fl fmfn">
					<div class="num">1</div>
					<a style="cursor: pointer;" class="fmclose"></a>
				</div>
				<textarea class="ipta iptatk fck" name="content"></textarea>
				<span class="error1">题干不能为空</span>
				<span class="error1">题干不能超过65535个字符</span>
				
				<div class="ta_r">
					<a href="javascript:;" id="addTk" style="display:none" onclick="addtk(this,true);return false;">插入填空</a>
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
		<span class="tit"><em class="rd">*</em>答案选项：</span>
		<a style="cursor: pointer;" class="a1" onclick="pladdfm(tplen = $('.fortkt>.fmbx1:not(.hide2)').size(),tplen)">批量添加选项</a><span class="error1" id="cberror"></span>
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
							<input type="text" name="choiceQuestionDtos[${s.index}].choiceAnswers[${as.index}].description" class="ipt wh1 required" value="${answer.description}"/><span class="fn"><a style="cursor: pointer;" class="add"></a><a style="cursor: pointer;" class="remove"></a><a style="cursor: pointer;" class="up"></a><a style="cursor: pointer;" class="down"></a></span><span class="error1"></span>
						</li>
					</c:forEach>
					</ul>
				</div>	
			</c:forEach>
		</c:otherwise>
	</c:choose>
	
	</div>
	

	<div class="fml">
		<span class="tit"><em class="rd">*</em>评分方式：</span>
		<c:choose>
			<c:when test="${empty(dto.complexQuestion.metewand)}">
				<label class="lab1" for="pffs1"><input type="radio" name="complexQuestion.metewand" value="0" checked="checked" id="pffs1">全对得分</label>
				<label class="lab1" for="pffs2"><input type="radio" name="complexQuestion.metewand" value="1"  id="pffs2">按空得分</label>
			</c:when>
			<c:otherwise>
				<label class="lab1" for="pffs1"><input type="radio" name="complexQuestion.metewand" value="0" <c:if test="${dto.complexQuestion.metewand==0}">checked="checked"</c:if> id="pffs1">全对得分</label>
				<label class="lab1" for="pffs2"><input type="radio" name="complexQuestion.metewand" value="1"  <c:if test="${dto.complexQuestion.metewand==1}">checked="checked"</c:if> id="pffs2">按空得分</label>
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
           oFCKeditor.Width="95.5%";
           oFCKeditor.Height="217";
           //oFCKeditor.Create();
           oFCKeditor.ReplaceTextarea() ;
        </script>
		</div>
	</div>
	<div class="wp1">
		<div class="ta_c pd1">
			<a style="cursor: pointer;" class="btn1" id="submitlisten">完&nbsp;&nbsp;成</a>
			<a style="cursor: pointer;" class="btn2 ml1" id="previewlisten">预&nbsp;&nbsp;览</a>
		</div>
	</div>
	
	<!-- added @ 0321 -->
	<script type="text/javascript">
    	//<![CDATA[
    	    var findIdCode = function() {
    			var str="["+$.toJSON($('input[name="questionDto_question_id"]').serializeObject())+",";
    			str+=$.toJSON($('input[name="questionDto.question.code"]').serializeObject())+"]";
    			return str;
    		};
    	    var checkBlank = function() {
    			$("input[name^='choiceQuestionDtos'][type='text']").each(function(){
    				var answer = $(this);
    				var isReturn = true;
    				if(isBlank(answer.val())){
    					isReturn = false;
    					$(this).focus();
    					alertMsg("答案不能为空!");	
    				}
    				return isReturn;
    			});
    	    };
			$("#submitlisten").click(function(){				
				//if(checkForm() && checkBlank() && checkChoiceTopic() && checkChoice() && checkCB()){
				if(checkForm() && checkChoiceTopic() && checkChoice() && checkCB()){
					$("#complexForm").attr("target","");
					$("#complexForm").attr("action","/maintain/complex/insertLcb");
					$("#complexForm").submit();
					//document.forms[0].action = "/maintain/complex/insertLcb";
					//document.forms[0].target = "";
					//document.forms[0].submit();
				}
				return false;
			});
			
			$("#previewlisten").click(function(){
				if(checkForm() && checkChoiceTopic() && checkChoice() && checkCB()) {
					var url = "/maintain/complex/preview/1";
					var height = 400;
					var width = 980;
					var iTop = (window.screen.availHeight-30-height)/2;
					var iLeft = (window.screen.availWidth-10-width)/2;
					var s=',width='+width+',height='+height+',top='+iTop+',left='+iLeft;
					window.open('','preview','toolbar=no,location=no,scrollbars=yes,status=no,menubar=no'+s);
					$("#complexForm").attr("target","preview");
					$("#complexForm").attr("action",url);
					$("#complexForm").submit();
				}
			});
			
			function handleCode(){
				if($('#questionDto_question_id').val()==0){
					if(parent.window.dialogArguments){
						var v=parent.window.dialogArguments.document.getElementById("questionDto.question.code").value+"_1";
						document.getElementById("questionDto.question.code").value=v;
					}
				}
			}
			
			handleCode();
	    //]]>
  
	</script>
</form>
</body>
</html>