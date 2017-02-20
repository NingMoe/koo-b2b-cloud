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
<fe:html title="小组列表" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-home-grouplist/page.css">
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-home-grouplist/page': 'project/b-ms-cloud/1.x/js/t-home-grouplist/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use(['project/b-ms-cloud/1.x/js/t-home-grouplist/page'],function(app){
            /**
             * setUrl:设置毕业
             * passUrl：学生名单-初始化密码
             * delUrl：学生名单-删除操作
             * subjectUrl： 小组名单-科目切换查询
             * lookupStuUrl：小组名单-查询未被分组的所有学生
             * completeAddUrl:小组名单-完成学生分组
             * monitorUrl：小组名单-设置班长
             * groupStuDelUrl:小组名单-学生删除操作
             * createGroupUrl：小組名单-创建分组
             * groupDelUrl：小組名单-小组删除
             * groupNameUpdateUrl：小組名单-名字修改
             * **/
            var json = {
                "setUrl" : "/teacher/studentManage/updateGraduateStatus",
                "lookupStuUrl" : "/teacher/studentManage/findNoClassesTeamStudents",
                "completeAddUrl" : "/teacher/studentManage/addTeamStudent",
                "monitorUrl" : "/teacher/studentManage/updateStudentJob",
                "subjectUrl": "/teacher/studentManage/findTeamBySubject",
                "groupStuDelUrl" : "/teacher/studentManage/deleteTeamStudents",
                "groupDelUrl" : "/teacher/studentManage/deleteTeam",
                "createGroupUrl" :"/teacher/studentManage/createTeamForClasses",
                "groupNameUpdateUrl" : "/teacher/studentManage/updateTeamName",
                "decorateWorkUrl":"/teacher/task/assign?classesId=${classes.id}"//布置作业跳转页面
            };
            app.init(json);
        });

    function goStudentList(   classesId ){
        if( classesId != null && classesId != "undefined" ){
            var turnForm = document.createElement("form");
            //一定要加入到body中！！
            document.body.appendChild(turnForm);
            turnForm.method = "get";
            turnForm.action = "/teacher/addStudent/showStudentInfo";
            turnForm.target = '_self';
            //创建隐藏表单
            var classIdElement = document.createElement("input");
            classIdElement.setAttribute("name","classesId");
            classIdElement.setAttribute("type","hidden");
            classIdElement.setAttribute("value",classesId);
            turnForm.appendChild(classIdElement);
            turnForm.submit();
        }
    }
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
        //禁用回车
        document.onkeydown = function(e) {
            //捕捉回车事件
            var ev = (typeof event!= 'undefined') ? window.event : e;
            if(ev.keyCode == 13 && document.activeElement.id == "msg") {
                return false;//禁用回车事件
            }
        }
