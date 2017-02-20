<%--
  教师 我的批阅记录页面
  User: haozipu
  Date: 2016/8/3
  Time: 11:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<fe:html title="作文批改-收益列表页面" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/zw-pg-sy/page.css">
    <script type="text/javascript">
        seajs.config({
            alias:{
                'project/b-ms-cloud/1.x/js/zw-pg-sy/page': 'project/b-ms-cloud/1.x/js/zw-pg-sy/page.js'
            }
        });
        seajs.use('project/b-ms-cloud/1.x/js/zw-pg-sy/page',function(app){
            app.init({});
        });
    </script>
    <title>作文批改</title>
</head>
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/header.jsp">
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="zwpg"/>
</jsp:include>
<!-- 公共的头部 E-->
<div class="i-main fc">
    <div class="p-pg-sy-box">
        <h2 class="p-sy-tit">批改收益：<strong>${payCount}</strong>元</h2>
        <table class="p-sy-table">
            <colgroup>
                <col width="142">
                <col width="142">
                <col width="">
                <col width="142">
            </colgroup>
            <tr>
                <th>时间</th>
                <th>学生姓名</th>
                <th>作文标题</th>
                <th>收益金额</th>
            </tr>
            <c:if test="${correctedOrders== null || fn:length(correctedOrders) == 0}">
                没有符合条件的数据
            </c:if>
            <c:forEach items="${correctedOrders}" var="order">

                <tr>
                    <td><span class="p-sp p-sp-wid"><fmt:formatDate value="${order.correctTime}" pattern="yyyy.MM.dd HH:mm:ss" /></span></td>
                    <td><span class="p-sp p-sp-wid">${order.studentName}</span></td>
                    <td><span class="p-sp p-sp-wid2">${order.compositionTitle}</span></td>
                    <td><span class="p-sp p-sp-wid">${order.price}</span></td>
                </tr>

            </c:forEach>


        </table>
        <!-- 分页 注:此分页只是一个暂时的样式-->
        <c:if test="${not empty correctedOrders}">
            <div class="i-center i-page">
                <!--页码 B-->
                <koo:pager name="listPager" iteration="true" link="/composition/correctList?pageNo={p}" ></koo:pager>
                <!--页码 E-->
            </div>
        </c:if>
        <%--<div class="p-page i-pd50">--%>
            <%--<img src="http://b-ms-cloud_b-zwpg-demo.ui.koolearn-inc.com/project/b-ms-cloud/1.x/i/page.png" style="margin:0 auto;display: block;">--%>
        <%--</div>--%>
    </div>
</div>
<!-- 公共的底部 B-->
<jsp:include page="/footer.jsp"></jsp:include>
<!-- 公共的底部 E-->
</body>
</fe:html>