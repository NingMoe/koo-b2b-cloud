<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://www.koolearn.com/taglib/koo" prefix="koo" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta charset="UTF-8">
		<title>题库-录题</title>
		<link rel="stylesheet" type="text/css" href="${ctx}/platform/c/c/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/platform/c/c/comm.css"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/platform/c/t/style.css"/>
		<script src="/js/examcore/question/question-header.js"></script> 
	</head>
	<body>
		<!-- 导航页头 -->
		<jsp:include page="q-header.jsp" />
		<div id="wrap" class="wrap">
			<div id="main" class="main">
				<div class="w cf">
					<!-- 题目录入页头 -->
					<jsp:include page="q-questionheader.jsp"/>
					<!------主框架部分 根据不同题目录入 增加录入模板-->
					<div id="q-question-body" class="q-question-body">
					<jsp:include page="q-question-body-include.jsp"/>
					</div>
					<!------主框架部分 E-->
				</div>
			</div>
			<!-- main end -->
		</div>
	<jsp:include page="../../common/footer.jsp" />	
	<script type="text/javascript" src="${ctx}/platform/j/c/jquery-1.8.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/platform/j/c/public.js"></script>
	<script type="text/javascript" src="${ctx}/platform/j/t/common.js"></script>
	<script type="text/javascript">
		jQuery(document).ready(function(){
			/** 显示处理 */
			function handShow(qts, flag){
				jQuery(".span_cur").each(function(index,ele){
					jQuery(ele).hide();
				});
				var cur = -1;
				if(null != qts && "" != qts && "null" != qts){
					var qtArr = qts.split(",");
					for (var int = 0; int < qtArr.length; int++) {
						jQuery("#t_"+qtArr[int]).show();
						if(int == 0){
							cur = qtArr[int];
						}
					}
				}
				/** 页面刷新 */
				if(cur > -1 && flag){
					goNew(cur);
				}
			}
			/** 获取对用题型 */
			function tag3(tag, flag){
				var url = "<%=basePath%>mc/tr";
				if(navigator.userAgent.indexOf("MSIE")>0){
					url = "/mc/tr";
				}
				var data = {"tag":tag};
				jQuery.post(url,data,function(result){
					handShow(result, flag);
				});
			}
			/** 首次进入处理 */
			tag3(jQuery("#header_tag3").val(), false);
			/** 事件绑定处理 */
			jQuery("#header_tag3").live("change",function(){
				var tag = jQuery(this).val();
				tag3(tag, true);
			});
		});
	</script>
	</body>
</html>