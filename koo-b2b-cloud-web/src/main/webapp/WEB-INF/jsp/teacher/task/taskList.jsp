<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<!DOCTYPE html>
<fe:html title="作业-列表" ie="true" initSeajs="true" 
assets="/project/b-ms-cloud/1.x/css/t-fz/page.css">
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="zy"/>
</jsp:include>
<!-- 公共的头部 E-->

<div class="i-main fc">
    <div class="p-hjg">
        <form  name="searchTaskForm" id="searchTaskForm">
        <input type="hidden" name="pageNo" id="pageNo" value="0"/>
            <select id="sel-show" style="display: none;" name="subjectId">
            	<option value="-1">全部</option>
            	<c:forEach items="${subjectList}" var="sub" varStatus="stat">
       				<option id="${stat.index+1}e" value="${sub.id}">${sub.name}</option>
                </c:forEach>
            </select>
            <div class="p-search-p fc">
                <input class="fl" type="text" id="keyWord" name="keyWord" placeholder="请输入作业关键词">
                <a class="jp-search-btn fr" href="javascript:searchFenye(0);"></a>
            </div>
        </form>
    </div>

    <div class="p-make-zy">
        
    </div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</body>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-zy/page': 'project/b-ms-cloud/1.x/js/t-zy/page.js'
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
	$("#pageNo").val(pageNo);
	var $keyWord = $('#keyWord');
	if($keyWord.val() == $keyWord.attr("placeholder")){
		$keyWord.val('');
	}
	$('#searchTaskForm').submit();
}

$('#searchTaskForm').submit(function(){
	$.ajax({
		url : '/teacher/task/dataList' ,
		type : 'POST',
		data:$('#searchTaskForm').serialize(),
		success : function(data) {
			$(".p-make-zy").html(data);
			window._ifhtml && window._ifhtml.ifhtmlheight();
			$('body,html').animate({ scrollTop: 0 }, 200);
		} ,
		error:function(data){ }
	});
	// 为了防止普通浏览器进行表单提交和产生页面导航（防止页面刷新？）返回false
	return false;
});


seajs.use(['project/b-ms-cloud/1.x/js/t-zy/page'],function(app){
    app.init({
        api:{
            removeList:'/teacher/task/delete',
            recall:'/teacher/task/revoke',
            copys:'/teacher/task/copy?id={id}'
        }
    });
});
</script>
</fe:html>

