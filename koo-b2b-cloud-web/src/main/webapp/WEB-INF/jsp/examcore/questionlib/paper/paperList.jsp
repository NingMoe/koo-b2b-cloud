<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>

<c:if test="${ empty paperList}"><div class="i-no-result">没有查询到相关数据</div></c:if>
<c:if test="${not empty paperList}">
        <table class="table-xbsj" >
            <colgroup>  <col width="750">  <col width="118"> <col width="118"></colgroup>
            <thead>
            <tr>
                <th class="fst-pd"><span class="sp-pd">试卷标题</span></th>
                <th><span class="sp-pd">学科</span></th>
                <th><span class="sp-pd">浏览</span></th>
                <th class="last-pd"><span class="sp-pd">操作</span></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${paperList}" var="paper"  varStatus="status">
                <tr>
                    <td class="fst-pd fst-td">
                 <span class="sp-pd">
                        <a style="width: 400px;" title="${pa.paperName}" href="/teacher/exam/core/paper/previewsjzt/?paperId=${paper.id}&createFrom=1&navigation=<%=GlobalConstant.PAPER_CREATE_TYPE_SJZT%>" paperId="${paper.id}">${paper.paperName}</a>
                  </span>
                    </td>
                    <td><span class="sp-pd">${paper.subject}</span></td>
                    <td><span class="sp-pd">${paper.browseTimes}</span></td>
                    <td class="last-pd">
                 <span class="sp-pd" style="height:55px">
                        <c:if test="${!paper.join}">
                            <a href="javascript:;" id="${paper.id}" class="white-btn-gry btn-set jion-button" style="margin-top:15px;width: 112px;">加入我的试卷库</a>
                        </c:if>
                        <c:if test="${paper.join}">
                            <a href="javascript:;" id="${paper.id}" class="white-btn-gry btn-set jion-button" style="margin-top:15px;width: 112px;">移出我的试卷库</a>
                        </c:if>
                  </span>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <!--页码 B-->
            <koo:pager name="listPager" iteration="true" link="javaScript:searchFenye({p});" ></koo:pager>
        <!--页码 E-->
    </c:if>


