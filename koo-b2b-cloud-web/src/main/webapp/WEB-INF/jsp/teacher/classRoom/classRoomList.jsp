<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="p-make-zy">
    <ul class="lst">
        <c:if test="${pageNo == 0}">
            <li>
                <div class="p-operation">
                    <a href="/teacher/classRoom/create" class="make-zy-add-link"></a>
                    <em class="make-zy-add">备课</em>
                </div>
            </li>
        </c:if>
        <c:forEach items="${resultList}" var="result" varStatus="i">
            <%--<li class="jp-revoke" id="${i.index}">--%>
            <li id="${result.id}">
                <div class="p-operation">
                    <div class="p-up-part">
                        <div class="p-num"><em>${result.hadDoneNum}</em>/${result.studentNum}</div>
                        <div class="p-theme-title" title="${result.tagStr}">${result.tagStr}</div>
                        <div class="p-theme" title="${result.examName}"><a href="/teacher/classRoom/previewDetail?classRoomId=${result.id}">${result.examName}</a></div>
                    </div>
                    <div class="p-bj" title="${result.classesStr}">${result.classesStr}</div>
                    <div class="p-btn">
                        <c:if test="${result.finishStatus == 1}">
                            <c:if test="${result.status == 1}">
                                <a href="/teacher/classRoom/updateIndex?id=${result.id}"><span class="green-btn">编辑课堂</span></a>
                            </c:if>
                            <c:if test="${result.status == 2}">
                                <a href="/teacher/classRoom/updateIndex?id=${result.id}"><span class="green-btn">编辑课堂</span></a>
                            </c:if>
                            <c:if test="${result.status == 4}">
                                <span class="green-btn recall">撤回课堂</span>
                            </c:if>
                        </c:if>
                        <span class="jp-copy white-btn">复制课堂</span>
                    </div>
                </div>
                <div class="p-tim ">
                    <c:if test="${result.finishStatus == 1}">
                        <c:if test="${result.status == 1}">
                            未发布
                        </c:if>
                        <c:if test="${result.status == 2}">
                            已撤回
                        </c:if>
                        <c:if test="${result.status == 4}">
                            <fmt:formatDate value="${result.startTime}" pattern="yyyy.MM.dd HH:mm"/>~<fmt:formatDate value="${result.endTime}" pattern="yyyy.MM.dd HH:mm"/>
                        </c:if>
                    </c:if>
                    <c:if test="${result.finishStatus == 0}">
                        已结束
                    </c:if>
                </div>
                <c:if test="${result.status != 4 && result.status != 0 && result.finishStatus != 0}">
                    <span class="jp-cls p-cls"></span>
                </c:if>
                <div class="p-revoke_ico"></div>
            </li>
        </c:forEach>
        <c:if test="${not empty resultList}">
            <div class="i-center i-page">
                <koo:pager name="listPager" iteration="true" link="javaScript:serachFenye({p});"></koo:pager>
            </div>
        </c:if>
    </ul>

</div>
