<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<fe:html title="首页 —— 学生首页" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/t-sy-xssy/page.css">
    <script src="/js/task.js" type="text/javascript"></script>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-sy-xssy/page': 'project/b-ms-cloud/1.x/js/t-sy-xssy/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/t-sy-xssy/page',function(app){
            app.init();
        });
        function searchFenye(pageNo){
            //当前选中id
            if(!pageNo){
                pageNo=0;
            }
            //var classesId =
            $("#pageNo").val(pageNo);
            $('#formId').submit();
        }
    </script>

    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/headerStudent.jsp" >
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="sy"/>
    </jsp:include>
    <!-- 公共的头部 E-->
    <div class="i-main p-courvare">
        <div style="margin-top:82px;text-align: center;"><font size=10 color=#000000>${message} </font>
        </div>
        <c:forEach items="${studentSubjectList}" var="subjectList" >
            <div class="p-path-subject">
                    <div class="p-subject-title" subjectId="${subjectList.subjectId}">
                        <h3>${subjectList.subjectName}</h3>

                        <!--  <span>任课老师：<c:forEach items="${subjectList.teacherSet}" var="teacherSet" >${teacherSet}&nbsp;&nbsp;</c:forEach><em>／</em>所在班级：<c:forEach items="${subjectList.classesSet}" var="classesSet" >${classesSet} &nbsp;&nbsp;</c:forEach></span>-->

                        <c:if test="${ subjectList.showButton == 1 }">
                            <a href="/student/test/self?subjectId=${subjectList.subjectId}"  class="orange-btn">组题自测</a>
                        </c:if>
                    </div>
                    <div class="p-subject-content">
                        <c:forEach items="${subjectList.tpExamList}" var="examList" >
                            <div class="p-content-path">
                                <div class="content-path-info">
                                    <c:if test="${ examList.type == 1 ||examList.type ==3 ||examList.type ==4 }">
                                          <p class="cp-info-title">作业：${ examList.examName}</p>
                                          <a href="javascript:void(0)" onclick="joinExam( ${examList.id},${studentId},this )" class="green-btn">${ examList.progressDis}</a>
                                     </c:if>
                                    <c:if test="${ examList.type == 2 || examList.type == 20}">
                                        <p class="cp-info-title">课堂：${ examList.examName}</p>
                                        <a href="/student/classRoom/detail/${examList.id}/${subjectList.subjectId}" class="green-btn">${ examList.progressDis}</a>
                                    </c:if>
                                </div>
                                <c:if test="${ examList.type == 1 ||examList.type ==2}">
                                    <div class="content-path-time">截止时间：${ examList.endTimeStr}</div>
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>

            </div>
        </c:forEach>
        <div class="i-center i-page">
            <koo:pager name="pageInfo" iteration="true" link="javaScript:searchFenye({p});" ></koo:pager>
        </div>
        <!--页码 B-->
        <form id="formId" action="/student/allSubject/findAllSubjectExam" method="get">
            <input type="hidden" id="pageNo" name="pageNo" value="${pageInfo.pageNo }">
            <a class="jp-search-btn fr" href="javascript:searchFenye(${pageInfo.pageNo });"></a>
        </form>
    </div>
    <jsp:include page="/footer.jsp"></jsp:include>
    </body>
    </fe:html>