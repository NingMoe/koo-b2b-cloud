<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.QuestionBarHtml" %>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.TestPaper" %>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.PaperQuestionType" %>
<%@ page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto" %>
<%@ page import="com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page import="com.koolearn.cloud.util.numberToChinese.NumberText" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<fe:html title="题库-试卷预览" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-tiku-detail/page.css,
         /project/b-ms-cloud/1.x/js/common/formJs/jquery-form.js">
<body>
<%TestPaper testPaper= (TestPaper) request.getAttribute("testPaper");
    String bt="";
    if(testPaper!=null){
        bt=testPaper.isJoin()?"移出我的试卷库":"加入我的试卷库";
    }%>
<c:set value="<%=bt%>" var="bt"/>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="tk"/>
</jsp:include>
<!--#include virtual="/project/b-ms-cloud/1.x/include/_header.html"-->
<form id="jp-form-chos" action=""  method="post">
        <div class="i-main">
        <!--#include virtual="/project/b-ms-cloud/1.x/include/_tiku-top.html"-->
            <jsp:include page="/WEB-INF/jsp/examcore/questionlib/questiontop.jsp">
                <jsp:param name="nav" value="${paperFilter.navigation}"/>
            </jsp:include>
                <div class="p-test-info">
                    <!--修改前的样式-->
                    <div class="fc" id="jp-change-tbox">
                        <%--<a href="javascript:;" class="p-test-bt fr" id="jp-change-title">修改标题</a>--%>
                        <div class="p-test-name" id="jp-tname"><%=testPaper==null?"":testPaper.getPaperName()%> </div>
                        <div style="font-size:12px;height:30px;">
                            <span class="fl" style="display: inline-block;line-height: 30px;">浏览：<%=testPaper==null?"":testPaper.getBrowseTimes()%></span>
                            <a href="javascript:;" id="<%=testPaper==null?"":testPaper.getId()%>" class="fr white-btn-gry btn-set jion-button" style="margin-top:1px;width: 112px;">${bt}</a>
                        </div>
                    </div>
                </div>

                    <div class="p-quest" id="jp-quest">
                        <%--迭代题型试题--%>
                            <% //TestPaper testPaper= (TestPaper) request.getAttribute("testPaper");
                                NumberText nt = NumberText.getInstance(NumberText.Lang.ChineseSimplified);
                                long typeCount=0;
                                QuestionViewDto questionViewDto=(QuestionViewDto)request.getAttribute("questionViewDto");
                                int count=1;// 试卷题序
                                if(testPaper!=null&&testPaper.getPaperQuestionTypeList()!=null&&testPaper.getPaperQuestionTypeList().size()>0){
                               for(PaperQuestionType questionType:testPaper.getPaperQuestionTypeList()){
                                   if(questionType.getQuestionDtoList()!=null && questionType.getQuestionDtoList().size()>0) typeCount++;
                                   //隐藏没有题的题型
                                   if(questionType.getQuestionDtoList()==null||questionType.getQuestionDtoList().size()<1) continue;
                            %>
                                <div data-type ="<%=questionType.getQuestionType()%>"  class="p-tbox">
                                  <div class="p-tname"><%=nt.getText(typeCount)%>、<%=questionType.getQuestionTypeName()%>（共<span><%=questionType.getQuestionDtoList().size()%></span>小题）</div>
                                     <%
                                         for(IExamQuestionDto questionDto:questionType.getQuestionDtoList()){
                                             questionDto.getQuestionDto().getQuestion().getQuestionViewDto().setQuestionNo((count++)+"");
                                             request.setAttribute("showGoodIco", true);
                                             request.setAttribute("questionViewDto", questionViewDto);
                                             request.setAttribute("questionDto",questionDto);%>
                                            <jsp:include page="/WEB-INF/jsp/examcore/question/questionSingleView.jsp">
                                                <jsp:param name="questionViewDto" value="${questionViewDto}"/>
                                                <jsp:param name="questionDto" value="${questionDto}"/>
                                            </jsp:include>
                                            <% }  %>
                                  </div>
                              <% } } %>
                    </div>

        </div>
    <input  type="hidden" value="${testPaper.subjectId}" id="subjectId" name="subjectId" />
    <input  type="hidden" value="${testPaper.rangeId}" id="rangeId" name="rangeId" />
    <input  type="hidden" value="${paperFilter.bookVersion}" id="bookVersion" name="bookVersion" />
    <input type="hidden" name="navigation"   value="<%=GlobalConstant.PAPER_CREATE_TYPE_SJZT%>"/>
    <input type="hidden" name="paperName"   value="<%=testPaper==null?"":testPaper.getPaperName()%>"/>
</form>
<!-- 试题栏-->
<jsp:include page="/WEB-INF/jsp/examcore/questionlib/questionBar.jsp">
    <jsp:param name="createType" value="3"/>
</jsp:include>
<jsp:include page="/footer.jsp"></jsp:include>
</body>
    <script type="text/javascript">
        seajs.config({
            alias:{
                'project/b-ms-cloud/1.x/js/t-tiku-detail/page': 'project/b-ms-cloud/1.x/js/t-tiku-detail/page.js'
            }
        });
        seajs.use(['project/b-ms-cloud/1.x/js/t-tiku-detail/page'],function(app){
            app.init({
               //组卷begin
                questionBar: '/teacher/exam/core/paper/barInit',
                clearCache: '/teacher/exam/core/paper/barDel',
                barUpdate: '/teacher/exam/core/paper/barUpdate',
                createPaper: '/teacher/exam/core/paper/create',
                previewPaper: '/teacher/exam/core/paper/preview'
                //组卷end

            });
        });
        $("#jp-quest").on("click",".jp-collect",function(){
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
        $("#jp-change-tbox").on("click",".jion-button",function(){
            var _this=$(this);
            var paperId=$(this).attr("id");
            var yc='移出我的试卷库';
            var jr='加入我的试卷库';
            var text=$(this).text();
            if(text==yc){
                text=jr;
            }else if(text==jr){
                text=yc;
            }
            $.ajax({
                type: "POST",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                url:  '${ctx }/teacher/exam/core/paper/jionMyself?paperId='+paperId,
                success: function(data) {
                    _this.text(text);
                }
            });
        });
        window._Q && window._Q.initPageFromLS();//刷新试题栏
        initStyle();//处理下方知识点样式
    </script>
</fe:html>