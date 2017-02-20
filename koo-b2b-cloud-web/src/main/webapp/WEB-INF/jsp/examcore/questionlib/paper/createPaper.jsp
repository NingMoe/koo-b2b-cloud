<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.QuestionBar" %>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.QuestionBarHtml" %>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.TestPaper" %>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.PaperQuestionType" %>
<%@ page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto" %>
<%@ page import="com.koolearn.cloud.exam.examcore.question.template.TemplateFtl" %>
<%@ page import="com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto" %>
<%@ page import="com.koolearn.cloud.util.numberToChinese.NumberText" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<fe:html title="题库-生成试卷" initSeajs="true" ie="true" defaultHead="<html>"
         assets="/project/b-ms-cloud/1.x/css/t-tiku-test/page.css,
         /project/b-ms-cloud/1.x/js/common/formJs/jquery-form.js">
<body>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="tk"/>
</jsp:include>
<!--#include virtual="/project/b-ms-cloud/1.x/include/_header.html"-->
<% TestPaper testPaper= (TestPaper) request.getAttribute("testPaper");%>
<form id="jp-form-test" action=""  method="post">
        <div class="i-main">
        <!--#include virtual="/project/b-ms-cloud/1.x/include/_tiku-top.html"-->
            <jsp:include page="/WEB-INF/jsp/examcore/questionlib/questiontop.jsp">
                <jsp:param name="nav" value="${paperFilter.navigation}"/>
            </jsp:include>
                <div class="p-test-info">
                    <!--修改前的样式-->
                    <div class="fc" id="jp-change-tbox">
                        <a href="javascript:;" class="p-test-bt fr" id="jp-change-title">修改标题</a>
                        <div class="p-test-name" id="jp-tname"><%=testPaper.getPaperName()%></div>
                    </div>
                    <!--修改后的样式-->
                    <div class="fc hide" id="jp-change-cbox">
                        <a href="javascript:;" class="p-test-qd fr" id="jp-change-confirm">确定</a>
                        <div class="p-test-input"><input id="jp-litle-input" type="text" name="" value="<%=testPaper.getPaperName()%>"/></div>
                    </div>
                </div>
                <div class="fc">
                    <div class="p-side fl" id="jp-side">
                        <% QuestionBarHtml questionBarHtml= (QuestionBarHtml) request.getAttribute("questionBarHtml");
                            request.setAttribute("questionBarHtml",questionBarHtml);
//                       out.println(questionBarHtml.getHtmlQuestionNum());
                        %>
                       <jsp:include page="createPaperTemp.jsp">
                         <jsp:param name="questionBarHtml" value="questionBarHtml"/>
                       </jsp:include>
                    </div>

                    <div class="p-content fr" id="jp-question-box">
                        <%--迭代题型试题--%>
                            <% NumberText nt = NumberText.getInstance(NumberText.Lang.ChineseSimplified);
                                long typeCount=0;
                                QuestionViewDto questionViewDto=(QuestionViewDto)request.getAttribute("questionViewDto");
                                int count=1;// 试卷题序
                               for(PaperQuestionType questionType:testPaper.getPaperQuestionTypeList()){
                                   if(questionType.getQuestionDtoList()!=null && questionType.getQuestionDtoList().size()>0) typeCount++;
                                   //隐藏没有题的题型
                                   if(questionType.getQuestionDtoList()==null||questionType.getQuestionDtoList().size()<1) continue;
                            %>
                                <div data-type ="<%=questionType.getQuestionType()%>"  class="p-tbox">
                                  <div class="p-tname"><%=nt.getText(typeCount)%>、<%=questionType.getQuestionTypeName()%>（共<span><%=questionType.getQuestionDtoList().size()%></span>小题）</div>
                                     <%
                                         for(IExamQuestionDto questionDto:questionType.getQuestionDtoList()){
                                             questionViewDto.setQuestionNo((count++)+"");
                                             questionViewDto.setScore(questionDto.getQuestionDto().getQuestion().getQuestionViewDto().getScore());
                                             questionDto.getQuestionDto().getQuestion().getQuestionViewDto().setButtonType(questionViewDto.getButtonType());
                                             questionDto.getQuestionDto().getQuestion().getQuestionViewDto().setViewType(questionViewDto.getViewType());
                                             request.setAttribute("questionViewDto", questionViewDto);
                                             request.setAttribute("questionDto",questionDto);%>
                                            <jsp:include page="/WEB-INF/jsp/examcore/question/questionSingleView.jsp">
                                                <jsp:param name="questionViewDto" value="${questionViewDto}"/>
                                                <jsp:param name="questionDto" value="${questionDto}"/>
                                            </jsp:include>
                                            <% }  %>
                                  </div>
                              <% }  %>
                    </div>
                </div>
        </div>
    <input  type="hidden" value="${paperFilter.subjectId}" id="subjectId" name="subjectId" />
    <input  type="hidden" value="${paperFilter.rangeId}" id="rangeId" name="rangeId" />
    <input  type="hidden" value="${paperFilter.bookVersion}" id="bookVersion" name="bookVersion" />
    <input  type="hidden" value="${paperFilter.obligatoryId}" id="obligatoryId" name="obligatoryId" />
    <input  type="hidden" value="${paperFilter.tagId}" id="tagId" name="tagId" />
</form>
<div class="i-foot">
    <a href="javascript:;" class="green-btn green-btn-style" id="jp-conserver-btn">保存习题</a>
    <%--<a href="javascript:;" class="green-btn green-btn-style" id="jp-download-btn">下载习题</a>--%>
    <%--<a href="#" class="orange-btn">布置作业</a>--%>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</body>
    <script type="text/javascript">
        seajs.config({
            alias:{
                'project/b-ms-cloud/1.x/js/t-tiku-test/page': 'project/b-ms-cloud/1.x/js/t-tiku-test/page.js'
            }
        });
        seajs.use(['project/b-ms-cloud/1.x/js/t-tiku-test/page'],function(app){
            app.init({
                updateQuestionBar: '/teacher/exam/core/paper/barUpdate',//更新试题栏
                changeQuestion: '/exam/core/questionChange',//打开话题页面
                getQuestionTpl: '/exam/core/searchQuestion',//返回要替换的题目页面
                getQuestionBar: '/teacher/exam/core/paper/barInit',//初始化试题栏
                savePaper: '/teacher/exam/core/paper/save',
                clearCache: '/teacher/exam/core/paper/barDel',//清空
                sendData: {
                    createFrom: '${paperFilter.createFrom}',//试卷创建来源
                    navigation:'${paperFilter.navigation}', //组卷类型,
                    subjectId:'${paperFilter.subjectId}',
                    rangeId:'${paperFilter.rangeId}',
                    bookVersion:'${paperFilter.bookVersion}',
                    obligatoryId:'${paperFilter.obligatoryId}',
                    tagId:'${paperFilter.tagId}'
                }
            });
        });
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
        initStyle();//处理下方知识点样式
    </script>
</fe:html>