<!DOCTYPE html>
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
<fe:html title="班级详情" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/s-class-detail/page.css">
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/s-class-detail/page': 'project/b-ms-cloud/1.x/js/s-class-detail/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
    seajs.use('project/b-ms-cloud/1.x/js/s-class-detail/page',function(init){
    init({

    });
    });
    function getOutClass( cId ){
        var turnForm = document.createElement("form");
        //一定要加入到body中！！
        document.body.appendChild(turnForm);
        turnForm.method = "post";
        turnForm.action = "/student/classHome/getOutClasses";
        turnForm.target = '_self';
        //创建隐藏表单
        var classIdElement = document.createElement("input");
        classIdElement.setAttribute("name","classesId");
        classIdElement.setAttribute("type","hidden");
        classIdElement.setAttribute("value",cId);
        turnForm.appendChild(classIdElement);
        turnForm.submit();
    }
    function goClassesDynamic( cId ){
        if( cId != null && cId != "undefined" ){
            var turnForm = document.createElement("form");
            //一定要加入到body中！！
            document.body.appendChild(turnForm);
            turnForm.method = "get";
            turnForm.action = "/student/classHome/goClassesDynamic";
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
    function goBackHome(){

    }
    </script>
    </head>
    <body>
    <jsp:include page="/WEB-INF/jsp/commons/headerStudent.jsp" >
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="bj"/>
    </jsp:include>
    <div class="i-main p-class-detail">
    <div class="p-title fc">
        <b class="p-name fl">${classes.className}</b>
    <a class="fl p-tabas s" href="javascript:void(0)">学生名单</a>
    <a class="fl p-tabas" href="javascript:void(0)" onclick="goClassesDynamic( ${classes.id})">班级动态</a>
    <a class="p-alink fr" href="/student/classHome/goStuClassHome"></a>
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
        <dl class="p-teaches">
            <dt>任课老师：</dt>
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
        <form id="jp-form" action="/student/classHome/getOutClasses" method="post">
            <input id="jp-classesid" type="hidden" value="${classes.id}" name="classesId">
        </form>
    <p class="p-exit">
    <a href="javascript:void(0)" id="jp-exit" >退出</a>
    </p>
    </div>
        <div class="p-cont fr" id="">
            <div class="p-zy-box">
            <table>
            <thead>
                    <tr>
                    <td width="10%">序号</td>
                    <td width="13%">姓名</td>
                    <td width="12%">学号</td>
                    <td width="13%">职位</td>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="user" varStatus="vs" >
                        <tr>
                            <td>${vs.index+1}</td>
                            <td>${user.realName}</td>
                            <td>${user.studentCode}</td>
                            <td>
                                <c:if test="${ user.headman == 1 }">
                                    组长
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
            </table>
        </div>
    </div>
    </div>

    </div>
    <jsp:include page="/footer.jsp"></jsp:include>
    </body>
    </fe:html>
