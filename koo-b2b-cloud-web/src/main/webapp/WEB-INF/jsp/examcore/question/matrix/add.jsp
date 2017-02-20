<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>手动录入试题_单选矩阵题_新东方在线</title>
<meta name="keywords" content="" />
<meta name="description" content=""/>
<meta name="robots" content="all" />
<link type="text/css" rel="stylesheet" href="<%=basePath%>/css-exam/main1026.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css-exam/main.css" />

<script type="text/javascript" src="http://images.koolearn.com/www/subject/script/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/jquery.json-2.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/examcore/question/public.js"></script>
<script type="text/javascript" src="http://www.koolearn.com/js/popup.js"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery.form.js"></script>
<script type="text/javascript" src="<%=basePath%>js/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=basePath%>js/examcore/question/validate.js"></script>
<script type="text/javascript" src="<%=basePath%>js/examcore/question/question.js"></script>
<script type="text/javascript" src="<%=basePath%>js/examcore/question/matrix.js"></script>
<script>
$(function(){
	var message = "${message}";
	if(message){
		popAlert(message);
	}
	
	$("#submit").click(function(){
		fillTextarea_param();
		fillHeadValue();
		if(validate() && checkChoice() && checkBox()){
			$("#matrixForm").attr("target","_self");
			$("#matrixForm").attr("action","/question/base/matrix/insert");
			$("#matrixForm").submit();
		}
			
	});
	checkOne();
});

function checkOne(){
	var v=$('#showForm').val();
	if(v==2){
		$('#ckda input[type="checkbox"]').live('click',function(index,el){
			var th=$(this); tr=th.closest('tr').find('input[type="checkbox"]');
			if(th.attr('checked')){
				tr.attr('checked', false);
				th.attr('checked', 'checked');
			}

		});
	}else if(v==1){
		$('#ckda input[type="checkbox"]').live('click',function(index,el){
			var th=$(this); tr=th.closest('tr');
			//同一行已经存在，不能在选
			th.closest('tr').find('input[type="checkbox"]').attr('checked', false);
			
			var t=th.attr('name');t1=t.indexOf('[');var t2=t.indexOf(']'); t=t.substring(t1+1,t2);
			var th_index=th.val()-1,tr_index=t+1;
			$('#ckda tr:not(:eq('+tr_index+'))').each(function(index,el){
				$('input[type="checkbox"]:eq('+th_index+')',$(this)).attr('checked', false);
			});
			th.attr('checked', 'checked');
			
		});
	}
}
</script>
<form id="matrixForm" action="<%=basePath%>/question/base/matrix/insert" method="post">
<div class="wp1">
	<div class="fml" style="display:none;">
		<input type="hidden" id="responseType" name="questionDto.responseType" value="${empty dto.questionDto.responseType ? 0 : dto.questionDto.responseType}"/>
		<input type="hidden" id="saveType" name="questionDto.saveType" value="${empty dto.questionDto.saveType ? 0 : dto.questionDto.saveType}"/>
		<input type="hidden" id="sta" name="questionDto.question.status" value="${empty dto.questionDto.question.status ? 0 : dto.questionDto.question.status}"/>
		<input type="hidden" id="version" name="questionDto.question.version" value="${empty dto.questionDto.question.version ? 0 : dto.questionDto.question.version}"/>
		<input type="hidden" id="cid" name="matrixQuestion.id" value="${empty dto.matrixQuestion ? 0 : dto.matrixQuestion.id}"/>
		<input type="hidden" id="qid" name="questionDto.question.id" value="${empty dto.questionDto.question ? 0 : dto.questionDto.question.id}"/>
		<input type="hidden" id="delqid" name="delQuestionId"/>
		<input type="hidden" id="delsid" name="delSubId"/>
		<input type="hidden" id="questionDto.questionBankExt.tag1" name="questionDto.questionBankExt.tag1" value="${dto.questionDto.questionBankExt.tag1 }"/> 
		<input type="hidden" id="questionDto.questionBankExt.tag2" name="questionDto.questionBankExt.tag2" value="${dto.questionDto.questionBankExt.tag2 }"/>
		<input type="hidden" id="questionDto.questionBankExt.tag3" name="questionDto.questionBankExt.tag3" value="${dto.questionDto.questionBankExt.tag3 }"/> 
		<input type="hidden" id="questionDto.questionBankExt.status" name="questionDto.questionBankExt.status" value="${dto.questionDto.questionBankExt.status }"/>
		<input type="hidden" id="questionDto.questionBankExt.schoolFrom" name="questionDto.questionBankExt.schoolFrom" value="${dto.questionDto.questionBankExt.schoolFrom }"/>
