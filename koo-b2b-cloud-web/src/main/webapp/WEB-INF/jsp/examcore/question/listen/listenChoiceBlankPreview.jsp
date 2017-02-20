<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.koolearn.cloud.exam.examcore.util.QuestionUtil"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.*"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.template.TemplateFtl"%>
<%@page import="java.util.*"%>
<%@ page import="com.koolearn.cloud.exam.examcore.question.entity.QuestionAttach" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="/css/kpt_qt.css" rel="stylesheet" type="text/css" />
<link href="/css/qt.css" rel="stylesheet" type="text/css" />
<link href="/jPlayer/skin/blue.monday/jplayer.blue.monday.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="http://images.koolearn.com/www/subject/script/jquery-1.4.2.min.js"></script>
<script src="/js/kpt_qt.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/1.9.1-jquery-ui.js"></script>
<script type="text/javascript" src="/jPlayer/js/jquery.jplayer.min.js"></script>
<script type="text/javascript" src="/js/examcore/question/qt.js"></script>


</head>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String imgPath = basePath + "/images";
ComplexQuestionDto dto = (ComplexQuestionDto)request.getAttribute("dto");
List<QuestionAttach> attachs = dto.getQuestionDto().getQuestionAttachs();
%>
<body>
<div class="tf_box">

    <div class="tf_main">
    	
    	<%if(dto.getQuestionDto().getQuestion().getQuestionTip()!=null && !"".equals(dto.getQuestionDto().getQuestion().getQuestionTip())){ %>
	    		<p class="music_title"><%= dto.getQuestionDto().getQuestion().getQuestionTip()%></p>
	    <%}
    	if(attachs != null && attachs.size()>0){
			for(QuestionAttach attach : attachs){%>
				<div class="xc_box">
					<div class="up_xz"><%=attach.getContent()%></div> 	
				</div>
			<%}
		}%>
    	
        <div class="xc_box">
        	  <div class="up_xz">
                        ${dto.complexQuestion.topic}
                  <div class="clear"></div>
              </div>
        </div>
        <!--弹框-->
        <div class="tk_ts">
        	 <img src="<%=imgPath%>/box_up_bg.png" />
             <div class="ts_box">
             	   <dl>
                   	   <dt><span>koolearn测试练习系统</span><img src="<%=imgPath%>/gbi_btn.jpg" /></dt>
                       <dd class="tk_title">同步练习：矩阵与数列（练习名称）</dd>
                       <dd class="tk_dtail">
                       		<p><span>答题时间：30分钟</span><span>已用时间：30分钟</span></p>
                            <p><span>总题数：10题</span><span>已答题：0题</span></p>
                       </dd>
                       <dd class="dt_ts"><span>练习不限次数，提交练习后可在需要时重新开始练习</span></dd>
                       <dd class="btn_box">
                       		<a href="javascript:;" class="conti_btn">
                            	<img src="<%=imgPath%>/btn_l_bg.jpg" />
                                <span>继续答题</span>
                                <img src="<%=imgPath%>/btn_r_bg.jpg" />
                            </a>
                            <a href="javascript:;" class="sub_btn">提交练习</a>
                       </dd>
                   </dl>
             </div>
             <img src="<%=imgPath%>/box_b_bg.png" />
        </div>
    </div>
</div>
<input type="hidden" id="keys" value="${keys}"/>
</body>
<script type="text/javascript">
    //<![CDATA[
	$(function(){
		var prefix = '<span class="btn_link"><img src="/images/l0_bg.jpg" /><em>';
		var suffix = '</em><img src="/images/r0_bg.jpg" /><img src="/images/duihao.jpg" class="duihao undis" /></span>';
		var keys = $('#keys').val();
		var keyArray = keys.split("##*");
		$('div[class="up_xz"]').find('span[class="tkbox"]').each(function(index,element){
			var options = keyArray[index].split("*#*");
			var option = '';
			for(var i=0;i<options.length;i++) {
				option += prefix + options[i] + suffix;
			}
			$(element).replaceWith("<i>"+ option +"</i>");
		});
		$(".up_xz p span[class='btn_link']").each(function(){
			this.btn = true;
		});
		$(".up_xz p span[class='btn_link']").hover(function(){
			if(this.btn==true){
				$(this).removeClass().addClass("btn_h");
				$(this).find("img").eq(0).attr("src","/images/l1_bg.jpg");
				$(this).find("img").eq(1).attr("src","/images/r1_bg.jpg");		
			}
			},function(){
		    if(this.btn==true){
				$(this).removeClass().addClass("btn_link");
				$(this).find("img").eq(0).attr("src","/images/l0_bg.jpg");
				$(this).find("img").eq(1).attr("src","/images/r0_bg.jpg");	
			}
		});
		$(".up_xz p span[class='btn_link']").click(function(){
			var aspan = $(this).parent().find("span");
			aspan.each(function(){
				this.btn = true;
				$(this).removeClass().addClass("btn_link");
				var aImg = $(this).find("img");
				aImg.eq(0).attr("src","/images/l0_bg.jpg");
				aImg.eq(1).attr("src","/images/r0_bg.jpg");	
			});
			$(this).removeClass().addClass("btn_act");
			$(this).find("img").eq(0).attr("src","/images/l2_bg.jpg");
			$(this).find("img").eq(1).attr("src","/images/r2_bg.jpg");	
			this.btn = false;
		});
		//样式调试。
		$(".jp-audio").css("overflow","hidden");
	});
	//]]>
</script>
</html>