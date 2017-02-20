<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils"%>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>

<fe:html title="班级" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/back-end/sch-tea-stu/page.css">
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/back-end/sch-tea-stu/page-student': 'project/b-ms-cloud/1.x/js/back-end/sch-tea-stu/page-student.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
    seajs.use('project/b-ms-cloud/1.x/js/back-end/sch-tea-stu/page-student',function(init){
    init({
    api:{
            actionUrl:'/school/student/findStudentPage',
            activateUrl: '/school/teacher/updateTeacherOrStudentStatus',
            resetPwUrl : '/school/teacher/resetTeacherPassword',
            //加载学科年级级联列表
            findAllSubjectClasses: '/school/student/findAllSubjectClasses'
    }
    });
    });
    </script>
    </head>
    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/schoolHeader.jsp" >
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="xs"/>
    </jsp:include>
    <!-- 公共的头部 E-->
    <div class="p-main">
    <div class="i-box search">
        <form >
            <input type="hidden" name="rangeName" value="">
            <input type="hidden" name="level" value="">
            <input type="hidden" name="studentName" value="">
        </form>
    <dl>
    <dt>学段</dt>
    <dd class="first range">
    </dd>
    <dt>年级</dt>
    <dd class="class">

    </dd>
    </dl>
    <div class="inputWrap absolute">
    <input type="text" placeholder="请输入学生姓名" >
    <a href="#" id="jp-search"></a>
    </div>
    </div>
    </div>
    <div class="i-main p-con">
    <div class="p-con-box">
    <div class="p-box-header">

    </div>
    <div class="table-box">

    </div>
    <!-- 分页开始 -->
    <div style="margin:20px;" class="pagination">
    </div>
    <!-- 分页结束 -->
    </div>

    </div>
    </body>
</fe:html>