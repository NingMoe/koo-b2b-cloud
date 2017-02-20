<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://www.koolearn.com/taglib/koo" prefix="koo" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>题库-(试卷选题)</title>
		<link rel="stylesheet" href="${ctx}/platform/c/c/reset.css"/>
		<link rel="stylesheet" href="${ctx}/platform/c/c/comm.css"/>
		<link rel="stylesheet" href="${ctx}/platform/c/t/style.css"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/platform/c/t/t.css"/>
	</head>
	<body>
		<div id="wrap" class="wrap">
			<!-- header start -->
			<jsp:include page="../q-header.jsp"/>
			<!-- header end -->
			<!-- main start -->
			<div id="main" class="main">
				<div class="w cf">
					<!-- page_tit start -->
					<div class="page_tit">
						<h2>题库</h2>
					</div>
					<!-- page_tit end -->
					<!------------------------------主框架部分 B-->
					<div class="stud_content">
						<div class="refer_wrap fc pos_r">
							<p class="refer_menu">
								<a href="/question/base/questionList">
								     题型选题
								</a>
								<a href="/question/base/questionListPaper" class="current">
								     试卷选题
								</a>
							</p>
							<a class="a_cur" href="/question/base/toAddIndex" >手动录题</a>
						</div>
						<form  name="searchQuestionPaperForm" id="searchQuestionPaperForm" action="${ctx }/question/base/questionListData"  enctype="multipart/form-data">
					       <input type="hidden" name="pageNo" id="pageNo" value="0"/>
					       <!-- 标识题型选题还是试卷选题，初始化模版 -->
					       <input type="hidden" name="questionOrPaperTypeSelect" id="questionOrPaperTypeSelect" value="2"/>
					       <input type="hidden" name="topCategoryId" id="topCategoryId" value="${paperQueryDto.topCategoryId==null?'':paperQueryDto.topCategoryId }"/>
					       <input type="hidden" name="isfrom" id="isfrom" value="${paperQueryDto.isfrom==null?0:paperQueryDto.isfrom }"/>
					       <input type="hidden" name="orderBy" id="orderBy" value="${ empty (paperQueryDto.orderBy)?3:paperQueryDto.orderBy }"/>
							<p class="wrong_txt fc mt28" id="tag1P">
								<label>试卷分类</label>
								<span>
								<a class="current" href="javascript:;" id="">全部</a>
								<c:forEach items="${tags1}" var="tag" varStatus="status">
								<a href="javascript:;" id="${tag.id }">${tag.name }</a>
								</c:forEach>
								</span>
							</p> 
							<p class="wrong_txt fc" id="isfromP">
								<label>试题来源</label>
								<span>
									<a class="current" href="javascript:;" id="0">全部</a>
									<a href="javascript:;" id="1">新东方</a>
									<a href="javascript:;" id="2">我的上传</a>
									<a href="javascript:;" id="3">本校共享</a>
								</span>
							</p>
							<div class="choose_cont sort" id="orderByP">
								<span id="1"><i></i><em>最新</em></span>
								<span id="2"><i></i><em >浏览量</em></span>
							</div>
					</form>
					<div class="choose_box fc">
						<div class="" id="examList">
							 <!-- 加载题库 --> 
						</div>
					</div>
					</div>
					<!------------------------------主框架部分 E-->
				</div>
			</div>
	<!-- main end -->
</div>
<script>
$(document).ready(function(){
	initQuestionData();
});
function initQuestionData(){
	//1.初始页面查询条件
    $("#tag1").val($("#tag1P a[class='current']").attr("id"));
    var isfromId=$("#isfromP span[class='current']").attr('id');
    $("#isfrom").val(isfromId==null?0:isfromId);
    if($("#orderByP span i").hasClass("cur")){
	    $("#orderBy").val($(this).attr("id"));
	}else{
		//默认3 试卷编码排序
		$("#orderBy").val(3);
	}
	//2.加载三级分类及加载试题
    searchFenye(0);
}
$("#tag1P a").live("click", function(){
	$(this).siblings().removeClass("current").end().addClass("current");
    //$("#tag1").val($(this).attr("id"));
    //试卷分类 tag1
    $("#topCategoryId").val($(this).attr("id"));
    searchFenye(0);
});
$("#orderByP span").live("click", function(){
	if($(this).find("i").hasClass("cur")){
	    $("#orderBy").val($(this).attr("id"));
	}else{
		$("#orderBy").val(3);
	}
    searchFenye(0);
});
$("#isfromP span").live("click", function(){
	var isfromId=$("#isfromP span a[class='current']").attr('id');
    $("#isfrom").val(isfromId==null?0:isfromId);
    searchFenye(0);
});
 
function searchFenye(pageNo){
	if(!pageNo){
		pageNo=0;
	}
	 $("#pageNo").val(pageNo)
	 $('#searchQuestionPaperForm').submit();
}
$('#searchQuestionPaperForm').submit(function(){ 
    var options = {
        url : '${ctx }/question/base/questionListPaperData' ,
        contentType : "multipart/form-data; charset=utf-8",
        type : 'POST',
        success : function(data) {
        	$("#examList").html(data);
        } ,
        error:function(data){ }
        
    };
    // 提交表单
    $(this).ajaxSubmit(options);
    // 为了防止普通浏览器进行表单提交和产生页面导航（防止页面刷新？）返回false
    return false;
});
//试卷预览，更新浏览量  
$(".paperQuestionView").live("click",function(){
	var $this = $(this);
	var paperId = $(this).data("paperid");
	if(paperId==null||paperId==null){
		pop_alert('温馨提示', '试卷数据缺失！');
		return ;
	}
	$.ajax({
		  url: "${ctx }/question/base/paperHot",
		  async:false,
		  data : {"paperId":paperId},
		  success:function(data) {
			  data = JSON.parse(data);
				if(data.success==true){
					$this.parent().parent().find(".no_bd").empty().append(data.messageInfo);
				}
		    $("#paperQuestionViewA_"+paperId).click();
		  },
		  error:function(){
			  pop_alert('温馨提示', '系统错误！');
		  }
	});
	
})
</script>
<!-- footer start -->
	<jsp:include page="../../../common/footer.jsp" />
	<!-- footer end -->
	</body>
</html>
<script type="text/javascript" src="${ctx}/platform/j/c/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="${ctx}/platform/j/c/public.js"></script>
<script type="text/javascript" src="${ctx}/platform/j/t/common.js"></script>
 <script src="${ctx}/js/jquery.form.js"></script>
 <script type="text/javascript" src="${ctx}/platform/j/t/jquery.cookie.js"></script>
<script type="text/javascript" src="${ctx}/platform/j/t/json2.js"></script>
