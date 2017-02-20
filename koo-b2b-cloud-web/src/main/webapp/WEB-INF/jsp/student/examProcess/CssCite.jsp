<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!-- ================================================CSS引用开始================================================ -->
<link rel="stylesheet" type="text/css" href="${ctx}/css/examProcess/reset.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/examProcess/comm.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/examProcess/style.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/examProcess/qt.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/examProcess/zw.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/examcore/jPlayer/skin/blue.monday/jplayer.blue.monday.css" rel="stylesheet" type="text/css" />
<!-- ================================================CSS引用结束================================================ -->