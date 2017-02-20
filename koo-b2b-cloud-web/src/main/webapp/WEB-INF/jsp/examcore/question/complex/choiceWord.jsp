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
<title>手动录入试题_选词填空题_新东方在线</title>
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
<script type="text/javascript" src="${examctx}/examcore/question/choiceWord.js"></script>
<link type="text/css" rel="stylesheet" href="${examctx}/examcorecss/kpt.css" />
<link type="text/css" rel="stylesheet" href="${examctx}/examcorecss/main.css" />
<script>
var TOTAL_CONTEXT_PATH="<%=request.getContextPath()%>";
var i_add = 0;
function fck_InsertBlank_fun(html,bd,name,span){
	var ipta = $(".fmbx2").find(".ipta").last();
	if(ipta.attr("id")==name){
		var size = $(bd).find(".tkbox").size();
		if(size == 1){
			var trSize = $(".dx_dt > tbody > tr").size();
			if(trSize > 2){
				for(var i=trSize;i>0;i--){
					remove_tr(i);
				}
			}
			$(".dx_dt").show();
			resetFcktk(bd);
		}else{
			add_tr(bd);
		}
	}else{
		$(span).remove();
		alert("只能在题干中插入填空！");
	}
}	
function fck_DeleteBlank_fun(html,bd,index,name){
	var ipta = $(".fmbx2").find(".ipta").last();
	if(ipta.attr("id")==name){
		var size = $(".dx_dt > tbody > tr").size();
		if(size == 2){
			$(".dx_dt").hide();
		}else{
			remove_tr(index);
		}
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
				/**var size = $(".tkbox",body).size();
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
				});*/
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
	$("#submitButton").click(function(){
		if(validate() &&checkChoiceTopic() && checkChoice() && checkCB()){
			$("#complexForm").attr("action","/maintain/complex/insert");
			$("#complexForm").attr("target","");
			$("#complexForm").submit();
		}
		return false;
	});
	$("#previewButton").click(function(){
		$(".error1").empty().hide();
		$("#cberror").empty().hide();
		if(validate()&&checkChoiceTopic() && checkChoice() && checkCB()){
			var preview = 'preview';
			window.open('',preview,'toolbar=no,location=no,scrollbars=yes,status=no,menubar=no,width=600,height=600');
			$("#complexForm").attr("target",preview);
			$("#complexForm").attr("action","/maintain/complex/preview/1");
			$("#complexForm").submit();
		}
	});
	$(".font").each(function(i){
		$(this).html(String.fromCharCode(65+i));
	});
	$(".back").each(function(i){
		if(i>0)
			$(this).html(String.fromCharCode(64+i));
	});
	<c:if test="${not empty(dto.questionDto.question.teId) and dto.questionDto.question.teId!=0}">
	if($('#qid').val()==0){
		if(parent.window.dialogArguments){
			var v=parent.window.dialogArguments.document.getElementById("questionDto.question.code").value+"_1";
			document.getElementById("code").value=v;
		}
	}
	</c:if>
});
function add_tr(bd){
	i_add++;
	var otr = $("<tr></tr>"),atr = $(".dx_dt tr"),atd = atr.eq(0).find("td"),sele = null;
	for(var i=0;i<atd.length;i++){
		if(i==0){
		   	sele='<td>'+atr.length+'</td>';
		}else{
			sele+='<td><input type="radio" /></td>';
		}
	}
	otr.html(sele);
	$(".dx_dt").append(otr);
	resetFcktk(bd);
	sort_nameAndRadio();
}
function remove_tr(num){
	$(".dx_dt tr").eq(num+1).remove();
	var atr = $(".dx_dt tr");
    atr.each(function(){
             if($(this).index()!=0){
                      $(this).find("td").eq(0).html($(this).index());  
             }
    });
    sort_nameAndRadio();
}
</script>
</head>
<body>
<div class="path"><a href="<%=basePath%>pages/index.jsp">首页</a><c:if test="${not empty sessionScope.userTemplateName}"><span>></span>${sessionScope.userTemplateName}</c:if><span>></span><a href="<%=basePath%>pages/menu.jsp">试题管理</a><span>></span><em>手动录入试题_选词填空</em></div>
<form id="complexForm" action="<%=request.getContextPath()%>/maintain/complex/insert" method="post">
	<c:if test="${dto.questionDto.responseType!=1}">
	<jsp:include page="/pages/menu1.jsp" flush="true" />
	</c:if>
