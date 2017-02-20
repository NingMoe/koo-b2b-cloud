<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="com.koolearn.cloud.exam.examcore.util.QuestionUtil"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.template.TemplateFtl"%>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<!DOCTYPE html>
<fe:html title="作业-个人作业详情" ie="true" initSeajs="true" 
assets="/project/b-ms-cloud/1.x/css/t-zy-perdetail/page.css">
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<!DOCTYPE html> 
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="zy"/>
</jsp:include>
<!-- 公共的头部 E-->
	<div class="i-main">
		<div class="i-box i-zytitle fc">
			<h1 class="fl" title="${task.examName}">${task.examName}
	        	<b>开始时间：<fmt:formatDate value="${task.startTime}" pattern="yyyy.MM.dd"/> ／ 截止时间：<fmt:formatDate value="${task.endTime}" pattern="yyyy.MM.dd"/> ／ 
	        		 <c:if test="${task.finishStatus == 1}">
	                    <c:if test="${task.status == 1}">
	                        未发布
	                    </c:if>
	                    <c:if test="${task.status == 2}">
	                        已撤回
	                    </c:if>
	                    <c:if test="${task.status == 4}">
	                        进行中
	                    </c:if>
	                </c:if>
	                <c:if test="${task.finishStatus == 0}">
	                    已结束
	                </c:if>
	        	</b>
        	</h1>
			<div class="p-dd-two fr i-sel" id="jp-detail-down" tt="${clist[0].fullName}">
				<span class="jp-subje" data-id="${clist[0].id}" data-name="${clist[0].fullName}">
					<b>${clist[0].fullName}</b>
					<i class="p-i-l"></i>
					<i class="p-i-r"></i>
				</span>
				<div class="jp-subje-div">
	            	<c:forEach items="${clist}" var="c" varStatus="stat">
	            		<c:choose>
	            			<c:when test="${stat.first&&taskPager.classId==0}" >
	            				<a data-id="${c.id}" class="cur" href="javascript:;">${c.fullName}</a>
	            			</c:when>
	            			<c:when test="${taskPager.classId==c.id}" >
	            				<a data-id="${c.id}" class="cur" href="javascript:;">${c.fullName}</a>
	            			</c:when>
	            			<c:otherwise>
	            				<a data-id="${c.id}" href="javascript:;">${c.fullName}</a>
	            			</c:otherwise>
	            		</c:choose>
	            	</c:forEach>
	            </div>
			</div>
		</div>
		<div class="p-prdet fc">
			<!-- 左侧 -->
			<form id="dataForm" method="post">
				<input id="ptype" name="ptype" value="${ptype}" type="hidden"/>
				<input id="classId" name="classId" value="${taskPager.classId}" type="hidden"/>
				<input id="examId" name="examId" value="${taskPager.examId}" type="hidden"/>
				<input id="userId" name="userId" value="${taskPager.userId}" type="hidden"/>
				<input id="curStudentId" name="curStudentId" value="${taskPager.userId}" type="hidden"/>
			<div class="p-side fl jp-side-float">
				<p class="p-search-p fc">
						<input id="jp-keyWord" class="fl" type="text" placeholder="输入关键词" value="${keyWord}" name="keyWord" maxlength="">
					<a class="fr" href="javascript:;" id="jp-searchbtn"></a>
				</p>
				<div class="p-names jp-names">
					<span class="spn-top jp-top"></span>
					<div class="p-divs">
						<div class="jp-as" id="jp-as">
							<!-- 学生列表 -->
						</div>
					</div>
					<span class="spn-btm jp-btm"></span>
				</div>
			</div>
			</form>
			<!-- 右侧 -->
			<div class="p-cont fr p-cont-perde  ji-pcont">
				<!-- 错题筛选 -->
				<div class="radio_area p-scree jp-radio">
					<input type="radio" name="radioType" id="a" class="for-radio" value="e"><span>只看错题</span>
			        <input type="radio" name="radioType" id="b" class="for-radio" value="p" checked="checked"><span>查看全部</span>
<%--			        <a class="orange-btn jp-corre" href="javascript:;">批阅作业</a>--%>
				</div>
				<!-- 试题模板 -->
				<div class="p-pract" id="showPaper">
				
				</div>
			</div>
			<!-- 右侧--无结果显示 -->
			<div class="p-cont p-results jp-noresults fr" style="display:none">
				<div class="i-no-result">搜索无结果</div>
			</div>
		</div>
	</div>
	<!-- 作业评语 -->
	<dl class="p-comments">
		<dt>作业评语</dt>
		<dd>
			<textarea id="jp-reply" readonly="readonly">我是评语</textarea>
		</dd>
	</dl>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
	<fe:seaConfig>
		alias:{
		'project/b-ms-cloud/1.x/js/t-zy-perdetail/page': 'project/b-ms-cloud/1.x/js/t-zy-perdetail/page.js',
		'project/b-ms-cloud/1.x/js/t-zy-correcting/page': 'project/b-ms-cloud/1.x/js/t-zy-correcting/page.js'
		}
	</fe:seaConfig>
<!-- 作业批阅提交表单 -->
<script type="text/javascript">
	seajs.use('project/b-ms-cloud/1.x/js/t-zy-perdetail/page',function(init){
		init({
			hidInpt:"#classId",//点击头部隐藏域的id
			hidForm:"#dataForm",//点击头部隐藏域的form表单的id
			actionUrl:"/teacher/task/mark",
			detailUrl:"/teacher/task/personStation",
			asideAjaxUrl:"/teacher/task/searchStudent",//学生列表
			curAsUrl:"/teacher/task/personResult_",
			wrongUrl:"/teacher/task/seeWrong_"
		});
	});
	seajs.use('project/b-ms-cloud/1.x/js/t-zy-correcting/page',function(init){
		init();
	});
</script>
</fe:html>

