<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.koolearn.exam.util.Constant"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.entity.Question"%>
<%@page import="com.koolearn.exam.util.QuestionUtil"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>手动录入试题_阅读理解_新东方在线</title>
<meta name="keywords" content="" />
<meta name="description" content=""/>
<meta name="robots" content="all" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css-exam/main.css" />
<script type="text/javascript" src="http://images.koolearn.com/www/subject/script/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="http://www.koolearn.com/js/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/jquery.json-2.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/public.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/question.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.form.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/page.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/question-ext.js"></script>
<script type="text/javascript">
TOTAL_CONTEXT_PATH="<%=request.getContextPath()%>";
/*
*获取大题首个题干内容
*/
function findFristFwbContent(){
	var oEditor=FCKeditorAPI.GetInstance(findSuitName());
	var content=oEditor.GetXHTML(true);
	return content;
}
function findSuitName(){
	var n=$('textarea[name^="content"]').last().attr('name');
	return n;
}
function findPreViewData2(){
	dealwithFwb();
	orderSubItemHidden();
	fillTextarea_param();
	return $("form").html();	
}
function dealwithFwb(){
$("textarea[name^='content']").each(function(){
			var id=$(this).attr('id');
			var oEditor=FCKeditorAPI.GetInstance(id);
			var content=oEditor.GetXHTML(true);
			 $(this).attr('value',content);
		});
}
function previewPop2(){
	if(checkForm()){//表单验证
		var url_='<%=request.getContextPath()%>/question/base/read/preview/0'+"?1="+Math.random();
		windowOpen(url_,780,520);
		//window.open(url_,'preview','toolbar=no,location=no,scrollbars=yes,status=yes,resizable=yes,menubar=no');
	}	
}
function findIdCode(){
		var str="["+$.toJSON($('input[name="questionDto_question_id"]').serializeObject())+",";
		str+=$.toJSON($('input[name="questionDto.question.code"]').serializeObject())+"]";
		return str;
}

function getTranslate(){//得到原文翻译
	$("textarea[name^='complexQuestion.translate']").each(function(){
		var id=$(this).attr('id');
		var oEditor=FCKeditorAPI.GetInstance(id);
		var content=oEditor.GetXHTML(true);
		$(this).attr('value',content);
	});
}

function checkTiming(){
	var reg = /^\d+$/; 
	var timingbool = true;
	$("input[name='complexQuestion.timing']").each(function(){
		if(this.checked&&this.value==3){
			var timing = $("input[name='complexQuestion.countdowntime']");
			var err = $("#errorTime");
			var err1 = $("#errorTime1");
			if(isBlank(timing.val())){
				alertMsg(err.html());
				timing.focus();
				err.show(); 
				timingbool = false;
				return;
			}
			if(!reg.test(timing.val())){
				alertMsg(err1.html());
				timing.focus();
				err1.show(); 
				timingbool = false;
				return;
			}
		}
	});
	return timingbool;
}