<%--		<input type="hidden" id="questionDto.question.questionTypeId" name="questionDto.question.questionTypeId" value="${param.questionTypeId }"/>--%>
		<c:if test="${showForm==1 || param.questionTypeId==40}">
			<input type="hidden" id="showForm" name="matrixQuestion.showForm" value="1"/>
			</c:if>
		<c:if test="${showForm==2 || param.questionTypeId==41}">
			<input type="hidden" id="showForm" name="matrixQuestion.showForm" value="2"/>
			</c:if>
		<c:if test="${showForm==3 || param.questionTypeId==42}">
			<input type="hidden" id="showForm" name="matrixQuestion.showForm" value="3"/>
			</c:if>
		<input type="hidden" id="code" name="questionDto.question.code" value="${dto.questionDto.question.code}" class="ipt required maxlength" ml="140"/>
	</div>
	<div class="fml" style="display:none;">
		<span class="tit">答题提示：</span><input type="text" class="ipt maxlength" ml="255" name="questionDto.question.questionTip" value="${dto.questionDto.question.questionTip}"/><a style="cursor: pointer;" class="a1 bjk">编辑框</a><span class="error1">提示不能超过255个字符</span>
	</div>
	<div class="fmt">
		<c:choose>
	<c:when test="${empty (dto.questionDto.questionAttachs) || fn:length(dto.questionDto.questionAttachs)==0}">
		<div class="fmbx fmbx2 fmone fc">
			<div class="fl fmfn">
				<div class="num">1</div>
				<a style="cursor: pointer;" class="fmclose"></a>
			</div>
			<textarea class="ipta iptatk fck" name="content">${dto.matrixQuestion.topic}</textarea>
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
			<textarea class="ipta iptatk fck" name="content">${dto.matrixQuestion.topic}</textarea>
			<div class="ta_r">
				<a style="cursor: pointer;" class="btn1 addtg">增加题干</a>
			</div>
		</div>
	</c:otherwise>
	</c:choose>
	</div>
	</div>
	<div class="wp1-lr bdb1"></div>
	<div class="wp1">
	<div class="fml">
		<span class="tit"><em class="rd">*</em>问题描述：</span>
		<a style="cursor: pointer;" class="a1" onclick="pladdfm(1,1,$('#fmt2'))">批量添加描述</a>
	</div>
	<div class="fmt" id="fmt2">
	<div class="fmbx fmbx1 fmone fc">
		<ul>
		<c:choose>
			<c:when test="${empty (dto.choiceQuestionDtos)}">
				<li class="ver">
					<span class="xh">1</span><input type="text" name="choiceQuestionDtos[0].choiceQuestion.topic" class="ipt wh1 required" /><span class="fn"><a style="cursor: pointer;" class="add"></a><a style="cursor: pointer;" class="remove"></a><a style="cursor: pointer;" class="up"></a><a style="cursor: pointer;" class="down"></a></span><a style="cursor: pointer;" class="a1 bjk">编辑框</a><span class="error1"></span>
				</li>
				<li class="ver">
					<span class="xh">2</span><input type="text" name="choiceQuestionDtos[1].choiceQuestion.topic" class="ipt wh1 required"/><span class="fn"><a style="cursor: pointer;" class="add"></a><a style="cursor: pointer;" class="remove"></a><a style="cursor: pointer;" class="up"></a><a style="cursor: pointer;" class="down"></a></span><a style="cursor: pointer;" class="a1 bjk">编辑框</a><span class="error1"></span>
				</li>
			</c:when>
			<c:otherwise>
				<c:forEach var="choice" items="${dto.choiceQuestionDtos}" varStatus="s">
					<li class="ver">
						<span class="xh">1</span>
						<input type="text" name="choiceQuestionDtos[${s.index}].choiceQuestion.topic" class="ipt wh1 required" value="${choice.choiceQuestion.topic}"/>
						<input type="hidden" name="choiceQuestionDtos[${s.index}].questionDto.question.id" value="${choice.questionDto.question.id}" class="hidid"/>
						<input type="hidden" name="choiceQuestionDtos[${s.index}].choiceQuestion.id" value="${choice.choiceQuestion.id}" class="hid"/>
						<span class="fn"><a style="cursor: pointer;" class="add"></a><a style="cursor: pointer;" class="remove"></a><a style="cursor: pointer;" class="up"></a><a style="cursor: pointer;" class="down"></a></span><a style="cursor: pointer;" class="a1 bjk">编辑框</a>
						<span class="error1"></span>
					</li>
				</c:forEach>
			</c:otherwise>
		</c:choose>
		</ul>
	</div>
	</div>
	</div>
	<div class="wp1">
	<div class="fml">
		<span class="tit"><em class="rd">*</em>备选答案描述：</span>
		<a style="cursor: pointer;" class="a1" onclick="pladdfm(1,1,$('#fmt1'))">批量添加描述</a>
	</div>
	<div class="fmt" id="fmt1">
	<div class="fmbx fmbx1 fmone fc">
		<ul>
		<c:choose>
			<c:when test="${empty (dto.choiceAnswers)}">
				<li class="hor">
					<span class="xh">A</span><input type="text" name="choiceAnswers[0].description" class="ipt wh1 required"/><span class="fn"><a style="cursor: pointer;" class="add"></a><a style="cursor: pointer;" class="remove"></a><a style="cursor: pointer;" class="up"></a><a style="cursor: pointer;" class="down"></a></span><a style="cursor: pointer;" class="a1 bjk">编辑框</a><span class="error1"></span>
				</li>
				<li class="hor">
					<span class="xh">B</span><input type="text" name="choiceAnswers[1].description" class="ipt wh1 required" /><span class="fn"><a style="cursor: pointer;" class="add"></a><a style="cursor: pointer;" class="remove"></a><a style="cursor: pointer;" class="up"></a><a style="cursor: pointer;" class="down"></a></span><a style="cursor: pointer;" class="a1 bjk">编辑框</a><span class="error1"></span>
				</li>
			</c:when>
			<c:otherwise>
				<c:forEach var="answer" items="${dto.choiceAnswers}" varStatus="s">
					<li class="hor">
						<span class="xh"></span><input type="text" name="choiceAnswers[${s.index}].description" class="ipt wh1 required" value="${answer.description}"/><span class="fn"><a style="cursor: pointer;" class="add"></a><a style="cursor: pointer;" class="remove"></a><a style="cursor: pointer;" class="up"></a><a style="cursor: pointer;" class="down"></a></span><a style="cursor: pointer;" class="a1 bjk">编辑框</a><span class="error1"></span>
					</li>
				</c:forEach>
			</c:otherwise>
		</c:choose>
		</ul>
	</div>
	</div>
	</div>
	<div class="wp1">
		<div class="fml">
			<span class="tit"><em class="rd">*</em>参考答案：</span><span id="checkRadio" class="error1">请选择参考答案</span>
		</div>
		<table class="tab5 mb10" id="ckda">
			<c:choose>
			<c:when test="${empty (dto.choiceQuestionDtos)}">
				<tr><td class="dx_td0"></td><td class="dx_td1">A</td><td class="dx_td1">B</td></tr>
				<tr><td class="order">1</td><td><input type="checkbox" name="choiceQuestionDtos[0].choiceQuestion.answer" value="1"/></td><td><input type="checkbox" name="choiceQuestionDtos[0].choiceQuestion.answer" value="2"/></td></tr>
				<tr><td class="order">2</td><td><input type="checkbox" name="choiceQuestionDtos[1].choiceQuestion.answer" value="1"/></td><td><input type="checkbox" name="choiceQuestionDtos[1].choiceQuestion.answer" value="2"/></td></tr>
			</c:when>
			<c:otherwise>
				<tr>
					<td class="dx_td0"></td>
					<c:forEach var="answer" items="${dto.choiceAnswers}" varStatus="s">
						<td class="dx_td1"></td>
					</c:forEach>
				</tr>
				<c:forEach var="choice" items="${dto.choiceQuestionDtos}" varStatus="s">
					<tr>
						<td class="order">${s.index+1}</td>
						<c:forEach var="answer" items="${dto.choiceAnswers}" varStatus="as">
							<td><input type="checkbox" name="choiceQuestionDtos[${s.index}].choiceQuestion.answer" value="${as.index+1}" <c:if test="${choice.choiceAnswers[as.index].isright==1}">checked="checked"</c:if>/></td>
						</c:forEach>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
		</table>
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
		<input type="hidden" value="" name="questionDto.textarea_param"/>
		<a style="cursor: pointer;" class="btn1" id="submit">完&nbsp;&nbsp;成</a>
		<a style="cursor: pointer;" class="btn2 ml1" id="preview">预&nbsp;&nbsp;览</a>
	</div>
</div>
</form>
