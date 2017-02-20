<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.koolearn.cloud.exam.examcore.util.QuestionUtil"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.template.TemplateFtl"%>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>

<%
    List<IExamQuestionDto> questionList= (List<IExamQuestionDto>) request.getAttribute("questionList");
    if(questionList!=null &&questionList.size()>0){
        for(IExamQuestionDto questionDto:questionList){
            questionDto.getQuestionDto().getQuestion().getQuestionViewDto().setButtonType(QuestionViewDto.button_ype_zuoye_error);
            questionDto.getQuestionDto().getQuestion().getQuestionViewDto().setQuestionNo(" ");
            questionDto.getQuestionDto().getQuestion().getQuestionViewDto().initSubViewByPaprentView();
            request.setAttribute("questionDto",questionDto);%>
<jsp:include page="/WEB-INF/jsp/examcore/question/questionSingleView.jsp">
    <jsp:param name="questionViewDto" value="${questionViewDto}"/>
    <jsp:param name="questionDto" value="${questionDto}"/>
</jsp:include>
<% }%>
<c:if test="${not empty listPager}">
<!--页码 B-->
<koo:pager name="listPager" iteration="true" link="javaScript:serachFenye({p});" ></koo:pager>
<!--页码 E-->
</c:if>
<%}else{%><div class="i-no-result">学霸君，还没有错题，继续保持喔！</div><%  } %>