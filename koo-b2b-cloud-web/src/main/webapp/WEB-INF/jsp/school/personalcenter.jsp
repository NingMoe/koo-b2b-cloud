<%@ taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.koolearn.cloud.util.GlobalConstant" %>

<fe:html title="个人中心" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/back-end/sch-personal/page.css">

<fe:seaConfig>
    alias:{
    'project/b-ms-cloud/1.x/js/back-end/sch-personal/page': 'project/b-ms-cloud/1.x/js/back-end/sch-personal/page.js'
    }
</fe:seaConfig>
    <script type="text/javascript">
    seajs.use('project/b-ms-cloud/1.x/js/back-end/sch-personal/page',function(init){
    init({
    successUrl:'/project/b-ms-cloud/1.x/json/t-find-pass/111.php',//保存请求地址
    loginUrl:'sch-login.html'//保存成功后跳转登录页地址
    });
    });
    </script>
    </head>
    <body>
    <!-- 公共的头部 B-->
    <jsp:include page="/WEB-INF/jsp/commons/schoolHeader.jsp" >
        <jsp:param name="user" value="user"/>
        <jsp:param name="nav" value="pc"/>
    </jsp:include>
    <!-- 公共的头部 E-->
    <div class="i-main p-personal">
    <h2 class="p-up-title">
    <b>修改密码</b>
    </h2>
    <form id="jp-personal-form" action="#" method="post">
    <div class="p-personal-cont">
    <div class="item">
    <label class="p-name">原密码：</label>
    <div class="field">
    <input id="passwordOld" class="p-inpt" type="text" name="passwordOld">
    </div>
    </div>
    <div class="item">
    <label class="p-name">新密码：</label>
    <div class="field p-amend-error">
    <input id="passwordNew" class="p-inpt" type="text" name="passwordNew">
    <em>请输入6-16位数字、字母作为新密码</em>
    </div>
    </div>
    <p class="p-savebtn">
    <button class="green-btn green-btn-style" id="jp-sure" type="button">保存</button>
    </p>
    </div>
    </form>
    </div>
    </body>
</fe:html>
