<%--
  作文批阅页面
  User: haozipu
  Date: 2016/8/3
  Time: 12:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<fe:html title="作文批改-批改页面" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/zw-pg/page.css">
    <script type="text/javascript">
        seajs.config({
            alias:{
                'project/b-ms-cloud/1.x/js/zw-pg/page': 'project/b-ms-cloud/1.x/js/zw-pg/page.js'
            }
        });
        seajs.use('project/b-ms-cloud/1.x/js/zw-pg/page',function(app){
            app.init({
                //设置评分
                setPfUrl:"/composition/queryCorrectRuleList",
                //保存设置评分
                saveSetPfUrl:"/composition/saveCorrectRule",
                //评分
                pfUrl:"/composition/queryScoreItems",
                //生成总体评价
                zpjfUrl:"/composition/makeCompositionReport",
                //完成批阅
                pyDoneUrl:"/composition/saveReport",
                skipUrl:"/composition/compositionDetail?cid=${composition.id}&oid=${order.id}",
                validate: {
                    errorPlacement:function(error,element) {
                        if (element.attr("name") == "fname" || element.attr("name") == "lname")
                            error.insertAfter("#lastname");
                        else
                            error.insertAfter(element);
                    },
                    rules: {
                        cj: {
                            required: true,
                            number:true,
                            digits:true
                            //decimal:true

                        }
                    },
                    messages: {
                        cj: {
                            required: '未填写成绩！',
                            number:'成绩只能输入数字！',
                            digits:'不能输入小数！'
                            //decimal:'最多输入一位小数！'

                        }
                    }
                }
            });
        });
    </script>
    <title>作文批改</title>
</head>
<body>
<ul class="jp-copy-im p-copy-im"></ul>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/header.jsp">
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="zwpg"/>
</jsp:include>
<!-- 公共的头部 E-->
<div class="i-main fc">
    <input type="hidden" id="compositionId" value="${composition.id}">
    <input type="hidden" id="compositionType" value="${composition.type}">
    <input type="hidden" id="orderId" value="${order.id}">
    <form method="post" action="/composition/saveReport" enctype="multipart/form-data" id="jsonUpForm">
        <input type="hidden" name="jsonData" value="" id="jsonUp">
    </form>
    <div class="p-pg-box">
        <div class="p-pg-pf">
            <a href="javascript:;" class="jp-pf-btn green-btn green-btn-style">评分</a>
        </div>
        <ul class="p-tab">
            <li class="on"><a href="javascript:;">作文</a></li>
        </ul>
        <div class="p-pg-ct">
            <div class="p-lft">

                <h2 class="p-title">${composition.title}</h2>
                <h3 class="p-title2">${userInfo.classesName} &nbsp; ${userInfo.realName} &nbsp;&nbsp;  提交时间:<fmt:formatDate value="${order.createTime}" pattern="yyyy.MM.dd HH:mm" /></h3>
                <div class="jp-canvas-box p-canvas-box">
                    <ul class="jp-canvas-ul p-canvas-ul">
                        <c:forEach items="${images}" var="image">
                            <li class="jp-canvas-li p-canvas-li">
                                <img src="${image.imgUrl}" alt="The Tulip" width="100%" style="display:none" id="1" />
                                <div class="jp-remove"></div>
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
                <h2 class="p-title">修改意见</h2>
                <div class="jp-lst-box"></div>
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
