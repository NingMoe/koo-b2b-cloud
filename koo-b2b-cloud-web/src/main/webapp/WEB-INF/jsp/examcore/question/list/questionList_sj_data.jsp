<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://www.koolearn.com/taglib/pager"  prefix="koo"%> 
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
 <div class="subj_wrap">
				<p class="topic bg fc">
					<span class="sps_1">试卷编码</span>
					<span class="sps_2">试卷名称</span>
					<span class="sps_3">创建时间</span>
					<span class="sps_4">创建人</span>
					<span class="sps_5 no_bd">浏览量</span>
				</p>
				<c:forEach items="${paperList}" var="paper"  varStatus="status">
				<p class="topic fc">
					<span class="sps_1">${paper.code }</span>
					<span class="sps_2"><a href="javascript:;" class="paperQuestionView" data-paperid="${paper.id}" >${paper.name }</a></span>
					<span class="sps_3">${paper.createtimeStr }</span>
					<span class="sps_4">${paper.creatorName }</span>
					<span class="sps_5 no_bd">${paper.hot==null?0:paper.hot }</span>
				</p>
				<a href="${ctx }/paper/showPaper/${paper.id}/0?tikuxuanshoucang=question" target='_blank' style="display:none;"><span id="paperQuestionViewA_${paper.id}">跳转</span></a>
				</c:forEach>
</div>
     <c:if test="${fn:length(paperList)==0}">
     	<div class="no_mess">暂无试卷信息</div>
     </c:if>				
<!--页码 -->
<koo:pager pager="${listPager}" link="javaScript:searchFenye({p})" template=""></koo:pager>
<!--页码 E-->