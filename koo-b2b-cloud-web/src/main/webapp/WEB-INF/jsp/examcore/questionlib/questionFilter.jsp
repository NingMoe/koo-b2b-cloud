<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!--题型、难度和过滤条件-->
<dl class="p-search-dl jp-search">
    <dt>题型</dt>
    <dd  id="jp-as">
        <a class="white-btn-tab on" href="javascript:;" data-tx="0">全部</a>
    </dd>
    <dt>难度</dt>
    <dd>
        <a class="white-btn-tab on" href="javascript:;" data-nd="0">全部</a>
        <c:forEach items="${questionHard}" var="tag"  varStatus="status">
            <a class="white-btn-tab" href="javascript:;" data-nd="${tag.id}">${tag.name}</a>
        </c:forEach>
    </dd>
</dl>
<p class="p-screen radio_area jp-search-label">
    <input type="radio" id="" class="for-radio" data-sc="0"><b>全部</b>
    <input type="radio" id="" class="for-radio" data-sc="<%=GlobalConstant.QUESTION_FILTER_USERED_NOT%>"><b>过滤使用过的</b>
    <input type="radio" id="" class="for-radio" data-sc="<%=GlobalConstant.QUESTION_FILTER_COLLECTION%>"><b>只选收藏的</b>
    <input type="radio" id="" class="for-radio" data-sc="<%=GlobalConstant.QUESTION_FILTER_USERED%>"><b>只选使用过的</b>
</p>