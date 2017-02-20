<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="http://www.koolearn.com/taglib/pager"  prefix="koo"%> 
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script>
function IFrameResize(myframe)
{
}
</script>
 <div class="subj_wrap">
				<p class="topic bg fc">
					<span class="sps_1"  style="width: 216px;">试题编码</span>
					<span class="sps_2">题干</span>
					<span class="sps_3" style="width: 204px;">创建时间</span>
					<span class="sps_4" style="width: 90px;">创建人</span>
					<span class="sps_5 no_bd" style="width: 454px;">操作</span>
				</p>
	 <c:forEach items="${questionList}" var="dto"  varStatus="status">
				<p class="topic fc">
					<span class="sps_1" style="width: 216px;" title="${dto.questionDto.question.code}">${dto.questionDto.question.code}</span>
					<span class="sps_2"> 
					<c:out value="${dto.shortTopic}" default="" escapeXml="true"/> 
					</span>
					<span class="sps_3" style="width: 204px;"><fmt:formatDate value="${dto.questionDto.question.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
					<span class="sps_4" style="width: 90px;">${dto.questionDto.question.createBy}</span>
					<span class="sps_5 no_bd" style="width: 454px;">
					<c:choose>
					<c:when test="${dto.questionDto.question.createBy=='新东方'}">
						<%@include file="questionViewLink.jsp" %>
						<%@include file="shoucangLink.jsp" %>
					</c:when>
					<c:when test="${dto.questionDto.questionBankExt.teacherId!=loginUser.id}">
						<%@include file="questionViewLink.jsp" %>
						<%@include file="shoucangLink.jsp" %>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${dto.questionDto.question.status==0}">
								<a class="" onclick="questionVerify('${dto.questionDto.question.id}','1','${dto.questionDto.question.code}',this)" href="javascript:;">审核</a>
								<%@include file="modifyLink.jsp" %>
								<a class="" onclick="questionDelete('${dto.questionDto.question.id}')" href="javascript:;">删除</a>
								<%@include file="questionViewLink.jsp" %>
							</c:when>
							<c:when test="${dto.questionDto.question.status==1}">
								  <!-- 已共享 -->
								   <c:if test="${dto.questionDto.questionBankExt.status eq 3}">
								    <a class="curr2" data="${dto.questionDto.questionBankExt.id}" onclick="" href="javascript:;">已共享</a>
								   </c:if>
								   
								   <c:if test="${dto.questionDto.questionBankExt.status eq 2}">
								    <a class=""  onclick="" href="javascript:shareQuestion(${dto.questionDto.questionBankExt.id});">分享</a>
								   </c:if>
								
								<a class="curr2" onclick="" href="javascript:;">已审核</a>
								<%@include file="modifyLink.jsp" %>
								<a class="" onclick="questionCancel('${dto.questionDto.question.id}','2')" href="javascript:;">作废</a>
								<%@include file="questionViewLink.jsp" %>
								<%@include file="shoucangLink.jsp" %>
							</c:when>
							<c:otherwise>
							<a class="curr2" onclick="" href="javascript:;">停用</a>
							<%@include file="questionViewLink.jsp" %>
							</c:otherwise>
						</c:choose>
						
					</c:otherwise>
					</c:choose>
						
					</span>
					<div class="anal_box" id="show_${dto.questionDto.question.id}" showflag="0">
					<iframe id="qshowDiv_${dto.questionDto.question.id}" onload="IFrameResize('qshowDiv_${dto.questionDto.question.id}')" src="" frameborder="0" height="100%" width="100%" scrolling="yes" marginheight="0" marginwidth="0"></iframe>
					</div>
				</p>
     </c:forEach>
</div>
     <c:if test="${fn:length(questionList)==0}">
     	<div class="no_mess">暂无试题信息</div>
     </c:if>				
<!--页码 B
<kooo:pager name="listPager" iteration="true" link="javaScript:serachFenye({p})"/>-->
<koo:pager pager="${listPager}" link="javaScript:searchFenye({p})" template=""></koo:pager>
<!--页码 E-->

