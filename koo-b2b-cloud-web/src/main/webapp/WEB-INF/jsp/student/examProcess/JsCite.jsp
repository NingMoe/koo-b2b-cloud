<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<!-- %
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String staticDomain=PropertiesConfigUtils.getProperty(Constant.STATIC_DOMAIN_KEY);
staticDomain=StringUtils.isBlank(staticDomain)?"":staticDomain;
request.setAttribute("ctx",staticDomain+request.getContextPath());


%> -->
	<!-- ================================================JS引用开始================================================ -->
	<script type="text/javascript" src="${ctx}/js/examProcess/jquery-1.8.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/examProcess/1.9.1-jquery-ui.js"></script>
	<script type="text/javascript" src="${ctx}/js/examProcess/public.js"></script>
	<script type="text/javascript" src="${ctx}/js/examProcess/s.js"></script>
	<script type="text/javascript" src="${ctx}/js/examProcess/process.js"></script>
	<script type="text/javascript" src="${ctx}/js/examProcess/rangy-core.js"></script>
	<script type="text/javascript" src="${ctx}/js/examProcess/zw.js"></script>
	<%--<script type="text/javascript" src="${ctx}/js/examProcess/qt.js"></script>--%>
	<script type="text/javascript" src="${ctx}/examcore/jPlayer/js/jquery.jplayer.min.js"></script>
	<!-- ================================================JS引用结束================================================ -->