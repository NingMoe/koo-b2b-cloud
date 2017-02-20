<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils"%>
<%@ page import="com.koolearn.cloud.login.entity.UserEntity" %>
<%@ page import="com.koolearn.sso.util.CookieUtil" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page import="com.koolearn.cloud.util.CacheTools" %>
<%@ page import="com.koolearn.cloud.school.entity.Manager" %>
<%@ page import="com.koolearn.cloud.school.CommonConstant" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    Cookie authCookie = CookieUtil.getCookie(request, CommonConstant.AUTH_KEY);
    String userName = "";
    if( null != authCookie ){
        String authId = authCookie.getValue();
        Manager manager = CacheTools.getCache(authId, Manager.class);
        userName = manager.getManagerName();
        if( StringUtils.isBlank( userName )){
            manager.setManagerName( "" );
        }
    }
//	UserEntity ue = (UserEntity)request.getSession().getAttribute("loginUser");;
%>
<!-- 公共的头部 B-->
<div class="p-header">
    <div class="i-box">
        <a class="p-lg fl" href="javascript:;">
            <img src=" /project/b-ms-cloud/1.x/i/i-logo.png">
        </a>
        <b class="p-b fl">中小学教育云平台</b>
        <div class="p-personl fr">
            <span id="userName" title="<%=userName%>"> <b><%=userName%></b></span>
            <div class="p-show">
                <a href="/school/personal/goPersonal" target="_bank">个人中心</a>
                <a class="p-exit" href="/logout">退出</a>
            </div>
        </div>
        <div class="p-nav fr">
            <a <%if("sy".equals(request.getParameter("nav"))){%>class="cur"<%}%> href="/school/homePage/goHomePage" >首页</a>
            <a <%if("bj".equals(request.getParameter("nav"))){%>class="cur"<%}%>href="/school/classes/goClassesManage" >班级</a>
            <a <%if("ls".equals(request.getParameter("nav"))){%>class="cur"<%}%>href="/school/teacher/goTeacherManage">老师</a>
            <a <%if("xs".equals(request.getParameter("nav"))){%>class="cur"<%}%>href="/school/student/goStudentManage" >学生</a>
            <a <%if("xx".equals(request.getParameter("nav"))){%>class="cur"<%}%>href="/school/manage/goSchoolManage" >学校</a>
            <a <%if("tj".equals(request.getParameter("nav"))){%>class="cur"<%}%>href="/school/bi/index" >数据统计</a>
            <a <%if("yh".equals(request.getParameter("nav"))){%>class="cur"<%}%>href="/school/authority/goAuthorityManage" >用户管理</a>
        </div>
    </div>
</div>
<!-- 公共的头部 E-->

