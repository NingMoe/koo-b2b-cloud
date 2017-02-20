<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- 试题 标签头 -->
 <c:forEach items="${tagList}" var="tag"  varStatus="status">
	    <c:choose>
	         <c:when test="${status.first}">
	              <a class="current" href="javascript:;" id=${tag.id }>${tag.name }</a>
	          </c:when>
	          <c:otherwise>
	               <a href="javascript:;" id=${tag.id }>${tag.name }</a>
	          </c:otherwise>
	     </c:choose>
 </c:forEach>
