<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>

<div class="p-search-p fc">
    <input class="jp-search-val fl" type="text" id="searchText" name="searchText" placeholder="输入关键词" value="${listPager.searchText}">
    <a class="jp-search-btn fr" href="javascript:;"></a>
</div>
<ul class="jp-work-list lst p-add-ul">
	<c:forEach items="${resultList}" var="paper">
		<li>
	        <input type="radio" class="for-checkbox" id="" value="${paper.id}">
	        <span class="jp-zt-name">${paper.paperName}</span>
	    </li>
	</c:forEach>
</ul>
<div class="jp-page i-center i-page">
	<!--页码 B-->
	<koo:pager name="listPager" iteration="true" link="javaScript:searchFenye({p});" ></koo:pager>
	<!--页码 E-->
</div>
<p class="p-sure">
    <a id="jp-surebtn" class="green-btn green-btn-style" href="javascript:;">确定</a>
</p>