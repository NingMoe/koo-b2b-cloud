<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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

<div class="p-class-paly fc">

    <div class="p-side fl">
        <!-- 预览 -->
        <div class="p-play">
            <div id="documentViewer" class="flexpaper_viewer" style="height:400px;"></div>
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
        <p class="p-ass">
            <jsp:include page="page.jsp"/>
        </p>
    </div>
    <div class="p-cont fr">
        <jsp:include page="learning.jsp"/>
    </div>
</div>
<jsp:include page="comment.jsp"/>

