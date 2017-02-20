<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<fe:html title="课堂-首页" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-kt-list/page.css,
         /project/b-ms-cloud/1.x/js/common/formJs/jquery-form.js">
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-kt-list/page': 'project/b-ms-cloud/1.x/js/t-kt-list/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/t-kt-list/page',function(init){
            init();
        });
    </script>

    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/headerStudent.jsp" >
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="fzkt"/>
    </jsp:include>
    <!-- 公共的头部 E-->
    <div class="i-main p-courvare">
        <div class="p-contvare fc">
            <!-- 左侧 -->
            <div class="p-side fl">
                <ul class="i-sidevare">
                    <c:forEach items="${subjectList}" var="sub" varStatus="i">
                        <li class=" <c:if test="${not empty subjectId && subjectId == sub.subjectId}">cur</c:if>
                                <c:if test="${empty subjectId && i.index == 0}">cur</c:if>
                                <c:if test="${sub.tagStatus == 1}"> p-never-selected</c:if> ">
                            <a href="/student/classRoom/index?subjectId=${sub.subjectId}"><span>${sub.subjectName}<i></i></span></a>
                            <em></em>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <!-- 右侧 -->
            <div class="p-cont fr">
                <div class="p-cont-inner">
                    <div class="p-search-box">
                        <form id="formId" enctype="multipart/form-data" action="/student/classRoom/index" method="post">
                            <input class="p-set-tim-input p-endtime" type="text" name="endTimeStr" placeholder="课堂截止日期" value="${endTimeStr}" readonly>
                            <div class="p-search-p">
                                    <input class="keytxt" type="text" name="searchValue" value="${searchValue}" placeholder="输入关键词">
                                    <input type="submit" class="keybtn" value="">
                                    <input type="hidden" name="pageNo" id="pageNo" value="0">
                                    <input type="hidden" name="subjectId" value="${subjectId}">
                            </div>
                            </form>
                        </div>
                    </div>
                    <div class="p-cont-search-list">
                        <ul>
                            <c:if test="${empty tpExamList}"><div class="i-no-result">没有查询到相关数据</div></c:if>
                            <c:forEach items="${tpExamList}" var="te" varStatus="i">

                                <li class=" <c:if test="${i.index % 2 == 0}"> bg-gray</c:if> ">
                                    <p class="s-list-title">${te.examName}</p>
                                    <div class="s-list-info">
                                        <span>截止时间：<fmt:formatDate value="${te.endTime}" pattern="yyyy.MM.dd HH:mm"/></span>
                                        <span class="p-divider">/</span>
                                        <c:if test="${not empty te.finishTime}">
                                            <span>完成时间：<fmt:formatDate value="${te.finishTime}" pattern="yyyy.MM.dd HH:mm"/></span>
                                            <span class="p-divider">/</span>
                                        </c:if>
                                        <span>完成率：</span>
                                <span class="p-bar">
                                    <span class="p-bar-over" style="width:${te.rate * 100}%"></span>
                                </span>
                                        <span class="p-bar-num"><fmt:formatNumber value="${te.rate}" type="number" pattern="#%" /></span>
                                    </div>
                                    <c:if test="${te.finishStatus == 1}">
                                        <c:if test="${te.rate * 100 == 0}">
                                            <a href="/student/classRoom/detail/${te.id}/${subjectId}?pageNo=${pageNo}&searchValue=${searchValue}&endTimeStr=${endTimeStr}" class="p-list-enter green-btn green-btn-style">开始学习</a>
                                        </c:if>
                                        <c:if test="${te.rate * 100 > 0 && te.rate * 100 < 100}">
                                            <a href="/student/classRoom/detail/${te.id}/${subjectId}?pageNo=${pageNo}&searchValue=${searchValue}&endTimeStr=${endTimeStr}" class="p-list-enter white-btn pdlr4">继续学习</a>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${te.finishStatus == 0 || te.rate * 100 == 100}">
                                        <a href="/student/classRoom/detail/${te.id}/${subjectId}?pageNo=${pageNo}&searchValue=${searchValue}&endTimeStr=${endTimeStr}" class="p-list-enter white-btn-gry">复习</a>
                                    </c:if>
                                </li>
                            </c:forEach>
                        </ul>
                        <c:if test="${not empty tpExamList}">
                            <div class="i-center i-page">
                                <koo:pager name="listPager" iteration="true" link="javaScript:serachFenye({p});"></koo:pager>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="/footer.jsp"/>
    </body>
</fe:html>
<script>

    serachFenye = function (pageNo) {
        if (!pageNo) {
            pageNo = 0;
        }
        $("#pageNo").val(pageNo);
       // window.isGetQuestions = true;
        $('#formId').submit();
    }
//    $('#formId').submit(function () {
//        if (window.isGetQuestions) {
//            var options = {
//                url: '/student/classRoom/index',
//                contentType: "multipart/form-data; charset=utf-8",
//                type: 'POST',
//                success: function (data) {
//                    $("#jp-quest").html(data);
//                },
//                error: function (data) {
//                }
//
//            };
//            // 提交表单
//            $(this).ajaxSubmit(options);
//            // 为了防止普通浏览器进行表单提交和产生页面导航（防止页面刷新？）返回false
//            return false;
//        }
//        return true;
//    });


</script>