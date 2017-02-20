<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.koolearn.cloud.exam.examcore.util.QuestionUtil"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.QuestionViewDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.dto.IExamQuestionDto"%>
<%@page import="com.koolearn.cloud.exam.examcore.question.template.TemplateFtl"%>
<%@page import="com.koolearn.cloud.task.dto.QuestionErrUser"%>
<%@ page import="java.util.*" %>
<%@ page import="com.koolearn.cloud.exam.examcore.question.entity.Question" %>
<%@page import="com.koolearn.cloud.exam.examcore.exam.entity.TpExamResultDetail"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page import="com.koolearn.cloud.login.entity.UserEntity" %>
<%@ page import="com.koolearn.cloud.util.CacheTools" %>
<%@ page import="com.koolearn.sso.util.CookieUtil" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%
	Cookie authCookie = CookieUtil.getCookie(request, GlobalConstant.AUTH_KEY);
	String authId = authCookie.getValue();
	UserEntity ue = CacheTools.getCache(authId, UserEntity.class);

    IExamQuestionDto questionDto=(IExamQuestionDto)request.getAttribute("questionDto");
    QuestionViewDto questionViewDto=(QuestionViewDto)request.getAttribute("questionViewDto");
    Question question=questionDto.getQuestionDto().getQuestion();
    request.setAttribute("qtest",question);
    request.setAttribute("qview",questionViewDto);
%>
<%--<jsp:include page="questionXXXX.jsp">--%>
    <%--<jsp:param name="qtest" value="${qtest}"/>--%>
    <%--<jsp:param name="qview" value="${qtest}"/>--%>
<%--</jsp:include>--%>
<!--单题展示 begin-->
<div class="jp-quest-temp" questiontypesx="<%=question.getQuestionTypeSX()%>" questiontypecn="<%=question.getQuestionTypeSXN()%>" >
<%--<div class="jp-quest-temp" questiontypesx="93489" >--%>
    <!--显示题目信息being-->
    <div class="p-icon">
        <%
            if(question.isGood()){
                //showGoodIco 题库在后台设置，其他在页面
        %>
             <span class="<c:if test='${showGoodIco}'> goods-ico</c:if>  p-fine"></span>
        <% }
        QuestionViewDto bigQuestionViewDto = questionDto.getQuestionDto().getQuestion().getQuestionViewDto();
        out.println(TemplateFtl.outHtml(questionDto,bigQuestionViewDto)); %>
    </div>
    <!--显示题目信息 end-->
    <!--操作按钮 begin-->
    <%if(questionViewDto.getButtonType()==QuestionViewDto.button_ype_question_lib){%>
            <!--题库列表操作按钮-->
    <div class="p-temp-wrap">
        <div class="p-temp-div fc">
            <div class="p-temp-left fl">
                <a class="collect-btn jp-collect <%if(question.isLoginUserCollectioned()){%>cur<%}%>" data-qustid="<%=question.getId()%>"  href="javascript:;"></a>
               <%if(question.getQuestionTypeId()!=Question.QUESTION_TYPE_READ
                       &&question.getQuestionTypeId()!=Question.QUESTION_TYPE_LISTEN
                   &&question.getQuestionTypeId()!=Question.QUESTION_TYPE_CHOICE_FILL_BLANK
                       &&question.getQuestionTypeId()!=Question.QUESTION_TYPE_CLOZE_FILL_BLANK){%>
                <a class="white-btn white-btn-jx jp-parsi" jiexi="1-题库列表" href="javascript:;"><i class="ico"></i>查看解析</a>
                <%}%>
                <a class="white-btn pdlr4 p-addtest jp-addtest" href="javascript:;" data-qustid="<%=question.getId()%>">加入试题篮</a>
            </div>
            <div class="p-temp-right fr">
            </div>
        </div>
    </div>
    <%}%>
    <%if(questionViewDto.getButtonType()==QuestionViewDto.button_ype_question_lib_change){%>
            <!--v换题 操作按钮-->
        <div class="p-temp-wrap">
            <div class="p-temp-div fc">
                <div class="p-temp-left fl">
                    <a class="collect-btn jp-collect <%if(question.isLoginUserCollectioned()){%>cur<%}%>" data-qustid="<%=question.getId()%>"  href="javascript:;"></a>
                    <%if(question.getQuestionTypeId()!=Question.QUESTION_TYPE_READ
                            &&question.getQuestionTypeId()!=Question.QUESTION_TYPE_LISTEN
                            &&question.getQuestionTypeId()!=Question.QUESTION_TYPE_CHOICE_FILL_BLANK
                            &&question.getQuestionTypeId()!=Question.QUESTION_TYPE_CLOZE_FILL_BLANK){%>
                    <a class="white-btn white-btn-jx jp-parsi" jiexi="2-换题"href="javascript:;"><i class="ico"></i>查看解析</a>
                    <%}%>
                </div>
                <div class="p-temp-right fr">
                </div>
            </div>
        </div>
    <%}%>
    <%if(questionViewDto.getButtonType()==QuestionViewDto.button_ype_create_paper){%>
    <!--题库列表操作按钮-->
    <div class="p-temp-wrap">
        <div class="p-temp-div fc">
            <div class="p-temp-left fl">
                <a class="collect-btn2 jp-collect <%if(question.isLoginUserCollectioned()){%>cur<%}%>" data-qustid="<%=question.getId()%>"  href="javascript:;"></a>
                <a href="javascript:;" class="remove-btn jp-remove"></a>
                <a href="javascript:;" class="flex-btn up jp-up"></a>
                <a href="javascript:;" class="flex-btn down jp-down"></a>
            </div>
            <div class="p-temp-right fr">
                <a href="javascript:;" class="white-btn-gry jp-points">总分：<span><%=question.getDefaultScore()%></span></a>
                <%if(question.getQuestionTypeId()!=Question.QUESTION_TYPE_READ
                        &&question.getQuestionTypeId()!=Question.QUESTION_TYPE_LISTEN
                        &&question.getQuestionTypeId()!=Question.QUESTION_TYPE_CHOICE_FILL_BLANK
                        &&question.getQuestionTypeId()!=Question.QUESTION_TYPE_CLOZE_FILL_BLANK){%>
                <a href="javascript:;" class="white-btn-gry white-btn-jx jp-ana" jiexi="3-组卷设置"><i class="ico"></i>解析</a>
                <%}%>
                <a href="javascript:;" class="white-btn-gry jp-question">换题</a>
            </div>
        </div>
    </div>
    <%}%>

  <!--题目详情-->
    <%if(questionViewDto.getButtonType()==QuestionViewDto.button_ype_zuoye_error){%>
    <!--学生: 错题本  操作按钮-->
    <div class="p-temp-wrap">
        <div class="p-temp-div fc">
            <div class="p-temp-left fl">
                <span class="p-test-numb">做错：<b><%=questionDto.getQuestionDto().getQuestion().getErrorTimes()%></b></span>
            </div>
            <div class="p-temp-right fr">
                <%if(question.getQuestionTypeId()!=Question.QUESTION_TYPE_READ
                        &&question.getQuestionTypeId()!=Question.QUESTION_TYPE_LISTEN
                        &&question.getQuestionTypeId()!=Question.QUESTION_TYPE_CHOICE_FILL_BLANK
                        &&question.getQuestionTypeId()!=Question.QUESTION_TYPE_CLOZE_FILL_BLANK){%>
                        <a href="javascript:;" class="white-btn white-btn-jx jp-parsi" jiexi="4-学生错题本">
                            <i class="ico"></i>解析
                        </a>
                <%}%>
                <a href="javascript:;" class="white-btn white-btn-jx add-btn">
                    加入复习
                </a>
                <a class="remove-btn" href="#"></a>
            </div>
        </div>
    </div>
    <%}%>
