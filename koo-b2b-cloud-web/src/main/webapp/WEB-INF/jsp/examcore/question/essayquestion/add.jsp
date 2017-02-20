<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.koolearn.exam.question.entity.Question" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
<title>手动录入试题_填空题_新东方在线</title>
<meta name="keywords" content="" />
<meta name="description" content=""/>
<meta name="robots" content="all" />
<script type="text/javascript" src="http://images.koolearn.com/www/subject/script/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="http://www.koolearn.com/js/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/jquery.json-2.2.min.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/question.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/public.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/essay4type2.js"></script>
<!-- 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/essay.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/examcore/question/menu.js"></script>
 -->
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css-exam/main.css" />
<style type="text/css">
.fn{
padding-left: 0px;
}
.fn a{
	margin-left: 0px;
	vertical-align: bottom;
}
.tklist li{
float:none;
width: 100%;
}
.tklist .ipt2,.tklist .ipt2_ext{width:80px}
.tklist .tks .a1{margin-left:0;}
.tklist .tks .fn{margin-right:0px;}
</style>
</head>
<script type="text/javascript">
//清除表单前后空格
String.prototype.trim=function(){
    return this.replace(/(^\s*)|(\s*$)/g, "");
}
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
					var ul = $("#tklist1"),
					index = (parseInt(ul.attr("index")) || 1)-1;
					setDeleteId(this);	
				});
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
	
	function clearAll(){
		$('form')[0].reset(); 
	}
	//长度大于某值
	function lengthLarger(value, n) {
		if(!value){
			return false;
		}
		var s = trim(value);
		return s.length>=n;
	} 
	function findIdCode(){
		var str="["+$.toJSON($('input[name="questionDto_question_id"]').serializeObject())+",";
		str+=$.toJSON($('input[name="questionDto.question.code"]').serializeObject())+"]";
		return str;
	}
	function save(){
		fillHeadValue();
		var isReturn = false;
		if(checkForm()){
			var answerBool = true;
			$("input[name^='essayQuestionDTOs']").each(function(){
				answerBool = false;
				var answer=$(this);	
				var err = $(this).closest('li').children(".error1:eq(0)");
				var err1 = $(this).closest('li').children(".error1:eq(1)");
				if($(this).attr('type')=='hidden'){
					return;
				}
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
			if(answerBool){
				alertMsg("没有插入填空!");
				return;
			}
			fecthTopicExt();
			if(!isReturn){
				setRadioValue();
				document.forms[0].submit();
			}
		}	
	}
	
	/**
	*预览填空题
	*/
function detail(){
	var context_path=TOTAL_CONTEXT_PATH||"";
	var isReturn = false;
	if(checkForm()){
		var answerBool = true;
		$("input[name^='essayQuestionDTOs']").each(function(){
			answerBool = false;
			var answer=$(this);	
			var err = $(this).parent().children(".error1:eq(0)");
			var err1 = $(this).parent().children(".error1:eq(1)");
			if($(this).attr('type')=='hidden'){
				return;
			}
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
		if(answerBool){
			alertMsg("没有插入填空!");
			return;
		}																				
		fecthTopicExt();
		if(!isReturn){
			setRadioValue();
			///question/view/index?questionId=
			windowOpen(context_path+'/question/base/essay/detail/0?questionType=<%=Question.QUESTION_TYPE_FILL_BLANK%>'+"&1="+Math.random(),780,400);
			//popUrl('填空题',context_path+'/maintain/essay/detail/0?questionType=<%=Question.QUESTION_TYPE_FILL_BLANK%>');
		}
	}	
}
	
	
	$(function(){
		$("#pgfs1").live("click",function(){
			$("#pingfen").show();
		});
		$("#pgfs2").live("click",function(){
			$("#pingfen").hide();
		});
		if($("#pgfs2").attr("checked")){
			$("#pingfen").hide();
		}
		initRadioEvent();
		initSelectAndCheckedValue();
	<% 
		String msg=(String)request.getAttribute("errMsg");
		if(msg!=null){
	%>
		popAlert('<%=msg%>');
	<%} 
	%>
	<c:if test="${not empty(essayQuestionDTO.questionDto.question.teId)}">
		fectchParentCode();
	</c:if>
	});	
</script>
<body>
<form action="/question/base/essay/insert" name="form1" method="post">
<jsp:include page="hiddenessay.jsp" flush="true"/> 
	<c:if test="${essayQuestionDTO.questionDto.responseType!=1}">
	  
	</c:if>
<div class="wp1">
	<c:if test="${essayQuestionDTO.questionDto.responseType==1}">
	<div class="fml">
		<span class="tit"><em class="rd">*</em>子题题型：</span>
		<select name='' disabled="disabled">
			<option value="2">普通填空</option>
		</select>
	</div>
	</c:if>
		<c:choose>
		<c:when test="${empty(essayQuestionDTO.questionDto.question.code)}">
			<input hidden type="text" id="questionDto.question.code" name='questionDto.question.code'  df="产品简写+日期+自定义编码" value="" class="ipt"/>
		</c:when>
		<c:otherwise>
		<input hidden type="text" id="questionDto.question.code" name='questionDto.question.code' readonly="readonly" df="产品简写+日期+自定义编码" value="${essayQuestionDTO.questionDto.question.code}" class="ipt"/>
		</c:otherwise>
		</c:choose>
	<!-- 
	<div class="fml">
		<span class="tit"><em class="rd">*</em><c:choose><c:when test="${essayQuestionDTO.questionDto.responseType==1}">子</c:when><c:otherwise>试</c:otherwise></c:choose>题编码：</span>
		<span class="error1">编码不能为空</span>
		<span class="error1">编码长度不能超过150个字符</span>
		<span class="error1">编码已经存在</span>
		<span class="error1">编码只能由数字,字母,下划线和斜线('/')组成</span>
	</div>
	 -->
	<div class="fml" style="display:none;">
		<span class="tit">答题提示：</span><input type="text" name='questionDto.question.questionTip' value='${essayQuestionDTO.questionDto.question.questionTip}' class="ipt"/><a href="javascript:;" class="a1 bjk">编辑框</a>
		<span class="error1">提示不能超过255字符</span>
	</div>
	<c:if test="${essayQuestionDTO.questionDto.responseType==1}">
<%--	<c:if test="${not empty(essayQuestionDTO.questionDto.question.teId)}">--%>
	<div class="fml ontab">
		<span class="tit wh_at">试题材料：</span>
		<div class="ipta" style="height:220px;">
           <textarea id="questionDto.question.topicExt" name='questionDto.question.topicExt' style="display: none;">
           	${essayQuestionDTO.questionDto.question.topicExt}
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
	<div  class="fmt BD_tkt" ul="#tklist1">
	<c:choose>
		<c:when test="${essayQuestionDTO.questionDto.question.id>0}">
			<% int i=1; %>
			<c:forEach var="name" items="${essayQuestionDTO.questionDto.questionAttachs}">
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
					<a href="javascript:;" id="addTk" style="display:none" onclick="addtk(this,false)">插入填空</a>
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
				<textarea class="ipta fck">${essayQuestionDTO.essayQuestion.topic}</textarea>
				<span class="error1">题干不能为空</span>
				<span class="error1">题干不能超过65535个字符</span>
				<div class="ta_r">
					<a href="javascript:;" id="addTk" style="display:none" onclick="addtk(this,false)">插入填空</a>
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
					<a href="javascript:;" id="addTk" style="display:none" onclick="addtk(this,false)">插入填空</a>
					<a href="javascript:;" class="btn1 addtg">增加题干</a>
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
	<c:when test="${empty (essayQuestionDTO.fillblankAnswers)}">
		<ul class="tklist fc mb10" id="tklist1" index="${fn:length(essayQuestionDTO.fillblankAnswers)}">
		</ul>
			<ul>
			<li>
			&nbsp;&nbsp;&nbsp;<input type="checkbox" name="essayQuestion_sensing_box" value="1" checked="checked"/>&nbsp;&nbsp;区分大小写
				<input type="hidden" name="essayQuestion.sensing" value="1"/>
				<br><br>
				<span id="autohidespan">
				&nbsp;&nbsp;&nbsp;<input type="checkbox" name="essayQuestion.one2one" value="1" checked="checked"/>&nbsp;&nbsp;答案与空必须按顺序一一对应
				<br><br><br>
				</span>
			</li>
		</ul>
	</c:when>
	<c:otherwise>
		<ul class="tklist fc mb10" id="tklist1" index="${fn:length(essayQuestionDTO.fillblankAnswers)}">
			<c:forEach var="name" items="${essayQuestionDTO.fillblankAnswers}" varStatus="s">
				<li index="${s.index+1}">
				<span class="tktit">(${s.index+1})</span>
				<input type="hidden" name="essayQuestionDTOs[${s.index}].essayQuestion.id" class="hidcid" value="${empty essay.essayQuestion ? 0 : essay.essayQuestion.id}">
				<input type="hidden" name="essayQuestionDTOs[${s.index}].essayQuestion.questtionId" class="hidqid" value="${empty essay.essayQuestion ? 0 : essay.essayQuestion.questtionId}">
				<input type="hidden" name="essayQuestionDTOs[${s.index}].fillblankAnswers[0].id" class="hid" value="${empty essay.fillblankAnswers[0] ? 0 : essay.fillblankAnswers[0].id}">
				<input type="hidden" name="essayQuestionDTOs[${s.index}].fillblankAnswers[0].answer" value="${name.answer}" class="ipt2">
				<c:forEach var="exts" items="${name.exts}" varStatus="n">
				<span class="tks">
				<input type="text" name="essayQuestionDTOs[${s.index}].fillblankAnswers[0].exts[${n.index}]" value="${exts}" class="ipt2_ext">
				<a href="javascript:;" class="a1 bjk pttk_1" >编辑框</a></span>
				</c:forEach>
				<span class="error1">答案不能为空</span>
				<span class="error1">答案长度不能超过900个字符</span>
				</li>
			</c:forEach>
		</ul>
		<ul>
			<li>
				<input type="checkbox" name="essayQuestion_sensing_box" value="1" <c:if test="${essayQuestionDTO.essayQuestion.sensing==1}">checked="checked"</c:if>/>&nbsp;&nbsp;区分大小写
				<input type="hidden" name="essayQuestion.sensing" value="${essayQuestionDTO.essayQuestion.sensing}"/>
				<br><br>
				<span id="autohidespan">
				<input type="checkbox" name="essayQuestion.one2one" value="1" <c:if test="${essayQuestionDTO.essayQuestion.one2one==1}">checked="checked"</c:if>/>&nbsp;&nbsp;答案与空必须按顺序一一对应
				<br><br><br>
				</span>
			</li>
		</ul>
	</c:otherwise>
	</c:choose>
	<div class="fml">
		<span class="tit"><em class="rd">*</em>批改方式：</span>
		<c:choose>
			<c:when test="${empty(essayQuestionDTO.essayQuestion.markType)}">
				<label class="lab1" for="pgfs1"><input type="radio" name="essayQuestion.markType" value="1" checked="checked" id="pgfs1">系统批改（客观题）</label>
				<label class="lab1" for="pgfs2"><input type="radio" name="essayQuestion.markType" value="0"  id="pgfs2">人工/自评（主观题）</label>
			</c:when>
			<c:otherwise>
				<label class="lab1" for="pgfs1"><input type="radio" name="essayQuestion.markType" value="1" <c:if test="${essayQuestionDTO.essayQuestion.markType==1}">checked="checked"</c:if> id="pgfs1">系统批改（客观题）</label>
				<label class="lab1" for="pgfs2"><input type="radio" name="essayQuestion.markType" value="0"  <c:if test="${essayQuestionDTO.essayQuestion.markType==0}">checked="checked"</c:if> id="pgfs2">人工/自评（主观题）</label>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="fml" id="pingfen">
		<span class="tit"><em class="rd">*</em>评分方式：</span>
		<c:choose>
			<c:when test="${empty(essayQuestionDTO.essayQuestion.metewand)}">
				<label class="lab1" for="pffs1"><input type="radio" name="essayQuestion.metewand" value="0" checked="checked" id="pffs1">全对得分</label>
				<label class="lab1" for="pffs2"><input type="radio" name="essayQuestion.metewand" value="1"  id="pffs2">按空得分</label>
			</c:when>
			<c:otherwise>
				<label class="lab1" for="pffs1"><input type="radio" name="essayQuestion.metewand" value="0" <c:if test="${essayQuestionDTO.essayQuestion.metewand==0}">checked="checked"</c:if> id="pffs1">全对得分</label>
				<label class="lab1" for="pffs2"><input type="radio" name="essayQuestion.metewand" value="1"  <c:if test="${essayQuestionDTO.essayQuestion.metewand==1}">checked="checked"</c:if> id="pffs2">按空得分</label>
			</c:otherwise>
		</c:choose>
	</div>
	
		<div class="fmt">
			<span class="tit">答题解析：</span><br>
		</div>
<!--		<textarea class="ipta"></textarea>-->
<div class="ipta">
           <textarea id="questionDto.question.explan" name='questionDto.question.explan' style="display: none;">
           	${essayQuestionDTO.questionDto.question.explan}
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
	<div class="wp1-lr bdb1"></div>
	<div class="wp1">
	<div class="ta_c pd1">
		<a href="#" class="btn1" onclick="javascript:save();return false;">完&nbsp;&nbsp;成</a>
		<a href="#" class="btn2 ml1" onclick="javascript:detail();return false;">预&nbsp;&nbsp;览</a>
	</div>
	<input type="hidden" id="questionDto.questionBankExt.tag1" name="questionDto.questionBankExt.tag1" value="${essayQuestionDTO.questionDto.questionBankExt.tag1 }"/>
	<input type="hidden" id="questionDto.questionBankExt.tag2" name="questionDto.questionBankExt.tag2" value="${essayQuestionDTO.questionDto.questionBankExt.tag2 }"/>
	<input type="hidden" id="questionDto.questionBankExt.tag3" name="questionDto.questionBankExt.tag3" value="${essayQuestionDTO.questionDto.questionBankExt.tag3 }"/>
	<input type="hidden" id="questionDto.questionBankExt.status" name="questionDto.questionBankExt.status" value="${essayQuestionDTO.questionDto.questionBankExt.status }"/>
</form>	
</div>
</body>
</html>