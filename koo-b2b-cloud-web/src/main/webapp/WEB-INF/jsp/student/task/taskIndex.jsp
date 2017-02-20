<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<!DOCTYPE html>
<fe:html title="作业" ie="true" initSeajs="true" 
assets="/project/b-ms-cloud/1.x/css/t-hw-homework/page.css">
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/headerStudent.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="zy"/>
</jsp:include>
<!-- 公共的头部 E-->
<div class="i-main p-courvare">
    <div class="p-contvare fc">
        <!-- 左侧 -->
        <div class="p-side fl">
            <ul class="i-sidevare" id="jp-sidevare">
            	<c:forEach items="${subjectList}" var="val" varStatus="stat">
            		<li class="
            		<c:choose>
	            		<c:when test="${val.subjectId==subjectId}">
	            			cur 
	            		</c:when>
	            		<c:when test="${stat.first&&empty subjectId}">
	            			cur
	            		</c:when>
	            	</c:choose>
	            		<c:if test="${val.tagStatus==1}">
	             			p-never-selected 
	            		</c:if>">
	                    <a href="javascript:changeSubject(${val.subjectId});"><span>${val.subjectName}<i></i></span></a>
	                    <em></em>
	                </li>
            	</c:forEach>
            </ul>
        </div>
        <!-- 右侧 -->
        <div class="p-cont fr">
            <div class="p-cont-inner">
                <div class="p-search-box">
                    <a href="javascript:;" onclick="gotoZc();" class="orange-btn">组题自测</a>
                    <form action="/student/pc/index" id="dataForm" method="post">
                    <input class="p-set-tim-input p-endtime jp-key" type="text" name="endTime" placeholder="作业截止日期">
                    <div class="p-search-p">
                        	<input type="hidden" name="pageNo" id="pageNo" value="${pageNo}"/>
                        	<input type="hidden" name="subjectId" id="subjectId" value="${subjectId}"/>
                            <input class="keytxt jp-key" type="text" name="keyWord" placeholder="输入关键词" value="${taskDto.examName}">
                            <input type="button" class="keybtn" value="">
                    </div>
                    </form>
                </div>
                <div class="p-cont-search-list">
                    <ul id="studenttask">
                    	<!--  学生作业列表数据 -->
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/footer.jsp"/>
</body>
<script type="text/javascript" src="/js/task.js"></script>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-hw-homework/page': 'project/b-ms-cloud/1.x/js/t-hw-homework/page.js'
        }
    </fe:seaConfig>
<script type="text/javascript">
$(document).ready(function(){
	searchFenye(0);
});

function searchFenye(pageNo){
	if(!pageNo){
		pageNo=0;
	}
	//ie8
    if($(".jp-key").val() == $(".jp-key").attr("placeholder")){
        $(".jp-key").val('');
    }
	$("#pageNo").val(pageNo);
	$('#dataForm').submit();
}

$('#dataForm').submit(function(){
	$.ajax({
		url : '/student/pc/indexData' ,
		type : 'POST',
		data:$('#dataForm').serialize(),
		success : function(data) {
			$("#studenttask").html(data);
			window._ifhtml && window._ifhtml.ifhtmlheight();
			$('body,html').animate({ scrollTop: 0 }, 200);
		} ,
		error:function(data){ }
	});
	// 为了防止普通浏览器进行表单提交和产生页面导航（防止页面刷新？）返回false
	return false;
});

function changeSubject(subjectId){
	//console.info(subjectId);
	$("#subjectId").val(subjectId);
	$("#pageNo").val(0);
	//console.info($("#subjectId").val());
	$("#dataForm").submit();
}

seajs.use('project/b-ms-cloud/1.x/js/t-hw-homework/page',function(app){
    app.init();
});
    function gotoZc(){
        location.href='/student/test/self?subjectId='+$("#subjectId").val();
    }
</script>
</fe:html>
