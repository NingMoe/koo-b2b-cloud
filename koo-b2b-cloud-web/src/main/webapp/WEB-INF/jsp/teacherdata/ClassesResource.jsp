<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils" %>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="com.koolearn.cloud.util.DateFormatUtils" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt' %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="koo" uri="http://www.koolearn.com/taglib/koo" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<fe:html title="首页-班级资源" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-home-zylb/page.css">
<fe:seaConfig>
    alias:{
    'project/b-ms-cloud/1.x/js/t-home-zylb/page': 'project/b-ms-cloud/1.x/js/t-home-zylb/page.js'
    }
</fe:seaConfig>
    <script type="text/javascript">
        seajs.use(['project/b-ms-cloud/1.x/js/t-home-zylb/page'],function(app){
            app.init({
                api:{
                    allZy: '/project/b-ms-cloud/1.x/json/t-home-zylb/allZy.php',
                    classroom: '/project/b-ms-cloud/1.x/json/t-home-zylb/allZy.php',
                    homework: '/project/b-ms-cloud/1.x/json/t-home-zylb/allZy.php'
                }
            });
        });

    </script>

    </head>
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="sy"/>
</jsp:include>
<!-- 公共的头部 E-->
<div class="i-main fc">
    <div class="fc">
        <div class="fl">

            <div class="p-side">
                <h2>${classes.fullName}</h2>
                <div class="p-s">
                    <p>班级编码：${classes.classCode}</p>
                    <p>班级类型：(${classes.typeName}）
                        <c:if test="${ classes.type == 1 }">
                            ${classes.subjectName}
                        </c:if>

                    </p>
                </div>
                <div class="p-s">
                    <p>学段：${classes.rangeName}</p>
                    <p>所任学科：
                        <c:forEach items="${teacherBookVersionsList }" var="teacherSubject"   >
                            ${teacherSubject.subjectName}
                        </c:forEach>
                    </p>
                    <p>人学年份：${classes.year}</p>
                    <p>学生人数：${classesStudensNum}</p>
                </div>
                <dl class="p-t">
                    <dt>任课老师</dt>
                    <c:forEach items="${classesTeacherList}" var="classTeacher"   >
                        <c:forEach items="${classTeacher.list}" var="list"   >
                            <dd><em>${list}</em>  ${classTeacher.teacherName}</dd>
                        </c:forEach>
                    </c:forEach>
                </dl>
                <div class="p-btm-btn">
                    <a href="/teacher/task/assign?classesId=${classes.id}" class="green-btn p-btn-style">布置作业</a>
                    <c:if test="${ classes.type == 0 }">
                        <a href="/teacher/classRoom/create?rangeId=${classes.rangeId}&classesId=${classes.id}" class="white-btn p-btn-style">备课</a>
                    </c:if>
                    <c:if test="${ classes.type == 1 }">
                        <a href="/teacher/classRoom/create?rangeId=${classes.rangeId}&classesId=${classes.id}&subjectId=${classes.subjectId}" class="white-btn p-btn-style">备课</a>
                    </c:if>
                </div>
            </div>
        </div>
        <div class="fr">
            <div class="p-con">
                <h2 class="jp-tab fc p-con-h2">
                        <a href="javascript:void(0)"   class="fl s" id="allZy" valueType="0">全部资源</a>
                        <a href="javascript:void(0)" onclick="showDisInfo( 2 )" class="fl"  id="classroom" valueType="2">课堂</a>
                        <a href="javascript:void(0)" onclick="showDisInfo( 1 )"  class="fl"  id="homework" valueType="1">作业</a>
                        <a class="p-back-home" title="返回首页" href="/teacher/choiceSubject/goClasssHomePage"></a>
                </h2>
                <div class="tab" style="display:block">
                    <div class="jp-p-brother p-brother">
                        <table class="p-assets-table">
                            <colgroup>
                                <col width="10%">
                                <col width="10%">
                                <col width="20%">
                                <col width="10%">
                                <col width="25%">
                                <col width="25%">
                            </colgroup>
                            <tr>
                                <th>序号</th>
                                <th>类型</th>
                                <th>标题</th>
                                <th>完成／总数</th>
                                <th>创建时间</th>
                                <th>截止时间</th>
                            </tr>
                            <c:forEach items="${tpExamClassesList}" var="exam"  varStatus="vs">
                                <tr>
                                    <td>${vs.index+1}</td>
                                    <td>${exam.typeName}</td>
                                    <td>
                                        <c:if test="${ exam.type == 2 }">
                                           <a href="/teacher/classRoom/previewDetail?classRoomId=${exam.examId}" class="p-lft-txt" title="${ exam.examName}">${ exam.examName}</a>
                                        </c:if>
                                        <c:if test="${ exam.type == 1 }">
                                            <a href="/teacher/task/situation?examId=${exam.examId}&classId=${exam.classesId}" class="p-lft-txt" title="${ exam.examName}">${ exam.examName}</a>
                                        </c:if>
                                    </td>
                                    <td>${exam.hadDoneNum}/${exam.studentNum}</td>
                                    <td>${exam.startTimeStr}</td>
                                    <td class="p-last-td">${exam.endTimeStr}</td>
                                </tr>
                            </c:forEach>
                        </table>
                        <!--页码 B-->
                        <form id="formId" action="/teacher/classesResource/findClassBaseInfo" method="post">
                            <input type="hidden" id="pageNo" name="pageNo" value="${pageInfo.pageNo }">
                            <input type="hidden" name="classesId" value="${classes.id}">
                            <input type="hidden" name="typeId" value="" id="typeId" value="0">
                            <a class="jp-search-btn fr" href="javascript:searchFenye(${pageInfo.pageNo });"></a>
                        </form>
                        <div class="i-center i-page">
                            <koo:pager name="pageInfo" iteration="true" link="javaScript:searchFenye({p});" ></koo:pager>
                        <!--页码 E-->
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
<script type="text/javascript">
    function showDisInfo( type ){
        $("#typeId").val( type );
        $("#pageNo").val(0);
        $('#formId').submit();
    }

    //分页
    function searchFenye(pageNo){
        var classesId = $("#tabType").val( );
        if(!pageNo){
            pageNo=0;
        }
        $("#pageNo").val(pageNo);
        $('#formId').submit();
    }

</script>
</body>
</fe:html>