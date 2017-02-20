<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils"%>
<%@ page import="com.koolearn.cloud.login.entity.UserEntity" %>
<%@ page import="com.koolearn.sso.util.CookieUtil" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page import="com.koolearn.cloud.util.CacheTools" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    Cookie authCookie = CookieUtil.getCookie(request, GlobalConstant.AUTH_KEY);
    String authId = authCookie.getValue();
    UserEntity ue = CacheTools.getCache(authId, UserEntity.class);
    String userName = ue.getRealName();
    if( StringUtils.isBlank( userName )){
        ue.setRealName( "" );
    }
//	UserEntity ue = (UserEntity)request.getSession().getAttribute("loginUser");;
%>
<!-- 公共的头部 B-->
<div class="p-header">
    <div class="i-box">
    	<a class="p-lg fl" href="javascript:;">
            <img src="<%=PropertiesConfigUtils.getProperty("domains.ui") %>/project/b-ms-cloud/1.x/i/i-logo.png">
        </a>
        <b class="p-b fl">中小学教育云平台</b>
        <div class="p-personl fr">
            <span id="userName" title="<%=ue.getRealName()%>"> <b><%=ue.getRealName()%></b></span>
            <div class="p-show">
                <a href="/teacher/info/index" target="_bank">个人资料</a>
                <a href="/teacher/info/myClasses" target="_bank">我的班级</a>
                <a class="p-exit" href="/logout">退出</a>
            </div>
        </div>
        <%
            String createFrom=request.getParameter("createFrom");
            createFrom= StringUtils.isBlank(createFrom)?"1":createFrom;
        %>
        <div class="p-nav fr">
            <%if(ue.isFusui()){%>
                <a <%if("sy".equals(request.getParameter("nav"))){%>class="cur"<%}%> href="/teacher/choiceSubject/goFusuiHomePage" >首页</a>
            <%}else{%>
                <a <%if("sy".equals(request.getParameter("nav"))){%>class="cur"<%}%> href="/teacher/choiceSubject/goClasssHomePage" >首页</a>
            <%}%>
            <a <%if("fzkt".equals(request.getParameter("nav"))){%>class="cur"<%}%>href="/teacher/classRoom/index" >翻转课堂</a>
            <a <%if("zy".equals(request.getParameter("nav"))){%>class="cur"<%}%>href="/teacher/task/list">作业</a>
            <a <%if("tk".equals(request.getParameter("nav"))){%>class="cur"<%}%>href="/teacher/exam/core/question/jdd?createFrom=<%=createFrom%>" >题库</a>
            <a <%if("zyk".equals(request.getParameter("nav"))){%>class="cur"<%}%>href="/teacher/resource/index" >资源库</a>
            <a <%if("xxsk".equals(request.getParameter("nav"))){%>class="cur"<%}%>href="/teacher/listenCourse/index" >小学说课</a>
            <%
                if((Boolean)request.getAttribute("compositionFlag")){
            %>
                    <%--<a <%if("zwpg".equals(request.getParameter("nav"))){%>class="cur"<%}%> href="/composition/compositionList" >作文批改</a>--%>
            <%
                }
            %>

        </div>
    </div>
</div>
<!-- 公共的头部 E-->

