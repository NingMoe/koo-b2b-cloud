<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="p-class-paly fc">

    <div class="p-side fl">
        <div class="p-play">
            <img id="jp-img" src="/data${resource.filePath}" style="display: block;margin:auto;max-width:640px;max-height:400px" >
        </div>
        <p class="p-ass">
            <jsp:include page="page.jsp"/>
        </p>
    </div>
    <div class="p-cont fr">
        <jsp:include page="learning.jsp"/>
    </div>
</div>
<jsp:include page="comment.jsp"/>
<script>
</script>
