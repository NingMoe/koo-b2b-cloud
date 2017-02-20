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
<fe:html title="作业-讲评" ie="true" initSeajs="true" 
assets="/project/b-ms-cloud/1.x/css/t-zy-comment/page.css,/project/b-ms-cloud/1.x/css/t-fz-common/page.css">
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
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
        <a href="/teacher/task/analysis?examId=${taskPager.examId}&classId=${taskPager.classId}">作业分析</a>
        <a href="javascript:;" class="s">作业讲评</a>
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
        <div class="p-sub">
            <a href="/teacher/task/situation?examId=${taskPager.examId}&classId=${taskPager.classId}&classRoomClassId=${classRoomClassId}&source=1" >作业情况</a>
            <a href="/teacher/task/analysis?examId=${taskPager.examId}&classId=${taskPager.classId}&classRoomClassId=${classRoomClassId}&source=1">作业分析</a>
            <a href="javascript:;" class="s">作业讲评</a>
        </div>
    </c:if>

    <div class="p-zy-cbox">
        <h2 class="fc">
            <div class="radio_area fl">
            <form id="dataForm" method="post" action="/teacher/task/comment">
                <input type="hidden" id="classRoomClassId" name="classRoomClassId" value="${classRoomClassId}">
				<input id="radioType" name="radioType" value="${radioType}" type="hidden"/><!-- 说明0.全部错题 1.错误率手动输入 -->
				<input id="classId" name="classId" value="${taskPager.classId}" type="hidden"/>
				<input id="examId" name="examId" value="${taskPager.examId}" type="hidden"/>
				<input id="source" name="source" value="${source}" type="hidden"/>
                <input type="radio" id="a" class="for-radio"/><span>全部错题</span>
                <input type="radio" id="b" class="for-radio"/><span>错题率大于<input type="text" value="${taskPager.errRate}" name="errRate">%</span>
            </form>
            </div>
            <%
            QuestionViewDto questionViewDto= new QuestionViewDto();
            questionViewDto.setButtonType(QuestionViewDto.button_ype_zuoye_jiangping);
            List<IExamQuestionDto> questionDtoList=(List<IExamQuestionDto>)request.getAttribute("questionDtoList");
                //下载暂时屏蔽
             boolean isDown=false;
            if(isDown&&questionDtoList!=null&&questionDtoList.size()>0){ %>
            <a href="javascript:downComment();" class="fr"><em></em>作业讲评</a>
            <%} %>
        </h2>
		<%
           int count=1;// 试卷题序
           if(questionDtoList!=null&&questionDtoList.size()>0){
        %>
       <div data-type =""  class="p-tbox">
            <%
                for(IExamQuestionDto questionDto:questionDtoList){
                    questionViewDto.setQuestionNo((count++)+"");
                    questionDto.getQuestionDto().getQuestion().getQuestionViewDto().setQuestionNo(questionViewDto.getQuestionNo());
                    System.out.println("========"+questionDto.getQuestionDto().getQuestion().getQuestionViewDto().getScore()+"  "+questionViewDto.getQuestionNo());
                    questionViewDto.setScore(questionDto.getQuestionDto().getQuestion().getQuestionViewDto().getScore());
                    request.setAttribute("questionViewDto", questionViewDto);
                    request.setAttribute("questionDto",questionDto);%>
                   <jsp:include page="/WEB-INF/jsp/examcore/question/questionSingleView.jsp">
                       <jsp:param name="questionViewDto" value="${questionViewDto}"/>
                       <jsp:param name="questionDto" value="${questionDto}"/>
                   </jsp:include>
             <% }  %>
        </div>
        <%}else{ %>
	        <div data-type =""  class="p-tbox">
	        	<span class="i-no-result">搜索无结果</span>
	        </div>
        <%} %>
    </div>
</div>
<c:if test="${empty source}">
    <jsp:include page="/footer.jsp"></jsp:include>
</c:if>
</body>
<script type="text/javascript">
function downComment(){
    $("#dataForm").attr("action","/teacher/task/commentPDF");
	$("#dataForm").submit();
}
$(function(){
    $(".jp-parsi").closest("[questiontypesx]").find(".oppClass").show();
})
</script>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-zy-comment/page': 'project/b-ms-cloud/1.x/js/t-zy-comment/page.js'
        }
    </fe:seaConfig>
<script type="text/javascript">
    seajs.use(['project/b-ms-cloud/1.x/js/t-zy-comment/page'],function(app){
        app.init({
            hidInpt:"#classId",//点击头部隐藏域的id
            hidForm:"#dataForm",//点击头部隐藏域的form表单的id
            actionUrl:"/teacher/task/comment"
        });
    });
</script>
</fe:html>

