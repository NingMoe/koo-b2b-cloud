<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" import="com.koolearn.exam.question.entity.Question" %>
<input type="hidden" id="questionDto.question.questionTypeId" name="questionDto.question.questionTypeId" value="<%=Question.QUESTION_TYPE_SORT%>">
<c:if test="${empty(choiceQuestionDto.questionDto.question.id)}">
	<input type="hidden" id="questionDto_question_id" name="questionDto_question_id" value="0"/>
</c:if>
<c:if test="${not empty(choiceQuestionDto.questionDto.question.id)}">
	<input type="hidden" id="questionDto_question_id" name="questionDto_question_id" value="${choiceQuestionDto.questionDto.question.id}"/>
</c:if>
<c:if test="${empty(choiceQuestionDto.questionDto.question.teId)}">
	<input type="hidden" name="questionDto.question.teId" value="0"/>
</c:if>
<c:if test="${not empty(choiceQuestionDto.questionDto.question.teId)}">
	<input type="hidden" name="questionDto.question.teId" value="${choiceQuestionDto.questionDto.question.teId}"/>
</c:if>
<c:if test="${empty(choiceQuestionDto.questionDto.saveType)}">
	<input type="hidden" name="questionDto.saveType" value="0"/>
</c:if>
<c:if test="${not empty(choiceQuestionDto.questionDto.saveType)}">
	<input type="hidden" name="questionDto.saveType" value="${choiceQuestionDto.questionDto.saveType}"/>
</c:if>
<c:if test="${empty(choiceQuestionDto.questionDto.responseType)}">
	<input type="hidden" name="questionDto.responseType" value="0"/>
</c:if>
<c:if test="${not empty(choiceQuestionDto.questionDto.responseType)}">
	<input type="hidden" name="questionDto.responseType" value="${choiceQuestionDto.questionDto.responseType}"/>
</c:if>
<c:if test="${empty(choiceQuestionDto.choiceQuestion.id)}">
	<input type="hidden" name="choiceQuestion.id" value="0"/>
</c:if>
<c:if test="${not empty(choiceQuestionDto.choiceQuestion.id)}">
	<input type="hidden" name="choiceQuestion.id" value="${choiceQuestionDto.choiceQuestion.id}"/>
</c:if>
<c:if test="${empty(choiceQuestionDto.choiceQuestion.questionId)}">
	<input type="hidden" name="choiceQuestion.questionId" value="0"/>
</c:if>
<c:if test="${not empty(choiceQuestionDto.choiceQuestion.questionId)}">
	<input type="hidden" name="choiceQuestion.questionId" value="${choiceQuestionDto.choiceQuestion.questionId}"/>
</c:if>
<input type="hidden" name="questionDto.textarea_param" value=""/>

<input type="hidden" id="questionDto.questionBankExt.tag1" name="questionDto.questionBankExt.tag1" value="${choiceQuestionDto.questionDto.questionBankExt.tag1 }"/>
<input type="hidden" id="questionDto.questionBankExt.tag2" name="questionDto.questionBankExt.tag2" value="${choiceQuestionDto.questionDto.questionBankExt.tag2 }"/>
<input type="hidden" id="questionDto.questionBankExt.tag3" name="questionDto.questionBankExt.tag3" value="${choiceQuestionDto.questionDto.questionBankExt.tag3 }"/>
<input type="hidden" id="questionDto.questionBankExt.status" name="questionDto.questionBankExt.status" value="${choiceQuestionDto.questionDto.questionBankExt.status }"/>
	