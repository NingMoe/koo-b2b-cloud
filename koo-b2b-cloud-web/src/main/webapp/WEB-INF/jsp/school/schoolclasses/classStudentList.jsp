<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.koolearn.cloud.common.serializer.CommonEnum" %>
<%
    Integer type = (Integer)request.getAttribute( "type" );
%>
<fe:html title="班级-学生名单" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/back-end/sch-class-student/page.css">

<fe:seaConfig>
    alias:{
    'project/b-ms-cloud/1.x/js/back-end/sch-class-student/page': 'project/b-ms-cloud/1.x/js/back-end/sch-class-student/page.js'
    }
</fe:seaConfig>
    <script type="text/javascript">
    seajs.use('project/b-ms-cloud/1.x/js/back-end/sch-class-student/page',function(init){
    init({
            tabListPhp:'/school/classes/findClassesStudentList',//tab数据
            removeClassPhp:'/school/classes/updateClassesTeacherOrStudentStatus',//移出本班请求地址
            resetPasswordPhp:'/school/classes/resetTeacherPassword'//重置密码接口
    });
    });
    function goTeacherList(   classesId ){
        if( classesId != null && classesId != "undefined" ){
            var turnForm = document.createElement("form");
            //一定要加入到body中！！
            document.body.appendChild(turnForm);
            turnForm.method = "post";
            turnForm.action = "/school/classes/goClassTeacherOrStudentList";
            turnForm.target = '_self';
            //创建隐藏表单
            var classIdElement = document.createElement("input");
            classIdElement.setAttribute("name","classesId");
            classIdElement.setAttribute("type","hidden");
            classIdElement.setAttribute("value",classesId);
            var typeElement = document.createElement("input");
            typeElement.setAttribute("name","goType");
            typeElement.setAttribute("type","hidden");
            typeElement.setAttribute("value","t");
            turnForm.appendChild(classIdElement);
            turnForm.appendChild(typeElement);
            turnForm.submit();
        }
    }
    </script>
    </head>
    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/schoolHeader.jsp" >
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="bj"/>
    </jsp:include>
    <!-- 公共的头部 E-->
    <div class="i-main">
    <!-- 数据加载参数 -->
    <form id="jp-student-form">
    <!-- 当前页码 -->
    <input type="hidden" name="currentPage" value="0" id="jp-page-num">
    <!-- 班级id -->
    <input type="hidden" name="classesId" value="${classes.id}" id="jp-classesid">
    </form>
    <div class="p-student-cont">
    <b>${classes.className}</b>
    <span class="p-stu-span">
    <em>班级码：${classes.classCode}</em>
    <em>班级类型：<%=CommonEnum.classesTypeEnum.getSource(type).getValue()%></em>
    <em>入学年份：${classes.year}年</em>
    <em>毕业年份：${finishDay}年</em>
    </span>
    <a class="p-stu-as a1" href="#">学员名单</a>
    <a class="p-stu-as a2" href="javascript:void(0)" onclick="goTeacherList( ${classes.id})">任课老师</a>
    </div>
    <div class="p-table-box">
    <table cellpadding="0" cellspacing="0" border="0">
    <thead>
    <tr>
    <th>姓名</th>
    <th>学号</th>
    <th>邮箱</th>
    <th>手机</th>
    <th>账号</th>
    <th>家长姓名</th>
    <th>家长手机</th>
    <th width="20%">操作</th>
    </tr>
    </thead>
    <tbody id="jp-student-table">
    <!-- <tr>
    <td>张三风</td>
    <td>12343423</td>
    <td>ffff@ss.com</td>
    <td>12345678910</td>
    <td>001</td>
    <td>张三</td>
    <td>12345678910</td>
    <td>
    <a class="op-btn" href="javascript:;">移除本班</a>
    <a class="op-btn" href="javascript:;">重置密码</a>
    </td>
    </tr> -->
    </tbody>
    </table>
    </div>
    <!-- 分页 -->
    <div class="p-pager-box"></div>
    </div>
    </body>
</fe:html>
