<%--
  教师作文列表查询页面
  User: haozipu
  Date: 2016/8/3
  Time: 11:58

--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<fe:html title="作文批改-列表页面" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/zw-pg-sy-lst/page.css">

    <script type="text/javascript">
        seajs.config({
            alias:{
                'project/b-ms-cloud/1.x/js/zw-pg-sy-lst/page': 'project/b-ms-cloud/1.x/js/zw-pg-sy-lst/page.js'
            }
        });
        seajs.use('project/b-ms-cloud/1.x/js/zw-pg-sy-lst/page',function(app){
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
    <div class="p-pg-sy-lst-box">
        <div class="p-sy-lst-tit">
            <form id="searchForm" action="/composition/compositionList" method="get" style="display:inline-block" y>
                <input type="text" name="keyWord" class="p-sy-search" placeholder="请输入关键字" value="${keyWord}">
                <input type="text" name="startDate" class="jp-set-tim jp-set-tim-star p-set-tim-input" readonly="" placeholder="起始时间" value="${startDateStr}">
                <input type="text" name="endDate" class="jp-set-tim jp-set-tim-end p-set-tim-input" readonly="" placeholder="截止时间" value="${endDateStr}">
                <select id="sel-show4" name="status">
                    <option value="-1" <c:if test="${status==-1||status==null}">selected="selected"</c:if>>全部</option>
                    <option value="3"  <c:if test="${status==3}">selected="selected"</c:if>>已批阅</option>
                    <option value="2"  <c:if test="${status==2}">selected="selected"</c:if>>未批阅</option>
                </select>
            </form>

            <a href="javascript:$('#searchForm').submit();" class="green-btn-tab">搜索</a>
            <c:if test="${!hideFlag}">
                <span class="p-sy-num">批改收益：
                    <a href="/composition/correctList" target="_blank">
                        <strong><c:out value="${payCount}"></c:out></strong>
                    </a>
                    元</span>
            </c:if>

        </div>
        <div class="p-sy-lst-pd">
            <table class="p-sy-lst-table">
                <colgroup>
                    <col width="340">
                    <col width="506">
                    <col width="">
                </colgroup>
                <c:if test="${compositionOrders== null || fn:length(compositionOrders) == 0}">
                    <c:if test="${condition==null||condition==''}">
                        还没有需要批阅的作文，赶快通知学生们提交作文进行批阅吧！
                    </c:if>
                    <c:if test="${condition!=null&&condition!=''}">
                        没有查询到相关数据！
                    </c:if>
                </c:if>
                <c:forEach items="${compositionOrders}" var="order">
                    <tr <c:if test="${order.viewFlag==0}">class="p-new"</c:if> >
                        <td><span class="p-sp p-sp-wid">
                        <c:if test="${order.viewFlag==0}"><span class="p-new-ico">new</span></c:if>
                        <a title="${order.compositionTitle}" <c:if test="${order.status==3}">href="/composition/compositionDetail?cid=${order.compositionId}&oid=${order.id}"</c:if> <c:if test="${order.status==2}">href="/composition/correctComposition?cid=${order.compositionId}&oid=${order.id}"</c:if>>
                                ${order.compositionTitle}
                        </a>
                        </span></td>
                        <td><span class="p-sp p-sp-wid2">${order.className}&nbsp;&nbsp; ${order.studentName} &nbsp;&nbsp;  <fmt:formatDate value="${order.createTime}" pattern="yyyy.MM.dd HH:mm" />
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <c:if test="${order.status==3}">
                                批阅时间:<fmt:formatDate value="${order.correctTime}" pattern="yyyy.MM.dd HH:mm" />
                        </c:if>
                        </span></td>
                        <c:if test="${order.status==2}">
                            <td class="td-center"><a href="/composition/correctComposition?cid=${order.compositionId}&oid=${order.id}" class="white-btn pdlr4">开始批阅</a></td>
                        </c:if>
                        <c:if test="${order.status==3}">
                            <td class="td-center"><a href="/composition/compositionDetail?cid=${order.compositionId}&oid=${order.id}" class="white-btn-gry">查看详情</a></td>
                        </c:if>


                    </tr>

                </c:forEach>

            </table>
        </div>
        <!-- 分页 注:此分页只是一个暂时的样式-->
        <c:if test="${not empty compositionOrders}">
            <div class="i-center i-page">
                <!--页码 B-->
                <koo:pager name="listPager" iteration="true" link="/composition/compositionList?pageNo={p}${condition}" ></koo:pager>
                <!--页码 E-->
            </div>
        </c:if>
        <%--<div class="p-page i-pd50">--%>
            <%--<img src="/project/b-ms-cloud/1.x/i/page.png" style="margin:0 auto;display: block;">--%>
        <%--</div>--%>
    </div>
</div>
<!-- 公共的底部 B-->
<jsp:include page="/footer.jsp"></jsp:include>
<!-- 公共的底部 E-->
</body>
</fe:html>
