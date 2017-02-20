<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty(readQuestionDto.questionDto.question.id)}">
	<input type="hidden" name="questionDto_question_id" value="0"/>
	<input type="hidden" name="questionDto.question.newVersion" value="0"/>
</c:if>
<c:if test="${not empty(readQuestionDto.questionDto.question.id)}">
	<input type="hidden" name="questionDto_question_id" value="${readQuestionDto.questionDto.question.id}"/>
</c:if>
<c:if test="${empty(readQuestionDto.questionDto.saveType)}">
	<input type="hidden" name="questionDto.saveType" value="0"/>
</c:if>
<c:if test="${not empty(readQuestionDto.questionDto.saveType)}">
	<input type="hidden" name="questionDto.saveType" value="${readQuestionDto.questionDto.saveType}"/>
</c:if>
<c:if test="${empty(readQuestionDto.complexQuestion.id)}">
	<input type="hidden" name="complexQuestion.id" value="0"/>
</c:if>
<c:if test="${not empty(readQuestionDto.complexQuestion.id)}">
	<input type="hidden" name="complexQuestion.id" value="${readQuestionDto.complexQuestion.id}"/>
</c:if>
<c:if test="${empty(readQuestionDto.questionDto.responseType)}">
	<input type="hidden" name="questionDto.responseType" value="0"/>
</c:if>
<c:if test="${not empty(readQuestionDto.questionDto.responseType)}">
	<input type="hidden" name="questionDto.responseType" value="${readQuestionDto.questionDto.responseType}"/>
</c:if>
<input type="hidden" name="questionDto.question.atomic" value="1"/>
	
	<input type="hidden" id="questionTypeTab" name="questionTypeTab" value="${questionTypeTab==null?0:1 }"/>
	<input type="hidden" id="questionDto.textarea_param" name="questionDto.textarea_param" value=""/>
	<input type="hidden" id="questionDto.questionBankExt.tag1" name="questionDto.questionBankExt.tag1" value="${readQuestionDto.questionDto.questionBankExt.tag1 }"/>
	<input type="hidden" id="questionDto.questionBankExt.tag2" name="questionDto.questionBankExt.tag2" value="${readQuestionDto.questionDto.questionBankExt.tag2 }"/>
	<input type="hidden" id="questionDto.questionBankExt.tag3" name="questionDto.questionBankExt.tag3" value="${readQuestionDto.questionDto.questionBankExt.tag3 }"/>
	<input type="hidden" id="questionDto.questionBankExt.status" name="questionDto.questionBankExt.status" value="${readQuestionDto.questionDto.questionBankExt.status }"/>
	<input type="hidden" id="questionDto.questionBankExt.schoolFrom" name="questionDto.questionBankExt.schoolFrom" value="0"/>