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
<fe:html title="班级动态" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/s-class-dynam/page.css">
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/s-class-dynam/page': 'project/b-ms-cloud/1.x/js/s-class-dynam/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
    seajs.use('project/b-ms-cloud/1.x/js/s-class-dynam/page',function(init){
    init({
    listUrl:'/project/b-ms-cloud/1.x/json/s-class-dynam/list1.php' //动态数据加载地址
    });
    });
    function goClassesList( cId ){
        if( cId != null && cId != "undefined" ){
            var turnForm = document.createElement("form");
            //一定要加入到body中！！
            document.body.appendChild(turnForm);
            turnForm.method = "get";
            turnForm.action = "/student/classHome/goClass";
            turnForm.target = '_self';
            //创建隐藏表单
            var classIdElement = document.createElement("input");
            classIdElement.setAttribute("name","classesId");
            classIdElement.setAttribute("type","hidden");
            classIdElement.setAttribute("value",cId);
            turnForm.appendChild(classIdElement);
            turnForm.submit();
        }

    }
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
    </head>
    <body>
    <jsp:include page="/WEB-INF/jsp/commons/headerStudent.jsp" >
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="bj"/>
    </jsp:include>
    <div class="i-main p-class-dynam">
            <div class="p-title fc">
                <b class="p-name fl">${classes.fullName}</b>
                <a class="fl p-tabas" href="javascript:void(0)" onclick="goClassesList( ${classes.id})">学生名单</a>
                <a class="fl p-tabas s" href="javascript:void(0)"  >班级动态</a>
                <a class="p-alink fr"  href="/student/classHome/goStuClassHome"></a>
            </div>
            <div class="p-wrap fc">
                <div class="p-side fl">
                <p class="p-txt">班级编码：${classes.classCode}</p>
                <p class="p-txt">班级类型：(${classes.typeName}）
                <c:if test="${ classes.type == 1 }">
                    ${classes.subjectName}
                </c:if></p>
                <p class="p-txt p-hr">学    段：${classes.rangeName}</p>
                <p class="p-txt">入学年份：${classes.year}</p>
                <p class="p-txt">学生人数：${classesStudensNum}</p>
                <dl class="p-teaches">任课老师：
                <c:forEach items="${classesTeacherList}" var="classTeacher" >
                    <c:forEach items="${classTeacher.list}" var="list" >
                        <dd>
                            <span>
                                <b>${list}</b>
                                <em>${classTeacher.teacherName}</em>
                            </span>
                        </dd>
                    </c:forEach>
                </c:forEach>
                </dl>
                </div>
                <div class="p-cont fr" id="jp-dynamic">
                    <div class="p-dynamic-cont">
                        <ul id="jp-dynamic-list">
                            <c:forEach items="${dynamicList}" var="dynamic" >
                                <li>
                                    <p>${dynamic.dynamicInfo}</p>
                                    <p>${dynamic.createTimeStr}</p>
                                    <i></i>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                    <!--页码 B-->
                    <form id="formId" action="/student/classHome/goClassesDynamic" method="get">
                        <input type="hidden" id="pageNo" name="pageNo" value="${pageInfo.pageNo }">
                        <input type="hidden" name="classesId" id="classesId" value="${classesId}">
                        <a class="jp-search-btn fr" href="javascript:searchFenye(${pageInfo.pageNo });"></a>
                    </form>
                    <div class="i-center i-page" style="padding: 20px 0;">
                        <koo:pager name="pageInfo" iteration="true" link="javaScript:searchFenye({p});" ></koo:pager>
                    </div>
                </div>
            </div>

    </div>
    <jsp:include page="/footer.jsp"></jsp:include>
    </body>
    </fe:html>