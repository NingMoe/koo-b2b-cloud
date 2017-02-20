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
			QuestionViewDto questionViewDto=(QuestionViewDto)request.getAttribute("questionViewDto");
            questionViewDto.setQuestionNo("");
            List<IExamQuestionDto> questionList= (List<IExamQuestionDto>) request.getAttribute("questionList");
            if(questionList!=null &&questionList.size()>0){
                for(IExamQuestionDto questionDto:questionList){
                    questionDto.getQuestionDto().getQuestion().getQuestionViewDto().setQuestionNo("");
                    QuestionUtil.getSubQuestionViewDto(questionDto,questionDto.getQuestionDto().getQuestion().getQuestionViewDto());
                    request.setAttribute("questionViewDto",questionViewDto);
                    request.setAttribute("questionDto",questionDto);%>
                       <jsp:include page="questionSingleView.jsp">
                           <jsp:param name="questionViewDto" value="${questionViewDto}"/>
                           <jsp:param name="questionDto" value="${questionDto}"/>
                       </jsp:include>
            <% }%>
                <!--页码 B-->
                <koo:pager name="listPager" iteration="true" link="javaScript:serachFenye({p});" ></koo:pager>
                <!--页码 E-->
            <%}else{%><div class="i-no-result">没有查询到相关数据</div><%  } %>
