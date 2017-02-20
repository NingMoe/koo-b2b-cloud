<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.QuestionBarHtml" %>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.TestPaper" %>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.PaperQuestionType" %>
<%@ page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto" %>
<%@ page import="com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto" %>
<%@ page import="com.koolearn.cloud.util.numberToChinese.NumberText" %>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.QuestionBarHtml" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<fe:html title="作业复习" ie="true" initSeajs="true" 
assets="/project/b-ms-cloud/1.x/css/t-hw-workreview/page.css,
/project/b-ms-cloud/1.x/css/s-zy-subjective/page.css">
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
<body>
<c:if test="${empty urlType}">
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/headerStudent.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="zy"/>
</jsp:include>
</c:if>
<!-- 公共的头部 E-->

<div class="i-main p-courvare">
    <div class="p-contvare fc">
        <c:if test="${empty urlType}">
        <div class="p-contvare-title">${exam.examName}</div>
        </c:if>
        <!-- 左侧 -->
        <div class="p-cont fl p-content" id="showPaper">
        	<%--迭代题型试题--%>
            <% 
            	   TestPaper testPaper= (TestPaper) request.getAttribute("testPaper");
                NumberText nt = NumberText.getInstance(NumberText.Lang.ChineseSimplified);
                long typeCount=0;
                QuestionViewDto questionViewDto=(QuestionViewDto)request.getAttribute("questionViewDto");
                questionViewDto.setButtonType(QuestionViewDto.button_ype_zuoye_person_situation);
				questionViewDto.setViewType(QuestionViewDto.view_type_review);
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
                             questionDto.getQuestionDto().getQuestion().getQuestionViewDto().setQuestionNo((count++)+"");
                             QuestionViewDto viewDto=questionDto.getQuestionDto().getQuestion().getQuestionViewDto();
                        	 System.out.println("questionViewDto.getViewType()="+questionViewDto.getViewType());
                             viewDto.setViewType(questionViewDto.getViewType());
                             viewDto.initSubViewByPaprentView();//初始化子题viewtype
                             questionDto.getQuestionDto().getQuestion().setQuestionViewDto(viewDto);
                             request.setAttribute("showGoodIco", false);//不显示精品
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
        <!-- 右侧 -->
        <div class="p-side fr">
            <div class="p-item-content-box">
                <div class="subject-all-score"><em>0</em>分</div>
                <!-- <div class="subject-ensure-acc">
                    <span>正确率：</span>
                    <span class="p-bar">
                        <span class="p-bar-over" style="width:0%"></span>
                    </span>
                    <span class="p-bar-num">0%</span>
                </div> -->
                <div class="subject-content-box">
                    <div class="p-item-title-box fc">
                        <div class="second-item-title fr">老师评语</div>
                        <div class="first-item-title fl item-selected">题目</div>
                    </div>
                    <div class="p-item-number-box" >
                    	<%
                    	QuestionBarHtml questionBarHtml = (QuestionBarHtml)request.getAttribute("questionBarHtml");
                    	out.println(questionBarHtml.getHtmlQuestionNum()); %>
                        <div class="second-content-box" style="display: none;" >
                            <textarea readonly="readonly"></textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">

    seajs.config({
        alias:{
            'project/b-ms-cloud/1.x/js/t-hw-workreview/page': 'project/b-ms-cloud/1.x/js/t-hw-workreview/page.js'
        }
    });
    seajs.use('project/b-ms-cloud/1.x/js/t-hw-workreview/page',function(exports){
        exports.init({
            isAjax:false
        });
    });
</script>
</fe:html>
