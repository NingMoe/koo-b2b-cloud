<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<fe:html title="小学说课-详情页" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/s-saidclass-detail/page.css">
<fe:seaConfig>
    alias:{
    'project/b-ms-cloud/1.x/js/s-saidclass-detail/page': 'project/b-ms-cloud/1.x/js/s-saidclass-detail/page.js'
    }
</fe:seaConfig>
    <script type="text/javascript">

        seajs.use('project/b-ms-cloud/1.x/js/s-saidclass-detail/page',function(init){
            init({
                treeId:'#treeDemo',//树id
                treeUrl:'/teacher/listenCourse/tree/${product.id}'//课程表加载数据
            });
        });
    </script>
    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/header.jsp">
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="xxsk"/>
    </jsp:include>
    <!-- 公共的头部 E-->
    <div class="i-main s-saidclass-detail">
        <p class="p-title">
            <span>${threeName}</span>
            <b title="${product.name}">${product.name}</b>
            <a href="/teacher/listenCourse/index?oneCategoryId=${oneCategoryId}&subCategoryId=${subCategoryId}&threeCategoryId=${threeCategoryId}&pageNo=${pageNo}" title="返回个人中心"></a>
        </p>
        <div class="s-detail-tile fc">
            <div class="p-img fl">
                <img src="<%=PropertiesConfigUtils.getProperty("product_image_url")%>${product.iconFile}">
            </div>
            <div class="p-cont fl">
                <p class="p-listen-btn">
                    <a href="/teacher/listenCourse/player/${product.id}/0/${oneCategoryId}/${subCategoryId}/${threeCategoryId}/${pageNo}">
                        <i></i>马上听课
                    </a>
                </p>
                <p class="p-teacher">
                    <span>主讲老师：${teacherStr}</span>
                    <span>课时：${product.lessonAmount}</span>
                </p>
                <dl class="p-hotclass fc">
                    <dd class="fl">
                        <c:forEach items="${productList}" var="p" end="3">
                            <a title="${p.name}" href="/teacher/listenCourse/detail/${p.id}/${oneCategoryId}/${subCategoryId}/${threeCategoryId}/0">${p.name}</a>
                        </c:forEach>
                    </dd>
                    <dd class="special fl">
                        <c:forEach items="${productList}" var="p" begin="4">
                            <a title="${p.name}" href="/teacher/listenCourse/detail/${p.id}/${oneCategoryId}/${subCategoryId}/${threeCategoryId}/0">${p.name}</a>
                        </c:forEach>
                    </dd>

                </dl>

            </div>
        </div>
        <div class="s-detail-cont">
            <p class="p-spans fc" id="jp-spans">
                <span class="cur">课程详情</span>
                <span>课程表</span>
            </p>
            <div id="jp-course">
                <dl class="public p-course-detail show">
                    <c:if test="${product.objectives != null && product.objectives != ''}">
                        <dt>学习目标</dt>
                        <dd>${product.objectives}</dd>
                    </c:if>
                    <c:if test="${product.crowd != null && product.crowd != ''}">
                        <dt>适用人群</dt>
                        <dd>
                           ${product.crowd}
                        </dd>
                    </c:if>
                    <c:if test="${product.introduction != null && product.introduction != ''}">
                        <dt>产品介绍</dt>
                        <dd>${product.introduction}</dd>
                    </c:if>
                </dl>
                <div class="public p-curriculum">
                    <ul id="treeDemo" class="ztree"></ul>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="/footer.jsp"/>
    </body>
</fe:html>
