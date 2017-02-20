<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.QuestionBarHtml" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>${examName}</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<%-- CSS文件引用开始 --%>
		<jsp:include page="CssCite.jsp" />
		<%-- CSS文件引用结束 --%>
		<script type="text/javascript">
			function soundUploadCallback(id,val){ // 口语题回调函数
				// 回调
				var valArr = val.split(",");
				if(valArr[0] == "0"){
					// 提交失败
					pop_alert("温馨提示", "录音提交失败，请重新提交！");
				}else{
					// 提交成功
					var _ur = jQuery("#hidUrKey").val();
					var _temp = tojson(window.localStorage.getItem(_ur));
					_temp[18+"_"+id] = valArr[1];
					window.localStorage.setItem(_ur, tostr(_temp));
					pop_alert("温馨提示", "录音提交成功！");
				}
			};
			function showMessage(message){
				pop_alert("温馨提示", "" + message);
			}
		</script>
	</head>  
	<body>
		<!-- 公共的头部 B-->
		<jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
		    <jsp:param name="user" value="user"/>
		    <jsp:param name="nav" value="zy"/>
		</jsp:include>
		<!-- 公共的头部 E-->

		<%-- 试卷展示部分开始 --%>
		<div class="stud_content mb150">
			<div class="error_wrap fc begin_exam">
				<div class="error_l">
					<c:out value="${resultHtml}" escapeXml="false"></c:out>
				</div>
			</div>
			<%
                    	QuestionBarHtml questionBarHtml = (QuestionBarHtml)request.getAttribute("questionBarHtml");
                    	out.println(questionBarHtml.getHtmlQuestionNum()); %>
		</div>
		<%-- 试卷展示部分结束 --%>

		<%-- 自动交卷遮罩层开始 --%>
		<div id="jsalert"  style="display: none;width:100%;height:100%;position: fixed;left:0;top: 0; z-index: 320;">
			<div class="jsalert">
				<p class="title fc">
					<span>温馨提示</span>
				</p>
				<div class="txt">
					考试时间到，正在为您自动提交答案！请不要关闭窗口！
				</div>
			</div>
			<div id="bgDiv" class="bgDiv" style="height: 1000px;"></div>
		</div>
		<%-- 自动交卷遮罩层结束 --%>

		<%-- 页面底部开始 --%>
		<%-- 页面底部结束 --%>
		<input type="text" id="subjectId" name="subjectId" value="${exam.subjectId}"/>
		<input type="hidden" id="hidBasePath" name="hidBasePath" value="<%=basePath%>"/>
		<input type="hidden" id="hidTime" name="hidTime" value="${time}" />
		<input type="hidden" id="hidMM" name="hidMM" value="${mm}" />
		<input type="hidden" id="hidSS" name="hidSS" value="${ss}" />
		<input type="hidden" id="hidExamType" name="hidExamType" value="${examType}" />
		<input type="hidden" id="hidExamId" name="hidExamId" value="${examId}" />
		<input type="hidden" id="hidUrKey" name="hidUrKey" value="${urKey}" />
		<input type="hidden" id="hidUserResult" name="hidUserResult" value='${userResult}' />
		<input type="hidden" id="hidResultId" name="hidResultId" value='${resultId}' />
	</body>
	<%-- JS文件引用开始 --%>
	<jsp:include page="JsCite.jsp" />
	<%-- JS文件引用结束 --%>
</html>
