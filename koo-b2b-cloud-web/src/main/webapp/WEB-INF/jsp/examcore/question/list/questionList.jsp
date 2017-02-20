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
		<title>题库-(题型选题)</title>
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
								<a href="/question/base/questionList" class="current">
								     题型选题
								</a>
								<a href="/question/base/questionListPaper">
								     试卷选题
								</a>
							</p>
							<a class="a_cur" href="/question/base/toAddIndex" >手动录题</a>
						</div>
						<form  name="searchQuestionForm" id="searchQuestionForm" action="${ctx }/question/base/questionListData"  enctype="multipart/form-data">
					       <input type="hidden" name="pageNo" id="pageNo" value="0"/>
					       <input type="hidden" name="tag1" id="tag1" value="${listPager.tag1 }"/>
					       <!-- 标识题型选题还是试卷选题，初始化模版 -->
					       <input type="hidden" name="questionOrPaperTypeSelect" id="questionOrPaperTypeSelect" value="0"/>
					       <input type="hidden" name="tag2" id="tag2" value="${listPager.tag2 }"/>
					       <input type="hidden" name="tag3" id="tag3" value="${listPager.tag3 }"/>
					       <input type="hidden" name="questionSource" id="questionSource" value="${listPager.questionSource }"/>
					       <input type="hidden" name="filterType" id="filterType" value="${listPager.filterType }"/>
							<p class="wrong_txt fc mt28" id="tag1P">
								<label>试卷分类</label>
								<span>
									 <c:forEach items="${tags1}" var="tag" varStatus="status">
										  <c:choose>
									         <c:when test="${status.first}">
									              <a class="current" href="javascript:;" id=${tag.id }>${tag.name }</a>
									          </c:when>
									          <c:otherwise>
									               <a href="javascript:;" id=${tag.id }>${tag.name }</a>
									          </c:otherwise>
									     </c:choose>
									</c:forEach>
								</span>
							</p> 
							<p class="wrong_txt fc" id="tag2P">
								<label>题型分类</label>
								<span id="tag2Data"> 
									<c:forEach items="${tags2}" var="tag"  varStatus="status">
									    <c:choose>
									         <c:when test="${status.first}">
									              <a class="current" href="javascript:;" id=${tag.id }>${tag.name }</a>
									          </c:when>
									          <c:otherwise>
									               <a href="javascript:;" id=${tag.id }>${tag.name }</a>
									          </c:otherwise>
									     </c:choose>
									</c:forEach>
								</span>
							</p>
							<p class="wrong_txt fc" id="tag3P">
								<label>题目名称</label>
								<span id="tag3Data"> 
									<c:forEach items="${tags3}" var="tag"  varStatus="status">
									    <c:choose>
									         <c:when test="${status.first}">
									              <a class="current" href="javascript:;" id=${tag.id }>${tag.name }</a>
									          </c:when>
									          <c:otherwise>
									               <a href="javascript:;" id=${tag.id }>${tag.name }</a>
									          </c:otherwise>
									     </c:choose>
									</c:forEach>
								</span>
							</p>
							<p class="wrong_txt fc" id="questionSourceP">
								<label>试题来源</label>
								<span>
									<a class="current" href="javascript:;" id="0">全部</a>
									<a href="javascript:;" id="1">新东方</a>
									<a href="javascript:;" id="2">我的上传</a>
									<a href="javascript:;" id="3">本校共享</a>
								</span>
							</p>
							<div class="choose_cont fc" id="filterTypeP">
								<span class="" id="1"></span><b>过滤使用过的试题</b>
								<span id="2"></span><b>只选收藏的试题</b>
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
	initQuestionData()
});
function initQuestionData(){
	//1.初始页面查询条件
    $("#tag1").val($("#tag1P a[class='current']").attr("id"));
    $("#tag2").val($("#tag2P a[class='current']").attr("id"));
    $("#tag3").val($("#tag3P a[class='current']").attr("id"));
    $("#questionSource").val($("#questionSourceP a[class='current']").attr("id"));
    var filterTypeId=$("#filterTypeP span[class='current']").attr('id');
    $("#filterType").val(filterTypeId==null?-1:filterTypeId);
	//2.加载三级分类及加载试题
    searchFenye(0);
}
$("#tag1P a").live("click", function(){
	$(this).siblings().removeClass("current").end().addClass("current");
    $("#tag1").val($(this).attr("id"));
    tag1Data();
});
$("#tag2P a").live("click", function(){
    $(this).siblings().removeClass("current").end().addClass("current");
    $("#tag2").val($(this).attr("id"));
    tag2Data();
});
$("#tag3P a").live("click", function(){
    $(this).siblings().removeClass("current").end().addClass("current");
    $("#tag3").val($(this).attr("id"));
    searchFenye(0);
});
$("#questionSourceP a").live("click", function(){
    $(this).siblings().removeClass("current").end().addClass("current");
    $("#questionSource").val($(this).attr("id"));
    searchFenye(0);
}); 
$("#filterTypeP span").live("click", function(){
	var filterTypeId=$("#filterTypeP span[class='current']").attr('id');
    $("#filterType").val(filterTypeId==null?-1:filterTypeId);
    searchFenye(0);
});
/*过滤选题--避免引用common.js和examjs冲突*/
$('.choose_cont span').click(function(){
	$('.choose_cont span').not($(this)).removeClass('current');
	$(this).toggleClass("current");
}) 
function searchFenye(pageNo){
	if(!pageNo){
		pageNo=0;
	}
	 $("#pageNo").val(pageNo)
	 $('#searchQuestionForm').submit();
}
function tag1Data(){
	queryTag("tag1","tag2");
	queryTag("tag2","tag3");
	searchFenye(0);
}
function tag2Data(){
	queryTag("tag2","tag3");
	searchFenye(0);
}
function tag3Data(){
	searchFenye(0);
}
function queryTag(tag1,tag2){
	var tagId= $("#"+tag1).val( );
	$.ajax({
		  url: "${ctx }/question/base/questionTag",
		  async:false,
		  data : {"tagId":tagId,tag_style:2},
		  success:function(data) {
		    $("#"+tag2+"Data").html(data);
		    var tag2Id=$("#"+tag2+"P a[class='current']").attr("id")
		    $("#"+tag2).val(tag2Id);
		  }
	});
}
$('#searchQuestionForm').submit(function(){ 
    var options = {
        url : '${ctx }/question/base/questionListData' ,
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
//收藏
function shoucang(id){
	//collectType:1 收藏题目id ,2收藏题目code
	var ajaxData = {
			"questionCode":id,
			"collectType":2
		}
	var url = '${ctx}/collection/insert';
	$.ajax({
		  data : ajaxData,
		  url: url,
		  async:false,
		  cache : false,
		  type : 'post',
		  //dataType : 'json',
		  success:function(data) {
			  if(data==0){
				  pop_alert('温馨提示', '试题已收藏！');
			  }else if(data>0){
				  pop_alert('温馨提示', '收藏成功！');
			  }
		  },
		  error:function(data){
			  pop_alert('错误', '请求失败，请稍后再试！');
		  }
	});
}
//题目审核
$(".questionVerify").live("click",function(){
	
});
function questionVerify(qid,status,qcode,th){
	var $th = $(th);
	var ajaxData = {
			"ids":qid,
			"status":status
		}
	var url = '${ctx}/question/base/updateStatus';
	$.ajax({
		  data : ajaxData,
		  url: url,
		  async:false,
		  cache : false,
		  type : 'post',
		  dataType : 'json',
		  success:function(data) {
			  if(data.success){
				  //不刷页面
				  $th.attr("onclick","");
				  $th.attr("href","javascript:;");
				  $th.text("已审核");
				  $th.addClass("curr2");
				  var shcan = "<a href=\"javascript:;\" onclick=\"shoucang(\'"+qcode+"\');\">收藏</a>"
				  $th.parent().append(shcan);
			  	  pop_alert('温馨提示',  "审核成功",2000);
			     
			      setTimeout("$('#searchQuestionForm').submit()",2000); 
			  }else{
				  pop_alert('温馨提示', '请求失败，请稍后再试！');
			  }
		  },
		  error:function(data){
			  pop_alert('错误', '请求失败，请稍后再试！');
		  }
	});
}
//题目作废
function questionCancel(qid,status){
	pop_confirm('id','温馨提示','作废后将不能恢复，确定执行？',function(){
		questionUpdateStatus(qid,status);
	});
}
//题目删除
function questionDelete(qid){
	pop_confirm('id','温馨提示','确定删除？',function(){
		var ajaxData = {
				"ids":qid
			}
			var url = '${ctx}/question/base/deleteQuestion';
			questionAjax(ajaxData,url);
	});
}
//题目：状态变更，（审核，作废）
function questionUpdateStatus(qid,status,msg){
	var ajaxData = {
		"ids":qid,
		"status":status
	}
	var url = '${ctx}/question/base/updateStatus';
	questionAjax(ajaxData,url,msg);
}
function questionAjax(ajaxData,url,msg){
	$.ajax({
		  data : ajaxData,
		  url: url,
		  async:false,
		  cache : false,
		  type : 'post',
		  dataType : 'json',
		  success:function(data) {
			  if(data.success){
				  if(msg!=null){
				  	pop_alert('温馨提示', msg+"成功");
				  }
				  searchFenye(0);
			  }else{
				  pop_alert('温馨提示', '请求失败，请稍后再试！');
			  }
		  },
		  error:function(data){
			  pop_alert('错误', '请求失败，请稍后再试！');
		  }
	});
}
function questionView($this){
	var anAl=$this.parent('span.sps_5').parent().next('div.anal_box');
	if(anAl.is(':hidden')){
		$('span.sps_5 a.show_div_click').removeClass('curr');
		$('div.anal_box').hide();
		$this.addClass('curr');
		anAl.show();
	}else{
		$this.removeClass('curr');
		anAl.hide();
	}	
}
//预览题目
$('a.show_div_click').live("click",function(){
	var $this = $(this);
	if($this.hasClass("curr")){
		questionView($this);
		return ;
	}
	var questionId = $this.data('questionid');
	var questionTypeId = $this.data('questiontypeid');
	var url = "${ctx}/question/view/preview";
	var ajaxData = {
			"questionId":questionId,
			"questionTypeId":questionTypeId
		}
	var showflag=$("#show_"+questionId).attr("showflag");
	if(showflag==0){
		$("#qshowDiv_"+questionId).attr("src","/question/view/preview?questionId="+questionId+"&questionTypeId="+questionTypeId);
		$("#show_"+questionId).attr("showflag",1);
	}
	questionView($this);
} )
function IFrameResize(myframe)
{
    var iframe = document.getElementById(myframe); 
    var iframeDocument = null;
    //safari和chrome都是webkit内核的浏览器，但是webkit可以,chrome不可以
    if (iframe.contentDocument)
    { 
        //ie 8,ff,opera,safari
        iframeDocument = iframe.contentDocument;
    } 
    else if (iframe.contentWindow) 
    { 
        // for IE, 6 and 7:
        iframeDocument = iframe.contentWindow.document;
    } 
    if (!!iframeDocument) {
        iframe.height=iframeDocument.documentElement.scrollHeight+10+"px";     
    } else {
        alert("this browser doesn't seem to support the iframe document object");
    } 
 
}


function shareQuestion(questionExtID){
	
	var content="共享后将会在学生自主练习、老师组卷选题可见，确定执行吗？";
	pop_confirm(0, '温馨提示', content, function() {
		$.ajax({
			url:"/question/base/shareQuestion",
			async:false,
			cache:false,
			dataType:'text',
			data:{"questionExtId":questionExtID},
			success:function(data){
				if(data>0){
					console.log("success");
				}
				
				$('#searchQuestionForm').submit();
			},
			error:function(XHR, textStatus, errorThrown){
				pop_alert('温馨提示', '请求超时，请稍后再试！');
	        }
		});
	}, function() {
		
	});
}

</script>
<!-- footer start -->
	<jsp:include page="../../../common/footer.jsp" />
	<!-- footer end -->
	</body>
</html>
<script type="text/javascript" src="${ctx}/platform/j/c/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="${ctx}/platform/j/c/public.js"></script>
<!--
<script type="text/javascript" src="${ctx}/platform/j/t/common.js"></script>
-->
 <script src="${ctx}/js/jquery.form.js"></script>
 <script type="text/javascript" src="${ctx}/platform/j/t/jquery.cookie.js"></script>
<script type="text/javascript" src="${ctx}/platform/j/t/json2.js"></script>
<script type="text/javascript" src="http://images.koolearn.com/www/subject/script/jquery-ui.min.js"></script>
