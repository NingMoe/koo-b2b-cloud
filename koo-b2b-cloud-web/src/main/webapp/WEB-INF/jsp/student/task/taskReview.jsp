<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.koolearn.cloud.exam.examcore.paper.entity.QuestionBarHtml" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<!DOCTYPE html>
<fe:html title="作业复习" ie="true" initSeajs="true" 
assets="/project/b-ms-cloud/1.x/css/t-hw-workreview/page.css">
<body>
<c:if test="${urlType == null}">
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/headerStudent.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="zy"/>
</jsp:include>
</c:if>
<!-- 公共的头部 E-->

<div class="i-main p-courvare">
    <div class="p-contvare fc">
        <c:if test="${urlType == null}">
        <div class="p-contvare-title">${task.examName}</div>
        </c:if>
        <!-- 左侧 -->
        <div class="p-cont fl" id="showPaper">
        	
        </div>
        <!-- 右侧 -->
        <div class="p-side fr">
            <div class="p-item-content-box">
            	<c:if test="${(examResult.isreply eq 1) || (examResult.examType==3) || (examResult.examType==4)}">
	            	<div class="subject-all-score"><em><fmt:formatNumber value="${examResult.score}" pattern="#.#"/></em>分</div>
	                <div class="subject-ensure-acc">
	                    <span>正确率：</span>
	                    <span class="p-bar">
	                        <%--<span class="p-bar-over" style="width:${studentResult.correctRateNum*100}%"></span>--%>
	                        <span class="p-bar-over" style="width:${examResult.rateDouble}%"></span>
	                    </span>
	                    <%--<span class="p-bar-num"><fmt:formatNumber value="${studentResult.correctRateNum}" type="number" pattern="0.##%" /></span>--%>
	                    <span class="p-bar-num">${examResult.rate}</span>
	                </div>
            	</c:if>
                <div class="subject-content-box">
                    <div class="p-item-title-box fc">
                        <div class="second-item-title fr <c:if test="${py==1}">item-selected</c:if>">老师评语</div>
                        <div class="first-item-title fl <c:if test="${py!=1}">item-selected</c:if>">题目</div>
                    </div>
                    <div class="p-item-number-box"  >
                    	<div <c:if test="${py==1}">style="display: none;"</c:if>>
                    	<%
                    	QuestionBarHtml questionBarHtml = (QuestionBarHtml)request.getAttribute("questionBarHtml");
                    	out.println(questionBarHtml.getHtmlQuestionNum()); %>
                    	</div>
                        <div class="second-content-box" <c:if test="${py!=1}">style="display: none;"</c:if> >
                            <textarea readonly="readonly">${examResult.reply}</textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
<%-- 自动交卷遮罩层结束 --%>
<jsp:include page="/ueditor/jsp/ueditorJs.jsp"/>
</body>
</body>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-hw-workreview/page': 'project/b-ms-cloud/1.x/js/t-hw-workreview/page.js'
        }
    </fe:seaConfig>
<script type="text/javascript">
    seajs.use('project/b-ms-cloud/1.x/js/t-hw-workreview/page',function(exports){
        exports.init({
            firstAjaxUrl:'/student/pc/review_'+${examResult.id}
        });
    });
</script>
</fe:html>