var saving=false;
function saveRead(el){
	var	isReturn = false;
	if(checkForm()&&checkTiming()){//表单验证
		if($('input[name="questionDto_question_id"]').val()==0){
			dealwithFwb();
			getTranslate();	//得到原文翻译
			//标签赋值
			fillHeadValue();
			$.ajax({
                    url: "<%=request.getContextPath()%>/question/base/read/save",
                    type: "POST",
                    cache: false,
                    async:false,
                    data: $('form').serializeObject(),
                    success: function(data){
                            var d =eval("("+data+")");//转换为json对象 
                            if(d && d.result == "true"){
                                    var ids=d.message.split(',');
                                    $('input[name="questionDto_question_id"]').val(ids[0]);
                                    $('input[name="complexQuestion.id"]').val(ids[1]);
                                    $("#questionDto\\.question\\.code").val(ids[2]);
                                    el.innerHTML='完&nbsp;&nbsp;成';
                                    alertMsg('保存成功,请继续!');
                            }else{
                                    isReturn=true;
                                    alertMsg(d.message)
                                    return ;
                            }
                    },
                    error: function(data){
                            alertMsg("请求失败，请重试！")
                            isReturn=true;
                            return ;
                    }
            });
			return ;
		}
		
		if(isReturn){
				return;
		}
		if(!chkReadListenSubItem()){
			document.getElementById("question_type_option").selected=true;
			alertMsg('至少要添加一道子试题!');
			return ;
		}
		//return ;
		orderSubItemHidden();
		//标签赋值
		fillHeadValue();
		
		if(saving==true){
		alertMsg("保存中...");
		return;
		}else{
		saving=true;
		document.forms[0].submit();
		alertMsg("保存中...");
		//saving=false;
		}
	}	
}
function orderSubItemHidden(){
//处理子试题顺序
	var t=document.createElement("input");
	t.type='hidden';
	t.name='subItemOrder';
	var arr=[];
	$('#zsttab .a1').each(function(i){
		arr[arr.length]=$(this).attr('questionId');
	});
	if(arr.length>1){
		t.value=arr.join();
		$('form').append(t);
	}
}
function readReload(){
	var questionId=$('input[name="questionDto_question_id"]').val();
	//局部刷新
	var ajaxData = {
			questionId : questionId
	};
	$.ajax({
		url : "/question/base/flushSubQuestion",
		type : 'post',
		data : ajaxData,
		async : false,
		cache : false,
		//dataType : 'json',
		success : function (data) {
			if($.trim(data)!=''){
				$("#zsttab").empty().append(data);
				$("#zsttab").removeClass('hide2');
			}
		},
		error : function () {
			alertMsg("请求失败，请重试！")
		}
	});
	//var saveType=$('input[name="questionDto.saveType"]').val();
	//if(saveType==0){
	//	window.location.href='<%=request.getContextPath()%>/question/base/read/index?questionId='+$('input[name="questionDto_question_id"]').val();
	//}else{
	//	window.location.reload();
	//}
}
function editRead(el){
	addtr($('#zsttab'),false,url);
}
function findEditReadUrl(trEl){
	var questionId=$(trEl).find('a').attr('questionId');
	var typeId=$(trEl).find('a').attr('typeId');
	var url='<%=request.getContextPath()%>/question/base/modify/'+questionId+'/'+typeId+'/<%=Constant.QUESTION_SAVETYPE_UPDATE%>/<%=Constant.QUESTION_RESPONSETYPE_SUBITEM%>/'+$('input[name="questionDto_question_id"]').val();
	return url+"?1="+Math.random();
}
function addReadSubItem(trEl){
	var questionId=$(trEl).find('a').attr('questionId');
	var typeId=$(trEl).find('a').attr('typeId');
	var url='<%=request.getContextPath()%>/question/base/modify/0/'+typeId+'/<%=Constant.QUESTION_SAVETYPE_SAVE%>/<%=Constant.QUESTION_RESPONSETYPE_SUBITEM%>/'+$('input[name="questionDto_question_id"]').val();
	addtr($('#zsttab'),false,url);
}
function removeReadSubItem(trEl){
	var questionId=$(trEl).find('a').attr('questionId');
	var typeId=$(trEl).find('a').attr('typeId');
	var url2='<%=request.getContextPath()%>/question/base/read/delSub/'+questionId+'/'+typeId;
	$.ajax({
			url: url2,
			type: "get",
			cache: false,
			async:false,
			//data: $('form').serializeObject(),
			success: function(data){
				var d =eval("("+data+")");//转换为json对象 
				if(d && d.result == "true"){
					alertMsg('删除成功,请继续!');
				}else{
					alertMsg(d.message)
					return ;
				}
			},
			error: function(data){
				alertMsg("请求失败，请重试！")
				return ;
			}
		});
}
function initEvent(){
	
}
$(function(){
	$('select[name="question_type"]').change(function(){
		var v=$(this).val();
		if($('input[name="questionDto_question_id"]').val()==0){
			document.getElementById("question_type_option").selected=true;
			alertMsg("请先进行前期保存,才可以添加子试题");
			return;
		}
		if(v!='0'){
			var url='<%=request.getContextPath()%>/question/base/modify/0/'+v+'/<%=Constant.QUESTION_SAVETYPE_SAVE%>/<%=Constant.QUESTION_RESPONSETYPE_SUBITEM%>/'+$('input[name="questionDto_question_id"]').val();
			//alert(url);
			document.cookie="selectedName="+$("select[name='question_type']").find("option:selected").text()+'; path=/';
			addtr($('#zsttab'),false,url);
			return;
		}
	});
	initEvent();
	
	//题干计时  切换控制
	$("input[name='complexQuestion.timing']").each(function(){
		if(this.checked&&this.value==3){
			$("input[name='complexQuestion.countdowntime']").attr("readonly","");
		}
		$(this).click(function(){
			if(this.value==1||this.value==2){
				$("input[name='complexQuestion.countdowntime']").val('');
				$("input[name='complexQuestion.countdowntime']").attr("readonly","readonly");
			}
			if(this.value==3){
				$("input[name='complexQuestion.countdowntime']").attr("readonly","");
			}
		});
	});
	
	
	<% 
		String msg=(String)request.getAttribute("errMsg");
		if(msg!=null){
	%>
		alertMsg('<%=msg%>');
	<%} 
	%>
});
</script>
<form action="<%=request.getContextPath()%>/question/base/read/save" method="post">
	
