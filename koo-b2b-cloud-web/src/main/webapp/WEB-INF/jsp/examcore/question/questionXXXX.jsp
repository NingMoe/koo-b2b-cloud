<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.koolearn.cloud.exam.examcore.question.entity.Question" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%--题目模版信息调试页面--%>
<%--学生作答页面、老师个人详情【 大题viewType:0，小题：2】--%>
<%--老师题库、组卷、试卷详情【 viewType:1】--%>
<%Question qtest=(Question)request.getAttribute("qtest");%>
  【 viewType:${qview.viewType}】
  <c:if test="${qtest.questionTypeId==1||qtest.questionTypeId==6 }">【1多选、6单选choiceQuestion】${qtest.questionTypeId}</c:if>
  <c:if test="${qtest.questionTypeId==2 }">【填空essayQuestion】${qtest.questionTypeId}</c:if>
  <c:if test="${qtest.questionTypeId==3}">【简答shortQuestion】${qtest.questionTypeId}</c:if>
  <c:if test="${qtest.questionTypeId==4 }">【写作writeQuestion】${qtest.questionTypeId}</c:if>
  <c:if test="${qtest.questionTypeId==7 ||qtest.questionTypeId==19}">【7阅读理解题.19听力题readQuestion】${qtest.questionTypeId}</c:if>
  <c:if test="${qtest.questionTypeId==15 }">【选择型完形填空题choiceFillBlankQuestion】${qtest.questionTypeId}</c:if>
  <c:if test="${qtest.questionTypeId==106}">【改错题correctionQuestion】${qtest.questionTypeId}</c:if>
  <c:if test="${qtest.questionTypeId==8 }">【填空完形填空clozeFillQuestion】${qtest.questionTypeId}</c:if>