<%
System.out.println("----------------------查看解析-----老师个人详情--------------------");
System.out.println(bigQuestionViewDto.getViewType()+"  questionDto.haveSubQuestion()=="+questionDto.haveSubQuestion()+"   question.getIssubjectived()="+question.getIssubjectived());
System.out.println("------------------------------------------");
if((bigQuestionViewDto.getViewType()==QuestionViewDto.view_type_all||bigQuestionViewDto.getViewType()==QuestionViewDto.view_type_review)&&!questionDto.haveSubQuestion()){%>
        <!--2v老师:个人作业详情    操作按钮-->
    <div class="p-temp-wrap">
        <div class="p-temp-div fc">
            <div class="p-temp-left fl">
            </div>
            <div class="p-temp-right fr">
            	<a class="white-btn white-btn-jx jp-parsi" href="javascript:;" jiexi="5-老师个人详情"><i class="ico"></i>查看解析</a>
            </div>
        </div>
    </div>
<%}%>
<%if(question.getQuestionTypeId()==Question.QUESTION_TYPE_CORRECTION 
	&& QuestionViewDto.button_ype_question_lib_change!=questionViewDto.getButtonType()
	&& bigQuestionViewDto.getViewType()==QuestionViewDto.view_type_all
	&& bigQuestionViewDto.getViewType()==QuestionViewDto.view_type_piyue
	){%>
        <!--改错题大题显示解析-->
    <div class="p-temp-wrap">
        <div class="p-temp-div fc">
            <div class="p-temp-left fl">
            </div>
            <div class="p-temp-right fr">
            	<a class="white-btn white-btn-jx jp-parsi" href="javascript:;" jiexi="6-改错题大题（）"><i class="ico"></i>查看解析</a>
            </div>
        </div>
    </div>
<%}%>
<%if(bigQuestionViewDto.getViewType()==QuestionViewDto.view_type_piyue && !questionDto.haveSubQuestion()){%>
     <%
        if(question.getIssubjectived()==1){%>
        <!--1 为客观 0 为非客观-->
        <dl class="p-objec-parklg">
            <dt class="p-parsi-top jp-coradio">
                <a href="javascript:;" class="white-btn white-btn-jx btn-a jp-parsi" jiexi="7-批阅无小题-客观"> <i class="ico"></i>查看解析</a>
            </dt>
        </dl>
    <%}%>
    <%
    if(question.getIssubjectived()==0){
    	//作业批阅的时候需要显示以下批阅相关操作按钮 其它页面不需要显示批阅操作按钮 markType在批阅页面传过来的值 
       	System.out.println("result_answer_"+questionDto.getQuestionDto().getQuestion().getId()+"   score_"+questionDto.getQuestionDto().getQuestion().getId());
       	String strUserScore = new java.text.DecimalFormat("#.#").format(bigQuestionViewDto.getUserScore());
    %>
     <!--1 为客观 0 为非客观-->
     <div class="p-zy-zg">
         <div class="p-parsi">
             <dl class="p-parklg">
                 <dt class="p-parsi-top jp-coradio">
                     <label>批阅</label>
                     <input type="radio" <%if(bigQuestionViewDto.getIsCorrect()==1){%>checked="checked"<%}%> data-status ="a" name="result_answer_<%=questionDto.getQuestionDto().getQuestion().getId()%>" id="a" value="1"><span>正确</span>
                     <input type="radio" <%if(bigQuestionViewDto.getIsCorrect()==0){%>checked="checked"<%}%> data-status ="b" name="result_answer_<%=questionDto.getQuestionDto().getQuestion().getId()%>" id="b" value="0"><span>错误</span>
                     <input type="radio" <%if(bigQuestionViewDto.getIsCorrect()==2){%>checked="checked"<%}%> data-status ="c"name="result_answer_<%=questionDto.getQuestionDto().getQuestion().getId()%>" id="c" value="2"><span>部分正确</span>
                     <i class="shu"></i>
                     <label>得分</label>
                     <input class="nums jp-inpt-numb" name="score_<%=questionDto.getQuestionDto().getQuestion().getId()%>" type="text" placeholder="0" readonly="readonly" value="<%=strUserScore%>">
                     <a href="javascript:;" class="white-btn white-btn-jx btn-a jp-parsi" jiexi="7-批阅无小题-主观">
                         <i class="ico"></i>查看解析
                     </a>
                 </dt>
             </dl>
         </div>
     </div>
   	<%}
    }else if(bigQuestionViewDto.getViewType()==QuestionViewDto.view_type_piyue && question.getQuestionTypeId()==Question.QUESTION_TYPE_CORRECTION ){%>
        <dl class="p-objec-parklg">
            <dt class="p-parsi-top jp-coradio">
                <a href="javascript:;" class="white-btn white-btn-jx btn-a jp-parsi" jiexi="100-批阅改错题"> <i class="ico"></i>查看解析</a>
            </dt>
        </dl>
    <%} %>
    <%if(questionViewDto.getButtonType()!=QuestionViewDto.button_ype_null&&ue.getType()==1
    		&&bigQuestionViewDto.getViewType()!=QuestionViewDto.view_type_piyue
    		&&bigQuestionViewDto.getViewType()!=QuestionViewDto.view_type_jiangping){%>
        <%--<!--知识点在题目下面统一-->--%>
        <div class="p-parsi-kldge jp-kldge p-kldge">
            <%if(bigQuestionViewDto.getViewType()!=QuestionViewDto.view_type_all
                    &&bigQuestionViewDto.getViewType()!=QuestionViewDto.view_type_jiangping
                    ){ %>
               <p><span>使用：<%=question.getUseTimes()%> </span></p>
            <% }%>
            <%if(question.getQuestionTypeId()==Question.QUESTION_TYPE_CORRECTION
            	||!questionDto.haveSubQuestion()){%>
	            <p><span>知识点：<%if(StringUtils.isNotBlank(question.getKnowledgeTags())) out.print(question.getKnowledgeTags());%> <%--进度点：<%=question.getTeacheringTagsFullPath()%>--%></span></p>
	            <p><span>考查能力：<%if(StringUtils.isNotBlank(question.getKaoChaNl())) out.print(question.getKaoChaNl());%></span></p>
            <%} %>
        </div>
    <%}%>
    
<!--操作按钮 end-->
</div>
<!--单题展示 end-->
