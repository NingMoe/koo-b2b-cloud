
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<h1>${resource.name}</h1>
<p class="p-source">课件大小：${resource.storageSize}K
    ／ 更新时间：<fmt:formatDate value="${resource.optTime}" pattern="yyyy-MM-dd" /> ／
    来 源：<c:if test="${resource.source != 1}">${resource.userStr}</c:if>
    <c:if test="${resource.source == 1}">新东方教育云</c:if> ／ 使用次数：${resource.useTimes}
</p>
<p class="p-klge">
    <c:if test="${resource.bookVersionNames != null}">
        教学进度点：${resource.bookVersionNames}
    </c:if>
</p>
<p class="p-klge">
    <c:if test="${resource.knowledgeSingleName != null}">
        知识点：${resource.knowledgeSingleName}
    </c:if>
</p>