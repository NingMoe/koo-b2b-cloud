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

<!DOCTYPE html>
<fe:html title="添加学生" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-home-addstudent/page.css">
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-home-addstudent/page': 'project/b-ms-cloud/1.x/js/t-home-addstudent/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
    seajs.use(['project/b-ms-cloud/1.x/js/t-home-addstudent/page'],function(app){
    var json = {
    "addUrl" : "/teacher/addStudent/writeAll",//批量添加学生地址
    "delUrl" : "/teacher/addStudent/deleteUserAndClassStudent", //删除学生
    "updateUrl" : "/teacher/addStudent/updateRealNameAndStudentCode",  // 修改学生姓名
    "headDownLoadUrl" : "/project/b-ms-cloud/1.x/json/t-ajax/ajax.php",
    "completeUrl" : "/teacher/addStudent/showStudentInfo",    //查询学生信息
    "downloadCodeListUrl" : "/teacher/studentManage/exportExcelFile"  //下载excel
    };
    app.init(json);
    });

    //禁止后退键 作用于Firefox、Opera
    document.onkeypress=banBackSpace;
    //禁止后退键 作用于IE、Chrome
    document.onkeydown=banBackSpace;
    //处理键盘事件 禁止后退键（Backspace）密码或单行、多行文本框除外
    function banBackSpace(e){
        var ev = e || window.event;//获取event对象
        var obj = ev.target || ev.srcElement;//获取事件源
        var t = obj.type || obj.getAttribute('type');//获取事件源类型
        //获取作为判断条件的事件类型
        var vReadOnly = obj.getAttribute('readonly');
        var vEnabled = obj.getAttribute('enabled');
        //处理null值情况
        vReadOnly = (vReadOnly == null) ? false : vReadOnly;
        vEnabled = (vEnabled == null) ? true : vEnabled;
        //当敲Backspace键时，事件源类型为密码或单行、多行文本的，
        //并且readonly属性为true或enabled属性为false的，则退格键失效
        var flag1=(ev.keyCode == 8 && (t=="password" || t=="text" || t=="textarea") && (vReadOnly==true || vEnabled!=true))?true:false;
        //当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效
        var flag2=(ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea") ?true:false;
        //判断
        if(flag2){
            return false;
        }
        if(flag1){
            return false;
        }
    }

    //页面跳转
    function goStudentList( subjectId,rangeId , classesId ){

        var turnForm = document.createElement("form");
        //一定要加入到body中！！
        document.body.appendChild(turnForm);
        turnForm.method = "post";
        turnForm.action = "/teacher/addStudent/showStudentInfo";
        turnForm.target = '_self';
        //创建隐藏表单
        var subjectIdElement = document.createElement("input");
        subjectIdElement.setAttribute("name","subjectId");
        subjectIdElement.setAttribute("type","hidden");
        subjectIdElement.setAttribute("value",subjectId);
        turnForm.appendChild(subjectIdElement);
        var classIdElement = document.createElement("input");
        classIdElement.setAttribute("name","classesId");
        classIdElement.setAttribute("type","hidden");
        classIdElement.setAttribute("value",classesId);
        turnForm.appendChild(classIdElement);
        var rangeElement = document.createElement("input");
        rangeElement.setAttribute("name","rangeId");
        rangeElement.setAttribute("type","hidden");
        rangeElement.setAttribute("value",rangeId);
        turnForm.appendChild(rangeElement);
        turnForm.submit();
    }

        //
        function goBack(){
            var turnForm = document.createElement("form");
            document.body.appendChild(turnForm);
            turnForm.method = "get";
            turnForm.action = "/teacher/choiceSubject/goClasssHomePage";
            turnForm.target = '_self';
            //创建隐藏表单
            turnForm.submit();
        }
    </script>
    <title>批量添加学生</title>
    </head>
    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="sy"/>
    </jsp:include>
    <!-- 公共的头部 E-->
    <div class="i-main">
    <div class="fc">
    <div class="fl">
    <div class="p-side">
    <h2>${classes.fullName}</h2>
    <div class="p-s">
    <p>学段：${classes.rangeName}</p>
    <p>所任学科：
        <c:forEach items="${teacherBookVersionsList }" var="teacherSubject"   >
            ${teacherSubject.subjectName}
        </c:forEach>
    </p>
    <p>人学年份：${classes.year}</p>
    <p>学生人数：<b id="jp-number">${classesStudensNum} </b></p>
    </div>
    <dl class="p-t">
    <dt>任课老师</dt>
        <c:forEach items="${teacherList}" var="classTeacher"   >
            <c:forEach items="${classTeacher.list}" var="list"   >
                <dd><em>${list}</em>  ${classTeacher.teacherName}</dd>
            </c:forEach>
        </c:forEach>
    </dl>
    </div>
    </div>
    <div class="fr">
    <div class="p-con">
    <h2 class="fc" id="jp-stuTab">
    <a href="javascript:;" class="fl s" data-type="1">邀请学生</a>
    <a href="javascript:;" class="fl" data-type="2">批量添加</a>
        <input type="hidden" value="${classes.id}" id="jp-classesId">
        <input type="hidden" value="${classes.classCode}" id="jp-classNo">
    </h2>
    <!--邀请学生页面 begin-->
    <div id="jp-invite">
    <div class="in-tab">
    <div class="p-box">班级编码：${classes.classCode}</div>
    <ul>
    <li>1. 使用电脑访问 cloud.koolearn.com</li>
    <li>2. 注册一个新东方教育云学生账号</li>
    <li>3. 注册账号时，在邀请码中输入班级编码${classes.classCode}</li>
    <li>4. 已有账号的，在首页加入班级页面中输入班级编码</li>
    </ul>
    </div>
    <div class="p-afoot">
    <a href="javascript:void(0)" onclick="goStudentList(${subjectId},${rangeId},${classes.id})" class="green-btn green-btn-style">完成</a>
    </div>
    </div>
    <!--邀请学生页面 end-->
    <!--批量添加页面 begin-->
    <div class="tab" id="jp-add">
    <!--添加之前的样式-->
    <div class="p-create" id="jp-create">
    <a href="javascript:;" class="green-btn green-btn-style jp-create-btn">批量创建账号</a>
    </div>
    <!--添加之后的样式-->
        <!--添加之后的样式-->
        <form action="" method="get" id="jp-form">
            <div id="jp-tab">

            </div>
        </form>
    </div>
    <!--批量添加页面 end-->
    </div>
    </div>
    </div>
    </div>
    <jsp:include page="/footer.jsp"></jsp:include>

    </body>
    </fe:html>
