<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.koolearn.exam.question.entity.Question"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>手动录入试题_选择题_新东方在线</title>
<meta name="keywords" content="" />
<meta name="description" content=""/>
<meta name="robots" content="all" />
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
	FWB_TOOLBAR_NAME='Basic';
	function FCKeditor_OnComplete(fck){
		var j=$('textarea[name^="content"]');
		var n=j.last().attr('name');
		if(fck.Name.indexOf('content')==0){
				if(n==fck.Name){
					$(fck.EditorWindow).blur(function(){
			            	//fck_insertbox_fun(fck.EditorDocument.body.innerHTML,fck.EditorDocument.body,fck.Name);
			        });
	            }
		}
	}
	function fck_insertbox_fun(html,name,fckName){
		//top.location.hash = "111";
		var n=findSuitName();
		if(n!=fckName){
			v=$('<div/>');
			v.html(html);
			v.find('.tb1').each(function(i){
				$(this).html(i+1);
			});
			name.innerHTML=v.html();
			return;
		}
		v=$('<div/>');
		v.html(html);
		addwz(v,$('#zstfm'),name);
		//添加方框后去除选择
		$("#zstfm .cbx1").attr("checked",false);
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
		
		var oEditor=FCKeditorAPI.GetInstance("choiceQuestion.feature");
		//火狐ie兼容
		var inContent=document.all?oEditor.EditorDocument.body.innerText:oEditor.EditorDocument.body.textContent ;
		if(inContent==null||trim(inContent)==''){
			var err1 = $("#choiceQuestion\\.feature").parent().children(".error1");
			err1.show();
			alertMsg("插入句子不能为空!");	
			return;
		}
		fecthTopicExt();
			if(!isReturn){
				setRadioValue();
				if(!chkIsright()){
					alertMsg("至少要选中一个选项!");	
					return;
				}
				setAnserField_boxChoice();
				document.forms[0].submit();
			}	
		}	
	}
	function setAnserField_boxChoice(){
		var arr=[];
		$('#zstfm ul li').each(function(i){
			//v=$(this).find('.tb1').html();
			var a=document.createElement('input');
			a.type='hidden';
			a.name='fmbx1_'+String.fromCharCode(97+i);
			a.value=i;
			arr[arr.length]=a;
		});
		$('form').append(arr);
	}
	
	$(function(){
		initRadioEvent();
		initSelectAndCheckedValue();
		autoShowAnswer();
	<% 
		String msg=(String)request.getAttribute("errMsg");
		if(msg!=null){
	%>
		popAlert('<%=msg%>');
	<%} 
	%>
	<c:if test="${not empty(choiceQuestionDto.questionDto.question.teId) and choiceQuestionDto.questionDto.question.teId!=0}">
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
<form action="<%=request.getContextPath()%>/question/base/choice/saveBox" method="post">

<div class="wp1">
	<c:if test="${choiceQuestionDto.questionDto.responseType==1}">
	<div class="fml">
		<span class="tit"><em class="rd">*</em>子题题型：</span>
		<select name='' disabled="disabled">
			<option value="21">方框题</option>
		</select>
	</div>
	</c:if>
	<div class="fml" style="display:none;">
		<span class="tit"><em class="rd">*</em><jsp:include page="codeNamePart.jsp" flush="true" />题编码：</span>
		<c:choose>
		<c:when test="${empty(choiceQuestionDto.questionDto.question.code)}">
			<input type="text" id='questionDto.question.code' name='questionDto.question.code'  df="产品简写+日期+自定义编码" value="" class="ipt"/>
		</c:when>
		<c:otherwise>
		<input type="text" disabled="disabled"  df="产品简写+日期+自定义编码" value="${choiceQuestionDto.questionDto.question.code}" class="ipt"/>
		<input type="text" id='questionDto.question.code' name='questionDto.question.code' style="display: none;" df="产品简写+日期+自定义编码" value="${choiceQuestionDto.questionDto.question.code}" class="ipt"/>
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
	<c:if test="${not empty(choiceQuestionDto.questionDto.question.teId) and choiceQuestionDto.questionDto.question.teId!=0}">
	<div class="fml ontab">
		<span class="tit wh_at">试题材料：</span>
		<div class="ipta" style="height:220px;">
           <textarea id="questionDto.question.topicExt" name='questionDto.question.topicExt' style="display: none;">
           	${choiceQuestionDto.questionDto.question.topicExt}
           </textarea>
			<script type="text/javascript">
			var oFCKeditor = new FCKeditor('questionDto.question.topicExt');
			           oFCKeditor.BasePath = "${examctx}/examcore/fckeditor/";
			           oFCKeditor.ToolbarSet = "boxBasic";
			           oFCKeditor.Width="100%";
			           oFCKeditor.Height="217";
			           oFCKeditor.ReplaceTextarea() ;
			       </script>
			</div>
			<div class="fml" style="color:red">
			请在试题材料处点击设置 <img src="/js/fckeditor/editor/plugins/insertbox/placeholder.png">插入句子的位置
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
	<div class="fml">
		<span class="tit wh_at">插入句子：</span>
		<div class="ipta">
           <textarea id="choiceQuestion.feature" name='choiceQuestion.feature' style="display: none;">
           	${choiceQuestionDto.choiceQuestion.feature}
           </textarea>
			<script type="text/javascript">
			var oFCKeditor = new FCKeditor('choiceQuestion.feature');
			           oFCKeditor.BasePath = "${examctx}/examcore/fckeditor/";
			           oFCKeditor.ToolbarSet = "Basic";
			           oFCKeditor.Width="100%";
			           oFCKeditor.Height="217";
			           oFCKeditor.ReplaceTextarea() ;
			       </script>
			<span class="error1">插入句子不能为空</span>
			</div>
	</div>
	</div>
	<div class="wp1-lr bdb1"></div>
	<div class="wp1" style="padding:40px 20px 20px 24px;">
		<strong>答案及选项</strong>
	<div class="fml" style="color:red">
	请先在试题材料处点击设置 <img src="/js/fckeditor/editor/plugins/insertbox/placeholder.png">插入句子的位置
	</div>
	<div class="fmbx fmbx1 fmone fc" id="zstfm">
		<div class="fl fmfn">
			<div class="num">1</div>
			<a href="javascript:;" class="fmclose"></a>
		</div>
		<ul class="hide2">
			<c:choose>
			<c:when test="${fn:length(choiceQuestionDto.choiceAnswers)>0}">
			<% int i=0; %>
				<c:forEach var="name" items="${choiceQuestionDto.choiceAnswers}">
				<li>
					<input type="radio" class="cbx1" name="isright" value='${name.isright}'/>&nbsp;<span class="xh"><%=(char)(65+i)%></span><span class="tb1"><%=++i %></span><span class="fn hide2"><a href="javascript:;" class="add"></a><a href="javascript:;" class="remove"></a><a href="javascript:;" class="up"></a><a href="javascript:;" class="down"></a></span>
					<span class="error1">答案不能为空</span>
					<span class="error1">答案长度不能超过900个字符</span>
				</li>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<li>
					<input type="radio" class="cbx1" name="isright" value=""/>&nbsp;<span class="xh">A</span><span class="tb1"></span><span class="fn hide2"><a href="javascript:;" class="add"></a><a href="javascript:;" class="remove"></a><a href="javascript:;" class="up"></a><a href="javascript:;" class="down"></a></span>
					<span class="error1" style="padding:0 0 0 20px">答案不能为空</span>
					<span class="error1" style="padding:0 0 0 20px">答案长度不能超过900个字符</span>
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
		<a href="javascript:;" class="btn2 ml1" onclick="previewPop();return false;">预&nbsp;&nbsp;览</a>
	</div>
</div>
<jsp:include page="hideField.jsp" flush="true"/> 
<input type="hidden" name="questionDto.question.questionTypeId" value="<%=Question.QUESTION_TYPE_DANXUAN_BOX%>"/>
</form>
</body>
</html>