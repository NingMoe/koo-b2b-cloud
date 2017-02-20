<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page language="java" import="com.koolearn.framework.common.utils.PropertiesConfigUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
    //预览
    var baseDomain = '',
            pcreadPath = '/js/reader/',
            htmlTemplate = '/js/reader/pcreadUI.html',
            pageViewerConfig = {
                IMGFiles: "/data${resource.fileConverPath}/index_{page}.png",
                JSONFile: "/data${resource.fileConverPath}/index.js",
                PDFFile: "/data${resource.fileConverPath}/index.pdf"
            };
    $(function(){
        $("#pcPageNumber").keyup(function(e){alert(777); if(e.keyCode == 13) $(".pagebtn1").trigger('click');})
    });
</script>

<link rel="stylesheet" href="/js/reader/css/flexpaper.css"/>
<script type="text/javascript" src="/js/reader/js/flexpaper.js"></script>
<script type="text/javascript" src="/js/reader/js/jquery-extensions-min.js"></script>
<script type="text/javascript" src="/js/reader/js/flexpaper-handlers.js"></script>
<script type="text/javascript" src="/js/reader/js/Fullscreen.js"></script>
<script type="text/javascript" src="/js/reader/js/ZeroClipboard.js"></script>
<script type="text/javascript" src="/js/reader/pc-read.js"></script>
<%--<link rel="stylesheet" href="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/css/flexpaper.css"/>--%>
<%--<script type="text/javascript" src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/js/jquery-extensions-min.js"></script>--%>
<%--<script type="text/javascript" src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/js/flexpaper.js"></script>--%>
<%--<script type="text/javascript" src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/js/flexpaper-handlers.js"></script>--%>
<%--<script type="text/javascript" src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/js/Fullscreen.js"></script>--%>
<%--<script type="text/javascript" src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/js/ZeroClipboard.js"></script>--%>
<%--<script type="text/javascript" src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/pc-read.js"></script>--%>

<!-- 右侧 -->
<div class="p-cont fr">
    <div class="p-browse">
        <h2>
            ${ta.attachmentName}
            <c:if test="${sType != 'detail'}">
                <span style="float: right;  cursor: pointer" onclick="findComment(${ta.id})">查看讨论</span>
            </c:if>
        </h2>
        <div class="p-pvm">
            <!-- 预览 -->
            <div style="margin:10px auto 20px;">
                    <div id="documentViewer" class="flexpaper_viewer" style="height:500px;"></div>
                </div>
                <div class='flexpaper_bottom tac' style="display:none;">
                    <div class="page_bottom fc">
                        <a class="pdfarrow pdfarrowl mt8 fl" href="javascript:;" target="_self" onclick='$FlexPaper("documentViewer").prevPage();pageNum(500);'></a>
                        <input id="PageNumberx" class="w32 colgreen tac mt6 fl flexpaper_txtPageNumber" name="" type="text" value="1">
                        <strong id="pagenumx" class='pagenum mt6 fl flexpaper_tblabel'> / </strong>
                        <a class="pdfarrow pdfarrowr mt8 fl" href="javascript:;" target="_self" onclick='$FlexPaper("documentViewer").nextPage();pageNum(500);'></a>
                    </div>
                </div>
                <div class="subarrow subarrowbg1">
                    <a class="subarrowt" href="javascript:;" target="_self" onclick='$FlexPaper("documentViewer").prevPage();pageNum(500);'></a>
                    <div class="subline"></div>
                    <span><em class="subnum">1</em><strong class="suballnum">/1</strong></span>
                    <div class="subline"></div>
                    <a class="subarrowb" href="javascript:;" target="_self" onclick='$FlexPaper("documentViewer").nextPage();pageNum(500);'></a>
                </div>
            <!-- 预览 -->
        </div>
        <c:if test="${sType == 'detail'}">
            <jsp:include page="comment.jsp"/>
        </c:if>
    </div>
</div>

