<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<ul class="lst">
    <jsp:useBean id="now" class="java.util.Date" />
    <c:if test="${pageNo == 0}">
        <li>
            <div class="p-operation">
                <a href="/teacher/task/assign" class="make-zy-add-link"></a>
                <em class="make-zy-add">布置作业</em>
            </div>
        </li>
    </c:if>
	<c:forEach items="${resultList}" var="val">
	   <li id="${val.id}">
	       <div class="p-operation">
	           <div class="p-up-part">
	               <div class="p-num"><em>${empty val.compStudentNum ? 0 : val.compStudentNum }</em>/${empty val.allStudentNum ? 0 : val.allStudentNum }</div>
	               <div class="p-theme" title="${val.examName}"><a href="/teacher/task/situation?examId=${val.id}">${val.examName}</a></div>
	           </div>
	           <div class="p-bj" title="${val.classesName}">${val.classesName}</div>
	           <div class="p-btn">
	           		<c:if test="${val.endTime>=now}">
	           			<c:choose>
	           				<c:when test="${val.status==2 || val.status==1}">
		           				<a href="/teacher/task/edit?id=${val.id}" class="green-btn" >编辑作业</a>
		           			</c:when>
		           			<c:when test="${val.status==4}">
		           				<a href="javascript:;" class="green-btn recall" >撤回作业</a>
		           			</c:when>
	           			</c:choose>
	           		</c:if>
	           		<a href="javascript:;" class="jp-copy white-btn"></i>复制作业</a>
	           </div>
	       </div>
	       <div class="p-tim">
		       <c:choose>
		       		<c:when test="${val.endTime<now}">
		       			已结束
		       		</c:when>
		       		<c:otherwise>
						<c:choose>
				       		<c:when test="${val.status==4}">
				       			<fmt:formatDate value="${val.startTime}" pattern="yyyy.MM.dd"/>-
			       				<fmt:formatDate value="${val.endTime}" pattern="yyyy.MM.dd HH:mm"/>
				       		</c:when>
				       		<c:when test="${val.status==1}">
				       			未发布
				       		</c:when>
				       		<c:when test="${val.status==2}">
				       			已撤回
				       		</c:when>
				       </c:choose>
		       		</c:otherwise>
		       </c:choose>
	       </div>
	       <c:if test="${val.status == 2}">
               <span class="jp-cls p-cls"></span>
           </c:if>
	   </li>
	</c:forEach>
    <c:if test="${not empty resultList}">
        <div class="i-center i-page">
            <!--页码 B-->
            <koo:pager name="listPager" iteration="true" link="javaScript:searchFenye({p});" ></koo:pager>
            <!--页码 E-->
        </div>
    </c:if>
</ul>
