<%--
  作文查看页面 如果是批阅过得作文 需要显示报告页面
  User: haozipu
  Date: 2016/8/3
  Time: 11:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<fe:html title="作文批改-作文查询详情页面" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/zw-pg/page.css">
    <script type="text/javascript">
        seajs.config({
            alias:{
                'project/b-ms-cloud/1.x/js/zw-pg-watch/page': 'project/b-ms-cloud/1.x/js/zw-pg-watch/page.js'
            }
        });
        seajs.use('project/b-ms-cloud/1.x/js/zw-pg-watch/page',function(app){
            app.init();
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
    <input type="hidden" id="compositionId" value="1">
    <input type="hidden" id="compositionType" value="2">
    <input type="hidden" id="orderId" value="3">
    <div class="p-pg-box">
        <div class="p-pg-pf p-pf-num">
            <strong>${report.score}</strong> <c:if test="${scoreFlag}">分</c:if>
        </div>
        <ul class="p-tab">
            <li class="jp-tab"><a href="javascript:;">作文</a></li>
            <li class="jp-tab on"><a href="javascript:;">报告</a></li>
        </ul>
        <div class="p-pg-ct">
            <div class="p-lft">
                <h2 class="p-title"><c:out value="${composition.title}"></c:out></h2>
                <h3 class="p-title2">
                ${userInfo.classesName}&nbsp;&nbsp; ${userInfo.realName} &nbsp;&nbsp;  提交时间: <fmt:formatDate value="${composition.createTime}" pattern="yyyy.MM.dd HH:mm" />
                    &nbsp;&nbsp;
                 ${teacherName}  批阅时间:<fmt:formatDate value="${report.createTime}" pattern="yyyy.MM.dd HH:mm" />
                </h3>
                <div class="jp-canvas-box p-canvas-box">
                    <ul class="jp-canvas-ul p-canvas-ul">

                        <c:forEach items="${images}" var="img">
                            <li class="jp-canvas-li p-canvas-li">
                                <img src="${img.imgUrl}" alt="The Tulip" width="100%" id=""/>
                            </li>
                        </c:forEach>
                    </ul>
                    <div class="p-page-box">
                        <a href="javascript:;" class="jp-page-turn p-page-turn p-page-lft"></a>
                        <a href="javascript:;" class="jp-page-turn p-page-turn p-page-rgt"></a>
                    </div>
                </div>
            </div>
            <div class="jp-rgt p-rgt">
                <div class="jp-lst-dv">
                    <div class="jp-lst-box">
                        <h2 class="p-title">修改意见</h2>

                        <c:forEach items="${images}" var="img">

                            <ul style="display: block;" class="jp-lst p-lst">

                                <c:forEach items="${correctRecords}" var="record">

                                    <c:if test="${record.picId==img.id}">
                                        <li>      <em>${record.picOrder}</em>
                                            <div>${record.remark}</div>
                                        </li>
                                    </c:if>

                                </c:forEach>

                            </ul>

                        </c:forEach>

                    </div>

                    <div class="jp-lst-box" style="display:none">
                        <h2 class="p-title">作文评价</h2>
                            <ul class="jp-lst-report p-lst p-lst-report">
                                <c:forEach items="${reportItems}" var="record">
                                <li><c:out value="${record}"></c:out></li>
                                </c:forEach>
                            </ul>
                    </div>
                </div>
            </div>
        </div>
        <div class="p-btm-bar">
            <div class="p-btm-bar-lft fl"><span class="jp-now-page">1</span>/${fn:length(images)}</div>

            <c:if test="${nextComposition!=null}">
                <c:choose>
                    <c:when test="${nextComposition.status==2}">
                        <a href="/composition/correctComposition?cid=${nextComposition.compositionId}&oid=${nextComposition.id}" class="p-next fr">下一篇</a>
                    </c:when>
                    <c:otherwise>
                        <a href="/composition/compositionDetail?cid=${nextComposition.compositionId}&oid=${nextComposition.id}" class="p-next fr">下一篇</a>
                    </c:otherwise>
                </c:choose>
            </c:if>

            <c:if test="${preComposition!=null}">
                <c:choose>
                    <c:when test="${preComposition.status==2}">
                        <a href="/composition/correctComposition?cid=${preComposition.compositionId}&oid=${preComposition.id}" class="p-next fr">上一篇</a>
                    </c:when>
                    <c:otherwise>
                        <a href="/composition/compositionDetail?cid=${preComposition.compositionId}&oid=${preComposition.id}" class="p-next fr">上一篇</a>
                    </c:otherwise>
                </c:choose>
            </c:if>



        </div>
    </div>
</div>
<!-- 公共的底部 B-->
<jsp:include page="/footer.jsp"></jsp:include>
<!-- 公共的底部 E-->
</body>
</fe:html>