</script>
<title>学生名单</title>
</head>
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="sy"/>
</jsp:include>
<!-- 公共的头部 E-->
<form id="form1" onkeydown="if(event.keyCode==13){return false;}"  >
<div class="i-main">
    <div class="fc">
        <div class="fl">
            <div class="p-side">
                <h2> ${classes.fullName}</h2>
                <div class="p-s">
                    <p>班级编码：${classes.classCode}</p>
                    <p>班级类型：(${classes.typeName}）
                                <c:if test="${ classes.type == 1 }">
                                    ${classes.subjectName}
                                </c:if>


                    </p>
                </div>
                <dl class="p-t">
                    <dt>学段：${classes.rangeName}</dt>
                    <dt>所任学科：
                        <c:forEach items="${teacherBookVersionsList }" var="teacherSubject"   >
                            ${teacherSubject.subjectName}
                        </c:forEach>
                    </dt>
                    <dt>人学年份：${classes.year}</dt>
                    <dt>学生人数：${classesStudensNum}</dt>
                </dl>
                <dl class="p-t">
                    <dt>任课老师</dt>

                    <c:forEach items="${classesTeacherList}" var="classTeacher"   >
                        <c:forEach items="${classTeacher.list}" var="list"   >
                            <dd><em>${list}</em>  ${classTeacher.teacherName}</dd>
                        </c:forEach>
                    </c:forEach>
                </dl>
                <div class="p-left-btn">
                    <c:if test="${ classes.graduate == 0 }">
                        <a href="javascript:;" class="green-btn green-btn-style" id="jp-set" data-type="${ classes.graduate}">设置毕业</a>
                    </c:if>
                    <c:if test="${ classes.graduate == 1 }">
                        <a href="javascript:;" class="green-btn green-btn-style" id="jp-set" data-type="${ classes.graduate}">已毕业</a>
                    </c:if>
                    <c:if test="${ classes.type == 0 }">
                        <a href="/teacher/classRoom/create?rangeId=${classes.rangeId}&classesId=${classes.id}" class="green-btn green-btn-style">备课</a>
                    </c:if>
                    <c:if test="${ classes.type == 1 }">
                        <a href="/teacher/classRoom/create?rangeId=${classes.rangeId}&classesId=${classes.id}&subjectId=${classes.subjectId}" class="green-btn green-btn-style">备课</a>
                    </c:if>
                    <a id="jp-decor-work" href="javascript:;" class="green-btn green-btn-style">布置作业</a>
                </div>
            </div>
        </div>
        <div class="fr">
            <div class="p-con">
                <h2 class="fc p-con-h2" id="jp-listTab">
                    <a href="javascript:void(0)" onclick="goStudentList( ${classes.id})" class="fl"  data-type="1">学生名单</a>
                    <a href="javascript:void(0)" class="fl s" data-type="2">小组名单</a>
                    <a class="p-back-home" title="返回首页" href="/teacher/choiceSubject/goClasssHomePage"></a>
                </h2>
                <input type="hidden" value="${classes.id}"  id="jp-classesId">
                <input type="hidden" value="${classes.rangeId}"  id="jp-rangId">
                <input type="hidden" value="${classes.subjectId}"  id="jp-subjectId">

                <!--小组名单页面 begin-->
                <div class="tab" id="jp-groupList" style="display: block">
                    <div class="p-sub" id="jp-sub" >
                        <c:forEach items="${teacherBookVersionsList}" var="bookVersion" varStatus="vs" >
                            <c:if test="${ vs.index == 0 }">
                                <a href="javascript:;" class="s" data-rangeid="${ bookVersion.rangeId}" data-id="${bookVersion.subjectId}">${bookVersion.subjectName} </a>
                            </c:if>
                            <c:if test="${ vs.index != 0 }">
                                <a href="javascript:;"  data-rangeid="${ bookVersion.rangeId}" data-id="${bookVersion.subjectId}">${bookVersion.subjectName} </a>
                            </c:if>
                        </c:forEach>
                    </div>
                    <div id="jp-subbox">
                        <c:forEach items="${teamList}" var="team"   >

                            <div class="p-ginfo" data-id="${team.id}" >
                                <!-- 当前班长id  没有为0-->
                                <input type="hidden" value="${team.teamManId}" class="jp-oldclassStudentPrimary">
                                <dl>
                                    <dt>
                                        <input class="p-gname" type="text" data-v="${team.teamName}" value="${team.teamName}">
                                        <em class="p-gdel" title="删除"></em>
                                    </dt>
                                    <dd class="fc">
                                        <!-- data-stuid:学生的id   data-type:1、是班长 ；2、不是班长 -->
                                        <c:forEach items="${team.classesStudentsList}" var="studentsList"   >
                                            <c:if test="${studentsList.headman != 1 }">
                                                <div class="p-stu" data-stuid="${studentsList.studentId}"  data-id="${studentsList.id}" data-man="${team.teamManId}">
                                                    <span title="${studentsList.studentName}">${studentsList.studentName}</span>
                                                    <em></em>
                                                    <ul>
                                                        <li class="jp-monitor">组长</li>
                                                        <li class="jp-delstu">删除</li>
                                                    </ul>
                                                </div>
                                            </c:if>
                                            <c:if test="${studentsList.headman == 1 }">
                                                <div class="p-stu s" data-stuid="${studentsList.studentId}"  data-id="${studentsList.id}" data-man="${team.teamManId}">
                                                    <span title="${studentsList.studentName}">${studentsList.studentName}</span>
                                                    <em></em>
                                                    <ul>
                                                        <li class="jp-monitor">组长</li>
                                                        <li class="jp-delstu">删除</li>
                                                    </ul>
                                                </div>
                                            </c:if>
                                        </c:forEach>
                                        <div class="p-stu p-def"></div>
                                    </dd>
                                </dl>
                            </div>
                        </c:forEach>
                    </div>
                    <a class="p-add-group" href="javascript:;"  id="jp-addgroup"><em></em>添加分组</a>

                </div>
                <!--小组名单页面 end-->
            </div>
        </div>
    </div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</form>
</body>
</fe:html>