<div class="wp1">
	<div class="fml" style="display:none">
		<span class="tit"><em class="rd">*</em>试题编码：</span>
		<c:choose>
			<c:when test="${empty(readQuestionDto.questionDto.question.code)}">
				<input type="text" id="questionDto.question.code" name='questionDto.question.code' df="产品简写+日期+自定义编码" value="" class="ipt"/>
			</c:when>
			<c:otherwise>
				<input type="text" disabled="disabled" value="${readQuestionDto.questionDto.question.code}" df="产品简写+日期+自定义编码" value="" class="ipt"/>
				<input type="text" id="questionDto.question.code" name='questionDto.question.code' style="display: none;" value="${readQuestionDto.questionDto.question.code}" df="产品简写+日期+自定义编码" value="产品简写+日期+自定义编码" class="ipt"/>
			</c:otherwise>
		</c:choose>
		<span class="error1">编码不能为空</span>
		<span class="error1">编码长度不能超过150个字符</span>
		<span class="error1">编码已经存在</span>
		<span class="error1">编码只能由数字,字母,下划线和斜线('/')组成</span>
	</div>
	<div class="fml" style="display:none">
		<span class="tit">答题提示：</span><input type="text" name='questionDto.question.questionTip' value="${readQuestionDto.questionDto.question.questionTip}" class="ipt"/><a href="javascript:;" class="a1 bjk">编辑框</a>
		<span class="error1">提示不能超过225字符</span>
	</div>
	<div class="fmt">
		<c:choose>
		<c:when test="${readQuestionDto.questionDto.question.id>0}">
			<% int i=1; %>
			<c:forEach var="name" items="${readQuestionDto.questionDto.questionAttachs}">
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
				<textarea class="ipta fck">${readQuestionDto.complexQuestion.topic}</textarea>
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
	<div class="fml" style="display:none">
		<span class="tit">题干计时：</span>
		<c:choose>
			<c:when test="${empty(readQuestionDto.complexQuestion.timing)}">
				<label class="lab1" for="cscl1"><input type="radio" checked="checked" name="complexQuestion.timing" id="cscl1" value="1">不计时</label>
				<label class="lab1" for="cscl2"><input type="radio" name="complexQuestion.timing" id="cscl2" value="2">正计时</label>
				<label class="lab1" for="cscl3" style="width:auto;"><input type="radio" name="complexQuestion.timing" id="cscl3" value="3">倒计时</label>
			</c:when>
			<c:otherwise>
				<label class="lab1" for="cscl1"><input type="radio" name="complexQuestion.timing" id="cscl1" value="1" <c:if test="${readQuestionDto.complexQuestion.timing==1||readQuestionDto.complexQuestion.timing==0}">checked="checked"</c:if> >不计时</label>
				<label class="lab1" for="cscl2"><input type="radio" name="complexQuestion.timing" id="cscl2" value="2" <c:if test="${readQuestionDto.complexQuestion.timing==2}">checked="checked"</c:if>>正计时</label>
				<label class="lab1" for="cscl3" style="width:auto;"><input type="radio" name="complexQuestion.timing" id="cscl3" value="3" <c:if test="${readQuestionDto.complexQuestion.timing==3}">checked="checked"</c:if>>倒计时</label>
			</c:otherwise>
		</c:choose>
				<input type="text" name="complexQuestion.countdowntime" maxlength="5" readonly="readonly" value="${readQuestionDto.complexQuestion.countdowntime }" class="ipt ipt3">&nbsp;秒
				<span class="error1" id="errorTime" style="display:none;">倒计时时间不能为空！</span>
				<span class="error1" id="errorTime1" style="display:none;">倒计时时间格式不正确！</span>
	</div>
	<div class="fml" style="display:none"">
		*不需要计时的不用设置，默认不计时即可。设置计时请不要使用多题干<br>
		*该设置目前仅适用于普通练习模板，对其它模板无效
	</div>
	</div>
	<div class="wp1-lr bdt1">
	<div class="fml">
