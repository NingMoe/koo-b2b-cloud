<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<fe:html title="首页" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-home/page.css">


    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="zwpg"/>
    </jsp:include>
    <!-- 公共的头部 E-->
    <div class="i-main">

        <a href="/composition/compositionList">您有<font color="red">${count}</font> 篇新作文需要批改</a>

    </div>
    <jsp:include page="/footer.jsp"></jsp:include>
    </body>
    <script type="text/javascript">
        $(function (){

        });
    </script>
</fe:html>
