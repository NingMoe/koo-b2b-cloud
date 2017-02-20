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
<fe:html title="班级首页" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/s-class/page.css">



    <link rel="stylesheet" href="/project/b-ms-cloud/1.x/css/s-class/page.css"/>
    <title>班级</title>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/s-class/page': 'project/b-ms-cloud/1.x/js/s-class/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
    seajs.use('project/b-ms-cloud/1.x/js/s-class/page',function(init){
        init({
            addClassUrl:'/student/classHome/checkStudentClasses',//加入班级弹层-加入请求数据
            classUrl:'/student/classHome/addStudentToClasses'

        });
    });

    function goClasses( cId){
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
    //禁用回车
    document.onkeydown = function(e) {
        //捕捉回车事件
        var ev = (typeof event!= 'undefined') ? window.event : e;
        if(ev.keyCode == 13 && document.activeElement.id == "msg") {
            return false;//禁用回车事件
        }
    }
    </script>
    </head>
    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/headerStudent.jsp" >
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="bj"/>
    </jsp:include>
    <!-- 公共的头部 E-->
    <div class="i-main">
    <p class="p-add-class">
        <a id="jp-addClass" href="javascript:;"><i>+</i>加入班级
        </a>
    </p>
    <form id="jp-classForm" action="" method="post">
        <input id="jp-classesid" type="hidden" value="" name="classesId">
    </form>
    <ul class="p-home fc" id="jp-postul">
        <c:forEach items="${list}" var="classesInfo" >
            <li>
                <p class="p-go-class">
                    <span>
                        <c:if test="${ classesInfo.type == 1 }">
                            ${classesInfo.typeName}:${classesInfo.subjectName}
                        </c:if>
                        <c:if test="${ classesInfo.type == 0 }">
                            ${classesInfo.typeName}
                        </c:if>
                    </span>
                </p>
                <p class="p-hclass">
                        <a class="jp-posturl-resou" href="javascript:void(0)" onclick="goClasses( ${classesInfo.id})"> ${classesInfo.fullName }</a>
                </p>
                <p class="p-hdynam">
                        <a class="jp-posturl-dynam" href="javascript:void(0)" onclick="goClasses( ${classesInfo.id})"> 学生（${classesInfo.studentNum}）</a>
                </p>
                <p class="p-hopera">
                        <a class="green-btn"  href="javascript:void(0)" onclick="goClasses( ${classesInfo.id})">进入班级</a>
                </p>
            </li>
        </c:forEach>
    </ul>

    </div>
    <jsp:include page="/footer.jsp"></jsp:include>
    </body>
    </fe:html>
