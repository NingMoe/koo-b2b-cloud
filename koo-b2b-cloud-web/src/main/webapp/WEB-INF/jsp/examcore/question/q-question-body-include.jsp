<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://www.koolearn.com/taglib/koo"  prefix="koo"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta charset="UTF-8">
		<title>题库-录题</title>
	</head>
	<body>
	<!-- 分题目类型引入录题模板 -->
	<c:choose>
		<c:when test="${questionTypeId==1}">
		<!-- 多选题 -->
		<jsp:include page="choice/multiChoice.jsp"/>
		</c:when>
		<c:when test="${questionTypeId==6}">
		<!-- 单选题 -->
		<jsp:include page="choice/commChoice.jsp"/>
		
		</c:when>
		<c:when test="${questionTypeId==18}">
			<!-- 口语题 -->
			<c:choose>
				<c:when test="${saveType==1 ||saveType==2 }">
					<jsp:include page="spokenquestion/modify.jsp"/>
				</c:when>
				<c:otherwise>
					<jsp:include page="spokenquestion/add.jsp"/>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:when test="${questionTypeId==40 || questionTypeId==41 || questionTypeId==12}">
			<!-- 拖拽矩阵、表格矩阵 -->
			<c:choose>
				<c:when test="${saveType==1 }">
					<jsp:include page="matrix/add.jsp"/>
				</c:when>
				<c:otherwise>
					<jsp:include page="matrix/add.jsp"/>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:when test="${questionTypeId==3}">
		<!-- 简答题 -->
			<c:choose>
				<c:when test="${saveType==1 ||saveType==2 }">
					<jsp:include page="shortQuestion/modify.jsp"/>
				</c:when>
				<c:otherwise>
					<jsp:include page="shortQuestion/add.jsp"/>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:when test="${questionTypeId==4}">
		<!-- 作文题 -->
			<c:choose>
				<c:when test="${saveType==1 ||saveType==2}">
					<jsp:include page="whriteQuestion/modify.jsp"/>
				</c:when>
				<c:otherwise>
					<jsp:include page="whriteQuestion/add.jsp"/>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:when test="${questionTypeId==2}">
		<!-- 填空 -->
		<jsp:include page="essayquestion/add.jsp"/>
		</c:when>
		<c:when test="${questionTypeId==16}">
		<!-- 计算填空 -->
		<jsp:include page="calculation/add.jsp"/>
		</c:when>
		<c:when test="${questionTypeId==7}">
		<!-- 阅读-->
		<jsp:include page="read/adddRead.jsp"/>
		</c:when>
		<c:when test="${questionTypeId==106}">
		<!-- 改错-->
		<jsp:include page="correction/add.jsp"/>
		</c:when>
		<c:when test="${questionTypeId==19}">
		<!-- 听力-->
		<jsp:include page="listen/adddRead.jsp"/>
		</c:when>
		<c:when test="${questionTypeId==8}">
		<!-- 填空型完形填空 -->
		<jsp:include page="complex/cloze.jsp"/>
		</c:when>
		<c:when test="${questionTypeId==15}">
		<!-- 选择型完形填空题 -->
		<jsp:include page="complex/add.jsp"/>
		</c:when>
		<c:when test="${questionTypeId==36}">
		<!-- 选择填空题 -->
		<jsp:include page="complex/choiceBlank.jsp"/>
		</c:when>
		<c:otherwise>
		暂时无输入模板
		</c:otherwise>
	</c:choose>
	
	</body>
</html>