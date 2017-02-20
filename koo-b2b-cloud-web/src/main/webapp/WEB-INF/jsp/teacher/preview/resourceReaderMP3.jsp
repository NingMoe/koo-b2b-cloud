<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<!DOCTYPE html>
<fe:html title="资源库" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-zyk-detail/page.css">

    <script type="text/javascript">
        seajs.config({
            alias: {
                'project/b-ms-cloud/1.x/js/t-zyk-detail/page': 'project/b-ms-cloud/1.x/js/t-zyk-detail/page.js'
            }
        });
        seajs.use('project/b-ms-cloud/1.x/js/t-zyk-detail/page', function (init) {
            init();
        });
    </script>
    <link rel="stylesheet"
          href="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/css/flexpaper.css"/>
    <script type="text/javascript"
            src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-media-class/1.x/js/jquery-1.8.2.min.js"></script>
    <script type="text/javascript"
            src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/js/jquery-extensions-min.js"></script>
    <script type="text/javascript"
            src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/js/flexpaper.js"></script>
    <script type="text/javascript"
            src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/js/flexpaper-handlers.js"></script>
    <script type="text/javascript"
            src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/js/Fullscreen.js"></script>
    <script type="text/javascript"
            src="<%=PropertiesConfigUtils.getProperty("domains.ui")%>project/b-ms-cloud/1.x/js/common/reader/js/ZeroClipboard.js"></script>
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
            <div>
                <embed autoplay="false" src="/data${resource.filePath}" style="display: block;margin:auto;"/>
            </div>
        <div class="p-opera">
            <jsp:include page="/WEB-INF/jsp/teacher/preview/footReader.jsp"/>
        </div>
    </div>
    <jsp:include page="/footer.jsp"/>
    </body>
</fe:html>