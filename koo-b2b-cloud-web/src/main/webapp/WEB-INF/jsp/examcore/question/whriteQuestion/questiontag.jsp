<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto"%>
<%@ page import="com.koolearn.cloud.exam.examcore.question.entity.Question" %>
<%@ page import="java.util.List" %>
<%@ page import="com.koolearn.cloud.exam.examcore.util.ConstantTe" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>

 <%
     IExamQuestionDto questionDto=(IExamQuestionDto)request.getAttribute("questionDto");
     Question question=questionDto.getQuestionDto().getQuestion();
     List<IExamQuestionDto> subDtoList=questionDto.getSubQuestions();
 %>
        考查能力：<%=StringUtils.isBlank(question.getKaoChaNl())?"":question.getKaoChaNl()%></br>
        知识点：<%=StringUtils.isBlank(question.getKnowledgeTags())?"":question.getKnowledgeTags()%></br>
        知识点FUll：<%=StringUtils.isBlank(question.getKnowledgeTagsFullPath())?"":question.getKnowledgeTagsFullPath()%></br>
        进度点：<%=StringUtils.isBlank(question.getTeacheringTags())?"":question.getTeacheringTags()%></br>
        进度点FUll：<%=StringUtils.isBlank(question.getTeacheringTagsFullPath())?"":question.getTeacheringTagsFullPath()%></br>
            <%
                if(question.getTeId()==0&&subDtoList!=null&&subDtoList.size()>0){
                    int index=1;
                    for(IExamQuestionDto subDto:subDtoList){
                        Question sub=subDto.getQuestionDto().getQuestion();%>
                        &nbsp;&nbsp;&nbsp;【第<%=index%>小题:<%=sub.getId()%>】</br>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;考查能力：<%=StringUtils.isBlank(sub.getKaoChaNl())?"":sub.getKaoChaNl()%></br>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;知识点：<%=StringUtils.isBlank(sub.getKnowledgeTags())?"":sub.getKnowledgeTags()%></br>
                        <%--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;知识点FUll：<%=StringUtils.isBlank(sub.getKnowledgeTagsFullPath())?"":sub.getKnowledgeTagsFullPath()%></br>--%>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;进度点：<%=StringUtils.isBlank(sub.getTeacheringTags())?"":sub.getTeacheringTags()%></br>
                        <%--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;进度点FUll：<%=StringUtils.isBlank(sub.getTeacheringTagsFullPath())?"":sub.getTeacheringTagsFullPath()%></br>--%>
                   <% index++;
                    }
                }
            %>
