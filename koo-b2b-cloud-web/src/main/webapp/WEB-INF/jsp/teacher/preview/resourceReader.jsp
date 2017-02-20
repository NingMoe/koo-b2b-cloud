<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" import="com.koolearn.framework.common.utils.PropertiesConfigUtils"%>
<!DOCTYPE html>
<fe:html title="资源库" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-zyk-detail/page.css">

    <script type="text/javascript">
        seajs.config({
            alias:{
                'project/b-ms-cloud/1.x/js/t-zyk-detail/page': 'project/b-ms-cloud/1.x/js/t-zyk-detail/page.js'
            }
        });
        seajs.use('project/b-ms-cloud/1.x/js/t-zyk-detail/page',function(init){
            init();
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
    <%--<script type="text/javascript" src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-media-class/1.x/js/jquery-1.8.2.min.js"></script>--%>
    <%--<script type="text/javascript" src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/js/jquery-extensions-min.js"></script>--%>
    <%--<script type="text/javascript" src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/js/flexpaper.js"></script>--%>
    <%--<script type="text/javascript" src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/js/flexpaper-handlers.js"></script>--%>
    <%--<script type="text/javascript" src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/js/Fullscreen.js"></script>--%>
    <%--<script type="text/javascript" src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/js/ZeroClipboard.js"></script>--%>
    <%--<script type="text/javascript" src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/pc-read.js"></script>--%>
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/header.jsp">
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="zyk"/>
</jsp:include>
<!-- 公共的头部 E-->
<div class="i-main p-detail">
    <div class="p-dtitle">
        <jsp:include page="/WEB-INF/jsp/teacher/preview/headReader.jsp"/>
    </div>
    <!-- 预览 -->
    <div class="p-previ pvw">
        <div style="margin:0 auto;">
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
    </div>
    <div class="p-opera">
        <jsp:include page="/WEB-INF/jsp/teacher/preview/footReader.jsp"/>
    </div>
</div>
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
<jsp:include page="/footer.jsp"/>
</body>
</fe:html>