<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
	<head>
		<%-- <base href="<%=basePath%>"> --%>
		
		<title>
			<c:if test="${examType == etE}">
				我的考试 -- 答案解析
			</c:if>
			<c:if test="${examType == etL}">
				随堂测评 -- 答案解析
			</c:if>
			<c:if test="${examType == etP}">
				在线练习 -- 答案解析
			</c:if>
		</title>
		
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<%-- CSS文件引用开始 --%>
		<jsp:include page="CssCite.jsp" />
		<%-- CSS文件引用结束 --%>
		<style type="text/css">
			.header {
				margin-bottom: 20px;
			}
			
			
			/*试题卡浮层*/
			.jsanal{min-height:800px;position: relative;padding-top:24px;}
			.jsanal .error_wrap,.jsanal .w255{margin-top:0;}
			.jstx2{float: left; margin-left: 18px;width: 255px;background:#fff;box-sizing: border-box;-webkit-box-sizing: border-box;-moz-box-sizing: border-box;-webkit-transition: background 500ms ease-out ;-moz-transition: background 500ms ease-out ;-o-transition: background 500ms ease-out ;transition: background 500ms ease-out ;z-index:100;}
			.autofix_sb.fixed2.bottom {bottom:20px; position: absolute !important;}
			.autofix_sb.fixed2{position: fixed !important;left: auto;}
			.jstx2 .w255{position: relative;}
			.jstx2 .types_atop{ background: url(<%=request.getContextPath()%>/platform/i/t/footer_1.jpg) no-repeat center center;bottom: 0;display: block;height: 39px;position: absolute;right: -39px;width: 39px;}
		</style>
	</head>
	
	<body>
		<a id="flagTop" name="flagTop"></a>
		<%-- 页面头部部分开始 --%>
		<%-- <jsp:include page="StudentHeader.jsp" /> --%>
		<jsp:include page="../common/student/header.jsp" />
		<%-- 页面头部部分结束 --%>
	
	
		<%-- 页面主体内容部分开始 --%>
		<div class="stud_content">
			<h2 class="h2_o">答案解析</h2>
			<div class="analysis fc jsanal">
				<div class="error_wrap fc w920">
					<div class="error_l">
						<c:out value="${resultHtml}" escapeXml="false"></c:out>
					</div>
				</div>
				<%-- 答题卡部分开始 --%>
				<div class="jstx2">
					<div class="w255">
						<div class="paper_card">
							<h4 class="pc_litle">试题卡</h4>
							<p class="pc_spans fc">
								<span>试题数：<c:out value="${countQs}" escapeXml="true"></c:out>题</span>
								<span>错题数：<c:out value="${err}" escapeXml="true"></c:out>题</span>
							</p>
							<p class="pc_spans fc">
								<span>未答数：<c:out value="${nop}" escapeXml="true"></c:out>题</span>
								<span>总得分：<c:out value="${score}" escapeXml="true"></c:out>分</span>
							</p>
							<p class="pc_is fc">
								<span><i class="i1"></i><b>正确题</b></span>
								<span><i class="i2"></i><b>错误题</b></span>
							</p>
							<p class="pc_is fc">
								<span><i class="i3"></i><b>未答题</b></span>
							</p>
						</div>
						<div class="card_as">
							<c:out value="${cardHtml}" escapeXml="false"></c:out>
						</div>
						<a id="hui_top" class="types_atop" href="#flagTop"></a>
					</div>
				</div>
				<%-- 答题卡部分结束 --%>
			</div>
		</div>
		<%-- 页面主体内容部分结束 --%>
		
		
					
		<%-- 页面底部开始 --%>
		<jsp:include page="Footer.jsp" />
		<%-- 页面底部结束 --%>
	</body>
	<script type="text/javascript" src="<%=basePath%>js/examProcess/jquery-1.8.2.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/examProcess/1.9.1-jquery-ui.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/examProcess/qt.js"></script>
	<script type="text/javascript" src="<%=basePath%>jPlayer/js/jquery.jplayer.min.js"></script>
	<script type="text/javascript">
			$(document).ready( function() {
				$(".jstx2").autofix_anything();
			});
			
			
			
			/*试题卡黏贴顶部 2015-04-29*/
			!function($){
			  var defaults = {
			    customOffset: false,
			    manual: false,
			    onlyInContainer: true
				};
			  $.fn.autofix_anything = function(options){
			    var settings = $.extend({}, defaults, options),
			        el = $(this),
			        curpos = el.position(),
			        offset = settings.customOffset,
			        pos = el.offset();
			    el.addClass("autofix_sb"); 
			    fixAll = function(el, settings, curpos, pos){
			      if (settings.customOffset == false){ 
			    	  offset =$(".jsanal").offset().top;
			      }
			      if($(document).scrollTop() > offset && $(document).scrollTop() <= ($(".jsanal").height() + (offset - $(window).height())))  {
			    	    var wind = $(window).width()/2;
			    	  el.removeClass("bottom").addClass("fixed2").css({
			            top: 0,
			            left:wind+322,
			            bottom: "auto"
			          });
			      } else {
			        if($(document).scrollTop() > offset) {
			          if (settings.onlyInContainer == true ) {
			            if($(document).scrollTop() > ($(".jsanal").height() - $(window).height())) {
			              el.addClass("bottom fixed2").removeAttr( 'style' ).css({
			               left: curpos.left,
			            	  /*right: 4,*/
			              });
			            }else {
			              el.removeClass("bottom fixed2").removeAttr( 'style' );
			            }
			          }
			        } else {
			          el.removeClass("bottom fixed2").removeAttr( 'style' );
			        }
			      }
			    }
			    if (settings.manual == false) {
			      $(window).scroll(function() {
			        fixAll(el, settings, curpos, pos)
			      });
			    }
			  }
			}(window.jQuery);
			
			
	</script>
	
</html>
