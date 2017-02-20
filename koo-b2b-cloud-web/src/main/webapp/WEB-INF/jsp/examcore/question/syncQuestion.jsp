<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript" src="/js/jquery-1.8.2.min.js"></script>
<body>
    <div>系统：${dataSync.cloudPlatform}</div>
    <div>开始同步时间:${dataSync.startSynctime}</div>
    <div>同步试卷更新时间:${dataSync.currentSynctime}</div>
    <div>试卷成功个数：${dataSync.successPaperNum}</div>
    <div>试卷失败个数：${dataSync.errorPaperNum}</div>
    <div>题目成功个数：${dataSync.successQuestionNum}</div>
    <div>题目失败个数：${dataSync.errorQuestionNum}</div>
    <c:forEach items="${dataSync.syncInfoList}" var="info"  varStatus="status">
        <div>${info}</div>
    </c:forEach>
<c:if test="${dataSync.errorPaper!=null&& fn:length(dataSync.errorPaper)>0}">
   </br> <span>================失败的试卷！====================</span></br>
</c:if>
 <div class="errlist">
<c:forEach items="${dataSync.errorPaper}" var="p"  varStatus="status">
    <div >
        <div class="errorTitle">${p.errorCode}</div>
        <div class="errorInfo" style="display: none">${p.errorInfo}</div>
    </div>
</c:forEach>
    </div>
<c:if test="${dataSync.errorQuestion!=null && fn:length(dataSync.errorQuestion)>0 }">
    </br> <span>================失败的题目！====================</span></br>
 </c:if>
 <div class="errlist">
<c:forEach items="${dataSync.errorQuestion}" var="q"  varStatus="status">
    <div >
        <div class="errorTitle">${q.errorCode}</div>
        <div class="errorInfo" style="display: none">${q.errorInfo}</div>
    </div>
</c:forEach>
</div>
<div>
<%--下面信息需要在单独打开--%>
<c:forEach items="${dataSync.syncPapgeCodeCountMap}" var="map" varStatus="stauts">
    试卷编码： ${map.key}
    试卷信息：
    <c:forEach items="${map.value}" var="v" varStatus="status">
          编码${v.code}-->试卷id${v.id}-->状态${v.status}
    </c:forEach>
    </br>
    </br>
    </br>
    </br>
</c:forEach>
</div>
</body>
<script>
    $(".errlist").on("click",".errorTitle",function(){
           $(".errorInfo").hide();
            $(this).siblings().show();;
    });
    setTimeout(function () {   // 定时循环：setInterval 定时：setTimeout
        location.href="/exam/core/syncQuestion";
    }, 2000);
</script>