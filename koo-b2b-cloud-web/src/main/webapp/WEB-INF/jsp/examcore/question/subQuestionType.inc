<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:choose>
	<c:when test="${name.questionDto.question.questionTypeId==6}">普通单选</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==20}">阴影单选</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==22}">段落单选</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==17}">阴影多选</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==23}">段落多选</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==1}">普通多选</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==14}">点句单选</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==21}">方框题</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==2}">普通填空</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==13}">拖拽排序</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==16}">计算填空</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==35}">判断单选</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==3}">简答题</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==4}">作文题</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==18}">口语题</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==101}">选择填空</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==102}">点句多选</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==37}">选词填空</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==38}">口语训练</c:when>
	<c:when test="${name.questionDto.question.questionTypeId==103}">引导作文</c:when>
	<c:when
		test="${name.questionDto.question.questionTypeId==12 && name.questionDto.question.showForm==1}">拖拽矩阵</c:when>
	<c:when
		test="${name.questionDto.question.questionTypeId==12 && name.questionDto.question.showForm==2}">表格矩阵</c:when>
	<c:when
		test="${name.questionDto.question.questionTypeId==12 && name.questionDto.question.showForm==3}">连线题</c:when>
	<c:otherwise>不知道的题型</c:otherwise>
</c:choose>