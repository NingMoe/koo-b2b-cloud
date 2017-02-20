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
<fe:html title="首页-创建班级" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-home-cjclass/page.css">
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="sy"/>
</jsp:include>
<!-- 公共的头部 E-->
<div class="i-main p-creat">
    <div class="fc">
        <div class="p-side fl">
                <p>选择学科</p>
                <select id="sel-show1" class="i-sel1" name="subjectId">
                    <option value="2465">数学</option>
                </select>
                <p>选择学段</p>
                <select id="sel-show2" class="i-sel1" name="rangeId">
                    <option value="27581">初中</option>
                </select>
        </div>
        <div class="p-cont fr">
            <div class="p-creat-chos">
                <label>选择入学年份</label>
                <select id="jp-time" class="i-sel4">
                    <option value="1">全部</option>
                </select>
                <a id="jp-creat-btn" class="orange-btn" href="javascript:;">创建新班级</a>
            </div>
            <div class="p-creat-class" id="jp-creat">
                <form id="jp-creatfrom" action="" method="post">
                    <input id="jp-classId" type="hidden" name="classesId">
                </form>
                <input type="hidden" name="nameT" id="nameT" value="${teacherName}" />
                <ul class="fc" id="jp-create-class">
                    <c:forEach items="${classesList}" var="classes">
                      <li data-type="${classes.type}" data-classesid="${classes.id}"data-subjectid="${subjectId}" data-rangeid="${rangeId}" data-isdeleteok="${classes.isDeleteOk}" data-teachername="${classes.teacherName}" data-subjectname="${classes.subjectName}">
                          <input type="radio" id="" class="for-radio" name="chek">
                        <b>${classes.className}
                            <c:if test="${ classes.isDeleteOk == 1 }">
                            <i></i>
                            </c:if>
                        </b>
                      </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
    <p class="p-creat-btn">
        <a id="jp-surebtn" class="green-btn green-btn-style" href="javascript:;">确定</a>
        <a id="jp-returnbtn" class="white-btn-gry" href="/teacher/choiceSubject/goClasssHomePage">返回</a>
    </p>
</div>
<jsp:include page="/footer.jsp"></jsp:include>
<fe:seaConfig>
    alias:{
    'project/b-ms-cloud/1.x/js/t-home-cjclass/page': 'project/b-ms-cloud/1.x/js/t-home-cjclass/page.js'
    }
</fe:seaConfig>
<script type="text/javascript">
    seajs.use('project/b-ms-cloud/1.x/js/t-home-cjclass/page',function(init){
        init({
            creatClassUrl:'/teacher/addClass/findAllClassesByYearAndSubRange',//根据入学年份查询班级列表
            locationUrl:'/teacher/addClass/goAddStudent',//确定后跳转页面
            //联级
            selSubjectId: '/teacher/resource/getSubject',//学科的数据地址
            selRangeId: '/teacher/resource/getRange',//学段的数据地址
            selLastId:'/teacher/resource/getStr',
            savePostUrl:'/teacher/addClass/addClassesInfo',//创建新班级
            deleteUrl:'/teacher/addClass/deleteClasses',//删除地址
            goClassUrl:'/teacher/addClass/findRangeIdBySubjectId',//走班制点击学科请求地址
            popSubjectId:'/teacher/addClass/findTeacherSubjects',//弹层学科请求地址
            subjectId:${subjectId}, //初始默认的学科id2464
            rangeId:${rangeId} //初始默认的学段id26995
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
   //加载下拉框年份
    $(function () {
        var myDate = new Date();
        var nowYear = myDate.getFullYear();
        for( var i = 0 ; i < 12 ;i++ ){
            $("#jp-time").append("<option value="+( nowYear - i )+">"+( nowYear - i )+"</option>");
        }
    });
</script>
</body>
</fe:html>