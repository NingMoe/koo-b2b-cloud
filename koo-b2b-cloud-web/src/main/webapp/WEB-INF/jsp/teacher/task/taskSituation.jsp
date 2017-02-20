<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<!DOCTYPE html>
<fe:html title="作业-情况" ie="true" initSeajs="true" 
assets="/project/b-ms-cloud/1.x/css/t-zy-situation/page.css,/project/b-ms-cloud/1.x/css/t-fz-common/page.css">
<body>
<c:if test="${empty source}">
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="zy"/>
</jsp:include>
<!-- 公共的头部 E-->
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
        <div class="p-dd-two fr i-sel" tt="${clist[0].className}">
			<span class="jp-subje" data-id="${clist[0].id}" data-name="${clist[0].fullName}">
				<b>${clist[0].fullName}</b>
				<i class="p-i-l"></i>
				<i class="p-i-r"></i>
			</span>
            <div class="jp-subje-div">
                <c:forEach items="${clist}" var="c" varStatus="stat">
                    <c:choose>
                        <c:when test="${stat.first&&classId==0}" >
                            <a data-id="${c.id}" class="cur" href="javascript:;">${c.fullName}</a>
                        </c:when>
                        <c:when test="${classId==c.id}" >
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
        <a href="javascript:;" class="s">作业情况</a>
        <a href="/teacher/task/analysis?examId=${taskPager.examId}&classId=${taskPager.classId}" >作业分析</a><em></em>
        <a href="/teacher/task/comment?examId=${taskPager.examId}&classId=${taskPager.classId}&errRate=50" >作业讲评</a>
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
            <a href="javascript:;" class="s">作业情况</a>
            <a href="/teacher/task/analysis?examId=${taskPager.examId}&classId=${taskPager.classId}&classRoomClassId=${classRoomClassId}&source=1">作业分析</a>
            <a href="/teacher/task/comment?examId=${examId}&classId=${classId}&classRoomClassId=${classRoomClassId}&errRate=50&source=1">作业讲评</a>
        </div>
    </c:if>
    <div class="p-zy-box">
        <table>
            <thead>
                <tr>
                    <td width="5%">排名</td>
                    <td width="10%">姓名</td>
                    <td width="10%">学号</td>
                    <td width="10%">得分率</td>
                    <td width="5%">分数</td>
                    <td width="20%">完成时间</td>
                    <td width="30%">评语</td>
                    <td width="10%">操作</td>
                </tr>
            </thead>
            <tbody>
            <!-- 此处显示交卷成功的用户  状态: status 0,未开始 1.考试中    2.已交卷-->
                <c:forEach items="${userResultList}" var="u">
            		<tr>
	                    <td>${u.rank}</td>
	                    <td><a href="javascript:personStation(${u.studentId});">${u.realName}</a></td>
	                    <td>${u.studentCode}</td>
	                    <td>${u.rate}</td>
	                    <td><fmt:formatNumber value="${u.score}" type="number" pattern="#.#" /></td>
	                    <td><fmt:formatDate value="${u.completeTime}" pattern="yyyy.MM.dd HH:mm:ss"/></td>
	                    <td class="p-left">
	                        <span class="p-txt" title="${u.reply}">${u.reply}</span>
                        </td>
	                    <td>
	                    	<!-- '是否批阅 0.未批阅 1.已批阅' -->  
	                    	<c:choose>
	                    		<c:when test="${u.isreply==1}">
	                    			<a class="p-read">已批阅</a>
	                    		</c:when>
	                    		<c:otherwise>
                                        <a href="javascript:mark(${u.studentId});" class="p-check">批阅</a>
	                    		</c:otherwise>
	                    	</c:choose>
	                    </td>
	                </tr>
            	</c:forEach>
            	<c:forEach items="${userNoResultList}" var="u">
            		<tr>
	                    <td>${u.rank}</td>
	                    <td>${u.realName}</td>
	                    <td>${u.studentCode}</td>
	                    <td>${u.rate}</td>
	                    <td>${u.score}</td>
	                    <td><fmt:formatDate value="${u.completeTime}" pattern="yyyy.MM.dd HH:mm:ss"/></td>
	                    <td class="p-left" title="${u.reply}">${u.reply}</td>
	                    <td></td>
	                </tr>
            	</c:forEach>
            </tbody>
        </table>
    </div>
</div>
<c:if test="${empty source}">
    <jsp:include page="/footer.jsp"></jsp:include>
</c:if>
</body>

<!-- 作业批阅提交表单 -->

    <form id="mark" method="post" action="/teacher/task/situation">
        <input type="hidden" id="classRoomClassId" name="classRoomClassId" value="${classRoomClassId}">
	<input id="classId" name="classId" value="${classId}" type="hidden"/>
	<input id="examId" name="examId" value="${examId}" type="hidden"/>
	<input id="userId" name="userId" value="" type="hidden"/>
	<input id="ptype" name="ptype" value="p" type="hidden"/>
    <input id="source" name="source" value="${source}" type="hidden"/>
</form>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-zy-situation/page': 'project/b-ms-cloud/1.x/js/t-zy-situation/page.js'
        }
    </fe:seaConfig>
<script type="text/javascript">
function mark(userId){
    var sourceVal = $('#source').val();
    if(sourceVal == 1){
        $("#mark").attr('target','_blank');
    }
	$("#userId").val(userId);
	$("#mark").attr("action","/teacher/task/mark");  
	$("#mark").submit();
}

function personStation(userId){
    var sourceVal = $('#source').val();
	$("#userId").val(userId);
    if(sourceVal == 1){
        $("#mark").attr('target','_blank');
    }
	$("#mark").attr("action","/teacher/task/personStation");  
	$("#mark").submit();
}

seajs.use(['project/b-ms-cloud/1.x/js/t-zy-situation/page'],function(app){
    app.init({
    	hidInpt:"#classId",//点击头部隐藏域的id
        hidForm:"#mark",
        actionUrl:"/teacher/task/situation"
    });
});
</script>

</fe:html>
