<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<!DOCTYPE html>
<fe:html title="作业-复制作业" ie="true" initSeajs="true" 
assets="/project/b-ms-cloud/1.x/css/t-zy-bzzy/page.css">
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="zy"/>
</jsp:include>
<!-- 公共的头部 E-->
    <form id="save">
    	<input type="hidden" id="id" name="id" value="${task.id}"/>
    	<input type="hidden" id="status" name="status" value="1"/>
        <fieldset id="zyArea">
	    	<input type="hidden" name="paperId" value="${task.paperId}"/>
	    	<input type="hidden" name="examName" value="${task.examName}"/>
        </fieldset>
        <fieldset id="bjArea">
        </fieldset>
	<div class="i-main fc">
        <div class="p-box i-mgb25">
            <div class="green-tit">
                选择作业
                <!-- <a href="javascript:;" class="white-btn pdlr4">组题新作业</a> -->
            </div>
            <div class="p-box-ct">
            	<h2 id="jp-bzzy-h2" class="p-bzzy-h2">${task.examName}</h2>
                <p class="p-bzzy-p">
                    <a id="jp-chosepop" href="javascript:;"></a>
                    <span>从我的试卷选择试卷</span>
                </p>
            </div>
        </div>

        <div class="p-box i-mgb4">
            <div class="green-tit">
                选择班级
            </div>
            <div class="jp-classes-lst p-box-ct">
            </div>
        </div>

        <div class="p-box">
            <div class="p-box-ct">
                <em class="p-set-tim-tit">设置时间</em>
                <input type="text" value="<fmt:formatDate value="${task.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" name="startTime" class="jp-set-tim jp-set-tim-star p-set-tim-input" readonly placeholder="设置时间">
                <span class="p-set-tim-for">至</span>
                <input type="text" value="<fmt:formatDate value="${task.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" name="endTime" class="jp-set-tim jp-set-tim-end p-set-tim-input" readonly placeholder="截止时间">
            </div>
            <div class="btn-area">
            	<a href="javascript:;" class="jp-save-btn green-btn">保存</a>
            	<a href="javascript:;" class="jp-finish-btn orange-btn">完成布置</a>
            </div>
        </div>
	</div>
</form>
     <!-- 弹出页面 -->
    <div id="jp-pop-exam" class="p-pop-exam">
        <div class="p-popadd-page">
            <div class="p-addpop-box p-box">
                <button id="jp-close" class="p-close" href="javascript:;">×</button>
                <div id="paperData">
	                
	            </div>
            </div>
        </div>
        <div class="p-bg" id="jp-bg"></div>
    </div>

    <!-- 弹出页面 -->
    <div id="jp-pop-exam2" class="p-pop-exam">
        <div class="p-popadd-page" style="width:300px;height:200px;">
            <div class="jp-pop-ct"></div>
            <p class="p-sure">
                <a id="jp-surebtn2" class="green-btn green-btn-style" href="javascript:;">确定</a>
            </p>
        </div>
        <div class="p-bg" id="jp-bg2"></div>
    </div>
<jsp:include page="/footer.jsp"></jsp:include>
</body>
<form  name="searchPaperForm" id="searchPaperForm">
<input type="hidden" name="pageNo" id="pageNo" value="0"/>
</form>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-zy-bzzy/page': 'project/b-ms-cloud/1.x/js/t-zy-bzzy/page.js',
        'project/cms-mix/1.x/common/datetimepicker/datetimepicker':'project/cms-mix/1.x/common/datetimepicker/datetimepicker.js'
        }
    </fe:seaConfig>
<script type="text/javascript">
seajs.use(['project/b-ms-cloud/1.x/js/t-zy-bzzy/page'],function(app){
    app.init({
        api:{
            chooseWork:'/project/b-ms-cloud/1.x/json/t-zy-bzzy/chooseWork.php',
            chooseClass:'/teacher/task/mclass?taskId=${task.id}',
            finishLink:'/teacher/task/save',
            saveLink:'/teacher/task/save',
            saveLocation:'/teacher/task/list',
            changeLocationk:'/teacher/task/assign',
            okLocation:'/teacher/task/list'
        }
    });
});
</script>
<script type="text/javascript">
function searchFenye(pageNo){
	if(!pageNo){
		pageNo=0;
	}
	$("#pageNo").val(pageNo);
	$('#searchPaperForm').submit();
}

$('#searchPaperForm').submit(function(){
	$.ajax({
		url : '/teacher/task/paper' ,
		type : 'POST',
		async : false,
		data:$('#searchPaperForm').serialize(),
		success : function(data) {
			$("#paperData").html(data);
			$(".p-box .jp-work-list .for-checkbox").radios();
			$('body,html').animate({ scrollTop: 0 }, 200);
		} ,
		error:function(data){ }
	});
	// 为了防止普通浏览器进行表单提交和产生页面导航（防止页面刷新？）返回false
	return false;
});
</script>
</fe:html>

