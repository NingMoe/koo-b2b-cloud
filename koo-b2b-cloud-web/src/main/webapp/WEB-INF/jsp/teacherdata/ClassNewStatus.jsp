
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="com.koolearn.cloud.util.DateFormatUtils" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<fe:html title="首页-班级动态" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-home-dynam/page.css">
    <!-- 公共部分结束 -->
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-home-dynam/page': 'project/b-ms-cloud/1.x/js/t-home-dynam/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/t-home-dynam/page',function(init){
            init({
                listUrl: '/teacher/classNewStatus/getNewsPost' //班级动态数据查询加载地址
            });
        });

        //分页
        function searchFenye(pageNo){
            //当前选中id
            var classesId = $('#jp-dynam-side li.cur').attr("data-id")
            if(!pageNo){
                pageNo=0;
            }
            //var classesId =
            $("#classesId").val( classesId );
            $("#pageNo").val(pageNo);
            $('#formId').submit();
        }
        function showDisInfo( classesId ){
            $("#classesId").val( classesId );
            $("#pageNo").val(0);
            $('#formId').submit();
        }
    </script>
</head>
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="sy"/>
</jsp:include>
<!-- 公共的头部 E-->
<div class="i-main p-dynam fc">
    <ul class="p-side fl" id="jp-dynam-side">
        <c:forEach items="${classList}" var="classesInfo" >
            <c:if test="${classesInfo.id == classesId }">
                <li class="cur" data-id="${classesInfo.id }">
                    <a href="javascript:;" title="${classesInfo.fullName}">${classesInfo.fullName}</a>
                    <em></em>
                </li>
            </c:if>
            <c:if test="${classesInfo.id != classesId }">
                <li data-id="${classesInfo.id }">
                    <a href="javascript:void(0)" onclick="showDisInfo( ${classesInfo.id} )" title="${classesInfo.fullName}" >${classesInfo.fullName}
                         <c:if test="${classesInfo.hasDynamic == 1 }">
                            <i></i>
                         </c:if>
                    </a>
                    <em></em>
                </li>
            </c:if>
        </c:forEach>
    </ul>
    <div class="p-cont fr" id="jp-dynamic">
        <a class="green-btn-tab p-back-home" href="/teacher/choiceSubject/goClasssHomePage" style="z-index:300">返回首页</a>
        <div class="p-list">
            <ul id="jp-dynamic-list">
                <c:forEach items="${list}" var="detailInfo" >
                 <li>
                     <p>${detailInfo.createTimeStr}:</p> <p>${detailInfo.dynamicInfo}</p>
                    <i></i>
                </li>
                </c:forEach>
            </ul>
        </div>
        <!--页码 B-->
        <form id="formId" action="/teacher/classNewStatus/getNews" method="post">
            <input type="hidden" id="pageNo" name="pageNo" value="${pageInfo.pageNo }">
            <input type="hidden" name="classesId" id="classesId" value="">
            <a class="jp-search-btn fr" href="javascript:searchFenye(${pageInfo.pageNo });"></a>
        </form>
        <div class="i-center i-page" style="padding: 20px 0;">
            <koo:pager name="pageInfo" iteration="true" link="javaScript:searchFenye({p});" ></koo:pager>
        </div>
    </div>

</div>
<jsp:include page="/footer.jsp"></jsp:include>
</body>
</fe:html>