<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<fe:html title="小学说课" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/s-saidclass/page.css">
<fe:seaConfig>
    alias:{
    'project/b-ms-cloud/1.x/js/s-saidclass/page': 'project/b-ms-cloud/1.x/js/s-saidclass/page.js'
    }
</fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/s-saidclass/page',function(init){
            init();
        });
    </script>
    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/header.jsp">
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="xxsk"/>
    </jsp:include>

    <!-- 公共的头部 E-->
    <div class="i-main fc p-say">
        <div class="p-navig" id="jp-navig">
            <c:forEach items="${oneCategoryList}" var="one" end="0">
                <a <c:if test="${one.id == oneCategoryId}">class="cur"</c:if>  href="javascript:;"  data-rangeid="${one.id}">${one.name}</a>
            </c:forEach>
        </div>
        <div class="p-search">
            <form id="jp-form" action="/teacher/listenCourse/index" method="post">
                <input id="jp-rangeId" type="hidden" value="${oneCategoryId}" name="oneCategoryId">
                <input id="jp-grade" type="hidden" value="${subCategoryId}" name="subCategoryId">
                <input id="jp-discipline" type="hidden" value="${threeCategoryId}" name="threeCategoryId">
                <input type="hidden" id="pageNo" name="pageNo" value="${pageNo}"/>
                <p class="p-search-p fc">
                    <input id="jp-keytxt" class="fl" type="text" value="${searchValue}" placeholder="输入关键词" name="searchValue" />
                    <a id="jp-keybtn" class="fr" href="javascript:;" onclick="_submit()" ></a>
                </p>
                <dl class="p-search-dl jp-search">
                    <dt>年级</dt>
                    <dd>
                        <c:forEach items="${subCategoryList}" var="sub">
                            <a class="white-btn-tab <c:if test="${sub.id == subCategoryId}">on</c:if>" href="javascript:;" data-nj="${sub.id}"  >${sub.name}</a>
                        </c:forEach>
                    </dd>
                    <dt>学科</dt>
                    <dd>
                        <c:forEach items="${threeCategoryList}" var="three">
                            <a class="white-btn-tab <c:if test="${three.id == threeCategoryId}">on</c:if>" href="javascript:;" data-xk="${three.id}"  >${three.name}</a>
                        </c:forEach>
                    </dd>
                </dl>
            </form>
        </div>
        <ul class="p-cont fc">
            <c:if test="${empty productList}"><div class="i-no-result">没有查询到相关数据</div></c:if>
            <c:forEach items="${productList}" var="product">
                <li>
                    <a href="/teacher/listenCourse/detail/${product.id}/${oneCategoryId}/${subCategoryId}/${threeCategoryId}/${pageNo}">
                        <img src="<%=PropertiesConfigUtils.getProperty("product_image_url")%>${product.iconFile}">
                        <i></i>
                    </a>
                    <p title="${product.name}"><a href="/teacher/listenCourse/detail/${product.id}/${oneCategoryId}/${subCategoryId}/${threeCategoryId}/${pageNo}" style="color: #4b4b4b">${product.name}</a></p>
                </li>
            </c:forEach>
            <c:if test="${not empty productList}">
                <div class="i-center i-page">
                    <koo:pager name="listPager" iteration="true" link="javaScript:searchFenye({p});"></koo:pager>
                </div>
            </c:if>
        </ul>
    </div>

    <jsp:include page="/footer.jsp"/>
    </body>
</fe:html>
<script>

    searchFenye=function(pageNo){
        if(!pageNo){
            pageNo=0;
        }
        $("#pageNo").attr("value",pageNo);
        $("#jp-form").submit();

    };

    _submit = function (){
        $("#pageNo").attr("value",0);
        $("#jp-form").submit();
    }


</script>