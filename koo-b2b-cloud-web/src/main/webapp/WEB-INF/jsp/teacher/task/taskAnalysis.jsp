<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="com.koolearn.cloud.exam.examcore.util.QuestionUtil"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.template.TemplateFtl"%>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<!DOCTYPE html>
<fe:html title="作业-分析" ie="true" initSeajs="true" 
assets="/project/b-ms-cloud/1.x/css/t-zy-analysis/page.css,
/project/b-ms-cloud/1.x/css/t-fz-common/page.css,
/project/b-ms-cloud/1.x/js/common/echarts/echarts.js">
<body>
<c:if test="${empty source}">
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="zy"/>
</jsp:include>
<div class="i-main">
    <div class="i-zy-h fc">
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
        <div class="p-dd-two fr i-sel">
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
    <div class="p-zy-title fc">
        <a href="/teacher/task/situation?examId=${taskPager.examId}&classId=${taskPager.classId}">作业情况</a><em></em>
        <a href="javascript:;" class="s">作业分析</a>
        <a href="/teacher/task/comment?examId=${taskPager.examId}&classId=${taskPager.classId}&errRate=50">作业讲评</a>
    </div>
    </c:if>
    <c:if test="${source == 1}"> <%--课堂--%>
        <div class="i-zytitle fc">
            <div class="p-dd-two fr i-sel" id="jp-detail-down">
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
        <div class="p-sub" style="width: 950px">
            <a href="/teacher/task/situation?examId=${taskPager.examId}&classId=${taskPager.classId}&classRoomClassId=${classRoomClassId}&source=1">作业情况</a><em></em>
	        <a href="javascript:;" class="s">作业分析</a>
	        <a href="/teacher/task/comment?examId=${taskPager.examId}&classId=${taskPager.classId}&classRoomClassId=${classRoomClassId}&errRate=50&source=1">作业讲评</a>
        </div>
    </c:if>
    <form id="dataForm" method="post" action="/teacher/task/analysis">
        <input type="hidden" id="classRoomClassId" name="classRoomClassId" value="${classRoomClassId}">
        <input id="classId" name="classId" value="${taskPager.classId}" type="hidden"/>
        <input id="examId" name="examId" value="${taskPager.examId}" type="hidden"/>
        <input id="source" name="source" value="${source}" type="hidden"/>
    </form>
    <!--题目得分率-->
    <div class="p-zy-box">
        <div id="jp-subject"  class="p-box"></div>
    </div>
    <!--知识点得分率-->
    <div class="p-zy-box">
        <div id="jp-knowledge"  class="p-box"></div>
    </div>
    <!--成绩分布-->
    <div class="p-zy-box">
        <div id="jp-score"  class="p-box"></div>
    </div>
</div>
<c:if test="${empty source}">
    <jsp:include page="/footer.jsp"></jsp:include>
</c:if>
</body>
<%
Map map = (Map<String, String>)request.getAttribute("map");
//题目得分率
String scoreRateXData = (String)map.get("question_socre_rate_xData");
String scoreRateSData = (String)map.get("question_socre_rate_sData");
System.out.println(scoreRateXData);
if(StringUtils.isBlank(scoreRateXData)&&StringUtils.isBlank(scoreRateSData)){
	scoreRateXData = "";
	scoreRateSData = "";
}
request.setAttribute("scoreRateXData", scoreRateXData);
request.setAttribute("scoreRateSData", scoreRateSData);
//知识点得分率
String knowlegeXData = (String)map.get("knowlege_socre_rate_xData");
String knowlegeSData = (String)map.get("knowlege_socre_rate_sData");
if(StringUtils.isBlank(knowlegeXData)&&StringUtils.isBlank(knowlegeSData)){
	knowlegeXData = "";
	knowlegeSData = "";
}
request.setAttribute("knowlegeXData", knowlegeXData);
request.setAttribute("knowlegeSData", knowlegeSData);
//成绩分布 
String scoreXData = (String)map.get("score_spread_xData");
String scoreSData = (String)map.get("score_spread_sData");
if(StringUtils.isBlank(scoreXData)&&StringUtils.isBlank(scoreSData)){
	scoreXData = "";
	scoreSData = "";
}
request.setAttribute("scoreXData", scoreXData);
request.setAttribute("scoreSData", scoreSData);
%>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-zy-analysis/page': 'project/b-ms-cloud/1.x/js/t-zy-analysis/page.js'
        }
    </fe:seaConfig>
<script type="text/javascript">
    seajs.use(['project/b-ms-cloud/1.x/js/t-zy-analysis/page'],function(app){
        $(function(){
            app.init({
                hidInpt:"#classId",
                hidForm:"#dataForm",
                actionUrl:"/teacher/task/analysis"
            });

            //题目得分率
            var subJson = {
                'xData' : ${scoreRateXData},
                'sData' : ${scoreRateSData}
            };
            app.subjectShow(subJson);
            //知识点得分率
            var kJson = {
                'xData' : ${knowlegeXData},
                'sData' : ${knowlegeSData}
            };
            app.knowledgeShow(kJson);
            //分数分布
            var scJson = {
                'xData' : ${scoreXData},
                'sData' : ${scoreSData}
            };
            app.scoreShow(scJson);
        });
    });
</script>
</fe:html>

