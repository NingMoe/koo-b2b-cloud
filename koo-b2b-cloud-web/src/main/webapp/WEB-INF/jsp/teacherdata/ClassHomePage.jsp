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
<fe:html title="首页" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-home/page.css">
    <!-- 公共部分结束 -->
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-home/page': 'project/b-ms-cloud/1.x/js/t-home/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use('project/b-ms-cloud/1.x/js/t-home/page',function(init){
            init({
                sureUrl:'/teacher/classHomePage/addTeacherRealName',//添加老师真实姓名
                classPostUrl:'/teacher/addStudent/showStudentInfo', //学生列表（E）
                dynamPostUrl:'/teacher/classNewStatus/getNews',  //学生动态地址
                addPostUrl:'/teacher/addClass/findAllClassByRangeSub', //添加班级
                classResouUrl:'/teacher/classesResource/findClassBaseInfo',//班级资源地址
                getUserName:'/getUser'

            });
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
    </script>

<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="sy"/>
</jsp:include>
<!-- 公共的头部 E-->
<div class="i-main">
    <form id="jp-homeForm" action="" method="post">
        <input id="jp-hidinpt" type="hidden" value="${classesInfo.classId}" name="classesId">
    </form>
    <!-- 添加班级的form -->
    <form id="jp-addForm" action="" method="post">
        <input id="jp-add-inpt" type="hidden" value="${classesInfo.classId}" name="classesId">
        <input type="hidden" name="subjectId" id="subjectId" value="${subjectId}" />
        <input type="hidden" name="rangeId" id="rangeId" value="${rangeId}" />
    </form>
   <!--作文批改功能-->
    <c:if test="${compositionNotifyFlag}">
        <div class="p-zw-pg-message">
            <a href="/composition/compositionList" target="_blank" class="pg-message-ico"></a>
            有<strong>${count}篇</strong>新的作文待批改。
        </div>
    </c:if>

    <ul id="jp-postul" class="p-home fc">
        <input type="hidden" name="nameT" id="nameT" value="${nameT}" />
        <!-- 添加班级 -->
        <li class="p-last-li">
            <div class="p-add-class">
                <a id="jp-posturl-add" href="javascript:;"></a>
                <p>添加班级</p>
            </div>
        </li>
        <c:forEach items="${classList}" var="classesInfo" >
            <li>
                <p class="p-htitl fc">
                    <a class="jp-posturl-class" href="javascript:;" data-subjectid="${classesInfo.subjectId}" data-rangeid="${classesInfo.rangeId}">
                        <c:if test="${ classesInfo.classType == 1 }">
                            ${classesInfo.classTypeName}:${classesInfo.subjectName}
                        </c:if>
                        <c:if test="${ classesInfo.classType == 0 }">
                            ${classesInfo.classTypeName}
                        </c:if>
                    </a>
                    <a class="ico jp-posturl-class" data-rangeid="${classesInfo.rangeId}" data-subjectid="${classesInfo.subjectId}" href="javascript:;"></a>
                </p>
                <p class="p-hclass">
                    <a class="jp-posturl-resou" href="#"> ${classesInfo.className}</a>
                </p>
                <p class="p-hdynam">
                    <b>学生(${classesInfo.pepoleNum})</b>
                    <a class="jp-posturl-dynam" href="javascript:;">
                        <c:if test="${ classesInfo.dynamicStatus == 1 }">
                            <i></i>
                        </c:if>
                    </a>
                </p>
                <div class="p-hwork">
                    <div class="p-hwork-div fc">
                        <c:forEach items="${classesInfo.examClassesList}" var="exam" >
                            <c:if test="${ exam.examType == 1 }">
                                <a href="/teacher/task/situation?examId=${exam.examId}&classId=${classesInfo.classId}" title="${exam.examName}"><b>同步作业：${exam.examName}</b>
                                </a>
                            </c:if>
                            <c:if test="${ exam.examType == 2 }">
                                <a href="/teacher/classRoom/previewDetail?classRoomId=${exam.examId}"  title="${exam.examName}"><b>翻转课堂：${exam.examName}</b>
                                </a>
                            </c:if>
                        </c:forEach>

                    </div>
                </div>
                <p class="p-hopera">
                    <a href="/teacher/task/assign?classesId=${classesInfo.classId}" class="green-btn">布置作业</a>
                    <c:if test="${ classesInfo.classType == 0 }">
                        <a href="/teacher/classRoom/create?rangeId=${classesInfo.rangeId}&classesId=${classesInfo.classId}" class="white-btn">备课</a>
                    </c:if>
                    <c:if test="${ classesInfo.classType == 1 }">
                        <a href="/teacher/classRoom/create?rangeId=${classesInfo.rangeId}&classesId=${classesInfo.classId}&subjectId=${classesInfo.subjectId}" class="white-btn">备课</a>
                    </c:if>
                </p>
                <input class="jp-hidinpt" type="hidden" value="${classesInfo.classId}" name="">
            </li>
        </c:forEach>

    </ul>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
</body>
    <script type="text/javascript">
        $(function (){

        });
    </script>
</fe:html>