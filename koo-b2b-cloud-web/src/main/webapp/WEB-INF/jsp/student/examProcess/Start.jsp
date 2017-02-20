<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.QuestionBarHtml" %>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.TestPaper" %>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.PaperQuestionType" %>
<%@ page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto" %>
<%@ page import="com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto" %>
<%@ page import="com.koolearn.cloud.util.numberToChinese.NumberText" %>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.QuestionBarHtml" %>
<%@page import="com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<fe:html title="作业详情" ie="true" initSeajs="true" 
assets="/project/b-ms-cloud/1.x/css/s-zy-subjective/page.css,
/project/b-ms-cloud/1.x/common/webuploader/css/webuploader.css,
/project/b-ms-cloud/1.x/common/webuploader/js/webuploader-min.js,
/common/dialog/css/ui-dialog.css,
/project/b-ms-cloud/1.x/js/common/dialog/css/dialog.css">
<body>
<jsp:include page="/WEB-INF/jsp/examcore/question/q-header.jsp"></jsp:include>
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
        <div class="p-cont fl">
            <div class="p-exam-content">
                   <%--迭代题型试题--%>
                       <% 
                       	   Map<String, TpExamResultDetail> detailsMap = (Map<String, TpExamResultDetail>)request.getAttribute("detailsMap");
                       System.out.println(detailsMap);
                       	   TestPaper testPaper= (TestPaper) request.getAttribute("testPaper");
                           NumberText nt = NumberText.getInstance(NumberText.Lang.ChineseSimplified);
                           long typeCount=0;
                           QuestionViewDto questionViewDto=(QuestionViewDto)request.getAttribute("questionViewDto");
                           questionViewDto.setButtonType(questionViewDto.getButtonType());
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
                                        QuestionViewDto viewDto=questionDto.getQuestionDto().getQuestion().getQuestionViewDto();
                                        viewDto.setViewType(questionViewDto.getViewType());
                                        viewDto.setQuestionNo((count++) + "");
                                        System.out.println("count=="+count);
                                        viewDto.initSubViewByPaprentView();//初始化子题viewtype
                                        questionDto.getQuestionDto().getQuestion().setQuestionViewDto(viewDto);
                                        request.setAttribute("questionDto",questionDto);%>
                                       <jsp:include page="/WEB-INF/jsp/examcore/question/questionSingleView.jsp">
                                           <jsp:param name="questionViewDto" value="${questionViewDto}"/>
                                           <jsp:param name="questionDto" value="${questionDto}"/>
                                       </jsp:include>
                                       <% }  %>
                             </div>
                         <% }  %>
            </div>
            <div class="p-exam-btn-box">
                <div class="path-btn-box">
                   <a href="javascript:;" class="green-btn p-prev-quest">上一题</a>
                   <a href="javascript:;" class="green-btn p-next-quest">下一题</a>
                   <a href="javascript:;" class="green-btn" id="hand_exam" style="display: none;">交 卷</a>
                </div>
            </div>
        </div>
        <!-- 右侧 -->
        <div class="p-side fr">
            <div class="p-item-title-box fc">
                <div class="first-item-title fl item-selected">题目</div>
            </div>
            <div class="p-item-content-box">
                <div class="first-content-box">
                    <%
                    	QuestionBarHtml questionBarHtml = (QuestionBarHtml)request.getAttribute("questionBarHtml");
                    	out.println(questionBarHtml.getHtmlQuestionNum()); %>
                </div>
            </div>
        </div>
    </div>
</div>
<c:if test="${empty urlType}">
<jsp:include page="/footer.jsp"></jsp:include>
</c:if>
<input type="hidden" id="subjectId" name="subjectId" value="${exam.subjectId}"/>
<input type="hidden" id="hidBasePath" name="hidBasePath" value="<%=basePath%>"/>
<input type="hidden" id="hidTime" name="hidTime" value="${time}" />
<input type="hidden" id="hidMM" name="hidMM" value="${mm}" />
<input type="hidden" id="hidSS" name="hidSS" value="${ss}" />
<input type="hidden" id="hidExamType" name="hidExamType" value="${examType}" />
<input type="hidden" id="hidExamId" name="hidExamId" value="${exam.id}" />
<input type="hidden" id="hidUrKey" name="hidUrKey" value="${urKey}" />
<input type="hidden" id="hidUserResult" name="hidUserResult" value='${userResult}' />
<input type="hidden" id="hidResultId" name="hidResultId" value='${resultId}' />
<input type="hidden" id="jp-handexam" name="jp-handexam" value='${handExam}' />
<input type="hidden" id="jp-urlType" name="jp-urlType" value='${urlType}' />
<input type="hidden" id="jp-classRoomId" name="jp-classRoomId" value='${classRoomId}' />
<%-- 自动交卷遮罩层开始 --%>
<div id="jsalert"  style="display: none;width:100%;height:100%;position: fixed;left:0;top: 0; z-index: 320;">
	<div class="jsalert">
		<p class="title fc">
			<span>温馨提示</span>
		</p>
		<div class="txt">
			考试时间到，正在为您自动提交答案！请不要关闭窗口！
		</div>
	</div>
	<div id="bgDiv" class="bgDiv" style="height: 1000px;"></div>
</div>
<%-- 自动交卷遮罩层结束 --%>
<jsp:include page="/ueditor/jsp/ueditorJs.jsp"/>
</body>
<%-- JS文件引用开始 --%>
<jsp:include page="JsCite.jsp" />
<%-- JS文件引用结束 --%>
<script type="text/javascript">
// 点击我要交卷的操作
function handExam(){
	submit();
}

    seajs.config({
        alias:{
            'project/b-ms-cloud/1.x/js/s-zy-subjective/page': 'project/b-ms-cloud/1.x/js/s-zy-subjective/page.js'
        }
    });
    seajs.use('project/b-ms-cloud/1.x/js/s-zy-subjective/page',function(init){
        init({
            fileUrl:'/student/pc/fileUpload',//上传地址
            fileResultId:'${resultId}'
        });
    });
</script>
</fe:html>