<div class="wp1">
	<c:if test="${dto.questionDto.responseType==1}">
	<div class="fml">
		<span class="tit"><em class="rd">*</em>子题题型：</span>
		<select name='' disabled="disabled">
			<option value="1">选词填空</option>
		</select>
	</div>
	</c:if>
	<div class="fml">
		<input type="hidden" id="saveType" name="questionDto.saveType" value="${empty dto.questionDto.saveType ? 0 : dto.questionDto.saveType}"/>
		<input type="hidden" id="responseType" name="questionDto.responseType" value="${empty dto.questionDto.responseType ? 0 : dto.questionDto.responseType}"/>
		<input type="hidden" id="sta" name="questionDto.question.status" value="${empty dto.questionDto.question.status ? 0 : dto.questionDto.question.status}"/>
		<input type="hidden" id="version" name="questionDto.question.version" value="${empty dto.questionDto.question.version ? 0 : dto.questionDto.question.version}"/>
		<input type="hidden" id="cid" name="complexQuestion.id" value="${empty dto.complexQuestion ? 0 : dto.complexQuestion.id}"/>
		<input type="hidden" id="qid" name="questionDto.question.id" value="${empty dto.questionDto.question ? 0 : dto.questionDto.question.id}"/>
		<input type="hidden" id="teId" name="questionDto.question.teId" value="${empty dto.questionDto.question ? 0 : dto.questionDto.question.teId}"/>
		<input type="hidden" id="delqid" name="delQuestionId"/>
		<input type="hidden" id="delsid" name="delSubId"/>
		<input type="hidden" id="delaid" name="delAnswerId"/>
		<input type="hidden" id="type" name="questionType" value="${empty questionType ? 37 : questionType}"/>
		<span class="tit"><em class="rd">*</em>试题编码：</span><input type="text" id="code" name="questionDto.question.code" df="产品简写+日期+自定义编码" class="ipt maxlength required" value="${dto.questionDto.question.code}" ml="140"/><span class="error1"></span>
	</div>
	<div class="fml">
		<span class="tit">答题提示：</span><input type="text" name="questionDto.question.questionTip" class="ipt maxlength" ml="255" value="${dto.questionDto.question.questionTip}"/><a style="cursor: pointer;" class="a1 bjk">编辑框</a><span class="error1">提示不能超过255个字符</span>
	</div>
	
	<c:if test="${not empty(dto.questionDto.question.teId) and dto.questionDto.question.teId!=0}">
	<div class="fml ontab">
		<span class="tit wh_at">试题材料 / 听力原文：</span>
		<div class="ipta">
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

	   <div class="star">
       	   <span class="ml"></span><label><img src="<%=basePath%>/images/star.png" />备选单词：</label><a href="javascript:;" class="blue line" id="piliang">批量添加备选单词</a>
       </div>
       <ul class="data_list ml cl">
       		<c:choose>
			<c:when test="${empty (dto.choiceAnswers)}">
				<li> 
	            	<span class="ml font fl">A</span>
	                <input type="text"  name="choiceAnswers[0].description" class="text ml fl" />
	                <span class="tbi ml fl"><a href="javascript:;" class="dt_add"></a><a href="javascript:;" class="dt_remove"></a><a href="javascript:;" class="dt_up"></a><a href="javascript:;" class="dt_down"></a></span>
	            	<!-- <span class="bianji ml fl">编辑框</span> -->
	            	<span class="error1"></span>
	            </li>
	            <li> 
	            	<span class="ml font fl">B</span>
	                <input type="text"  name="choiceAnswers[1].description" class="text ml fl" />
	                <span class="tbi ml fl"><a href="javascript:;" class="dt_add"></a><a href="javascript:;" class="dt_remove"></a><a href="javascript:;" class="dt_up"></a><a href="javascript:;" class="dt_down"></a></span>
	            	<!-- <span class="bianji ml fl">编辑框</span> -->
	            	<span class="error1"></span>
	            </li>
			</c:when>
			<c:otherwise>
				<c:forEach var="answer" items="${dto.choiceAnswers}" varStatus="s">
					<li> 
		            	<span class="ml font fl"></span>
		                <input type="text"  name="choiceAnswers[${s.index}].description" class="text ml fl"  value="${answer.description}"/>
		                <span class="tbi ml fl"><a href="javascript:;" class="dt_add"></a><a href="javascript:;" class="dt_remove"></a><a href="javascript:;" class="dt_up"></a><a href="javascript:;" class="dt_down"></a></span>
		            	<!-- <span class="bianji ml fl">编辑框</span> -->
		            	<span class="error1"></span>
		            </li>
				</c:forEach>
			</c:otherwise>
			</c:choose>
       </ul>
       
       <div class="star">
       	   <span class="ml"></span><label><img src="<%=basePath%>images/star.png" />参考答案：</label><span class="error1" id="cberror"></span>
       </div>
       <table width="518" border="0" cellspacing="0" cellpadding="0" class="dx_dt ml" <c:if test="${empty (dto.choiceQuestionDtos)}">style="display:none"</c:if>>
        <c:choose>
			<c:when test="${empty (dto.choiceQuestionDtos)}">
				<tr><td class="back" width="200"></td><td class="back">A</td><td class="back">B</td></tr>
              	<tr><td>1</td><td><input type="radio" name="choiceQuestionDtos[0].choiceQuestion.answer" value="1"/></td><td><input type="radio" name="choiceQuestionDtos[0].choiceQuestion.answer" value="2"/></td></tr>
			</c:when>
			<c:otherwise>
				<tr>
					<td class="back" width="200"></td>
					<c:forEach var="answer" items="${dto.choiceAnswers}" varStatus="s">
						<td class="back"></td>
					</c:forEach>
				</tr>
				<c:forEach var="choice" items="${dto.choiceQuestionDtos}" varStatus="s">
					<tr>
						<td>${s.index+1}</td>
						<c:forEach var="answer" items="${dto.choiceAnswers}" varStatus="as">
							<td><input type="radio" name="choiceQuestionDtos[${s.index}].choiceQuestion.answer" value="${as.index+1}" <c:if test="${choice.choiceAnswers[as.index].isright==1}">checked="checked"</c:if>/></td>
						</c:forEach>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
        </table>

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
		<a style="cursor: pointer;" class="btn1" id="submitButton">完&nbsp;&nbsp;成</a>
		<a style="cursor: pointer;" class="btn2 ml1" id="previewButton">预&nbsp;&nbsp;览</a>
	</div>
	<div class="tk_box">
       		<p><span>编辑文本</span><i></i></p>
            <textarea class="tk_wb"></textarea>
            <a class="button3 h2">完成</a>
            <a class="button2">添加图片</a>
            <a class="button2">添加音频</a>
            <p class="upload undis"><span>添加音频：</span><input type="file" /><a class="button3 ml">上传</a></p>
            <p class="ts"><strong>帮助：</strong>如何修改图片的长和宽，在自动生成img标签中，添加 width='200px;' hight='200px;' ，注意之间需要空格。</p>
       </div>
</div>
</form>
</body>
</html>