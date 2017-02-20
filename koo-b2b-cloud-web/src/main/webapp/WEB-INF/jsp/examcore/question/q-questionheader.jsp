<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!-- 试题 标签头 -->

<!-- 题目类型页头 -->
<div class="page_tit">
<h2 class="h2_o h2_nom">试题录入</h2>
</div>
<c:choose>
<c:when test="${questionTypeTab!=null&&questionTypeTab==1}">
<div>
</c:when>
<c:otherwise>
<!-- 更新没有题目模板标签style="display:none" -->
<div style="display:none">
</c:otherwise>
</c:choose>
<div class="sttemp_list fc">
	<input type="hidden" id="questionTypeId" name="questionTypeId" value="${questionTypeId==null?0:questionTypeId}"/>
	<span class="stbq">试题标签</span>
	<select id="header_tag1" name="tag1" onchange="queryFirstTag('header_tag1','header_tag2');">
		<c:forEach var="tmpTag" items="${tags1}">
			<option value="${tmpTag.id}" <c:if test="${tag1==tmpTag.id}">selected</c:if> >${tmpTag.name}</option>
		</c:forEach>
	</select>
	<span>题型</span>
	<select id="header_tag2" name="tag2" onchange="queryTagTwo('header_tag2','header_tag3')">
		<c:forEach var="tmpTag" items="${tags2}">
			<option value="${tmpTag.id}" <c:if test="${tag2==tmpTag.id}">selected</c:if> >${tmpTag.name}</option>
		</c:forEach>
	</select>
	<span>内容</span>
	<select id="header_tag3" name="tag3" onchange="questionTypeMode()">
		<c:forEach var="tmpTag" items="${tags3}">
			<option value="${tmpTag.id}" <c:if test="${tag3==tmpTag.id}">selected</c:if> >${tmpTag.name}</option>
		</c:forEach>
	</select>
	<!--  
	<span class="click_sp"><i class="<c:if test="${shareStatus==3}">cur2</c:if>" name="shareStatus"></i>
	</span>
	<span class="wd90">本校共享</span>
	-->
</div>
<input type="hidden" id="header_shareStatus" name="shareStatus" value="${shareStatus==null?2:shareStatus}"/>
<div class="jp_ltmb">
	<p class="comp_title sttemp_p fc p_ltmb" id="questionTypeTags">
		${questionTypeTags}
	</p>
	<b class="ltmb">录题模板</b>
</div>
</div>
