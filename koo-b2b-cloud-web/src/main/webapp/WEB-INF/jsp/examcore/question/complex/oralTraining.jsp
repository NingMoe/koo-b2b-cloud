<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%><jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>手动录入试题_口语训练题_新东方在线</title>
<meta name="keywords" content="" />
<meta name="description" content=""/>
<meta name="robots" content="all" />
<script type="text/javascript" src="http://images.koolearn.com/www/subject/script/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery.json-2.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="http://www.koolearn.com/js/popup.js"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery.form.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/public.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/validate.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/complex.js"></script>
<script type="text/javascript" src="${examctx}/examcore/question/question.js"></script>
<script type="text/javascript" src="${examctx}/examcore/oralTraining.js"></script>
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css/kpt.css" />
<link type="text/css" rel="stylesheet" href="${examctx}/examcore/css/main.css" />
<script>
var TOTAL_CONTEXT_PATH="<%=request.getContextPath()%>";
var i_add = 0;
$(function(){
	
	var ct = $("#composeType").attr("ct") || "0";
	$("#composeType").val(ct);
	var returnFlag , alertStr;
	$("#submitButton").click(function(){
		$(".error1").each(function(){
			$(this).empty().hide();
		});
		$(".error2").each(function(){
			$(this).empty().hide();
		});
		alertStr="";
		returnFlag=true;
		$("textarea[name='essayQuestion.topic']").each(function(i){
			var id="newFCK"+parseInt($(this).parents(".casual_work").find(".num2").text());
			var oEditor=FCKeditorAPI.GetInstance(id);
			var content=oEditor.GetXHTML(true);
			 $(this).html(content);
			 if(content==null || content==""){
				 returnFlag=false;
				 alertStr =(alertStr=="")? (i+1) : alertStr+ ","+(i+1);
				 $(this).siblings(".error1").html("不能为空").show(); 
			 }
		});
		if(!returnFlag){
			alertMsg("问题"+alertStr+"内容为空，请填写");
			return false;
		}
		if(validate() && checkChoice() && checkCB() && checkMyTopic() && checkAnswer()){
			$("#complexForm").attr("action","/maintain/complex/insert");
			$("#complexForm").attr("target","");
			$("#complexForm").submit();
		}
		return false;
	});
	$("#previewButton").click(function(){
		$(".error1").each(function(){
			$(this).empty().hide();
		});
		$(".error2").each(function(){
			$(this).empty().hide();
		});
		$("#cberror").empty().hide();
		if(validate() && checkChoice() && checkCB() && checkMyTopic() && checkAnswer()){
			var preview = 'preview';
			window.open('',preview,'toolbar=no,location=no,scrollbars=yes,status=no,menubar=no,width=600,height=600');
			$("#complexForm").attr("target",preview);
			$("#complexForm").attr("action","/maintain/complex/preview/1");
			$("#complexForm").submit();
		}
	});
	$(".casual_work div input").live("click",function(){
		  var otigan = $(this).parents(".casual_work").clone();
		  $(this).parents(".casual_work").after(otigan);
		  var newTpbox=$(this).parents(".casual_work").next(".casual_work").find(".tpbox");
		  newTpbox.html('');
		  var name="newFCK"+(parseInt($(this).parents(".casual_work").find(".num2").text())+1);
		  var oFCKeditor= new FCKeditor(name);
			var context_path=TOTAL_CONTEXT_PATH||"";
	         oFCKeditor.BasePath = context_path+"/js/fckeditor/";
	         oFCKeditor.ToolbarSet = "InsertBlank";//Basic
	         oFCKeditor.Width="705";
	         oFCKeditor.Height="217";
	         oFCKeditor.Create(newTpbox);
		  var aPar_box = $(".casual_work");
		  aPar_box.each(function(){
			   $(this).find(".num2").html($(this).index()+1);
			   $(this).find(".fmt i").html($(this).index()+1);
			//   $(this).find(".ipta").attr("name","essayQuestionDTOs["+$(this).index()+"].fillblankAnswers[0].answer");
			  $(this).find(".fckk").attr("id","newFCK"+parseInt($(this).find(".num2").text()));
			   //$(this).find("input[type=hidden]").attr("id","essayQuestionDTOs["+$(this).index()+"].essayQuestion.topic___Config");
			   //$(this).find("iframe").attr("id","essayQuestionDTOs["+$(this).index()+"].essayQuestion.topic___Frame");
			   $(this).find("ul").show();
		  });
	});
	//鼠标滑入滑出"增加段落"的关闭按钮的效果，鼠标点击"增加"的关闭按钮的时候关闭该题干,当剩下一个"增加段落"的时候，关闭按钮隐藏。每次关闭一个题干的时候，所有的题干的序号要重新排一次。
	$(".casual_work ul li.gbi").live("click mouseover mouseout",function(event){
		if(event.type=="click"){
			 var aPar_box = $(this).parents(".casual_work").siblings(".casual_work");
			 if(aPar_box.length != 0){
				$(this).parents(".casual_work").remove();
			 }
		    aPar_box.each(function(){
		       $(this).find(".num2").html($(this).index()+1);
		       $(this).find(".fmt .font_zh").html($(this).index()+1);
		    });	
			
		}else if(event.type=="mouseover"){
			 $(this).addClass("gbi_h");
		}else{
			$(this).removeClass("gbi_h");	
		}
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
</script>
</head>
<body>
<div class="path"><a href="<%=basePath%>pages/index.jsp">首页</a><c:if test="${not empty sessionScope.userTemplateName}"><span>></span>${sessionScope.userTemplateName}</c:if><span>></span><a href="<%=basePath%>pages/menu.jsp">试题管理</a><span>></span><em>手动录入试题_口语训练</em></div>
<form id="complexForm" action="<%=request.getContextPath()%>/maintain/complex/insert" method="post">
	<c:if test="${dto.questionDto.responseType!=1}">
	<jsp:include page="/pages/menu1.jsp" flush="true" />
	</c:if>
<div class="wp1">
	<c:if test="${dto.questionDto.responseType==1}">
	<div class="fml">
		<span class="tit"><em class="rd">*</em>子题题型：</span>
		<select name='' disabled="disabled">
			<option value="1">口语训练</option>
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
		<input type="hidden" id="type" name="questionType" value="38"/>
		<input type="hidden" id="issubjectived" name="questionDto.question.issubjectived" value="0"/>
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
	</div>
	
	
	<!--增加段落开始-->
	<div class="fmt">
		<c:choose>
			<c:when test="${empty (dto.essayQuestionDTOs)}">
				<div class="casual_work">
					<div class="fmt">
						<span class="tit wh_at"><em class="rd">*</em>问题<i class="font_zh">1</i>：</span><br>
					</div>
		            <ul class="num">
		                <li class="num2">1</li>
		                <li class="gbi"></li>
		            </ul>
		            <span class="error1"></span>
		            <textarea class="fckk wt" name="essayQuestion.topic"></textarea>
					<div class="fmt">
						<span class="tit wh_at"><em class="rd">*</em>问题<i class="font_zh">1</i>答案：（此部分只能录入英文，且录入内容不含有回车）</span><br>
					</div>
					<span class="error2"></span>
					<textarea class="ipta" name="fillblankAnswers[0].answer" onkeyup="value=value.replace(/[^\x00-\xff]/g,'')"
							onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\x00-\xff]/g,''))"></textarea>
		            <div class="btn_box tf_tl"><input type="button" class="button" value="增加问题" onfocus="this.blur()" /></div>
		         </div>
			</c:when>
			<c:otherwise>
				<ul class="fc mb10" id="tklist1" index="${fn:length(dto.essayQuestionDTOs)}">
					<c:forEach var="essay" items="${dto.essayQuestionDTOs}" varStatus="s">
						<div class="casual_work">
							<div class="fmt">
								<span class="tit wh_at"><em class="rd">*</em>问题<i class="font_zh">${s.index+1}</i>：</span><br>
							</div>
				            <ul class="num">
				                <li class="num2">${s.index+1}</li>
				                <li class="gbi"></li>
				            </ul>
				             <span class="error1"></span>
				            <textarea class="fckk" name="essayQuestion.topic" id="newFCK${s.index+1}">${essay.essayQuestion.topic}</textarea>
							<div class="fmt">
								<span class="tit wh_at"><em class="rd">*</em>问题<i class="font_zh">${s.index+1}</i>答案：（此部分只能录入英文，且录入内容不含有回车）</span><br>
							</div>
							<span class="error2"></span>
							<textarea class="ipta" name="fillblankAnswers[0].answer" onkeyup="value=value.replace(/[^\x00-\xff]/g,'')"
							onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\x00-\xff]/g,''))">${essay.fillblankAnswers[0].answer }</textarea>
				            <div class="btn_box tf_tl"><input type="button" class="button" value="增加问题" onfocus="this.blur()" /></div>
				         </div>
					</c:forEach>
				</ul>
			</c:otherwise>
		</c:choose>
	</div>
	<!--增加段落结束-->
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
		<a style="cursor: pointer;" class="btn1" id="submitButton">完&nbsp;&nbsp;成</a>
		<a style="cursor: pointer;" class="btn2 ml1" id="previewButton">预&nbsp;&nbsp;览</a>
	</div>
</div>
</form>
</body>
</html>