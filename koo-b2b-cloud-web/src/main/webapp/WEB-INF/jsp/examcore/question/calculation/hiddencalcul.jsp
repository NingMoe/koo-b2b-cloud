<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" import="com.koolearn.exam.question.entity.Question" %>
<input type="hidden" name="questionDto.question.questionTypeId" value="<%=Question.QUESTION_TYPE_FILL_CALCULATION %>">
<c:if test="${empty(essayQuestionDTO.questionDto.question.id)}">
	<input type="hidden" id="questionDto_question_id" name="questionDto_question_id" value="0"/>
</c:if>
<c:if test="${not empty(essayQuestionDTO.questionDto.question.id)}">
	<input type="hidden" id="questionDto_question_id" name="questionDto_question_id" value="${essayQuestionDTO.questionDto.question.id}"/>
</c:if>
<c:if test="${empty(essayQuestionDTO.questionDto.question.teId)}">
	<input type="hidden" name="questionDto.question.teId" value="0"/>
</c:if>
<c:if test="${not empty(essayQuestionDTO.questionDto.question.teId)}">
	<input type="hidden" name="questionDto.question.teId" value="${essayQuestionDTO.questionDto.question.teId}"/>
</c:if>
<c:if test="${empty(essayQuestionDTO.questionDto.saveType)}">
	<input type="hidden" name="questionDto.saveType" value="0"/>
</c:if>
<c:if test="${not empty(essayQuestionDTO.questionDto.saveType)}">
	<input type="hidden" name="questionDto.saveType" value="${essayQuestionDTO.questionDto.saveType}"/>
</c:if>
<c:if test="${empty(essayQuestionDTO.questionDto.responseType)}">
	<input type="hidden" name="questionDto.responseType" value="0"/>
</c:if>
<c:if test="${not empty(essayQuestionDTO.questionDto.responseType)}">
	<input type="hidden" name="questionDto.responseType" value="${essayQuestionDTO.questionDto.responseType}"/>
</c:if>

<c:if test="${empty(essayQuestionDTO.essayQuestion.id)}">
	<input type="hidden" name="essayQuestion.id" value="0"/>
</c:if>
<c:if test="${not empty(essayQuestionDTO.essayQuestion.id)}">
	<input type="hidden" name="essayQuestion.id" value="${essayQuestionDTO.essayQuestion.id}"/>
</c:if>
<input type="hidden" name="questionDto.textarea_param" value=""/>
