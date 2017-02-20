<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<c:forEach items="${resultList}" var="val" varStatus="stat">
  <li <c:if test="${stat.index%2==0}">class="bg-gray"</c:if>>
	  <p class="s-list-title">
	       <a class="s-title" href="#">${val.examName}</a>
	       <c:if test="${val.studentView==1}"><a class="s-comments" href="/student/pc/reviewPage?resultId=${val.resultId}&py=1">评语</a></c:if>
	  </p>
      <div class="s-list-info">
      	<!-- 组题自测作业不显示截止时间 -->
      	<c:choose>
      		<c:when test="${val.type==3}"></c:when>
      		<c:otherwise>
	      		<span>截止时间：<fmt:formatDate value="${val.endTime}" pattern="yyyy.MM.dd HH:mm"/></span>
	          	<c:if test="${val.resultStatus==2}">
		          	<span class="p-divider">/</span>
		         	<span>完成时间：<fmt:formatDate value="${val.completeTime}" pattern="yyyy.MM.dd HH:mm"/></span>
	          	</c:if>
	          <span class="p-divider">/</span>
      		</c:otherwise>
      	</c:choose>
          <span>完成率：</span>
          <span class="p-bar">
              <span class="p-bar-over" style="width:${val.completeRate}%"></span>
          </span> 
          <span class="p-bar-num">${val.completeRate}%</span>
          <p>
              <span>习题（${val.questionMinCount}）</span>
          </p>
      </div>
      <!-- <a href="${val.buttonUrl}" class="${val.buttonCss}">${val.buttonWords}</a> -->
      <jsp:useBean id="now" class="java.util.Date" />
       <c:choose>
      	<c:when test="${val.resultStatus==null&&val.endTime<now}">
      		<a href="/student/pc/showPaper?examId=${val.id}" class="p-list-enter white-btn-gry" review="复习1">复习</a>
      	</c:when>
      	<c:when test="${val.resultStatus==1&&val.endTime>now}">
      		<a href="javascript:joinExam(${val.id},${ue.id},this);" class="p-list-enter white-btn pdlr4">继续作业</a>
      	</c:when>
      	<c:when test="${val.resultStatus==2||(val.resultStatus!=null&&val.endTime<now)}">
      		<a href="/student/pc/reviewPage?resultId=${val.resultId}" class="p-list-enter white-btn-gry" review="复习2">复习</a>
      	</c:when>
      	<c:otherwise>
      		<a href="javascript:joinExam(${val.id},${ue.id},this);" class="p-list-enter green-btn green-btn-style">开始作业</a>
      	</c:otherwise>
      </c:choose>
  </li>
</c:forEach>
<c:choose>
	<c:when test="${resultList==null|| empty resultList}">
        <div class="i-no-result">没有搜索到内容</div>
	</c:when>
	<c:otherwise>
        <c:if test="${not empty resultList}">
		<div class="i-center i-page">
			<!--页码 B-->
		    <koo:pager name="listPager" iteration="true" link="javaScript:searchFenye({p});" ></koo:pager>
			<!--页码 E-->
		</div>
        </c:if>
	</c:otherwise>
</c:choose>
