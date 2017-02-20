<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="fe" uri="http://fe.koolearn.com/" %>
<%@ page import="com.koolearn.framework.common.utils.PropertiesConfigUtils"%>
<!DOCTYPE html>
<fe:html title="个人中心-编辑" ie="true" initSeajs="true" 
assets="/project/b-ms-cloud/1.x/css/t-personal-edit/page.css">
<body>
<!-- 公共的头部 B-->
<jsp:include page="/WEB-INF/jsp/commons/headerStudent.jsp" >
    <jsp:param name="user" value="user"/>
    <jsp:param name="nav" value="sy"/>
</jsp:include>
<!-- 公共的头部 E-->
<div class="i-main">
    <h2 class="p-user-header p-personadd">
        <span>个人中心</span>
        <a title="返回个人中心" href="/student/info/index"></a>
    </h2>
    <form action="" id="jp-user-form">
        <div class="fc p-user">
            <div class="fl p-user-left">
                <div class="p-img">
                   <!-- <input type="file" name="" id="jp-img-edit"> -->
                    <span id="jp-img-edit" class="p-img-edit" style="cursor: pointer;">编辑</span>
                    <div class="p-img-box">
                    	<c:choose>
		            		<c:when test="${empty person.ico}">
		            			<img src="<%=PropertiesConfigUtils.getProperty("domains.ui") %>project/b-ms-cloud/1.x/i/u-def.png"  alt="" id="jp-imgpr" width="90" height="90">
		            		</c:when>
		            		<c:otherwise>
		            			<img src="${person.ico}" alt="" id="jp-imgpr" width="90" height="90"/>
		            		</c:otherwise>
		            	</c:choose>
                    </div>
                </div>
                <p class="p-name" userId="${person.userId}" teacherId="${person.id}" title="${person.realName}">${person.realName}</p>
            </div>
            <div class="fr p-user-right">
                <ul>
                    <li>所在学校：${schoolName}</li>
                    <li class="fc">
                        <span class="fl">密码：</span>
                        <div class="fl p-input w1" title="输入原密码"><input type="password" placeholder="输入原密码" id="jp-pass1"/></div>
                        <div class="fl p-input w1" title="输入新密码"><input type="password" placeholder="输入新密码" id="jp-pass2"/></div>
                        <div class="fl p-input w1" title="再次输入新密码"><input type="password" placeholder="再次输入新密码" id="jp-pass3"/></div>
                        <a href="javascript:savePassword();" class="p-b-btn fl jp-password-save">保存</a>
                        <!-- <div class="fl p-jp-passinput w1 ml" title="再次输入新密码"><input type="password" placeholder="再次输入新密码" id="jp-pass-two"/></div> -->
                        <div class="fl p-error"><em></em><b></b></div>
                    </li>
                    <li class="fc">
                        <span class="fl">姓名：</span>
                        <div class="fl p-input w1">
                            <input type="text" id="realName" value="${person.realName}"/>
                        </div>
                        <a href="javascript:saveRealName();" class="p-b-btn fl jp-name-save">保存</a>
                        <div class="fl p-error"><em></em><b></b></div>
                    </li>
                    <li class="fc">
                        <span class="fl">手机：</span>
                        <div class="fl p-input w2">
                            <input type="text" id="mobile" value="${person.mobile}"/>
                            <span class="p-yzm jp-phone-random-send" onclick="sendMobileRandom();">发送验证码</span>
                        </div>
                        <span class="fl ml">输入验证码：</span>
                        <div class="fl p-input w3">
                            <input type="text" id="mobileRandom">
                        </div>
                        <a href="javascript:saveMobile();" class="p-b-btn fl jp-phone-save">保存</a>
                        <div class="fl p-error"><em></em><b></b></div>
                    </li>
                    <li class="fc">
                        <span class="fl">邮箱：</span>
                        <div class="fl p-input w2">
                            <input type="text" id="email" value="${person.email}"/>
                            <span class="p-yzm jp-email-random-send" onclick="sendEmailRandom();">发送验证码</span>
                        </div>
                        <span class="fl ml">输入验证码：</span>
                        <div class="fl p-input w3">
                            <input type="text" id="emailRandom">
                        </div>
                        <a href="javascript:saveEmail();" class="p-b-btn fl jp-email-save">保存</a>
                        <div class="fl p-error"><em></em><b></b></div>
                    </li>
                </ul>
            </div>
        </div>
    </form>
</div>

<!-- 截图弹出框 B -->
<div id="jp-dialog-cropfile" class="cutImg" style="display: none;">
    <img id="jp-crop-image" alt=""/>
    <div>
        <a class="i-btn btn" href="###" id="jp-crop-avatar">确定</a>
        <span class="gap">&nbsp;</span>
        <a class="i-btn i-btn-cancel btn" href="###" id="jp-upload-redo">重传</a>
    </div>
</div>
<!-- 截图弹出框 E -->

</body>
    <fe:seaConfig>
        alias:{
        'project/b-ms-cloud/1.x/js/t-personal-edit/page': 'project/b-ms-cloud/1.x/js/t-personal-edit/page.js'
        }
    </fe:seaConfig>
<script type="text/javascript">
 seajs.use(['project/b-ms-cloud/1.x/js/t-personal-edit/page'],function(app){
     app.init({
        api: {
          submit: '111',//上传图片
          saveAvatar: '/student/info/saveIco'
        },
        passwordSaveUrl: '/student/info/savePassword',
        nameSaveUrl: '/student/info/saveRealName',
        phoneSaveUrl: '/student/info/saveMobile',
        emailSaveUrl: '/student/info/saveEmail',
        emailRandomSendUrl: '/sendMail',
        phoneRandomSendUrl: '/sendRegisterCode',
        loginUrl:'/loginPage',//修改密码成功后跳转登录页面
         uuid:'${uuid}'
     });
 });
</script>
</fe:html>
