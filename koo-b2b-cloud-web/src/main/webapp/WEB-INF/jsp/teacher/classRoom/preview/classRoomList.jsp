<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- 右侧 -->
<div class="p-cont fr">
    <div class="p-rgt-ct">
        <ul class="p-lst-ul">
            <li class="p-lst-li">
                <em class="p-lst-tit">
                    <span class="p-txt">全部</span>
                    <span class="line"></span>
                </em>
                <dl class="p-lst-dl">
                    <dt class="p-lst-dt">完成率：</dt>
                    <dd class="p-lst-dd">
                                <span class="p-bar">
                                    <span class="p-bar-over" style="width:${allRate * 100}%"></span>
                                </span>
                        <span class="p-bar-num"><fmt:formatNumber value="${allRate}" type="number" pattern="#%" /></span>
                    </dd>
                    <dt class="p-lst-dt">未完成名单：</dt>
                    <dd class="p-lst-dd">
                        <span>${allStudentName}</span>

                    </dd>
                </dl>
            </li>
            <c:forEach items="${taList}" var="ta">
                <li class="p-lst-li">
                    <em class="p-lst-tit">
                        <span class="p-txt">${ta.attachmentName}</span>
                        <span class="line"></span>
                    </em>
                    <dl class="p-lst-dl">
                        <dt class="p-lst-dt">完成率：</dt>
                        <dd class="p-lst-dd">
                                <span class="p-bar">
                                    <span class="p-bar-over" style="width:${ta.rate * 100}%"></span>
                                </span>
                            <span class="p-bar-num"><fmt:formatNumber value="${ta.rate}" type="number" pattern="#%" /></span>
                        </dd>
                        <dt class="p-lst-dt">未完成名单：</dt>
                        <dd class="p-lst-dd">
                            <span>${ta.unFinishStudentName}</span>
                        </dd>
                    </dl>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>