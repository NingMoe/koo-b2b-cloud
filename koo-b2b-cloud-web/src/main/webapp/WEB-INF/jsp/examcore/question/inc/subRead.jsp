<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<c:if test="${fn:length(readQuestionDto.subItems)>0}">
			<tr>
				<th class="td41">子试题编码</th><th class="td44">子试题题型</th><th class="td42">子试题题干</th><th class="td45">操作</th>
			</tr>
		<c:forEach var="name" items="${readQuestionDto.subItems}">
			<tr>
				<td class="td41">
				
				<a href="javascript:;" style="display: none;" class="a1" questionId="${name.questionDto.question.id}" typeId="${name.questionDto.question.questionTypeId}">${name.questionDto.question.code}</a>
				${name.questionDto.question.code}
				</td>
				<td class="td44">
				<%@include file="../subQuestionType.inc"%> 
				</td>
				<td class="td42">${name.shortTopic}</td>
				<td class="td45"><span class="fn"><a href="javascript:;" class="edit"></a><a href="javascript:;" class="add"></a><a href="javascript:;" class="remove"></a><a href="javascript:;" class="up"></a><a href="javascript:;" class="down"></a></span></td>
			</tr>
		</c:forEach>
</c:if>