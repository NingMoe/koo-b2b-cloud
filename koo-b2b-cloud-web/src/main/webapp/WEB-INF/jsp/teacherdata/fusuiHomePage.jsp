<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>
<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
        function tohexin(){
//            $("#redirect").val(encodeURIComponent('http://edu.hexinedu.com/t'));//调整地址
            $("#hexinForm").attr('action','<%=GlobalConstant.KAOYUE_AUTOURL_DOMAIN%>/api/open/login');
            document.getElementById('hexinForm').submit();
        }
    </script>
    <input type="hidden" name="nameT" id="nameT" value="${nameT}" />
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/header.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="sy"/>
</jsp:include>
<!-- 公共的头部 E-->
<body>

<!-- 内容部分 -->
<div class="i-main t-jump-cont">
    <ul class="fc">
        <li>
            <a href="/teacher/choiceSubject/goClasssHomePage"  target="_blank">
                <span class="p-icon1"></span>
                <b>班级</b>
            </a>
        </li>
        <li>
            <a href="/teacher/resource/index"  target="_blank">
                <span class="p-icon2"></span>
                <b>资源库</b>
            </a>
        </li>
        <li>
            <a href="/teacher/exam/core/question/jdd?createFrom=1"  target="_blank">
                <span class="p-icon3"></span>
                <b>题库</b>
            </a>
        </li>
        <li>
            <a href="/teacher/classRoom/index" target="_blank">
                <span class="p-icon4"></span>
                <b>翻转课堂</b>
            </a>
        </li>
        <li>
            <a href="/teacher/listenCourse/index"  target="_blank">
                <span class="p-icon5"></span>
                <b>名师说课</b>
            </a>
        </li>
        <li>
            <a href="javascript:;"  onclick="tohexin();">
                <span class="p-icon6"></span>
                <b>考试中心</b>
            </a>
        </li>
        <li>
                <a href="http://<%=GlobalConstant.K12TSG_AUTOURL%>/index" target="_blank">
                <%--<a href="http://<%=GlobalConstant.K12TSG_AUTOURL%>/loginByURL?userName=zxxjyyptzxx&password=123" target="_blank">--%>
                  <span class="p-icon7"></span>
                    <b>数字图书馆</b>
                  </a>
        </li>
        <li>
            <a href="/teacher/resource/index?type=8" target="_blank">
                <span class="p-icon8"></span>
                <b>实验视频</b>
            </a>
        </li>
    </ul>
</div>
<form action=""  method="post" name="hexinForm" id="hexinForm" target="_blank">
    <input type="hidden" name="uid"  value="${userEntity.uuid}"/>
    <input type="hidden" name="redirect" id="redirect" value="/"/>
    <input type="hidden" name="access_key" value="${access_key}"/>
    <input type="hidden" name="nonce" value="${nonce}"/>
    <input type="hidden" name="timestamp" value="${timestamp}"/>
    <input type="hidden" name="signature" value="${signature}"/>
</form>
</body>
<jsp:include page="/footer.jsp"></jsp:include>
</body>

</fe:html>