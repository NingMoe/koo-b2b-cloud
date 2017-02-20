<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 右侧 -->
<div class="p-cont fr">
    <div class="p-browse" >
        <h2>
            ${ta.attachmentName}
            <c:if test="${sType != 'detail'}">
            <span style="float: right;  cursor: pointer" onclick="findComment(${ta.id})">查看讨论</span>
            </c:if>
        </h2>
        <div class="p-pvm">
            <!-- 预览 B-->
            <img id="jp-img" src="/data${resource.filePath}" style="display: block;margin:auto;max-width:640px;max-height:400px">
            <!-- 预览 E-->
        </div>
        <c:if test="${sType == 'detail'}">
            <jsp:include page="comment.jsp"/>
        </c:if>
    </div>
</div>
