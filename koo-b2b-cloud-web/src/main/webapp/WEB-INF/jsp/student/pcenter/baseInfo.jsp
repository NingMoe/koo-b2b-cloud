<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils"%>
<!DOCTYPE html>
<fe:html title="个人中心" ie="true" initSeajs="true" 
assets="/project/b-ms-cloud/1.x/css/t-personal/page.css">
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/headerStudent.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="sy"/>
</jsp:include>
<!-- 公共的头部 E-->
<div class="i-main">
    <h2 class="p-user-header">
        <span>个人中心</span>
    </h2>
    <div class="fc p-user">
        <div class="fl p-user-left">
            <p class="p-img">
            	<c:choose>
            		<c:when test="${empty person.ico}">
            			<img src="<%=PropertiesConfigUtils.getProperty("domains.ui") %>project/b-ms-cloud/1.x/i/u-def.png" alt="" width="90" height="90">
            		</c:when>
            		<c:otherwise>
            			<img src="${person.ico}" alt="" width="90" height="90">
            		</c:otherwise>
            	</c:choose>
            </p>
        </div>
        <div class="fr p-user-right">
            <a href="/student/info/modifyPage" class="p-user-edit">修改</a>
            <ul>
                <li>所在学校：${person.schoolName}</li>
                <li>姓名：${person.realName}</li>
                <li>手机：${person.mobile}</li>
                <li>邮箱：${person.email}</li>
            </ul>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">
    /*seajs.config({
        alias:{
            'project/b-ms-cloud/1.x/js/t-personal/page': 'project/b-ms-cloud/1.x/js/t-personal/page.js'
        }
    });
    seajs.use(['project/b-ms-cloud/1.x/js/t-personal/page'],function(app){
        app.init();
    });*/
</script>
</fe:html>
