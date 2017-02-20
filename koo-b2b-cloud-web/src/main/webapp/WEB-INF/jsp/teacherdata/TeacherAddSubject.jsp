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
<fe:html title="作业-选择教材" initSeajs="true" ie="true"
         assets="/project/b-ms-cloud/1.x/css/t-home-edition/page.css">
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-home-edition/page': 'project/b-ms-cloud/1.x/js/t-home-edition/page.js'
        }
    </fe:seaConfig>
    <script type="text/javascript">
        seajs.use(['project/b-ms-cloud/1.x/js/t-home-edition/page'],function(app){
            app.init({
                api:{
                    editionLst: '/teacher/choiceSubject/addTeacherRangeAndJiaoCai',//添加老师选择的教材
                    location:"/teacher/choiceSubject/goClasssHomePage",//跳转到首页
                    locationFusui:"/teacher/choiceSubject/goFusuiHomePage"//跳转到扶绥首页
                }
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

        //getUser(); 同步用户姓名
    </script>

</head>
<body>


<div class="i-main fc">
    <div class="p-box-edition i-pd50">
        <div class="p-select-edition">
            <em class="p-tit-select">您选择的是：</em>
            <span class="jp-tit-lst p-tit-lst" teach-book-id="${teacherBookVersionId}">${rangeName}:${subjectName}<span class="jp-edition-title"></span></span>
        </div>
        <div class="fc">
            <ul class="jp-lst-edition p-lst-edition fc">
                <c:forEach items="${list}" var="tags" >
                    <li>
                        <a href="javascript:;" class="jp-edition-link p-link" id="${tags.id}">
                            <img src="/picture/${tags.title}" >
                            <em class="p-name-edition">${tags.name}</em>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <div class="fc">
            <ul class="jp-lst-edition p-lst-edition fc"></ul>
        </div>
    </div>
    <div class="jp-btn-box p-btn-box" style="display:none">
        <a href="javascript:;" class="jp-form-submit green-btn green-btn-style">完成</a>
    </div>
</div>
<jsp:include page="/footer.jsp"></jsp:include>

</body>
</fe:html>
