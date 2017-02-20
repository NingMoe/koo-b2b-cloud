<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="kl" uri="kl_security" %>
<!-- 公共的头部 B-->
<div class="p-header">
    <div class="i-box">
        <a class="p-lg fl" href="#" target="_bank">
            <img src="<%=PropertiesConfigUtils.getProperty("domains.ui") %>/project/b-ms-cloud/1.x/i/i-logo.png">
        </a>
        <b class="p-b fl">中小学教育云平台</b>
        <div class="p-personl fr">
            <span><b>${user.securityUser.description}</b><i></i></span>
            <div class="p-show">
                <a class="p-exit" href="/logout">退出</a>
            </div>
        </div>
        <div class="p-nav fr">

            <kl:view url="">
                <a <%if("sy".equals(request.getParameter("nav"))){%>class="cur"<%}%> href="/operation/core/index">首页</a>
            </kl:view>
            <%--<kl:view url="">--%>
                <%--<a <%if("sjtj".equals(request.getParameter("nav"))){%>class="cur"<%}%> href="/operation/core/schoolDictionary">数据统计</a>--%>
            <%--</kl:view>--%>
            <kl:view url="/operation/core/resource/index">
                <a <%if("zy".equals(request.getParameter("nav"))){%>class="cur"<%}%> href="/operation/core/resource/index">资源</a>
            </kl:view>
            <%--<kl:view url="">--%>
                <%--<a <%if("xx".equals(request.getParameter("nav"))){%>class="cur"<%}%> target="_bank">学校</a>--%>
            <%--</kl:view>--%>
            <%--<kl:view url="">--%>
                <%--<%if("dls".equals(request.getParameter("nav"))){%>class="cur"<%}%> target="_bank">代理商</a>--%>
            <%--</kl:view>--%>
            <kl:view url="/operation/core/schoolDictionary">
                <a <%if("sjzd".equals(request.getParameter("nav"))){%>class="cur"<%}%> href="/operation/core/schoolDictionary">数据字典</a>
            </kl:view>
            <%--<kl:view url="">--%>
                <%--<a <%if("xtgl".equals(request.getParameter("nav"))){%>class="cur"<%}%> target="_bank">系统管理</a>--%>
            <%--</kl:view>--%>
        </div>
    </div>
</div>
<!-- 公共的头部 E-->


