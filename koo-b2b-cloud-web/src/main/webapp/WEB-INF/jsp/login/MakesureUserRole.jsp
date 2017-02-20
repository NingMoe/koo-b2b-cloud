<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<fe:html title="用户选择" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/t-fusui-user-choose/page.css">
<head>

    <script type="text/javascript">
        seajs.config({
            alias:{
                'project/b-ms-cloud/1.x/js/t-fusui-user-choose/page': 'project/b-ms-cloud/1.x/js/t-fusui-user-choose/page.js'
            }
        });
        seajs.use('project/b-ms-cloud/1.x/js/t-fusui-user-choose/page',function(init){
            init();
        });
    </script>
</head>
<body class="colfff">
<!-- 头部 -->
<div class="p-header">
    <div class="i-box">
        <a class="p-lg fl" href="#" target="_bank">
            <img src="<%=PropertiesConfigUtils.getProperty("domains.ui") %>/project/b-ms-cloud/1.x/i/i-logo.png">
        </a>
        <b class="p-b fl">中小学教育云平台</b>
    </div>
</div>
<!-- 内容部分 -->
<div class="i-main t-user-cont">
    <form id="jp-form" action="/addUserRoleAndGoHome" method="post">
        <input id="jp-userid" type="hidden" name="userId" value="${userId}">
        <input id="jp-roleid" type="hidden" name="roleId">
        <ul id="jp-select">
            <li class="p-teacher">
                <span class="p-user-teach"></span>
                <b>我是老师</b>
					<span class="p-select">
						<i></i>
					</span>
            </li>
            <li>
                <span class="p-user-stud"></span>
                <b>我是学生</b>
					<span class="p-select">
						<i></i>
					</span>
            </li>
        </ul>
        <p class="p-hint">
            选择身份：身份选择后将不能变更，请慎重选择自己的真实身份。
        </p>
        <p class="p-sure">
            <a id="jp-sure-btn" href="javascript:;">确定</a>
        </p>
    </form>

</div>
<fe:seaConfig>
    alias:{
    'project/b-ms-cloud/1.x/js/i-footer/page': 'project/b-ms-cloud/1.x/js/i-footer/page.js'
    }
</fe:seaConfig>
<script type="text/javascript">
    seajs.use('project/b-ms-cloud/1.x/js/i-footer/page',function(init){
       init();
    });
</script>
</body>
</fe:html>