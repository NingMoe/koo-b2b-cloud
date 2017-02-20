<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.QuestionBarHtml" %>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.TestPaper" %>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.PaperQuestionType" %>
<%@ page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto" %>
<%@ page import="com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto" %>
<%@ page import="com.koolearn.cloud.util.numberToChinese.NumberText" %>
<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<form id="jp-form-test" action=""  method="post">
        <div>
                <div class="">
                    <div class="p-content p-quest" id="jp-question-box" style="width:100%">
                        <%--迭代题型试题--%>
                            <%
                                QuestionViewDto questionViewDto= new QuestionViewDto();
                                questionViewDto.setButtonType(QuestionViewDto.button_ype_zuoye_person_situation);
                                List<IExamQuestionDto> questionDtoList=(List<IExamQuestionDto>)request.getAttribute("questionDtoList");
                                int count=1;// 试卷题序
                            %>
                            <div data-type =""  class="p-tbox">
                                 <%
                                     for(IExamQuestionDto questionDto:questionDtoList){
                                         questionViewDto.setQuestionNo((count++)+"");
                                         questionViewDto.setViewType(QuestionViewDto.view_type_all);
                                         questionViewDto.setScore(questionDto.getQuestionDto().getQuestion().getQuestionViewDto().getScore());//题目分数
                                         request.setAttribute("questionViewDto", questionViewDto);
                                         request.setAttribute("questionDto",questionDto);%>
                                        <jsp:include page="/WEB-INF/jsp/examcore/question/questionSingleView.jsp">
                                            <jsp:param name="questionViewDto" value="${questionViewDto}"/>
                                            <jsp:param name="questionDto" value="${questionDto}"/>
                                        </jsp:include>
                                  <% }  %>
                              </div>
                    </div>
                </div>
        </div>
</form>
    <script type="text/javascript">
        $("#jp-question-box").on("click",".jp-collect",function(){
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
    </script>
	<script type="text/javascript">
	//老师端题目不可编辑
	$("textarea.dhl_xz").attr("disabled","disabled");
	$(".dhl_fill").attr("disabled","disabled");
	initStyle();//处理下方知识点样式
	</script>
