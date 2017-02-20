<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.koolearn.cloud.exam.examcore.question.entity.Question" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript" src="/js/jquery-1.8.2.min.js"></script>
<body>
<c:if test="${ empty dataSync}" >请开始重建......</c:if>
<c:if test="${not empty dataSync}" >
重建时间：${dataSync.currentRebuildtime}</br>
重建结束时间：${dataSync.currentRebuildEndtime}</br>
当前重建页数：${dataSync.currentPage}</br>
当前重建条数：${dataSync.rebuilderCount}</br>
<c:if test="${dataSync.errorResourceList!=null && fn:length(dataSync.errorResourceList)>0 }">
    </br> <span>================失败 ！====================</span></br>
 </c:if>
 <div class="errlist">
<c:forEach items="${dataSync.errorResourceList}" var="q"  varStatus="status">
    <div >
        <div class="errorTitle">${q.id}${q.name}</div>
    </div>
</c:forEach>
</div>
</c:if>
</body>
<script>
    $(".errlist").on("click",".errorTitle",function(){
           $(".errorInfo").hide();
            $(this).siblings().show();;
    });

    setTimeout(function () {   // 定时循环：setInterval 定时：setTimeout
        location.href="/exam/core/rebuildResource";
    }, 2000);
</script>