<!--		<a href="javascript:;" class="a1" onclick="addtr($('#zsttab'))">添加子试题</a>-->
		<select name='question_type'>
			<option id="question_type_option" value ='0'>添加子试题</option>
			<c:forEach items="${qtList}" var="qt">
				<option value="${qt.id}">${qt.name}</option>
			</c:forEach>
		</select>
	</div>
	<c:choose>
	<c:when test="${fn:length(readQuestionDto.subItems)>0}">
		<table class="tab4 mb10 " id="zsttab">
			<tr>
				<th class="td41">子试题编码</th><th class="td44">子试题题型</th><th class="td42">子试题题干</th><th class="td45">操作</th>
			</tr>
		<c:forEach var="name" items="${readQuestionDto.subItems}">
			<tr>
				<td class="td41">
				<a href="javascript:;" style="display: none;" class="a1" questionId="${name.questionDto.question.id}" typeId="${name.questionDto.question.questionTypeId}">${name.questionDto.question.code}</a>
				${name.questionDto.question.code}
				</td>
				<td class="td44">
				<%@include file="../subQuestionType.inc"%>
				</td>
				<td class="td42">${name.shortTopic}</td>
				<td class="td45"><span class="fn"><a href="javascript:;" class="edit"></a><a href="javascript:;" class="add"></a><a href="javascript:;" class="remove"></a><a href="javascript:;" class="up"></a><a href="javascript:;" class="down"></a></span></td>
			</tr>
		</c:forEach>
		</table>
	</c:when>
	<c:otherwise>
		<table class="tab4 mb10 hide2" id="zsttab">
			<tr>
				<th class="td41">子试题编码</th><th class="td44">子试题题型</th><th class="td42">子试题题干</th><th class="td45">操作</th>
			</tr>
		</table>
	</c:otherwise>
	</c:choose>
	</div>
		<div class="wp1-lr bdb1"></div>
		<div class="wp1">
			<span class="tit">原文翻译：</span><br><br>
			<div class="fmbx fmbx2 fc">
				<textarea id="complexQuestion.translate" name='complexQuestion.translate' style="display: none;">
	           		${readQuestionDto.complexQuestion.translate}
	            </textarea>
				<script type="text/javascript">
					var oFCKeditor = new FCKeditor('complexQuestion.translate');
		           oFCKeditor.BasePath = "<%=request.getContextPath()%>/js/fckeditor/";
		           oFCKeditor.ToolbarSet = "Basic";
		           oFCKeditor.Width="100%";
		           oFCKeditor.Height="217";
		           //oFCKeditor.Create();
		           oFCKeditor.ReplaceTextarea() ;
		       </script>
				</div>
				<div class="fml" style="color:red">
					*此项仅在普通考试和练习前台页面显示
				</div>
		</div>	
	<div class="wp1-lr bdb1"></div>
	<div class="ta_c pd1">
		<c:if test="${empty(readQuestionDto.questionDto.question.id)}">
		<a href="javascript:;" class="btn1" onclick="saveRead(this);return false;">前期保存</a>
		</c:if>
		<c:if test="${not empty(readQuestionDto.questionDto.question.id)}">
		<a href="javascript:;" class="btn1" onclick="saveRead(this);return false;">完&nbsp;&nbsp;成</a>
		</c:if>
		<a href="javascript:;" class="btn2 ml1" onclick="previewPop2();return false;">预&nbsp;&nbsp;览</a>
	</div>
<jsp:include page="hideField.jsp" flush="true"/> 
</form>
