<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<c:if test="${ empty paperList}"><div class="i-no-result">没有查询到相关数据</div></c:if>
<c:if test="${not empty paperList}">
<table class="table-xbsj">
    <colgroup>
        <col width="340">
        <col width="80">
        <col width="100">
        <col width="200">
        <col width="100">
    </colgroup>
    <thead>
    <tr>
        <th class="fst-pd"><span class="sp-pd">试卷标题</span></th>
        <th><span class="sp-pd">学科</span></th>
        <th><span class="sp-pd">浏览次数</span></th>
        <th><span class="sp-pd">创建日期</span></th>
        <th><span class="sp-pd">创建人</span></th>
        <th class="last-pd"><span class="sp-pd">操作</span></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${paperList}" var="pa"  varStatus="status">
        <tr>
            <td class="fst-pd fst-td">
                        <span class="sp-pd">
                             <a title="${pa.paperName}" style="width:263px"  href="/teacher/exam/core/paper/preview/?paperId=${pa.id}&createFrom=1&navigation=<%=GlobalConstant.PAPER_CREATE_TYPE_bxsj%>" paperId="${pa.id}">${pa.paperName}</a>
                        </span>
            </td>
            <td><span class="sp-pd">${pa.subject}</span></td>
            <td><span class="sp-pd">${pa.browseTimes}</span></td>
            <td><span class="sp-pd">${pa.createTimeStr}</span></td>
            <td><span class="sp-pd">${pa.teacherName}</span></td>
            <td class="last-pd">
                        <span class="sp-pd">
                             <%--//下载暂时屏蔽--%>
                            <%--<a href="javascript:;" data-paperid="${pa.id}" data-papername="${pa.paperName}" class="download-btn"></a>--%>
                            <a href="/teacher/task/assign?paperId=${pa.id}" class="white-btn-orange">布置作业</a>
                        </span>
            </td>
        </tr>
    </c:forEach>

    </tbody>
</table>
<!--页码 B-->
    <koo:pager name="listPager" iteration="true" link="javaScript:serachFenye({p});" ></koo:pager>
<!--页码 E-->
</c:if>