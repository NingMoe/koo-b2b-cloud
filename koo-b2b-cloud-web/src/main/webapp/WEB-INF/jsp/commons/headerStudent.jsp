<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils"%>
<%@ page import="com.koolearn.cloud.login.entity.UserEntity" %>
<%@ page import="com.koolearn.sso.util.CookieUtil" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page import="com.koolearn.cloud.util.CacheTools" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.koolearn.cloud.util.hexin.SHA1Util" %>
<%
    Cookie authCookie = CookieUtil.getCookie(request, GlobalConstant.AUTH_KEY);
    String authId = authCookie.getValue();
    UserEntity ue = CacheTools.getCache(authId, UserEntity.class);
    String userName = ue.getRealName();
    if( StringUtils.isBlank( userName )){
        ue.setRealName( "" );
    }
    Map<String,String> paramMap= SHA1Util.getSignature(ue);//获取合心登录认证信息
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
            <span id="userName" title="<%=ue.getRealName()%>"><%=ue.getRealName()%></span>
            <div class="p-show">
                <a href="/student/info/index" target="_bank">个人资料</a>
                <a class="p-exit" href="/logout">退出</a>
            </div>
        </div>
        <%
            String createFrom=request.getParameter("createFrom");
            createFrom= StringUtils.isBlank(createFrom)?"1":createFrom;
        %>
        <div class="p-nav fr">
            <a <%if("sy".equals(request.getParameter("nav"))){%>class="cur"<%}%> href="/student/allSubject/findAllSubjectExam" >首页</a>
            <a <%if("fzkt".equals(request.getParameter("nav"))){%>class="cur"<%}%>href="/student/classRoom/index" >课堂</a>
            <a <%if("zy".equals(request.getParameter("nav"))){%>class="cur"<%}%>href="/student/pc/index">作业</a>
            <a <%if("ctb".equals(request.getParameter("nav"))){%>class="cur"<%}%>href="/student/test/errornote" >错题本</a>
            <a <%if("bj".equals(request.getParameter("nav"))){%>class="cur"<%}%>href="/student/classHome/goStuClassHome" >班级</a>
            <%if(ue.isFusui()){%>
                <a  href="javascript:;"  onclick="tohexin();">考试中心</a>
            <%}%>
        </div>
    </div>
</div>
<%if(ue.isFusui()&&paramMap!=null){%>
<form action=""  method="post" name="hexinForm" id="hexinForm" target="_blank">
    <input type="hidden" name="uid"  value="<%=ue.getUuid()%>"/>
    <input type="hidden" name="redirect" id="redirect" value="/"/>
    <input type="hidden" name="access_key" value="<%=paramMap.get(SHA1Util.access_key)%>"/>
    <input type="hidden" name="nonce" value="<%=paramMap.get(SHA1Util.nonce)%>"/>
    <input type="hidden" name="timestamp" value="<%=paramMap.get(SHA1Util.timestamp)%>"/>
    <input type="hidden" name="signature" value="<%=paramMap.get(SHA1Util.signature)%>"/>
</form>
<%}%>
<script>
    function tohexin(){
        $("#hexinForm").attr('action','http://kaoyue.koolearn.com/api/open/login');
        document.getElementById('hexinForm').submit();
    }
</script>