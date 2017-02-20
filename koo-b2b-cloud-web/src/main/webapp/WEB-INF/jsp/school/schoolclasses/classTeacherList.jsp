<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.koolearn.cloud.common.serializer.CommonEnum" %>
<%
Integer type = (Integer)request.getAttribute( "type" );
%>
<fe:html title="班级-老师名单" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/back-end/sch-class-teacher/page.css">
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/back-end/sch-class-teacher/page': 'project/b-ms-cloud/1.x/js/back-end/sch-class-teacher/page.js'
        }
    </fe:seaConfig>
        <script type="text/javascript">
            seajs.use('project/b-ms-cloud/1.x/js/back-end/sch-class-teacher/page',function(init){
                init({
                    tabListPhp:'/school/classes/findClassesTeachersList',//tab列表接口
                    removeClassPhp:'/school/classes/updateClassesTeacherOrStudentStatus',//移出本班接口
                    resetPasswordPhp:'/school/classes/resetTeacherPassword',//重置密码接口
                    subjectAjaxPhp:'/school/classes/initSubjectAndClassesTeachers',//弹层中学科接口
                    subjectStuPhp:'/school/classes//findSubjectClassesTeacher',//弹层中学科下的老师接口
                    saveAjax:'/school/classes/addClassesTeacher'//保存添加老师接口
                });
            });

            function goStudentList(   classesId ){
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
                    typeElement.setAttribute("value","s");
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
            <input type="hidden" name="pageNo" value="0" id="jp-page-num">
            <!-- 班级id -->
            <input type="hidden" name="classesId" value="${classesId}" id="jp-classesid">
        </form>
        <div class="p-student-cont">
            <b id="jp-classname">${classes.className}</b>
            <span class="p-stu-span">
                <em>班级码：${classes.classCode}</em>
                <em>班级类型：<%=CommonEnum.classesTypeEnum.getSource(type).getValue()%></em>
                <em>入学年份：${classes.year}年</em>
                <em>毕业年份：${finishDay}</em>
            </span>

            <a href="javascript:void(0)" onclick="goStudentList( ${classes.id})" class="p-stu-as a1" >学员名单</a>
            <a class="p-stu-as a2" href="#">任课老师</a>
        </div>
        <p class="p-add-teacher">
            <a href="javascript:;" id="jp-add-teacher">添加任课老师</a>
        </p>
        <div class="p-table-box">
            <table cellpadding="0" cellspacing="0" border="0">
                <thead>
                <tr>
                    <th>姓名</th>
                    <th>科目</th>
                    <th>邮箱</th>
                    <th>手机</th>
                    <th>账号</th>
                    <th width="20%">操作</th>
                </tr>
                </thead>
                <tbody id="jp-student-table">
                <!-- <tr>
                    <td>张三风</td>
                    <td>语文</td>
                    <td>ffff@ss.com</td>
                    <td>12345678910</td>
                    <td>001</td>
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
