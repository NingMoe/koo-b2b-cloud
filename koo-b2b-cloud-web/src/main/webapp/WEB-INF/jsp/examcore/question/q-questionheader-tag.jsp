<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- 试题 标签头 -->
<c:forEach var="tag1" items="${tagList}">
	<option value="${tag1.id}">${tag1.name}</option>
</c:forEach>
