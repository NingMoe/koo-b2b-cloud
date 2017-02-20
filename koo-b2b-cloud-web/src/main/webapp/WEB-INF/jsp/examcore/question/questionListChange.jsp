<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto"%>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<!--组卷换题页面-->
<input type="hidden" name="questionCount" value="${questionCount}" id="questionCount"/>
<% List<IExamQuestionDto> questionList= (List<IExamQuestionDto>) request.getAttribute("questionList");%>
<div class="p-change">
    <ul class="fc p-tnum" id="jp-cn">
        <%
            int num=0;
            if(questionList!=null &&questionList.size()>0){num=questionList.size();}
            for (int i = 0; i <num ; i++) {
                if(i==0){%>
                <li class="s"><%=(i+1)%></li>
        <%}else{%>
              <li><%=(i+1)%></li>
        <%} } %>
    </ul>
    <div id="jp-tbox">
        <!-- 试题模板 S -->
        <%if(questionList==null || questionList.size()<1){%>
        <div id="nullinfo">没有找到相似题目</div>
        <%}%>
        <%
            QuestionViewDto questionViewDto=(QuestionViewDto)request.getAttribute("questionViewDto");
            if(questionList!=null &&questionList.size()>0){
                for(IExamQuestionDto questionDto:questionList){
                    questionDto.getQuestionDto().getQuestion().getQuestionViewDto().setButtonType(questionViewDto.getButtonType());
                    questionDto.getQuestionDto().getQuestion().getQuestionViewDto().setViewType(questionViewDto.getViewType());
                    request.setAttribute("questionViewDto",questionViewDto);
                    request.setAttribute("questionDto",questionDto);%>
        <jsp:include page="questionSingleView.jsp">
            <jsp:param name="questionViewDto" value="${questionViewDto}"/>
            <jsp:param name="questionDto" value="${questionDto}"/>
        </jsp:include>
        <% } } %>
        <!-- 试题模板 E -->

    </div>
</div>
<script>
    $(".p-change").on("click",".jp-quest-temp .collect-btn",function(){
        //题目收藏
        var hasCur=$(this).hasClass('cur');
        var _this=this;
        var qid=$(this).attr("data-qustid");
        setTimeout(function(){
            $.ajax({
                url: "/teacher/exam/core/question/collection?questionId="+qid,    //请求的url地址
                dataType: "json",   //返回格式为json
                async: true,//请求是否异步，默认为异步，这也是ajax重要特性
                type: "post",   //请求方式
                success: function (data) {
                    if(hasCur){
                        $(_this).removeClass('cur');
                    }else{
                        $(_this).addClass('cur');
                    }
                }, error: function () {
                    if(hasCur){
                        $(_this).removeClass('cur');
                    }else{
                        $(_this).addClass('cur');
                    }
                }
            })
        },1000);
    });
    initStyle();//处理下方知识点样式
</script>