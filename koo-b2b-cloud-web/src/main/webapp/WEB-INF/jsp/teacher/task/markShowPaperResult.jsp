<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.QuestionBarHtml" %>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.TestPaper" %>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.PaperQuestionType" %>
<%@ page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto" %>
<%@ page import="com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto" %>
<%@ page import="com.koolearn.cloud.exam.examProcess.vo.ResultVO" %>
<%@ page import="com.koolearn.cloud.exam.examProcess.vo.ResultStructureVO" %>
<%@ page import="com.koolearn.cloud.exam.examProcess.vo.ResultDetailVO" %>
<%@ page import="com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail" %>
<%@ page import="com.koolearn.cloud.util.numberToChinese.NumberText" %>
<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
        <div>
                <div class="">
                	<div class="p-content p-quest" id="jp-question-box" style="width:100%">
                        <%--迭代题型试题--%>
			            <% 
			            	Map<String, TpExamResultDetail> detailsMap= (Map<String, TpExamResultDetail>) request.getAttribute("detailsMap");
			            	TestPaper testPaper= (TestPaper) request.getAttribute("testPaper");
			                NumberText nt = NumberText.getInstance(NumberText.Lang.ChineseSimplified);
			                long typeCount=0;
			                QuestionViewDto questionViewDto=(QuestionViewDto)request.getAttribute("questionViewDto");
			                int count=1;// 试卷题序
			               for(PaperQuestionType questionType:testPaper.getPaperQuestionTypeList()){
			                   if(questionType.getQuestionDtoList()!=null && questionType.getQuestionDtoList().size()>0) typeCount++;
			                   //隐藏没有题的题型
			                   if(questionType.getQuestionDtoList()==null||questionType.getQuestionDtoList().size()<1) continue;
			                   System.out.println("-------------------题结构展示-----------------------");
			            %>
			                <div data-type ="<%=questionType.getQuestionType()%>"  class="p-tbox">
			                  <div class="p-tname"><%=nt.getText(typeCount)%>、<%=questionType.getQuestionTypeName()%>（共<span><%=questionType.getQuestionDtoList().size()%></span>小题）</div>
			                     <%
			                     System.out.println("-------------------进入题展示-----------------------");
			                         for(IExamQuestionDto questionDto:questionType.getQuestionDtoList()){
			                             questionViewDto.setQuestionNo((count++)+"");
			                             questionViewDto.setButtonType(QuestionViewDto.button_ype_zuoye_detaile);
			                            request.setAttribute("questionViewDto", questionViewDto);
			                            request.setAttribute("questionDto",questionDto);%>
			                            <jsp:include page="/WEB-INF/jsp/examcore/question/questionSingleView.jsp">
			                                <jsp:param name="questionViewDto" value="${questionViewDto}"/>
			                                <jsp:param name="questionDto" value="${questionDto}"/>
			                                <jsp:param name="markType" value="markType"/>
			                            </jsp:include>
			                            <% }  %>
			                  </div>
			              <% }  %>
                    </div>
                </div>
        </div>
<script type="text/javascript">
$(document).ready(function(){
    /*$("textarea.dhl_xz").attr("disabled","disabled");
    $(".dhl_fill").attr("disabled","disabled");*/
    initStyle();
});
</script>
