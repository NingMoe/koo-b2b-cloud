<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<iframe id="examIframe" class="jp-iframe-style" frameborder="no" src="/student/pc/start_${examId}_${studentId}?urlType=1&classRoomId=${tpExamId}" width="1000" height="600" style="border:0;margin-bottom:-20px"/>
<!-- 预览 -->

<p class="p-ass">
    <jsp:include page="page.jsp"/>
</p>
