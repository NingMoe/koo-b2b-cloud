<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


    <c:forEach items="${cList}" var="c">
        <li>
            <dl class="p-dlone">
                <dt class="fc">
                    <span class="fl"><i></i>${c.userName}</span>
                </dt>
                <dd>
                    <p class="p-txt">
                            ${c.comment}
                    </p>
                    <p class="p-time"><fmt:formatDate value="${c.createTime}"
                                                      pattern="yyyy.MM.dd HH:mm:ss"></fmt:formatDate></p>
                </dd>
            </dl>
            <div class="p-divs coo_${c.id}">
                <c:forEach items="${c.lists}" var="cc">
                    <dl>
                        <dt class="fc">
                            <span class="fl"><i></i><b>${cc.userName}</b> 回复 ${cc.replyUserName}</span>
                        </dt>
                        <dd>
                            <p class="p-txt">
                                    ${cc.comment}
                            </p>
                            <p class="p-time"><fmt:formatDate value="${cc.createTime}"
                                                              pattern="yyyy.MM.dd HH:mm:ss"></fmt:formatDate></p>
                        </dd>
                    </dl>
                </c:forEach>
            </div>
        </li>
    </c:forEach>
